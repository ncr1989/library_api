package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Bibliothecaire;

import com.cs2i.libraryapi.service.BibliothecaireService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/bibliothecaires")
@RequiredArgsConstructor
public class BibliothecaireController {

    private final BibliothecaireService bibliothecaireService;

    @GetMapping
    public List<Bibliothecaire> getAll() {
        return bibliothecaireService.findAll();
    }

    @GetMapping("/{id}")
    public Bibliothecaire getById(@PathVariable Long id) {
        return bibliothecaireService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bibliothecaire create(@RequestBody @Valid Bibliothecaire Bibliothecaire) {
        return bibliothecaireService.create(Bibliothecaire);
    }

    @PutMapping("/{id}")
    public Bibliothecaire update(@PathVariable Long id, @RequestBody @Valid Bibliothecaire updated) {
        return bibliothecaireService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bibliothecaireService.delete(id);
    }
}
