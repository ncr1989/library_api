package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Utilisateur;
import com.cs2i.libraryapi.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService implements CrudService<Utilisateur, Long> {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAll();
    }

    @Override
    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
    }

    @Override
    public Utilisateur create(Utilisateur entity) {
        return utilisateurRepository.save(entity);
    }

    @Override
    public Utilisateur update(Long id, Utilisateur entity) {
        Utilisateur utilisateur = findById(id);
        utilisateur.setNom(entity.getNom());
        utilisateur.setPrenom(entity.getPrenom());
        utilisateur.setEmail(entity.getEmail());
        utilisateur.setAdresse(entity.getAdresse());
        utilisateur.setCaution(entity.getCaution());
        if (entity.getPassword() != null && !entity.getPassword().isBlank()) {
            utilisateur.setPassword(passwordEncoder.encode(entity.getPassword()));
        }
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void delete(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }
        try {
            utilisateurRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet utilisateur ne peut pas être supprimé car il est associé à des emprunts."
            );
        }
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
    }

    public boolean existsById(Long id) {
        return utilisateurRepository.existsById(id);
    }
}
