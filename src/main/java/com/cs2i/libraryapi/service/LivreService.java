package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Livre;
import com.cs2i.libraryapi.repository.LivreRepository;
import com.cs2i.libraryapi.service.CrudService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivreService implements CrudService<Livre, Long> {

    private final LivreRepository livreRepository;

    @Override
    public List<Livre> findAll() {
        return livreRepository.findAll();
    }

    @Override
    public Livre findById(Long id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livre non trouvé"));
    }

    @Override
    public Livre create(Livre entity) {
        return livreRepository.save(entity);
    }

    @Override
    public Livre update(Long id, Livre entity) {
        Livre livre = findById(id);
        livre.setTitre(entity.getTitre());
        livre.setCaution(entity.getCaution());
        livre.setAnneePublication(entity.getAnneePublication());
        livre.setAuteurs(entity.getAuteurs());
        livre.setThemes(entity.getThemes());
        livre.setExemplaires(entity.getExemplaires());
        livre.setIsbn(entity.getIsbn());
        return livreRepository.save(livre);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livre non trouvé");
        }
        try {
            livreRepository.deleteById(id);
            livreRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce livre ne peut pas être supprimé car il possède des exemplaires liés à des emprunts."
            );
        }
    }

    public List<Livre> findByTitreContainingIgnoreCase(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre); // fixed
    }
}
