package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Utilisateur;
import com.cs2i.libraryapi.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService implements CrudService<Utilisateur, Long> {

    private final UtilisateurRepository utilisateurRepository;

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
        utilisateur.setPassword(entity.getPassword());
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void delete(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }
        utilisateurRepository.deleteById(id);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
    }

    public boolean existsById(Long id) {
        return utilisateurRepository.existsById(id);
    }
}
