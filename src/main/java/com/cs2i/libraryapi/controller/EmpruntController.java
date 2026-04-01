package com.cs2i.libraryapi.controller;




import com.cs2i.libraryapi.entity.Emprunt;

import com.cs2i.libraryapi.service.EmpruntService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/emprunts")
@RequiredArgsConstructor
public class EmpruntController {

    private final EmpruntService empruntService;

    @GetMapping
    public List<Emprunt> getAll() {
        return empruntService.findAll();
    }

    @GetMapping("/{id}")
    public Emprunt getById(@PathVariable Long id) {
        return empruntService.findById(id);
    }

    // Get all emprunts for a specific user
    @GetMapping("/utilisateur/{userId}")
    public List<Emprunt> getByUtilisateur(@PathVariable Long userId) {
        return empruntService.findByUtilisateurId(userId);
    }

    @PostMapping("/{id}/retour")
    public Emprunt retour(@PathVariable Long id) {
        return empruntService.retour(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Emprunt create(@RequestBody Emprunt emprunt) {
        return empruntService.create(emprunt);
    }

    @PutMapping("/{id}")
    public Emprunt update(@PathVariable Long id, @RequestBody Emprunt updated) {
        return empruntService.update(id,updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        empruntService.delete(id);
    }
}