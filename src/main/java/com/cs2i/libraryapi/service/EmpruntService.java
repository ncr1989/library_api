package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.entity.Exemplaire;
import com.cs2i.libraryapi.repository.EmpruntRepository;
import com.cs2i.libraryapi.service.observateur.EmpruntEnRetardEvenement;
import com.cs2i.libraryapi.service.observateur.NotificateurRetardEmprunt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpruntService implements CrudService<Emprunt, Long> {

    private final EmpruntRepository empruntRepository;
    private final NotificateurRetardEmprunt notificateurRetardEmprunt;

    @Override
    public List<Emprunt> findAll() {
        return empruntRepository.findAll();
    }

    @Override
    public Emprunt findById(Long id) {
        return empruntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprunt non trouvé"));
    }

    @Override
    public Emprunt create(Emprunt entity) {
        entity.setEnRetard(false);
        entity.setMontantAmende(0.0);
        return empruntRepository.save(entity);
    }

    @Override
    public Emprunt update(Long id, Emprunt entity) {
        Emprunt emprunt = findById(id);
        emprunt.setDateDebut(entity.getDateDebut());
        emprunt.setDateFinPrevue(entity.getDateFinPrevue());
        emprunt.setDateRetourEffective(entity.getDateRetourEffective());
        emprunt.setUtilisateur(entity.getUtilisateur());
        emprunt.setExemplaire(entity.getExemplaire());

        Emprunt sauvegarde = empruntRepository.save(emprunt);
        verifierRetard(sauvegarde);
        return sauvegarde;
    }

    public Emprunt retour(Long empruntId) {
        Emprunt emprunt = findById(empruntId);

        if (emprunt.getDateRetourEffective() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet emprunt est déjà retourné");
        }

        emprunt.setDateRetourEffective(LocalDate.now());

        Exemplaire exemplaire = emprunt.getExemplaire();
        if (exemplaire != null) {
            exemplaire.setDisponible(true);
        }

        Emprunt sauvegarde = empruntRepository.save(emprunt);
        verifierRetard(sauvegarde);
        return sauvegarde;
    }

    @Override
    public void delete(Long id) {
        if (!empruntRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprunt non trouvé");
        }
        empruntRepository.deleteById(id);
    }

    public List<Emprunt> findByUtilisateurId(Long utilisateurId) {
        return empruntRepository.findByUtilisateurId(utilisateurId);
    }

    public void verifierRetard(Emprunt emprunt) {
        LocalDate dateReference = emprunt.getDateRetourEffective() != null
                ? emprunt.getDateRetourEffective()
                : LocalDate.now();

        if (emprunt.getDateFinPrevue() == null || dateReference.isBefore(emprunt.getDateFinPrevue().plusDays(1))) {
            emprunt.setEnRetard(false);
            emprunt.setMontantAmende(0.0);
            empruntRepository.save(emprunt);
            return;
        }

        long joursRetard = ChronoUnit.DAYS.between(emprunt.getDateFinPrevue(), dateReference);
        notificateurRetardEmprunt.notifierObservateurs(new EmpruntEnRetardEvenement(emprunt, joursRetard));
    }
}
