package com.cs2i.libraryapi.controller;

import com.cs2i.libraryapi.entity.Theme;
import com.cs2i.libraryapi.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    public List<Theme> getAll() {
        return themeService.findAll();
    }

    @GetMapping("/{id}")
    public Theme getById(@PathVariable Long id) {
        return themeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Theme create(@RequestBody Theme theme) {
        return themeService.create(theme);
    }

    @PutMapping("/{id}")
    public Theme update(@PathVariable Long id, @RequestBody Theme updatedTheme) {
        return themeService.update(id,updatedTheme);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        themeService.delete(id);
    }
}
