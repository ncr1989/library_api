package com.cs2i.libraryapi.controller;




import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.repository.EmpruntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/emprunts")
@RequiredArgsConstructor
public class EmpruntController {

    private final EmpruntRepository empruntRepository;

    @GetMapping
    public List<Emprunt> getAll() {
        return empruntRepository.findAll();
    }

    @GetMapping("/{id}")
    public Emprunt getById(@PathVariable Long id) {
        return empruntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // Get all emprunts for a specific user
    @GetMapping("/utilisateur/{userId}")
    public List<Emprunt> getByUtilisateur(@PathVariable Long userId) {
        return empruntRepository.findByUtilisateurId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Emprunt create(@RequestBody Emprunt emprunt) {
        return empruntRepository.save(emprunt);
    }

    @PutMapping("/{id}")
    public Emprunt update(@PathVariable Long id, @RequestBody Emprunt updated) {
        empruntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updated.setId(id);
        return empruntRepository.save(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        empruntRepository.deleteById(id);
    }
}