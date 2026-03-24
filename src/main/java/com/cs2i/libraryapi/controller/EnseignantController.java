package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Enseignant;
import com.cs2i.libraryapi.repository.EnseignantRepository;
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

    private final EnseignantRepository enseignantRepository;

    @GetMapping
    public List<Enseignant> getAll() {
        return enseignantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Enseignant getById(@PathVariable Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Enseignant create(@RequestBody @Valid Enseignant Enseignant) {
        return enseignantRepository.save(Enseignant);
    }

    @PutMapping("/{id}")
    public Enseignant update(@PathVariable Long id, @RequestBody @Valid Enseignant updated) {
        Enseignant existing = enseignantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updated.setId(id);
        return enseignantRepository.save(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        enseignantRepository.deleteById(id);
    }
}
