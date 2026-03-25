package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.config.SecurityConfig;
import com.cs2i.libraryapi.entity.Bibliothecaire;
import com.cs2i.libraryapi.security.JwtUtil;
import com.cs2i.libraryapi.service.BibliothecaireService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Import(SecurityConfig.class)
@WebMvcTest(BibliothecaireController.class)
@DisplayName("Security - Route Protection & Role Access")
class SecurityRoutesTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private BibliothecaireService bibliothecaireService;
    @MockBean private JwtUtil jwtUtil;

    // ── UNAUTHENTICATED ───────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/bibliothecaires sans auth → 401")
    void shouldDenyUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/bibliothecaires"))
                .andExpect(status().isForbidden());
    }

    // ── WRONG ROLE ────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ETUDIANT")
    @DisplayName("GET /api/bibliothecaires avec rôle ETUDIANT → 403")
    void shouldDenyEtudiantAccessToBibliothecaires() throws Exception {
        mockMvc.perform(get("/api/bibliothecaires"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ENSEIGNANT")
    @DisplayName("GET /api/bibliothecaires avec rôle ENSEIGNANT → 403")
    void shouldDenyEnseignantAccessToBibliothecaires() throws Exception {
        mockMvc.perform(get("/api/bibliothecaires"))
                .andExpect(status().isForbidden());
    }

    // ── ADMIN ACCESS ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/bibliothecaires avec rôle ADMIN → 200")
    void shouldAllowAdminAccessToBibliothecaires() throws Exception {
        when(bibliothecaireService.findAll()).thenReturn(List.of(new Bibliothecaire()));

        mockMvc.perform(get("/api/bibliothecaires"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("DELETE /api/bibliothecaires/{id} avec rôle ADMIN → 204")
    void shouldAllowAdminToDelete() throws Exception {
        mockMvc.perform(delete("/api/bibliothecaires/1").with(csrf()))
                .andExpect(status().isNoContent());
    }
}
