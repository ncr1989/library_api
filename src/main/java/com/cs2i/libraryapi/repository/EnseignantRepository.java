package com.cs2i.libraryapi.repository;

import com.cs2i.libraryapi.entity.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

}
