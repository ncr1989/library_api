package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Exemplaire;

import com.cs2i.libraryapi.service.ExemplaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/exemplaires")
@RequiredArgsConstructor
public class ExemplaireController {

    private final ExemplaireService exemplaireService;

    @GetMapping
    public List<Exemplaire> getAll() {
        return exemplaireService.findAll();
    }

    @GetMapping("/{id}")
    public Exemplaire getById(@PathVariable Long id) {
        return exemplaireService.findById(id);
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        exemplaireService.delete(id);
    }
}

