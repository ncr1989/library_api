package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Etudiant;
import com.cs2i.libraryapi.service.EtudiantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;

    @GetMapping
    public List<Etudiant> getAll() {
        return etudiantService.findAll();
    }

    @GetMapping("/{id}")
    public Etudiant getById(@PathVariable Long id) {
        return etudiantService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Etudiant create(@RequestBody @Valid Etudiant etudiant) {
        return etudiantService.create(etudiant);
    }


    @PutMapping("/{id}")
    public Etudiant update(@PathVariable Long id, @RequestBody @Valid Etudiant updated) {

        return etudiantService.update(id,updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        etudiantService.delete(id);
    }
}