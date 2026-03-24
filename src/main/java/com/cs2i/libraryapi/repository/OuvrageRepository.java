package com.cs2i.libraryapi.repository;

import com.cs2i.libraryapi.entity.Ouvrage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OuvrageRepository extends JpaRepository<Ouvrage, Long> {
    List<Ouvrage> findByTitreContainingIgnoreCase(String titre);
}
