package com.cs2i.libraryapi.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomTheme;

    @ManyToOne
    @JoinColumn(name = "ouvrage_id")
    private Ouvrage ouvrage;
}
