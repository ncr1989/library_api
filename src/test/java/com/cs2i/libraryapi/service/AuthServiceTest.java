package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.dto.LoginRequest;
import com.cs2i.libraryapi.dto.LoginResponse;
import com.cs2i.libraryapi.dto.RegisterRequest;
import com.cs2i.libraryapi.entity.Bibliothecaire;
import com.cs2i.libraryapi.entity.Enseignant;
import com.cs2i.libraryapi.entity.Etudiant;
import com.cs2i.libraryapi.entity.Utilisateur;
import com.cs2i.libraryapi.repository.UtilisateurRepository;
import com.cs2i.libraryapi.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Login & Register")
class AuthServiceTest {

    @Mock private UtilisateurRepository utilisateurRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Etudiant etudiant;
    private Enseignant enseignant;
    private Bibliothecaire bibliothecaire;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
        etudiant.setNom("Doe");
        etudiant.setPrenom("John");
        etudiant.setEmail("john@mail.com");
        etudiant.setPassword("hashed_password");

        enseignant = new Enseignant();
        enseignant.setNom("Martin");
        enseignant.setPrenom("Alice");
        enseignant.setEmail("alice@mail.com");
        enseignant.setPassword("hashed_password");

        bibliothecaire = new Bibliothecaire();
        bibliothecaire.setNom("Admin");
        bibliothecaire.setPrenom("Super");
        bibliothecaire.setEmail("admin@mail.com");
        bibliothecaire.setPassword("hashed_password");
    }

    // ── LOGIN ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("login() valide → retourne token + rôle ETUDIANT")
    void shouldLoginEtudiantSuccessfully() {
        when(utilisateurRepository.findByEmail("john@mail.com")).thenReturn(Optional.of(etudiant));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
        when(jwtUtil.generateToken("john@mail.com", "ETUDIANT")).thenReturn("jwt_token");

        LoginRequest request = new LoginRequest();
        request.setEmail("john@mail.com");
        request.setPassword("password123");

        LoginResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt_token");
        assertThat(response.getRole()).isEqualTo("ETUDIANT");
        assertThat(response.getNom()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("login() → rôle ADMIN pour Bibliothecaire")
    void shouldReturnAdminRoleForBibliothecaire() {
        when(utilisateurRepository.findByEmail("admin@mail.com")).thenReturn(Optional.of(bibliothecaire));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken("admin@mail.com", "ADMIN")).thenReturn("admin_token");

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@mail.com");
        request.setPassword("adminpass");

        LoginResponse response = authService.login(request);

        assertThat(response.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("login() → rôle ENSEIGNANT pour Enseignant")
    void shouldReturnEnseignantRole() {
        when(utilisateurRepository.findByEmail("alice@mail.com")).thenReturn(Optional.of(enseignant));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken("alice@mail.com", "ENSEIGNANT")).thenReturn("enseignant_token");

        LoginRequest request = new LoginRequest();
        request.setEmail("alice@mail.com");
        request.setPassword("pass");

        LoginResponse response = authService.login(request);

        assertThat(response.getRole()).isEqualTo("ENSEIGNANT");
    }

    @Test
    @DisplayName("login() email inconnu → lève UNAUTHORIZED")
    void shouldThrowWhenEmailNotFound() {
        when(utilisateurRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@mail.com");
        request.setPassword("pass");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("incorrect");
    }

    @Test
    @DisplayName("login() mauvais mot de passe → lève UNAUTHORIZED")
    void shouldThrowWhenPasswordWrong() {
        when(utilisateurRepository.findByEmail("john@mail.com")).thenReturn(Optional.of(etudiant));
        when(passwordEncoder.matches("wrongpass", "hashed_password")).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setEmail("john@mail.com");
        request.setPassword("wrongpass");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("incorrect");
    }

    // ── REGISTER ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("register() → crée un Etudiant par défaut")
    void shouldRegisterAsEtudiantByDefault() {
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(utilisateurRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("token");

        RegisterRequest request = new RegisterRequest();
        request.setNom("Doe");
        request.setPrenom("John");
        request.setEmail("new@mail.com");
        request.setPassword("pass");
        request.setTelephone("0600000000");
        request.setRole("ETUDIANT");

        LoginResponse response = authService.register(request);

        assertThat(response.getRole()).isEqualTo("ETUDIANT");
        assertThat(response.getNom()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("register() → crée un Bibliothecaire si rôle=BIBLIOTHECAIRE")
    void shouldRegisterAsBibliothecaire() {
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(utilisateurRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(anyString(), eq("ADMIN"))).thenReturn("admin_token");

        RegisterRequest request = new RegisterRequest();
        request.setNom("Admin");
        request.setPrenom("Super");
        request.setEmail("admin2@mail.com");
        request.setPassword("pass");
        request.setRole("BIBLIOTHECAIRE");

        LoginResponse response = authService.register(request);

        assertThat(response.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("register() email déjà utilisé → lève CONFLICT")
    void shouldThrowWhenEmailAlreadyExists() {
        when(utilisateurRepository.findByEmail("john@mail.com")).thenReturn(Optional.of(etudiant));

        RegisterRequest request = new RegisterRequest();
        request.setEmail("john@mail.com");
        request.setRole("ETUDIANT");

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("déjà utilisé");
    }

    @Test
    @DisplayName("register() → le mot de passe est encodé avec BCrypt")
    void shouldHashPasswordOnRegister() {
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plaintext")).thenReturn("bcrypt_hash");
        when(utilisateurRepository.save(any())).thenAnswer(inv -> {
            Utilisateur u = inv.getArgument(0);
            assertThat(u.getPassword()).isEqualTo("bcrypt_hash");
            return u;
        });
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("token");

        RegisterRequest request = new RegisterRequest();
        request.setNom("Test");
        request.setPrenom("User");
        request.setEmail("test@mail.com");
        request.setPassword("plaintext");
        request.setRole("ETUDIANT");

        authService.register(request);

        verify(passwordEncoder).encode("plaintext");
    }
}
