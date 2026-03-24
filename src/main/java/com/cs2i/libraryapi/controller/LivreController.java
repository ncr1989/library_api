package com.cs2i.libraryapi.controller;


import com.cs2i.libraryapi.entity.Livre;
import com.cs2i.libraryapi.repository.LivreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/Livres")
@RequiredArgsConstructor
public class LivreController {

    private final LivreRepository livreRepository;

    @GetMapping
    public List<Livre> getAll() {
        return livreRepository.findAll();
    }

    @GetMapping("/{id}")
    public Livre getById(@PathVariable Long id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public List<Livre> searchByTitre(@RequestParam String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        livreRepository.deleteById(id);
    }
}