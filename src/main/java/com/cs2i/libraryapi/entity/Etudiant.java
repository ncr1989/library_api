package com.cs2i.libraryapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "etudiant")
public class Etudiant extends Utilisateur {

    private LocalDate anneeUniversitaire;

    @Column(unique = true)
    private int numeroEtudiant;
}
