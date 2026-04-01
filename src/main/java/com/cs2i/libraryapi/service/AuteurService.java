package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Auteur;
import com.cs2i.libraryapi.repository.AuteurRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuteurService implements CrudService<Auteur, Long> {

    private final AuteurRepository auteurRepository;

    @Override
    public List<Auteur> findAll() {
        return auteurRepository.findAll();
    }

    @Override
    public Auteur findById(Long id) {
        return auteurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Auteur non trouvé"));
    }

    @Override
    public Auteur create(Auteur entity) {
        return auteurRepository.save(entity);
    }

    @Override
    public Auteur update(Long id, Auteur entity) {
        Auteur auteur = findById(id);
        auteur.setNom(entity.getNom());
        auteur.setPrenom(entity.getPrenom());
        return auteurRepository.save(auteur);
    }

    @Override
    public void delete(Long id) {
        if (!auteurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auteur non trouvé");
        }
        auteurRepository.deleteById(id);
    }
}
