package com.cs2i.libraryapi.entity;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emprunt")
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFinPrevue;
    private LocalDate dateRetourEffective;

    private boolean enRetard;
    private double montantAmende;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    @JsonIgnoreProperties({"password", "adresse"})
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "exemplaire_id")
    @JsonIgnoreProperties({ "emplacement"})
    private Exemplaire exemplaire;
}
