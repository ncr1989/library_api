package com.cs2i.libraryapi.repository;

import com.cs2i.libraryapi.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByNumeroEtudiant(int numeroEtudiant);
}
