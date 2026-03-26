package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Etudiant;
import com.cs2i.libraryapi.repository.EtudiantRepository;
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
public class EtudiantService implements CrudService<Etudiant, Long> {

    private final EtudiantRepository etudiantRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant findById(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé"));
    }

    @Override
    public Etudiant create(Etudiant entity) {
        return etudiantRepository.save(entity);
    }

    @Override
    public Etudiant update(Long id, Etudiant entity) {
        Etudiant etudiant = findById(id);
        etudiant.setNom(entity.getNom());
        etudiant.setPrenom(entity.getPrenom());
        etudiant.setEmail(entity.getEmail());
        etudiant.setAdresse(entity.getAdresse());
        etudiant.setCaution(entity.getCaution());
        if (entity.getPassword() != null && !entity.getPassword().isBlank()) {
            etudiant.setPassword(passwordEncoder.encode(entity.getPassword()));
        }
        return etudiantRepository.save(etudiant);
    }

    @Override
    public void delete(Long id) {
        if (!etudiantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé");
        }
        try {
            etudiantRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet utilisateur ne peut pas être supprimé car il est associé à des emprunts."
            );
        }
    }
}
