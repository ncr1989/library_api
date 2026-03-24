package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Revue;
import com.cs2i.libraryapi.repository.RevueRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevueService implements CrudService<Revue, Long> {

    private final RevueRepository revueRepository;

    @Override
    public List<Revue> findAll() {
        return revueRepository.findAll();
    }

    @Override
    public Revue findById(Long id) {
        return revueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Revue non trouvée"));
    }

    @Override
    public Revue create(Revue entity) {
        return revueRepository.save(entity);
    }

    @Override
    public Revue update(Long id, Revue entity) {
        Revue revue = findById(id);
        revue.setTitre(entity.getTitre());
        revue.setCaution(entity.getCaution());
        revue.setAnneePublication(entity.getAnneePublication());
        revue.setAuteurs(entity.getAuteurs());
        revue.setThemes(entity.getThemes());
        revue.setExemplaires(entity.getExemplaires());
        revue.setNumeroVolume(entity.getNumeroVolume());
        return revueRepository.save(revue);
    }

    @Override
    public void delete(Long id) {
        if (!revueRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Revue non trouvée");
        }
        revueRepository.deleteById(id);
    }

    public List<Revue> findByTitreContainingIgnoreCase(String titre) {
        return null;
    }
}
