package com.cs2i.libraryapi.service.amende;

public class ContexteAmende {

    public double calculerAmende(long joursRetard) {
        EtatAmende etat = resoudreEtat(joursRetard);
        return etat.calculerAmende(joursRetard);
    }

    private EtatAmende resoudreEtat(long joursRetard) {
        if (joursRetard <= 0) {
            return new AucuneAmendeEtat();
        } else if (joursRetard <= 7) {
            return new PetiteAmendeEtat();
        } else if (joursRetard <= 30) {
            return new MoyenneAmendeEtat();
        }
        return new GrosseAmendeEtat();
    }
}
