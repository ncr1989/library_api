package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.repository.EmpruntRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VerificateurRetardEmpruntService - Scheduled Job")
class VerificateurRetardEmpruntServiceTest {

    @Mock private EmpruntRepository empruntRepository;
    @Mock private EmpruntService empruntService;

    @InjectMocks
    private VerificateurRetardEmpruntService verificateur;

    @Test
    @DisplayName("Vérifie retard pour un emprunt en retard non retourné")
    void shouldCheckRetardForOverdueEmprunt() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateFinPrevue(LocalDate.now().minusDays(3)); // overdue
        emprunt.setDateRetourEffective(null);                    // not returned

        when(empruntRepository.findAll()).thenReturn(List.of(emprunt));

        verificateur.verifierTousLesEmpruntsEnRetard();

        verify(empruntService).verifierRetard(emprunt);
    }

    @Test
    @DisplayName("Ne vérifie pas un emprunt déjà retourné")
    void shouldSkipAlreadyReturnedEmprunt() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateFinPrevue(LocalDate.now().minusDays(3));
        emprunt.setDateRetourEffective(LocalDate.now().minusDays(1)); // already returned

        when(empruntRepository.findAll()).thenReturn(List.of(emprunt));

        verificateur.verifierTousLesEmpruntsEnRetard();

        verify(empruntService, never()).verifierRetard(any());
    }

    @Test
    @DisplayName("Ne vérifie pas un emprunt pas encore en retard")
    void shouldSkipEmpruntNotYetOverdue() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateFinPrevue(LocalDate.now().plusDays(5)); // not overdue yet
        emprunt.setDateRetourEffective(null);

        when(empruntRepository.findAll()).thenReturn(List.of(emprunt));

        verificateur.verifierTousLesEmpruntsEnRetard();

        verify(empruntService, never()).verifierRetard(any());
    }

    @Test
    @DisplayName("Ne vérifie pas un emprunt sans dateFinPrevue")
    void shouldSkipEmpruntWithNoDeadline() {
        Emprunt emprunt = new Emprunt();
        emprunt.setDateFinPrevue(null);
        emprunt.setDateRetourEffective(null);

        when(empruntRepository.findAll()).thenReturn(List.of(emprunt));

        verificateur.verifierTousLesEmpruntsEnRetard();

        verify(empruntService, never()).verifierRetard(any());
    }

    @Test
    @DisplayName("Vérifie plusieurs emprunts en retard")
    void shouldCheckMultipleOverdueEmprunts() {
        Emprunt e1 = new Emprunt();
        e1.setDateFinPrevue(LocalDate.now().minusDays(1));
        e1.setDateRetourEffective(null);

        Emprunt e2 = new Emprunt();
        e2.setDateFinPrevue(LocalDate.now().minusDays(10));
        e2.setDateRetourEffective(null);

        Emprunt e3 = new Emprunt(); // not overdue
        e3.setDateFinPrevue(LocalDate.now().plusDays(5));
        e3.setDateRetourEffective(null);

        when(empruntRepository.findAll()).thenReturn(List.of(e1, e2, e3));

        verificateur.verifierTousLesEmpruntsEnRetard();

        verify(empruntService).verifierRetard(e1);
        verify(empruntService).verifierRetard(e2);
        verify(empruntService, never()).verifierRetard(e3);
    }
}
