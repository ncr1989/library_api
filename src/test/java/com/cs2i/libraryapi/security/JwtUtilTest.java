package com.cs2i.libraryapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JwtUtil - Token Generation & Validation")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    @DisplayName("generateToken() → produit un token non null")
    void shouldGenerateNonNullToken() {
        String token = jwtUtil.generateToken("test@mail.com", "ETUDIANT");
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("extractEmail() → retourne le bon email")
    void shouldExtractCorrectEmail() {
        String token = jwtUtil.generateToken("john@mail.com", "ETUDIANT");
        assertThat(jwtUtil.extractEmail(token)).isEqualTo("john@mail.com");
    }

    @Test
    @DisplayName("extractRole() → retourne le bon rôle")
    void shouldExtractCorrectRole() {
        String token = jwtUtil.generateToken("admin@mail.com", "ADMIN");
        assertThat(jwtUtil.extractRole(token)).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("isTokenValid() → token valide retourne true")
    void shouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken("user@mail.com", "ENSEIGNANT");
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    @Test
    @DisplayName("isTokenValid() → token falsifié retourne false")
    void shouldReturnFalseForTamperedToken() {
        String token = jwtUtil.generateToken("user@mail.com", "ETUDIANT");
        String tampered = token + "tampered";
        assertThat(jwtUtil.isTokenValid(tampered)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid() → token vide retourne false")
    void shouldReturnFalseForEmptyToken() {
        assertThat(jwtUtil.isTokenValid("")).isFalse();
    }

    @Test
    @DisplayName("isTokenValid() → token aléatoire retourne false")
    void shouldReturnFalseForRandomString() {
        assertThat(jwtUtil.isTokenValid("not.a.jwt")).isFalse();
    }

    @Test
    @DisplayName("Différents rôles → tokens distincts")
    void shouldGenerateDifferentTokensForDifferentRoles() {
        String tokenEtudiant = jwtUtil.generateToken("user@mail.com", "ETUDIANT");
        String tokenAdmin = jwtUtil.generateToken("user@mail.com", "ADMIN");
        assertThat(tokenEtudiant).isNotEqualTo(tokenAdmin);
    }
}
