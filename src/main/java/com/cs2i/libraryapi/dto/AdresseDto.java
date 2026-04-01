package com.cs2i.libraryapi.dto;

import lombok.Data;

@Data
public class AdresseDto {
    private int numero;
    private String rue;
    private String ville;
    private int codePostal;
}
