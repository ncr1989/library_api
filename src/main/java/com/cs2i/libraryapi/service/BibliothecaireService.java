package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Adresse;
import com.cs2i.libraryapi.entity.Bibliothecaire;
import com.cs2i.libraryapi.repository.AdresseRepository;
import com.cs2i.libraryapi.repository.BibliothecaireRepository;
import com.cs2i.libraryapi.repository.UtilisateurRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BibliothecaireService implements CrudService<Bibliothecaire, Long> {

    private final BibliothecaireRepository bibliothecaireRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdresseRepository adresseRepository;

    @Override
    public List<Bibliothecaire> findAll() {
        return bibliothecaireRepository.findAll();
    }

    @Override
    public Bibliothecaire findById(Long id) {
        return bibliothecaireRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bibliothécaire non trouvé"));
    }

    @Override
    public Bibliothecaire create(Bibliothecaire entity) {
        return bibliothecaireRepository.save(entity);
    }

    @Override
    public Bibliothecaire update(Long id, Bibliothecaire entity) {
        Bibliothecaire bibliothecaire = findById(id);
        bibliothecaire.setNom(entity.getNom());
        bibliothecaire.setPrenom(entity.getPrenom());
        bibliothecaire.setEmail(entity.getEmail());
        bibliothecaire.setTelephone(entity.getTelephone());
        bibliothecaire.setCaution(entity.getCaution());

        if (entity.getPassword() != null && !entity.getPassword().isBlank()) {
            bibliothecaire.setPassword(passwordEncoder.encode(entity.getPassword()));
        }

        if (entity.getAdresse() != null) {
            if (bibliothecaire.getAdresse() != null) {
                bibliothecaire.getAdresse().setNumero(entity.getAdresse().getNumero());
                bibliothecaire.getAdresse().setRue(entity.getAdresse().getRue());
                bibliothecaire.getAdresse().setVille(entity.getAdresse().getVille());
                bibliothecaire.getAdresse().setCodePostal(entity.getAdresse().getCodePostal());
                adresseRepository.save(bibliothecaire.getAdresse());
            } else {
                Adresse adresse = entity.getAdresse();
                adresseRepository.save(adresse);
                bibliothecaire.setAdresse(adresse);
            }
        }

        return bibliothecaireRepository.save(bibliothecaire);
    }

    @Override
    public void delete(Long id) {
        if (!bibliothecaireRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé");
        }
        try {
            bibliothecaireRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet utilisateur ne peut pas être supprimé car il est associé à des emprunts."
            );
        }
    }
}
