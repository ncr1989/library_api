package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Adresse;
import com.cs2i.libraryapi.repository.AdresseRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdresseService implements CrudService<Adresse, Long> {

    private final AdresseRepository adresseRepository;

    @Override
    public List<Adresse> findAll() {
        return adresseRepository.findAll();
    }

    @Override
    public Adresse findById(Long id) {
        return adresseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Adresse non trouvée"));
    }

    @Override
    public Adresse create(Adresse entity) {
        return adresseRepository.save(entity);
    }

    @Override
    public Adresse update(Long id, Adresse entity) {
        Adresse adresse = findById(id);
        adresse.setRue(entity.getRue());
        adresse.setVille(entity.getVille());
        adresse.setCodePostal(entity.getCodePostal());
        adresse.setPays(entity.getPays());
        return adresseRepository.save(adresse);
    }

    @Override
    public void delete(Long id) {
        if (!adresseRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Adresse non trouvée");
        }
        adresseRepository.deleteById(id);
    }
}
