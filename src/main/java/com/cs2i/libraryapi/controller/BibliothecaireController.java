package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Bibliothecaire;
import com.cs2i.libraryapi.repository.BibliothecaireRepository;
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

    private final BibliothecaireRepository bibliothecaireRepository;

    @GetMapping
    public List<Bibliothecaire> getAll() {
        return bibliothecaireRepository.findAll();
    }

    @GetMapping("/{id}")
    public Bibliothecaire getById(@PathVariable Long id) {
        return bibliothecaireRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bibliothecaire create(@RequestBody @Valid Bibliothecaire Bibliothecaire) {
        return bibliothecaireRepository.save(Bibliothecaire);
    }

    @PutMapping("/{id}")
    public Bibliothecaire update(@PathVariable Long id, @RequestBody @Valid Bibliothecaire updated) {
        Bibliothecaire existing = bibliothecaireRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updated.setId(id);
        return bibliothecaireRepository.save(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bibliothecaireRepository.deleteById(id);
    }
}
