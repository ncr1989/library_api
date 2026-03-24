package com.cs2i.libraryapi.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "revue")
public class Revue extends Ouvrage {

    private int numeroVolume;
    private LocalDate dateDeParution;
}