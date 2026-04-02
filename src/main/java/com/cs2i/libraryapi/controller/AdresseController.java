package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Adresse;
import com.cs2i.libraryapi.service.AdresseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/adresses")
@RequiredArgsConstructor
public class AdresseController {

    private final AdresseService adresseService;

    @GetMapping
    public List<Adresse> getAll() {
        return adresseService.findAll();
    }

    @GetMapping("/{id}")
    public Adresse getById(@PathVariable Long id) {
        return adresseService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Adresse create(@RequestBody @Valid Adresse adresse) {
        return adresseService.create(adresse);
    }

    @PutMapping("/{id}")
    public Adresse update(@PathVariable Long id, @RequestBody Adresse updated) {
        return adresseService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        adresseService.delete(id);
    }
}