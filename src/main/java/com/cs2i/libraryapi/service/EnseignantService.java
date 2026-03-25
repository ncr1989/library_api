package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Enseignant;
import com.cs2i.libraryapi.repository.EnseignantRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnseignantService implements CrudService<Enseignant, Long> {

    private final EnseignantRepository enseignantRepository;
    private final PasswordEncoder passwordEncoder;

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
        enseignant.setPassword(passwordEncoder.encode(entity.getPassword()));
        return enseignantRepository.save(enseignant);
    }

    @Override
    public void delete(Long id) {
        if (!enseignantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enseignant non trouvé");
        }
        enseignantRepository.deleteById(id);
    }
}
