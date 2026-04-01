package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Categorie;
import com.cs2i.libraryapi.repository.CategorieRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategorieService implements CrudService<Categorie, Long> {

    private final CategorieRepository categorieRepository;

    @Override
    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    @Override
    public Categorie findById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie non trouvée"));
    }

    @Override
    public Categorie create(Categorie entity) {
        return categorieRepository.save(entity);
    }

    @Override
    public Categorie update(Long id, Categorie entity) {
        Categorie categorie = findById(id);
        categorie.setNom(entity.getNom());
        return categorieRepository.save(categorie);
    }

    @Override
    public void delete(Long id) {
        if (!categorieRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Catégorie non trouvée");
        }
        categorieRepository.deleteById(id);
    }
}
