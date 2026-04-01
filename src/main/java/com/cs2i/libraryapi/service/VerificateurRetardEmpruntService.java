package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.entity.Emprunt;
import com.cs2i.libraryapi.repository.EmpruntRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VerificateurRetardEmpruntService {

    private final EmpruntRepository empruntRepository;
    private final EmpruntService empruntService;

    @Scheduled(cron = "0 0 2 * * *")
    public void verifierTousLesEmpruntsEnRetard() {
        List<Emprunt> emprunts = empruntRepository.findAll();

        for (Emprunt emprunt : emprunts) {
            if (emprunt.getDateFinPrevue() != null && emprunt.getDateRetourEffective() == null) {
                if (LocalDate.now().isAfter(emprunt.getDateFinPrevue())) {
                    empruntService.verifierRetard(emprunt);
                }
            }
        }
    }
}
