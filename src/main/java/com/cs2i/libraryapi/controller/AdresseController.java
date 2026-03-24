package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Adresse;
import com.cs2i.libraryapi.repository.AdresseRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/Adresses")
@RequiredArgsConstructor
public class AdresseController {

    private final AdresseRepository adresseRepository;

    @GetMapping
    public List<Adresse> getAll() {
        return adresseRepository.findAll();
    }

    @GetMapping("/{id}")
    public Adresse getById(@PathVariable Long id) {
        return adresseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Adresse create(@RequestBody @Valid Adresse adresse) {
        return adresseRepository.save(adresse);
    }

    @PutMapping("/{id}")
    public Adresse update(@PathVariable Long id, @RequestBody @Valid Adresse updated) {
        Adresse existing = adresseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updated.setId(id);
        return adresseRepository.save(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        adresseRepository.deleteById(id);
    }
}
