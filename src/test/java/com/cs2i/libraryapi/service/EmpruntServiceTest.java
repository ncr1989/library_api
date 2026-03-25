package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.entity.Exemplaire;
import com.cs2i.libraryapi.entity.Etudiant;
import com.cs2i.libraryapi.repository.EmpruntRepository;
import com.cs2i.libraryapi.service.observateur.EmpruntEnRetardEvenement;
import com.cs2i.libraryapi.service.observateur.NotificateurRetardEmprunt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmpruntService - Business Logic")
class EmpruntServiceTest {

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private NotificateurRetardEmprunt notificateurRetardEmprunt;

    @InjectMocks
    private EmpruntService empruntService;

    private Emprunt emprunt;
    private Exemplaire exemplaire;

    @BeforeEach
    void setUp() {
        exemplaire = new Exemplaire();
        exemplaire.setId(1L);
        exemplaire.setDisponible(true);

        emprunt = new Emprunt();
        emprunt.setId(1L);
        emprunt.setDateDebut(LocalDate.now());
        emprunt.setDateFinPrevue(LocalDate.now().plusDays(14));
        emprunt.setExemplaire(exemplaire);
        emprunt.setUtilisateur(new Etudiant());
        emprunt.setEnRetard(false);
        emprunt.setMontantAmende(0.0);
    }

    // ── CREATE ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create() → initialise enRetard=false et montantAmende=0")
    void shouldInitializeEmpruntOnCreate() {
        when(empruntRepository.save(any())).thenReturn(emprunt);

        Emprunt result = empruntService.create(emprunt);

        assertThat(result.isEnRetard()).isFalse();
        assertThat(result.getMontantAmende()).isEqualTo(0.0);
        verify(empruntRepository).save(emprunt);
    }

    // ── RETOUR ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("retour() → set dateRetourEffective à aujourd'hui")
    void shouldSetReturnDateOnRetour() {
        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));
        when(empruntRepository.save(any())).thenReturn(emprunt);

        Emprunt result = empruntService.retour(1L);

        assertThat(result.getDateRetourEffective()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("retour() → marque l'exemplaire comme disponible")
    void shouldMarkExemplaireAsAvailableOnRetour() {
        exemplaire.setDisponible(false);
        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));
        when(empruntRepository.save(any())).thenReturn(emprunt);

        empruntService.retour(1L);

        assertThat(exemplaire.isDisponible()).isTrue();
    }

    @Test
    @DisplayName("retour() deux fois → lève BAD_REQUEST")
    void shouldThrowWhenAlreadyReturned() {
        emprunt.setDateRetourEffective(LocalDate.now().minusDays(1));
        when(empruntRepository.findById(1L)).thenReturn(Optional.of(emprunt));

        assertThatThrownBy(() -> empruntService.retour(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("déjà retourné");
    }

    @Test
    @DisplayName("retour() emprunt inexistant → lève NOT_FOUND")
    void shouldThrowWhenEmpruntNotFoundOnRetour() {
        when(empruntRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empruntService.retour(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("non trouvé");
    }

    // ── VÉRIFICATION RETARD ─────────────────────────────────────────────────

    @Test
    @DisplayName("verifierRetard() → pas de retard si retour avant dateFinPrevue")
    void shouldNotMarkAsLateWhenReturnedOnTime() {
        emprunt.setDateFinPrevue(LocalDate.now().plusDays(5));
        emprunt.setDateRetourEffective(LocalDate.now());


        empruntService.verifierRetard(emprunt);

        assertThat(emprunt.isEnRetard()).isFalse();
        assertThat(emprunt.getMontantAmende()).isEqualTo(0.0);
        verify(notificateurRetardEmprunt, never()).notifierObservateurs(any());
    }

    @Test
    @DisplayName("verifierRetard() → retard détecté si retour après dateFinPrevue")
    void shouldMarkAsLateWhenReturnedLate() {
        emprunt.setDateFinPrevue(LocalDate.now().minusDays(5));
        emprunt.setDateRetourEffective(LocalDate.now());


        empruntService.verifierRetard(emprunt);

        ArgumentCaptor<EmpruntEnRetardEvenement> captor =
                ArgumentCaptor.forClass(EmpruntEnRetardEvenement.class);
        verify(notificateurRetardEmprunt).notifierObservateurs(captor.capture());
        assertThat(captor.getValue().getJoursRetard()).isEqualTo(5L);
    }

    @Test
    @DisplayName("verifierRetard() → pas de retard si dateFinPrevue est null")
    void shouldNotMarkAsLateWhenNoDeadline() {
        emprunt.setDateFinPrevue(null);
        when(empruntRepository.save(any())).thenReturn(emprunt);

        empruntService.verifierRetard(emprunt);

        verify(notificateurRetardEmprunt, never()).notifierObservateurs(any());
    }

    // ── FIND ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findById() emprunt inexistant → lève NOT_FOUND")
    void shouldThrowWhenEmpruntNotFound() {
        when(empruntRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empruntService.findById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("non trouvé");
    }

    @Test
    @DisplayName("findByUtilisateurId() → retourne les emprunts de l'utilisateur")
    void shouldReturnEmpruntsByUserId() {
        when(empruntRepository.findByUtilisateurId(1L)).thenReturn(List.of(emprunt));

        List<Emprunt> result = empruntService.findByUtilisateurId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(emprunt);
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete() emprunt inexistant → lève NOT_FOUND")
    void shouldThrowWhenDeletingNonExistentEmprunt() {
        when(empruntRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> empruntService.delete(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("non trouvé");
    }
}
