package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Bibliothecaire;
import com.cs2i.libraryapi.repository.BibliothecaireRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BibliothecaireService implements CrudService<Bibliothecaire, Long> {

    private final BibliothecaireRepository bibliothecaireRepository;
    private final PasswordEncoder passwordEncoder;
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
        bibliothecaire.setPassword(passwordEncoder.encode(entity.getPassword()));
        bibliothecaire.setAdresse(entity.getAdresse());
        return bibliothecaireRepository.save(bibliothecaire);
    }

    @Override
    public void delete(Long id) {
        if (!bibliothecaireRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bibliothécaire non trouvé");
        }
        bibliothecaireRepository.deleteById(id);
    }
}
