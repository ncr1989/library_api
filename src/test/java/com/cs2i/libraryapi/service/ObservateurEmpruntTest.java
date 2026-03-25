package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.repository.EmpruntRepository;
import com.cs2i.libraryapi.service.observateur.CalculAmendeObservateur;
import com.cs2i.libraryapi.service.observateur.EmpruntEnRetardEvenement;
import com.cs2i.libraryapi.service.observateur.NotificateurRetardEmprunt;
import com.cs2i.libraryapi.service.observateur.ObservateurEmprunt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Observer Pattern - Retard Emprunt")
class ObservateurEmpruntTest {

    // ── NotificateurRetardEmprunt ────────────────────────────────────────────

    @Test
    @DisplayName("notifierObservateurs() → appelle tous les observateurs enregistrés")
    void shouldNotifyAllObservers() {
        ObservateurEmprunt obs1 = mock(ObservateurEmprunt.class);
        ObservateurEmprunt obs2 = mock(ObservateurEmprunt.class);

        NotificateurRetardEmprunt notificateur = new NotificateurRetardEmprunt(List.of(obs1, obs2));

        Emprunt emprunt = new Emprunt();
        EmpruntEnRetardEvenement evenement = new EmpruntEnRetardEvenement(emprunt, 5L);

        notificateur.notifierObservateurs(evenement);

        verify(obs1).notifierRetard(evenement);
        verify(obs2).notifierRetard(evenement);
    }

    @Test
    @DisplayName("notifierObservateurs() sans observateurs → ne plante pas")
    void shouldNotFailWithNoObservers() {
        NotificateurRetardEmprunt notificateur = new NotificateurRetardEmprunt(List.of());

        Emprunt emprunt = new Emprunt();
        EmpruntEnRetardEvenement evenement = new EmpruntEnRetardEvenement(emprunt, 3L);

        // Should not throw
        notificateur.notifierObservateurs(evenement);
    }

    // ── CalculAmendeObservateur ──────────────────────────────────────────────

    @Test
    @DisplayName("CalculAmendeObservateur → calcule et persiste l'amende correctement")
    void shouldCalculateAndSaveAmende() {
        EmpruntRepository empruntRepository = mock(EmpruntRepository.class);
        CalculAmendeObservateur observateur = new CalculAmendeObservateur(empruntRepository);

        Emprunt emprunt = new Emprunt();
        emprunt.setEnRetard(false);
        emprunt.setMontantAmende(0.0);

        EmpruntEnRetardEvenement evenement = new EmpruntEnRetardEvenement(emprunt, 3L); // 3 days → PetiteAmende → 3.0€

        observateur.notifierRetard(evenement);

        assertThat(emprunt.isEnRetard()).isTrue();
        assertThat(emprunt.getMontantAmende()).isEqualTo(3.0);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    @DisplayName("CalculAmendeObservateur → grosse amende après 31 jours")
    void shouldApplyGrosseAmendeAfter30Days() {
        EmpruntRepository empruntRepository = mock(EmpruntRepository.class);
        CalculAmendeObservateur observateur = new CalculAmendeObservateur(empruntRepository);

        Emprunt emprunt = new Emprunt();
        EmpruntEnRetardEvenement evenement = new EmpruntEnRetardEvenement(emprunt, 31L); // 31 days → GrosseAmende → 155.0€

        observateur.notifierRetard(evenement);

        assertThat(emprunt.getMontantAmende()).isEqualTo(155.0);
    }
}
