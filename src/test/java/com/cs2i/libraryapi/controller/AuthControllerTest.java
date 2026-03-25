package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.config.SecurityConfig;
import com.cs2i.libraryapi.dto.LoginRequest;
import com.cs2i.libraryapi.dto.LoginResponse;
import com.cs2i.libraryapi.dto.RegisterRequest;
import com.cs2i.libraryapi.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@DisplayName("AuthController - Endpoints /api/auth")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    // ── LOGIN ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/auth/login → 200 avec token")
    void shouldReturn200OnValidLogin() throws Exception {
        LoginResponse response = new LoginResponse("jwt_token", "ETUDIANT", "Doe", "John");
        when(authService.login(any())).thenReturn(response);

        LoginRequest request = new LoginRequest();
        request.setEmail("john@mail.com");
        request.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token"))
                .andExpect(jsonPath("$.role").value("ETUDIANT"))
                .andExpect(jsonPath("$.nom").value("Doe"))
                .andExpect(jsonPath("$.prenom").value("John"));
    }

    @Test
    @DisplayName("POST /api/auth/login mauvais identifiants → 401")
    void shouldReturn401OnInvalidLogin() throws Exception {
        when(authService.login(any())).thenThrow(
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect"));

        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@mail.com");
        request.setPassword("wrongpass");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // ── REGISTER ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/auth/register → 200 avec token")
    void shouldReturn200OnValidRegister() throws Exception {
        LoginResponse response = new LoginResponse("jwt_token", "ETUDIANT", "Doe", "John");
        when(authService.register(any())).thenReturn(response);

        RegisterRequest request = new RegisterRequest();
        request.setNom("Doe");
        request.setPrenom("John");
        request.setEmail("new@mail.com");
        request.setPassword("pass");
        request.setRole("ETUDIANT");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token"))
                .andExpect(jsonPath("$.role").value("ETUDIANT"));
    }

    @Test
    @DisplayName("POST /api/auth/register email existant → 409")
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        when(authService.register(any())).thenThrow(
                new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé"));

        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@mail.com");
        request.setPassword("pass");
        request.setRole("ETUDIANT");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
