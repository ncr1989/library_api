package com.cs2i.libraryapi.controller;


import com.cs2i.libraryapi.entity.Livre;
import com.cs2i.libraryapi.service.LivreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/Livres")
@RequiredArgsConstructor
public class LivreController {

    private final LivreService livreService;

    @GetMapping
    public List<Livre> getAll() {
        return livreService.findAll();
    }

    @GetMapping("/{id}")
    public Livre getById(@PathVariable Long id) {
        return livreService.findById(id);
    }

    @GetMapping("/search")
    public List<Livre> searchByTitre(@RequestParam String titre) {
        return livreService.findByTitreContainingIgnoreCase(titre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        livreService.delete(id);
    }
}