package com.cs2i.libraryapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bibliothecaire")
public class Bibliothecaire extends Utilisateur {

    @Column(unique = false)
    private Integer numeroMatricule;
}
