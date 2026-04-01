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


    @GetMapping
    public List<Utilisateur> getAll() {
        return utilisateurService.findAll();
    }


    @GetMapping("/{id}")
    public Utilisateur getById(@PathVariable Long id) {
        return utilisateurService.findById(id);

    }


    @GetMapping("/email/{email}")
    public Utilisateur getByEmail(@PathVariable String email) {
        return utilisateurService.findByEmail(email);

    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!utilisateurService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }
        utilisateurService.delete(id);
    }
}

