package com.cs2i.libraryapi.repository;


import com.cs2i.libraryapi.entity.Revue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevueRepository extends JpaRepository<Revue, Long> {
    List<Revue> findByTitreContainingIgnoreCase(String titre);
}