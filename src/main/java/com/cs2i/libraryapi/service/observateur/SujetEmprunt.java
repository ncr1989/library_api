package com.cs2i.libraryapi.service.observateur;

public interface SujetEmprunt {
    void ajouterObservateur(ObservateurEmprunt observateur);
    void supprimerObservateur(ObservateurEmprunt observateur);
    void notifierObservateurs(EmpruntEnRetardEvenement evenement);
}
