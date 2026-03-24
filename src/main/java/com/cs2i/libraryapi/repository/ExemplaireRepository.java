package com.cs2i.libraryapi.repository;

import com.cs2i.libraryapi.entity.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    Optional<Exemplaire> findByCodeBarre(int codeBarre);
}
