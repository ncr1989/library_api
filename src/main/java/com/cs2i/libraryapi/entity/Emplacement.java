package com.cs2i.libraryapi.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emplacement")
public class Emplacement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int niveau;
    private int numeroTravee;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;
}
