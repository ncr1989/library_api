package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Livre;
import com.cs2i.libraryapi.entity.Ouvrage;
import com.cs2i.libraryapi.repository.OuvrageRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OuvrageService implements CrudService<Ouvrage, Long> {

    private final OuvrageRepository ouvrageRepository;

    @Override
    public List<Ouvrage> findAll() {
        return ouvrageRepository.findAll();
    }

    @Override
    public Ouvrage findById(Long id) {
        return ouvrageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ouvrage non trouvé"));
    }

    @Override
    public Ouvrage create(Ouvrage entity) {
        return ouvrageRepository.save(entity);
    }

    @Override
    public Ouvrage update(Long id, Ouvrage entity) {
        Ouvrage ouvrage = findById(id);
        ouvrage.setTitre(entity.getTitre());
        ouvrage.setCaution(entity.getCaution());
        ouvrage.setAnneePublication(entity.getAnneePublication());
        ouvrage.setAuteurs(entity.getAuteurs());
        ouvrage.setThemes(entity.getThemes());
        ouvrage.setExemplaires(entity.getExemplaires());
        return ouvrageRepository.save(ouvrage);
    }

    public List<Ouvrage> findByTitreContainingIgnoreCase(String titre) {
        return ouvrageRepository.findByTitreContainingIgnoreCase(titre); // fixed
    }



    @Override
    public void delete(Long id) {
        if (!ouvrageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ouvrage non trouvé");
        }
        try {
            ouvrageRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet ouvrage ne peut pas être supprimé car il est associé à des emprunts."
            );
        }


    }}
