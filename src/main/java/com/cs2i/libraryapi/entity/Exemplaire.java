package com.cs2i.libraryapi.entity;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "emplacement_id")
    @JsonIgnoreProperties({"categorie"})
    private Emplacement emplacement;

    @ManyToOne
    @JoinColumn(name = "ouvrage_id")
    @JsonIgnoreProperties({"exemplaires", "themes", "auteurs"})
    private Ouvrage ouvrage;
}
