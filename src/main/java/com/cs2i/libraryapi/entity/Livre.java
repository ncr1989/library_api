package com.cs2i.libraryapi.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livre")
public class Livre extends Ouvrage {

    private long isbn;
}
