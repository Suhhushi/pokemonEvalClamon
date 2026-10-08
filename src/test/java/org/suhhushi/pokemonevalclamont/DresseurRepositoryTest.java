package com.ligue.pokemon.repository;

import com.ligue.pokemon.model.Dresseur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Les données viennent de src/test/resources/data.sql (base H2)
@DataJpaTest
class DresseurRepositoryTest {

    @Autowired
    private DresseurRepository dresseurRepository;

    @Test
    void findById_dresseurExistant_retourneLeDresseurAvecSesPokemons() {
        // Act
        Optional<Dresseur> resultat = dresseurRepository.findById(1L);

        // Assert
        assertTrue(resultat.isPresent());
        assertEquals("Sacha", resultat.get().getNom());
        assertEquals("Kanto", resultat.get().getRegion());
        assertEquals(3, resultat.get().getPokemons().size());
    }

    @Test
    void findById_dresseurInexistant_retourneVide() {
        // Act
        Optional<Dresseur> resultat = dresseurRepository.findById(99L);

        // Assert
        assertTrue(resultat.isEmpty());
    }

    @Test
    void findById_dresseurSansPokemon_retourneListeVide() {
        // Act
        Dresseur pierre = dresseurRepository.findById(3L).orElseThrow();

        // Assert
        assertEquals("Pierre", pierre.getNom());
        assertTrue(pierre.getPokemons().isEmpty());
    }

    @Test
    void findByRegion_kanto_retourneTroisDresseurs() {
        // Act
        List<Dresseur> resultat = dresseurRepository.findByRegion("Kanto");

        // Assert
        assertEquals(3, resultat.size());
    }

    @Test
    void findByNom_morgane_retourneLeBonDresseur() {
        // Act
        Dresseur morgane = dresseurRepository.findByNom("Morgane").orElseThrow();

        // Assert
        assertEquals("Johto", morgane.getRegion());
        assertEquals(1, morgane.getPokemons().size());
    }

    @Test
    void save_nouveauDresseur_genereUnIdEtAugmenteLeNombreDeDresseurs() {
        // Arrange
        Dresseur regis = new Dresseur(null, "Régis", "Hoenn");

        // Act
        Dresseur sauvegarde = dresseurRepository.save(regis);

        // Assert
        assertNotNull(sauvegarde.getId());
        assertEquals(5, dresseurRepository.count());
    }
}
