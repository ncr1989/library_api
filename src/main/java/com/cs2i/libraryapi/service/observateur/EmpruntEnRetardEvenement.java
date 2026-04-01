package com.cs2i.libraryapi.service.observateur;

import com.cs2i.libraryapi.entity.Emprunt;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmpruntEnRetardEvenement {
    private final Emprunt emprunt;
    private final long joursRetard;
}
