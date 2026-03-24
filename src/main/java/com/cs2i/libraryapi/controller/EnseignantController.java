package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Enseignant;

import com.cs2i.libraryapi.service.EnseignantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
@RequiredArgsConstructor
public class EnseignantController {

    private final EnseignantService enseignantService;

    @GetMapping
    public List<Enseignant> getAll() {
        return enseignantService.findAll();
    }

    @GetMapping("/{id}")
    public Enseignant getById(@PathVariable Long id) {
        return enseignantService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enseignant create(@RequestBody @Valid Enseignant Enseignant) {
        return enseignantService.create(Enseignant);
    }

    @PutMapping("/{id}")
    public Enseignant update(@PathVariable Long id, @RequestBody @Valid Enseignant updated) {
        return enseignantService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        enseignantService.delete(id);
    }
}
