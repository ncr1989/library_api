package com.cs2i.libraryapi.repository;

import com.cs2i.libraryapi.entity.Bibliothecaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BibliothecaireRepository extends JpaRepository<Bibliothecaire, Long> {
    Optional<Bibliothecaire> findByNumeroMatricule(int numeroMatricule);
}
