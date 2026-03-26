package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.entity.Exemplaire;
import com.cs2i.libraryapi.entity.Utilisateur;
import com.cs2i.libraryapi.repository.EmpruntRepository;
import com.cs2i.libraryapi.repository.UtilisateurRepository;
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
    private final UtilisateurRepository utilisateurRepository;
    private final NotificateurRetardEmprunt notificateurRetardEmprunt;

    @Override
    public List<Emprunt> findAll() {
        return empruntRepository.findAll();
    }

    @Override
    public Emprunt findById(Long id) {
        return empruntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Emprunt non trouvé"));
    }

    @Override
    public Emprunt create(Emprunt entity) {
        // ── Validate exemplaire ───────────────────────────────────────────
        if (entity.getExemplaire() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exemplaire requis");
        }

        // ── Validate utilisateur ──────────────────────────────────────────
        if (entity.getUtilisateur() == null || entity.getUtilisateur().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utilisateur requis");
        }

        // ── Reload fresh user from DB ─────────────────────────────────────
        Utilisateur freshUser = utilisateurRepository
                .findById(entity.getUtilisateur().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        // ── Check caution ─────────────────────────────────────────────────
        float cautionOuvrage = 0;
        if (entity.getExemplaire().getOuvrage() != null) {
            cautionOuvrage = entity.getExemplaire().getOuvrage().getCaution();
        }

        if (freshUser.getCaution() < cautionOuvrage) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(
                            "Caution insuffisante. Votre solde : %.2f€ — Caution requise : %.2f€",
                            freshUser.getCaution(),
                            cautionOuvrage
                    )
            );
        }

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
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cet emprunt est déjà retourné");
        }

        emprunt.setDateRetourEffective(LocalDate.now());

        // ── Mark exemplaire as available ──────────────────────────────────
        Exemplaire exemplaire = emprunt.getExemplaire();
        if (exemplaire != null) {
            exemplaire.setDisponible(true);
        }

        Emprunt sauvegarde = empruntRepository.save(emprunt);
        verifierRetard(sauvegarde);

        // ── Deduct amende from user caution ───────────────────────────────
        if (sauvegarde.getMontantAmende() > 0 && sauvegarde.getUtilisateur() != null) {
            utilisateurRepository.findById(sauvegarde.getUtilisateur().getId())
                    .ifPresent(utilisateur -> {
                        float nouveauSolde = utilisateur.getCaution()
                                - (float) sauvegarde.getMontantAmende();
                        utilisateur.setCaution(Math.max(0, nouveauSolde));
                        utilisateurRepository.save(utilisateur);
                    });
        }

        return sauvegarde;
    }

    @Override
    public void delete(Long id) {
        if (!empruntRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Emprunt non trouvé");
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

        if (emprunt.getDateFinPrevue() == null
                || dateReference.isBefore(emprunt.getDateFinPrevue().plusDays(1))) {
            emprunt.setEnRetard(false);
            emprunt.setMontantAmende(0.0);
            empruntRepository.save(emprunt);
            return;
        }

        long joursRetard = ChronoUnit.DAYS.between(
                emprunt.getDateFinPrevue(), dateReference);
        notificateurRetardEmprunt.notifierObservateurs(
                new EmpruntEnRetardEvenement(emprunt, joursRetard));
    }
}