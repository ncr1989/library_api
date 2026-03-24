package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Etudiant;
import com.cs2i.libraryapi.repository.EtudiantRepository;
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

    private final EtudiantRepository etudiantRepository;

    @GetMapping
    public List<Etudiant> getAll() {
        return etudiantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Etudiant getById(@PathVariable Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Etudiant create(@RequestBody @Valid Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }


    @PutMapping("/{id}")
    public Etudiant update(@PathVariable Long id, @RequestBody @Valid Etudiant updated) {
        Etudiant existing = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updated.setId(id);
        return etudiantRepository.save(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        etudiantRepository.deleteById(id);
    }
}