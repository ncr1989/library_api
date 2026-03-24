package com.cs2i.libraryapi.service.observateur;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificateurRetardEmprunt implements SujetEmprunt {

    private final List<ObservateurEmprunt> observateurs = new ArrayList<>();

    @Override
    public void ajouterObservateur(ObservateurEmprunt observateur) {
        observateurs.add(observateur);
    }

    @Override
    public void supprimerObservateur(ObservateurEmprunt observateur) {
        observateurs.remove(observateur);
    }

    @Override
    public void notifierObservateurs(EmpruntEnRetardEvenement evenement) {
        for (ObservateurEmprunt observateur : observateurs) {
            observateur.notifierRetard(evenement);
        }
    }
}
