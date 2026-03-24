package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Theme;
import com.cs2i.libraryapi.repository.ThemeRepository;
import com.cs2i.libraryapi.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService implements CrudService<Theme, Long> {

    private final ThemeRepository themeRepository;

    @Override
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Override
    public Theme findById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thème non trouvé"));
    }

    @Override
    public Theme create(Theme entity) {
        return themeRepository.save(entity);
    }

    @Override
    public Theme update(Long id, Theme entity) {
        Theme theme = findById(id);
        theme.setNomTheme(entity.getNomTheme());
        theme.setOuvrage(entity.getOuvrage());
        return themeRepository.save(theme);
    }

    @Override
    public void delete(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thème non trouvé");
        }
        themeRepository.deleteById(id);
    }
}
