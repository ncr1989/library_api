package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Revue;
import com.cs2i.libraryapi.repository.RevueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/Revues")
@RequiredArgsConstructor
public class RevueController {

    private final RevueRepository revueRepository;

    @GetMapping
    public List<Revue> getAll() {
        return revueRepository.findAll();
    }

    @GetMapping("/{id}")
    public Revue getById(@PathVariable Long id) {
        return revueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public List<Revue> searchByTitre(@RequestParam String titre) {
        return revueRepository.findByTitreContainingIgnoreCase(titre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        revueRepository.deleteById(id);
    }
}

