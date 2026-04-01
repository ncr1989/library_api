package com.cs2i.libraryapi.repository;

import com.cs2i.libraryapi.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findByUtilisateurId(Long utilisateurId);
    void deleteByUtilisateurId(Long utilisateurId);
}
