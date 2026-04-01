package com.cs2i.libraryapi.service.amende;

public class PetiteAmendeEtat implements EtatAmende {
    @Override
    public double calculerAmende(long joursRetard) {
        return joursRetard * 1.0;
    }
}
