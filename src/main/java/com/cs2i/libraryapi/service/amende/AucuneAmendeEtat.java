package com.cs2i.libraryapi.service.amende;

public class AucuneAmendeEtat implements EtatAmende {
    @Override
    public double calculerAmende(long joursRetard) {
        return 0.0;
    }
}