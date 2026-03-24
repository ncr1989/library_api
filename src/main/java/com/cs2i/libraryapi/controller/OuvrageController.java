package com.cs2i.libraryapi.controller;


import com.cs2i.libraryapi.entity.Ouvrage;

import com.cs2i.libraryapi.service.OuvrageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/ouvrages")
@RequiredArgsConstructor
public class OuvrageController {

    private final OuvrageService ouvrageService;

    @GetMapping
    public List<Ouvrage> getAll() {
        return ouvrageService.findAll();
    }

    @GetMapping("/{id}")
    public Ouvrage getById(@PathVariable Long id) {
        return ouvrageService.findById(id);
    }

    @GetMapping("/search")
    public List<Ouvrage> searchByTitre(@RequestParam String titre) {
        return ouvrageService.findByTitreContainingIgnoreCase(titre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ouvrageService.delete(id);
    }
}

