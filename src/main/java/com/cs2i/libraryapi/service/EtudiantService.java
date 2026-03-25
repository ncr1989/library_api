package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Etudiant;
import com.cs2i.libraryapi.repository.EtudiantRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EtudiantService implements CrudService<Etudiant, Long> {

    private final EtudiantRepository etudiantRepository;
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
        etudiant.setPassword(entity.getPassword());
        return etudiantRepository.save(etudiant);
    }

    @Override
    public void delete(Long id) {
        if (!etudiantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Étudiant non trouvé");
        }
        etudiantRepository.deleteById(id);
    }
}
