package com.cs2i.libraryapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "adresse")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;
    private String rue;
    private String ville;
    private int codePostal;

    @OneToMany(mappedBy = "adresse", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Utilisateur> utilisateurs;
}
