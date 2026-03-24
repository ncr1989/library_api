package com.cs2i.libraryapi.service.observateur;

import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.repository.EmpruntRepository;
import com.cs2i.libraryapi.service.amende.ContexteAmende;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculAmendeObservateur implements ObservateurEmprunt {

    private final EmpruntRepository empruntRepository;
    private final ContexteAmende contexteAmende = new ContexteAmende();

    @Override
    public void notifierRetard(EmpruntEnRetardEvenement evenement) {
        Emprunt emprunt = evenement.getEmprunt();
        double montant = contexteAmende.calculerAmende(evenement.getJoursRetard());

        emprunt.setEnRetard(true);
        emprunt.setMontantAmende(montant);
        empruntRepository.save(emprunt);
    }
}
