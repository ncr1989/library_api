package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Emplacement;
import com.cs2i.libraryapi.repository.EmplacementRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmplacementService implements CrudService<Emplacement, Long> {

    private final EmplacementRepository emplacementRepository;

    @Override
    public List<Emplacement> findAll() {
        return emplacementRepository.findAll();
    }

    @Override
    public Emplacement findById(Long id) {
        return emplacementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emplacement non trouvé"));
    }

    @Override
    public Emplacement create(Emplacement entity) {
        return emplacementRepository.save(entity);
    }

    @Override
    public Emplacement update(Long id, Emplacement entity) {
        Emplacement emplacement = findById(id);
        emplacement.setNiveau(entity.getNiveau());
        emplacement.setNumeroTravee(entity.getNumeroTravee());
        emplacement.setCategorie(entity.getCategorie());
        return emplacementRepository.save(emplacement);
    }

    @Override
    public void delete(Long id) {
        if (!emplacementRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Emplacement non trouvé");
        }
        emplacementRepository.deleteById(id);
    }
}
