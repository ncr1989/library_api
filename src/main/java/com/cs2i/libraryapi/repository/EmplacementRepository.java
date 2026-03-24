package com.cs2i.libraryapi.repository;

import com.cs2i.libraryapi.entity.Emplacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmplacementRepository extends JpaRepository<Emplacement, Long> {}
