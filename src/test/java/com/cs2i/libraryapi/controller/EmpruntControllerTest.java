package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.config.SecurityConfig;
import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.entity.Etudiant;
import com.cs2i.libraryapi.entity.Exemplaire;
import com.cs2i.libraryapi.security.JwtUtil;
import com.cs2i.libraryapi.service.EmpruntService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpruntController.class)
@Import(SecurityConfig.class)
@DisplayName("EmpruntController - Endpoints /api/emprunts")
class EmpruntControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private EmpruntService empruntService;
    @MockBean private JwtUtil jwtUtil;

    private Emprunt emprunt;

    @BeforeEach
    void setUp() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);

        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setId(1L);
        exemplaire.setDisponible(true);

        emprunt = new Emprunt();
        emprunt.setId(1L);
        emprunt.setDateDebut(LocalDate.of(2024, 1, 1));
        emprunt.setDateFinPrevue(LocalDate.of(2024, 1, 15));
        emprunt.setEnRetard(false);
        emprunt.setMontantAmende(0.0);
        emprunt.setUtilisateur(etudiant);
        emprunt.setExemplaire(exemplaire);
    }

    // ── SECURITY ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/emprunts sans token → 403")
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/emprunts"))
                .andExpect(status().isForbidden());
    }

    // ── GET ALL ───────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("GET /api/emprunts → 200 avec liste")
    void shouldReturnAllEmprunts() throws Exception {
        when(empruntService.findAll()).thenReturn(List.of(emprunt));

        mockMvc.perform(get("/api/emprunts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].enRetard").value(false));
    }

    // ── GET BY ID ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("GET /api/emprunts/{id} → 200")
    void shouldReturnEmpruntById() throws Exception {
        when(empruntService.findById(1L)).thenReturn(emprunt);

        mockMvc.perform(get("/api/emprunts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/emprunts/{id} inexistant → 404")
    void shouldReturn404WhenNotFound() throws Exception {
        when(empruntService.findById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprunt non trouvé"));

        mockMvc.perform(get("/api/emprunts/99"))
                .andExpect(status().isNotFound());
    }

    // ── GET BY UTILISATEUR ────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("GET /api/emprunts/utilisateur/{id} → 200 avec liste")
    void shouldReturnEmpruntsByUserId() throws Exception {
        when(empruntService.findByUtilisateurId(1L)).thenReturn(List.of(emprunt));

        mockMvc.perform(get("/api/emprunts/utilisateur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("POST /api/emprunts → 201 créé")
    void shouldCreateEmprunt() throws Exception {
        when(empruntService.create(any())).thenReturn(emprunt);

        mockMvc.perform(post("/api/emprunts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emprunt)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    // ── RETOUR ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("POST /api/emprunts/{id}/retour → 200 avec dateRetourEffective")
    void shouldProcessRetour() throws Exception {
        emprunt.setDateRetourEffective(LocalDate.now());
        when(empruntService.retour(1L)).thenReturn(emprunt);

        mockMvc.perform(post("/api/emprunts/1/retour")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateRetourEffective").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/emprunts/{id}/retour déjà retourné → 400")
    void shouldReturn400WhenAlreadyReturned() throws Exception {
        when(empruntService.retour(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet emprunt est déjà retourné"));

        mockMvc.perform(post("/api/emprunts/1/retour")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/emprunts/{id} → 204")
    void shouldDeleteEmprunt() throws Exception {
        mockMvc.perform(delete("/api/emprunts/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
