package com.cs2i.libraryapi.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ouvrage")
public class Ouvrage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private float caution;
    private LocalDate anneePublication;

    @ManyToMany
    @JoinTable(
            name = "ouvrage_auteur",
            joinColumns = @JoinColumn(name = "ouvrage_id"),
            inverseJoinColumns = @JoinColumn(name = "auteur_id")
    )
    private List<Auteur> auteurs;

    @OneToMany(mappedBy = "ouvrage")
    private List<Theme> themes;

    @OneToMany(mappedBy = "ouvrage")
    private List<Exemplaire> exemplaires;
}
