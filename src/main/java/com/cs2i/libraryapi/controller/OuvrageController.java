package com.cs2i.libraryapi.controller;


import com.cs2i.libraryapi.entity.Ouvrage;
import com.cs2i.libraryapi.repository.OuvrageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/ouvrages")
@RequiredArgsConstructor
public class OuvrageController {

    private final OuvrageRepository ouvrageRepository;

    @GetMapping
    public List<Ouvrage> getAll() {
        return ouvrageRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ouvrage getById(@PathVariable Long id) {
        return ouvrageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public List<Ouvrage> searchByTitre(@RequestParam String titre) {
        return ouvrageRepository.findByTitreContainingIgnoreCase(titre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ouvrageRepository.deleteById(id);
    }
}

