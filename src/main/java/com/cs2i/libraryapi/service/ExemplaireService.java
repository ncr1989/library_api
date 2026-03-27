package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Exemplaire;
import com.cs2i.libraryapi.repository.ExemplaireRepository;
import com.cs2i.libraryapi.repository.OuvrageRepository;
import com.cs2i.libraryapi.service.CrudService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExemplaireService implements CrudService<Exemplaire, Long> {

    private final ExemplaireRepository exemplaireRepository;
    private final OuvrageRepository ouvrageRepository;

    @Override
    public List<Exemplaire> findAll() {
        return exemplaireRepository.findAll();
    }

    @Override
    public Exemplaire findById(Long id) {
        return exemplaireRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exemplaire non trouvé"));
    }

    @Override
    public Exemplaire create(Exemplaire entity) {
        return exemplaireRepository.save(entity);
    }

    @Override
    public Exemplaire update(Long id, Exemplaire entity) {
        Exemplaire exemplaire = findById(id);
        exemplaire.setDisponible(entity.isDisponible());
        exemplaire.setOuvrage(entity.getOuvrage());
        return exemplaireRepository.save(exemplaire);
    }



    @Override
    @Transactional
    public void delete(Long id) {
        if (!exemplaireRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exemplaire non trouvé");
        }
        try {
            exemplaireRepository.deleteById(id);
            exemplaireRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cet exemplaire ne peut pas être supprimé car il est associé à des emprunts."
            );
        }
    }
}
