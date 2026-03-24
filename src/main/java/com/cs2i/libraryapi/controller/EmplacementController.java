package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Emplacement;
import com.cs2i.libraryapi.service.EmplacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/emplacements")
@RequiredArgsConstructor
public class EmplacementController {

    private final EmplacementService emplacementService;

    @GetMapping
    public List<Emplacement> getAll() {
        return emplacementService.findAll();
    }

    @GetMapping("/{id}")
    public Emplacement getById(@PathVariable Long id) {
        return emplacementService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Emplacement create(@RequestBody Emplacement emplacement) {
        return emplacementService.create(emplacement);
    }

    @PutMapping("/{id}")
    public Emplacement update(@PathVariable Long id, @RequestBody Emplacement updatedEmplacement) {
        return emplacementService.update(id, updatedEmplacement);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        emplacementService.delete(id);
    }
}
