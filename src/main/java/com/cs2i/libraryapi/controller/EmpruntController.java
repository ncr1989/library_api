package com.cs2i.libraryapi.controller;




import com.cs2i.libraryapi.entity.Emprunt;

import com.cs2i.libraryapi.service.EmpruntService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "GET tous les emprunts",
            description = "Recuperer tous les emprunts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retour enregistré"),
            @ApiResponse(responseCode = "400", description = "Emprunt déjà retourné"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    })
    @GetMapping
    public List<Emprunt> getAll() {
        return empruntService.findAll();
    }

    @GetMapping("/{id}")
    public Emprunt getById(@PathVariable Long id) {
        return empruntService.findById(id);
    }

    @Operation(summary = "GET tous les emprunts associés à un utilisateur",
            description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retour enregistré"),
            @ApiResponse(responseCode = "400", description = "Emprunt déjà retourné"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    })
    @GetMapping("/utilisateur/{userId}")
    public List<Emprunt> getByUtilisateur(@PathVariable Long userId) {
        return empruntService.findByUtilisateurId(userId);
    }

    @Operation(summary = "Retour d'un emprunt",
            description = "Enregistre le retour d'un emprunt et calcule l'amende si en retard")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retour enregistré"),
            @ApiResponse(responseCode = "400", description = "Emprunt déjà retourné"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    })
    @PostMapping("/{id}/retour")
    public Emprunt retour(@PathVariable Long id) {
        return empruntService.retour(id);
    }

    @Operation(summary = "Créer un emprunt",
            description = "Crée un nouvel emprunt après vérification de la caution et de la disponibilité de l'exemplaire")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Emprunt créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Caution insuffisante"),
            @ApiResponse(responseCode = "409", description = "Exemplaire non disponible")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Emprunt create(@RequestBody Emprunt emprunt) {
        return empruntService.create(emprunt);
    }


    @PutMapping("/{id}")
    public Emprunt update(@PathVariable Long id, @RequestBody Emprunt updated) {
        return empruntService.update(id,updated);
    }
    @Operation(summary = "Supprimer un emprunt",
            description = "Supprimer un nouvel emprunt après vérification si l'emprunt est liées a un emprunt")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Emprunt supprimé avec succès"),
            @ApiResponse(responseCode = "409", description = "Exemplaire non disponible")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        empruntService.delete(id);
    }
}