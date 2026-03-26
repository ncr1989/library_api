package com.cs2i.libraryapi.config;

import com.cs2i.libraryapi.entity.*;
import com.cs2i.libraryapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;
    private final LivreRepository livreRepository;
    private final RevueRepository revueRepository;
    private final AuteurRepository auteurRepository;
    private final CategorieRepository categorieRepository;
    private final EmplacementRepository emplacementRepository;
    private final ExemplaireRepository exemplaireRepository;
    private final EmpruntRepository empruntRepository;
    private final ThemeRepository themeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ── ADRESSES ─────────────────────────────────────────────────────────
        Adresse adresse1 = new Adresse();
        adresse1.setNumero(12);
        adresse1.setRue("Rue de la Paix");
        adresse1.setVille("Paris");
        adresse1.setCodePostal(75001);

        Adresse adresse2 = new Adresse();
        adresse2.setNumero(45);
        adresse2.setRue("Avenue des Champs");
        adresse2.setVille("Lyon");
        adresse2.setCodePostal(69001);

        Adresse adresse3 = new Adresse();
        adresse3.setNumero(8);
        adresse3.setRue("Boulevard Victor Hugo");
        adresse3.setVille("Marseille");
        adresse3.setCodePostal(13001);

        adresseRepository.saveAll(List.of(adresse1, adresse2, adresse3));

        // ── BIBLIOTHECAIRE (ADMIN) ────────────────────────────────────────────
        Bibliothecaire admin = new Bibliothecaire();
        admin.setNom("Admin");
        admin.setPrenom("Super");
        admin.setEmail("admin@mail.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setTelephone("0600000001");
        admin.setAdresse(adresse1);
        admin.setNumeroMatricule(1001);
        utilisateurRepository.save(admin);

        // ── ENSEIGNANTS ───────────────────────────────────────────────────────
        Enseignant enseignant1 = new Enseignant();
        enseignant1.setNom("Martin");
        enseignant1.setPrenom("Alice");
        enseignant1.setEmail("alice@mail.com");
        enseignant1.setPassword(passwordEncoder.encode("password123"));
        enseignant1.setTelephone("0611111111");
        enseignant1.setAdresse(adresse2);
        enseignant1.setNomDepartement("Informatique");
        utilisateurRepository.save(enseignant1);

        Enseignant enseignant2 = new Enseignant();
        enseignant2.setNom("Bernard");
        enseignant2.setPrenom("Paul");
        enseignant2.setEmail("paul@mail.com");
        enseignant2.setPassword(passwordEncoder.encode("password123"));
        enseignant2.setTelephone("0622222222");
        enseignant2.setAdresse(adresse3);
        enseignant2.setNomDepartement("Mathématiques");
        utilisateurRepository.save(enseignant2);

        // ── ETUDIANTS ─────────────────────────────────────────────────────────
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setNom("Dupont");
        etudiant1.setPrenom("Jean");
        etudiant1.setEmail("jean@mail.com");
        etudiant1.setPassword(passwordEncoder.encode("password123"));
        etudiant1.setTelephone("0633333333");
        etudiant1.setAdresse(adresse1);
        etudiant1.setNumeroEtudiant(20240001);
        etudiant1.setAnneeUniversitaire(LocalDate.of(2024, 9, 1));
        utilisateurRepository.save(etudiant1);

        Etudiant etudiant2 = new Etudiant();
        etudiant2.setNom("Leroy");
        etudiant2.setPrenom("Sophie");
        etudiant2.setEmail("sophie@mail.com");
        etudiant2.setPassword(passwordEncoder.encode("password123"));
        etudiant2.setTelephone("0644444444");
        etudiant2.setAdresse(adresse2);
        etudiant2.setNumeroEtudiant(20240002);
        etudiant2.setAnneeUniversitaire(LocalDate.of(2024, 9, 1));
        utilisateurRepository.save(etudiant2);

        Etudiant etudiant3 = new Etudiant();
        etudiant3.setNom("Moreau");
        etudiant3.setPrenom("Lucas");
        etudiant3.setEmail("lucas@mail.com");
        etudiant3.setPassword(passwordEncoder.encode("password123"));
        etudiant3.setTelephone("0655555555");
        etudiant3.setAdresse(adresse3);
        etudiant3.setNumeroEtudiant(20240003);
        etudiant3.setAnneeUniversitaire(LocalDate.of(2024, 9, 1));
        utilisateurRepository.save(etudiant3);

        // ── AUTEURS ───────────────────────────────────────────────────────────
        Auteur hugo = new Auteur();
        hugo.setNom("Hugo");
        hugo.setPrenom("Victor");
        auteurRepository.save(hugo);

        Auteur exupery = new Auteur();
        exupery.setNom("de Saint-Exupéry");
        exupery.setPrenom("Antoine");
        auteurRepository.save(exupery);

        Auteur camus = new Auteur();
        camus.setNom("Camus");
        camus.setPrenom("Albert");
        auteurRepository.save(camus);

        Auteur orwell = new Auteur();
        orwell.setNom("Orwell");
        orwell.setPrenom("George");
        auteurRepository.save(orwell);

        Auteur tolkien = new Auteur();
        tolkien.setNom("Tolkien");
        tolkien.setPrenom("J.R.R.");
        auteurRepository.save(tolkien);

        // ── CATEGORIES ────────────────────────────────────────────────────────
        Categorie roman = new Categorie();
        roman.setNom("Roman");
        categorieRepository.save(roman);

        Categorie scienceFiction = new Categorie();
        scienceFiction.setNom("Science Fiction");
        categorieRepository.save(scienceFiction);

        Categorie jeunesse = new Categorie();
        jeunesse.setNom("Jeunesse");
        categorieRepository.save(jeunesse);

        // ── EMPLACEMENTS ──────────────────────────────────────────────────────
        Emplacement emp1 = new Emplacement();
        emp1.setNiveau(1); emp1.setNumeroTravee(1); emp1.setCategorie(roman);
        emplacementRepository.save(emp1);

        Emplacement emp2 = new Emplacement();
        emp2.setNiveau(1); emp2.setNumeroTravee(2); emp2.setCategorie(roman);
        emplacementRepository.save(emp2);

        Emplacement emp3 = new Emplacement();
        emp3.setNiveau(2); emp3.setNumeroTravee(1); emp3.setCategorie(jeunesse);
        emplacementRepository.save(emp3);

        Emplacement emp4 = new Emplacement();
        emp4.setNiveau(2); emp4.setNumeroTravee(2); emp4.setCategorie(roman);
        emplacementRepository.save(emp4);

        Emplacement emp5 = new Emplacement();
        emp5.setNiveau(3); emp5.setNumeroTravee(1); emp5.setCategorie(scienceFiction);
        emplacementRepository.save(emp5);

        Emplacement emp6 = new Emplacement();
        emp6.setNiveau(3); emp6.setNumeroTravee(2); emp6.setCategorie(scienceFiction);
        emplacementRepository.save(emp6);

        // ── LIVRES ────────────────────────────────────────────────────────────
        Livre lesMiserables = new Livre();
        lesMiserables.setTitre("Les Misérables");
        lesMiserables.setCaution(5.0f);
        lesMiserables.setAnneePublication(LocalDate.of(1862, 1, 1));
        lesMiserables.setIsbn(9782070409228L);
        lesMiserables.setAuteurs(List.of(hugo));
        livreRepository.save(lesMiserables);

        Livre lePetitPrince = new Livre();
        lePetitPrince.setTitre("Le Petit Prince");
        lePetitPrince.setCaution(3.0f);
        lePetitPrince.setAnneePublication(LocalDate.of(1943, 4, 6));
        lePetitPrince.setIsbn(9782070612758L);
        lePetitPrince.setAuteurs(List.of(exupery));
        livreRepository.save(lePetitPrince);

        Livre lEtranger = new Livre();
        lEtranger.setTitre("L'Étranger");
        lEtranger.setCaution(3.5f);
        lEtranger.setAnneePublication(LocalDate.of(1942, 5, 19));
        lEtranger.setIsbn(9782070360024L);
        lEtranger.setAuteurs(List.of(camus));
        livreRepository.save(lEtranger);

        Livre animalFarm = new Livre();
        animalFarm.setTitre("La Ferme des Animaux");
        animalFarm.setCaution(4.0f);
        animalFarm.setAnneePublication(LocalDate.of(1945, 8, 17));
        animalFarm.setIsbn(9782070368228L);
        animalFarm.setAuteurs(List.of(orwell));
        livreRepository.save(animalFarm);

        Livre leHobbit = new Livre();
        leHobbit.setTitre("Le Hobbit");
        leHobbit.setCaution(5.0f);
        leHobbit.setAnneePublication(LocalDate.of(1937, 9, 21));
        leHobbit.setIsbn(9782267012835L);
        leHobbit.setAuteurs(List.of(tolkien));
        livreRepository.save(leHobbit);

        // ── REVUES ────────────────────────────────────────────────────────────
        Revue scienceVie = new Revue();
        scienceVie.setTitre("Science & Vie");
        scienceVie.setCaution(2.0f);
        scienceVie.setAnneePublication(LocalDate.of(2024, 1, 1));
        scienceVie.setNumeroVolume(1260);
        scienceVie.setDateDeParution(LocalDate.of(2024, 1, 15));
        revueRepository.save(scienceVie);

        Revue leMonde = new Revue();
        leMonde.setTitre("Le Monde Diplomatique");
        leMonde.setCaution(2.5f);
        leMonde.setAnneePublication(LocalDate.of(2024, 2, 1));
        leMonde.setNumeroVolume(835);
        leMonde.setDateDeParution(LocalDate.of(2024, 2, 1));
        revueRepository.save(leMonde);

        // ── THEMES ────────────────────────────────────────────────────────────
        Theme themeLitterature = new Theme();
        themeLitterature.setNomTheme("Littérature française");
        themeLitterature.setOuvrage(lesMiserables);
        themeRepository.save(themeLitterature);

        Theme themePhilosophie = new Theme();
        themePhilosophie.setNomTheme("Philosophie");
        themePhilosophie.setOuvrage(lEtranger);
        themeRepository.save(themePhilosophie);

        Theme themeFantasy = new Theme();
        themeFantasy.setNomTheme("Fantasy");
        themeFantasy.setOuvrage(leHobbit);
        themeRepository.save(themeFantasy);

        // ── EXEMPLAIRES ───────────────────────────────────────────────────────
        Exemplaire ex1 = new Exemplaire();
        ex1.setCodeBarre(100001); ex1.setDisponible(false); ex1.setOuvrage(lesMiserables);
        exemplaireRepository.save(ex1);
        ex1.setEmplacement(emp1);
        exemplaireRepository.save(ex1);

        Exemplaire ex2 = new Exemplaire();
        ex2.setCodeBarre(100002); ex2.setDisponible(true); ex2.setOuvrage(lesMiserables);
        exemplaireRepository.save(ex2);
        ex2.setEmplacement(emp2);
        exemplaireRepository.save(ex2);

        Exemplaire ex3 = new Exemplaire();
        ex3.setCodeBarre(100003); ex3.setDisponible(false); ex3.setOuvrage(lePetitPrince);
        exemplaireRepository.save(ex3);
        ex3.setEmplacement(emp3);
        exemplaireRepository.save(ex3);

        Exemplaire ex4 = new Exemplaire();
        ex4.setCodeBarre(100004); ex4.setDisponible(true); ex4.setOuvrage(lEtranger);
        exemplaireRepository.save(ex4);
        ex4.setEmplacement(emp4);
        exemplaireRepository.save(ex4);

        Exemplaire ex5 = new Exemplaire();
        ex5.setCodeBarre(100005); ex5.setDisponible(false); ex5.setOuvrage(leHobbit);
        exemplaireRepository.save(ex5);
        ex5.setEmplacement(emp5);
        exemplaireRepository.save(ex5);

        Exemplaire ex6 = new Exemplaire();
        ex6.setCodeBarre(100006); ex6.setDisponible(true); ex6.setOuvrage(animalFarm);
        exemplaireRepository.save(ex6);
        ex6.setEmplacement(emp6);
        exemplaireRepository.save(ex6);

        // ── EMPRUNTS ──────────────────────────────────────────────────────────

        // Emprunt retourné à temps
        Emprunt emprunt1 = new Emprunt();
        emprunt1.setDateDebut(LocalDate.of(2024, 1, 1));
        emprunt1.setDateFinPrevue(LocalDate.of(2024, 1, 15));
        emprunt1.setDateRetourEffective(LocalDate.of(2024, 1, 14));
        emprunt1.setEnRetard(false);
        emprunt1.setMontantAmende(0.0);
        emprunt1.setUtilisateur(etudiant1);
        emprunt1.setExemplaire(ex1);
        empruntRepository.save(emprunt1);

        // Emprunt en retard avec amende
        Emprunt emprunt2 = new Emprunt();
        emprunt2.setDateDebut(LocalDate.of(2026, 3, 10));
        emprunt2.setDateFinPrevue(LocalDate.of(2026, 3, 25));
        emprunt2.setDateRetourEffective(null);
        emprunt2.setEnRetard(true);
        emprunt2.setMontantAmende(15.5);
        emprunt2.setUtilisateur(etudiant2);
        emprunt2.setExemplaire(ex3);
        empruntRepository.save(emprunt2);

        // Emprunt en cours
        Emprunt emprunt3 = new Emprunt();
        emprunt3.setDateDebut(LocalDate.now().minusDays(5));
        emprunt3.setDateFinPrevue(LocalDate.now().plusDays(9));
        emprunt3.setDateRetourEffective(null);
        emprunt3.setEnRetard(false);
        emprunt3.setMontantAmende(0.0);
        emprunt3.setUtilisateur(etudiant1);
        emprunt3.setExemplaire(ex5);
        empruntRepository.save(emprunt3);

        // Emprunt enseignant retourné
        Emprunt emprunt4 = new Emprunt();
        emprunt4.setDateDebut(LocalDate.of(2024, 2, 1));
        emprunt4.setDateFinPrevue(LocalDate.of(2024, 2, 28));
        emprunt4.setDateRetourEffective(LocalDate.of(2024, 2, 25));
        emprunt4.setEnRetard(false);
        emprunt4.setMontantAmende(0.0);
        emprunt4.setUtilisateur(enseignant1);
        emprunt4.setExemplaire(ex4);
        empruntRepository.save(emprunt4);

        // Emprunt étudiant en cours
        Emprunt emprunt5 = new Emprunt();
        emprunt5.setDateDebut(LocalDate.now().minusDays(2));
        emprunt5.setDateFinPrevue(LocalDate.now().plusDays(12));
        emprunt5.setDateRetourEffective(null);
        emprunt5.setEnRetard(false);
        emprunt5.setMontantAmende(0.0);
        emprunt5.setUtilisateur(etudiant3);
        emprunt5.setExemplaire(ex6);
        empruntRepository.save(emprunt5);


        System.out.println("   Admin:      admin@mail.com  / admin123");
        System.out.println("   Enseignant: alice@mail.com  / password123");
        System.out.println("   Étudiant:   jean@mail.com   / password123");
    }
}