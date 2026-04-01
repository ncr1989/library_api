package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Auteur;
import com.cs2i.libraryapi.service.AuteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auteurs")
@RequiredArgsConstructor
public class AuteurController {

    private final AuteurService auteurService;

    @GetMapping
    public List<Auteur> getAll() {
        return auteurService.findAll();
    }

    @GetMapping("/{id}")
    public Auteur getById(@PathVariable Long id) {
        return auteurService.findById(id);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Auteur create(@RequestBody Auteur auteur) {
        return auteurService.create(auteur);
    }

    @PutMapping("/{id}")
    public Auteur update(@PathVariable Long id, @RequestBody Auteur updatedAuteur) {
       return auteurService.update(id, updatedAuteur);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        auteurService.delete(id);
    }
}
