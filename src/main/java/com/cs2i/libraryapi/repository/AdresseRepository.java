package com.cs2i.libraryapi.repository;

import com.cs2i.libraryapi.entity.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {}
