package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Exemplaire;
import com.cs2i.libraryapi.repository.ExemplaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/exemplaires")
@RequiredArgsConstructor
public class ExemplaireController {

    private final ExemplaireRepository exemplaireRepository;

    @GetMapping
    public List<Exemplaire> getAll() {
        return exemplaireRepository.findAll();
    }

    @GetMapping("/{id}")
    public Exemplaire getById(@PathVariable Long id) {
        return exemplaireRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        exemplaireRepository.deleteById(id);
    }
}

