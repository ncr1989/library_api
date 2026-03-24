package com.cs2i.libraryapi.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exemplaire")
public class Exemplaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int codeBarre;
    private boolean disponible;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "emplacement_id")
    private Emplacement emplacement;

    @ManyToOne
    @JoinColumn(name = "ouvrage_id")
    private Ouvrage ouvrage;
}
