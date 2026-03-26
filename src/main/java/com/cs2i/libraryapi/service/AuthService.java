package com.cs2i.libraryapi.service;



import com.cs2i.libraryapi.dto.*;
import com.cs2i.libraryapi.entity.*;
import com.cs2i.libraryapi.repository.AdresseRepository;
import com.cs2i.libraryapi.repository.UtilisateurRepository;
import com.cs2i.libraryapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AdresseRepository adresseRepository;

    public LoginResponse login(LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), utilisateur.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect");
        }

        String role = determineRole(utilisateur);
        String token = jwtUtil.generateToken(utilisateur.getEmail(), role);
        return new LoginResponse(utilisateur.getId(),token ,role, utilisateur.getNom(), utilisateur.getPrenom(),utilisateur.getCaution());
    }

    public LoginResponse register(RegisterRequest request) {
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
        }

        Utilisateur utilisateur = switch (request.getRole().toUpperCase()) {
            case "ENSEIGNANT"     -> new Enseignant();
            case "BIBLIOTHECAIRE" -> new Bibliothecaire();
            default               -> new Etudiant();
        };

        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setCaution(request.getCaution());  // ← add this

        // Handle address if provided
        if (request.getAdresse() != null && request.getAdresse().getRue() != null
                && !request.getAdresse().getRue().isBlank()) {
            Adresse adresse = new Adresse();
            adresse.setNumero(request.getAdresse().getNumero());
            adresse.setRue(request.getAdresse().getRue());
            adresse.setVille(request.getAdresse().getVille());
            adresse.setCodePostal(request.getAdresse().getCodePostal());
            adresseRepository.save(adresse);
            utilisateur.setAdresse(adresse);
        }

        utilisateurRepository.save(utilisateur);

        String role = determineRole(utilisateur);
        String token = jwtUtil.generateToken(utilisateur.getEmail(), role);
        return new LoginResponse(utilisateur.getId(), token, role,
                utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getCaution());
    }

    private String determineRole(Utilisateur utilisateur) {
        if (utilisateur instanceof Bibliothecaire) return "ADMIN";
        if (utilisateur instanceof Enseignant)     return "ENSEIGNANT";
        if (utilisateur instanceof Etudiant)       return "ETUDIANT";
        return "USER";
    }
}
