package com.cs2i.libraryapi.controller;
import com.cs2i.libraryapi.entity.Utilisateur;

import com.cs2i.libraryapi.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    // Get all users (returns all: etudiants, enseignants, bibliothecaires)
    @GetMapping
    public List<Utilisateur> getAll() {
        return utilisateurService.findAll();
    }

    // Get one by ID
    @GetMapping("/{id}")
    public Utilisateur getById(@PathVariable Long id) {
        return utilisateurService.findById(id);

    }

    // Get by email
    @GetMapping("/email/{email}")
    public Utilisateur getByEmail(@PathVariable String email) {
        return utilisateurService.findByEmail(email);

    }

    // Delete any user by ID (admin only ideally)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!utilisateurService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }
        utilisateurService.delete(id);
    }
}

