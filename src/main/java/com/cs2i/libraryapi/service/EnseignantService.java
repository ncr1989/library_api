package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Adresse;
import com.cs2i.libraryapi.entity.Enseignant;
import com.cs2i.libraryapi.repository.AdresseRepository;
import com.cs2i.libraryapi.repository.EnseignantRepository;
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
public class EnseignantService implements CrudService<Enseignant, Long> {

    private final EnseignantRepository enseignantRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdresseRepository adresseRepository;

    @Override
    public List<Enseignant> findAll() {
        return enseignantRepository.findAll();
    }

    @Override
    public Enseignant findById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enseignant non trouvé"));
    }

    @Override
    public Enseignant create(Enseignant entity) {
        return enseignantRepository.save(entity);
    }

    @Override
    public Enseignant update(Long id, Enseignant entity) {
        Enseignant enseignant = findById(id);
        enseignant.setNom(entity.getNom());
        enseignant.setPrenom(entity.getPrenom());
        enseignant.setEmail(entity.getEmail());
        enseignant.setTelephone(entity.getTelephone());
        enseignant.setCaution(entity.getCaution());

        if (entity.getPassword() != null && !entity.getPassword().isBlank()) {
            enseignant.setPassword(passwordEncoder.encode(entity.getPassword()));
        }

        if (entity.getAdresse() != null) {
            if (enseignant.getAdresse() != null) {
                enseignant.getAdresse().setNumero(entity.getAdresse().getNumero());
                enseignant.getAdresse().setRue(entity.getAdresse().getRue());
                enseignant.getAdresse().setVille(entity.getAdresse().getVille());
                enseignant.getAdresse().setCodePostal(entity.getAdresse().getCodePostal());
                adresseRepository.save(enseignant.getAdresse());
            } else {
                Adresse adresse = entity.getAdresse();
                adresseRepository.save(adresse);
                enseignant.setAdresse(adresse);
            }
        }

        return enseignantRepository.save(enseignant);
    }

    @Override
    public void delete(Long id) {
        if (!enseignantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé");
        }
        try {
            enseignantRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet utilisateur ne peut pas être supprimé car il est associé à des emprunts."
            );
        }
    }
}
