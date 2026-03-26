package com.cs2i.libraryapi.dto;


import lombok.Data;

@Data
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String telephone;
    private String role;
    private float caution;        // ← add this
    private AdresseDto adresse;  // "ETUDIANT", "ENSEIGNANT", "BIBLIOTHECAIRE"
}
