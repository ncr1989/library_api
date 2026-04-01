package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Categorie;

import com.cs2i.libraryapi.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    @GetMapping
    public List<Categorie> getAll() {
        return categorieService.findAll();
    }

    @GetMapping("/{id}")
    public Categorie getById(@PathVariable Long id) {
        return categorieService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categorie create(@RequestBody Categorie categorie) {
        return categorieService.create(categorie);
    }

    @PutMapping("/{id}")
    public Categorie update(@PathVariable Long id, @RequestBody Categorie updatedCategorie) {

        return categorieService.update(id,updatedCategorie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categorieService.delete(id);
    }
}
