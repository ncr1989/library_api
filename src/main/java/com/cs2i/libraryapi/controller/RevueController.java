package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Livre;
import com.cs2i.libraryapi.entity.Revue;
import com.cs2i.libraryapi.service.RevueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/revues")
@RequiredArgsConstructor
public class RevueController {

    private final RevueService revueService;

    @GetMapping
    public List<Revue> getAll() {
        return revueService.findAll();
    }

    @GetMapping("/{id}")
    public Revue getById(@PathVariable Long id) {
        return revueService.findById(id);
    }

    @GetMapping("/search")
    public List<Revue> searchByTitre(@RequestParam String titre) {
        return revueService.findByTitreContainingIgnoreCase(titre);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Revue create(@RequestBody Revue revue) {
        return revueService.create(revue);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        revueService.delete(id);
    }
}

