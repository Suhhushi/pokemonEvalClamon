package org.suhhushi.pokemonevalclamont;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.suhhushi.pokemonevalclamont.dto.DresseurDto;
import org.suhhushi.pokemonevalclamont.model.Dresseur;
import org.suhhushi.pokemonevalclamont.model.Pokemon;
import org.suhhushi.pokemonevalclamont.repository.DresseurRepository;
import org.suhhushi.pokemonevalclamont.service.DresseurService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DresseurServiceTest {

    @Mock
    private DresseurRepository dresseurRepository;

    @InjectMocks
    private DresseurService dresseurService;

    private void dresseurAvec(Pokemon... pokemons) {
        Dresseur dresseur = new Dresseur(1L, "Sacha", "Kanto");
        for (Pokemon pokemon : pokemons) {
            dresseur.ajouterPokemon(pokemon);
        }
        when(dresseurRepository.findById(1L)).thenReturn(Optional.of(dresseur));
    }

    @Test
    void calculerNiveau_casNominal_retourneLaSommeDesNiveaux() {
        // Arrange : 2 types, aucun élite -> aucun bonus
        dresseurAvec(new Pokemon("Pikachu", "Électrik", 10),
                     new Pokemon("Salamèche", "Feu", 20));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30, niveau);
    }

    @Test
    void calculerNiveau_aucunPokemon_retourneUn() {
        // Arrange
        dresseurAvec();

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(1, niveau);
    }

    @Test
    void calculerNiveau_troisTypesDistincts_ajouteBonusDiversite() {
        // Arrange : 10 + 20 + 30 = 60, +20 diversité
        dresseurAvec(new Pokemon("Pikachu", "Électrik", 10),
                     new Pokemon("Salamèche", "Feu", 20),
                     new Pokemon("Carapuce", "Eau", 30));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(80, niveau);
    }

    @Test
    void calculerNiveau_deuxTypesSeulement_pasDeBonusDiversite() {
        // Arrange : 3 pokémons mais seulement 2 types
        dresseurAvec(new Pokemon("Carapuce", "Eau", 10),
                     new Pokemon("Stari", "Eau", 10),
                     new Pokemon("Salamèche", "Feu", 10));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30, niveau);
    }

    @Test
    void calculerNiveau_pokemonNiveau50_ajouteBonusElite() {
        // Arrange : le seuil 50 est inclus -> 50 + 10 = 60, +10 élite
        dresseurAvec(new Pokemon("Roucool", "Vol", 50),
                     new Pokemon("Rattata", "Vol", 10));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(70, niveau);
    }

    @Test
    void calculerNiveau_niveau49_pasDeBonusElite() {
        // Arrange
        dresseurAvec(new Pokemon("Roucool", "Vol", 49));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(49, niveau);
    }

    @Test
    void calculerNiveau_cumulDesBonus_additionneTout() {
        // Arrange : base 55+30+20 = 105, +20 diversité, +10 élite (Pikachu)
        dresseurAvec(new Pokemon("Pikachu", "Électrik", 55),
                     new Pokemon("Salamèche", "Feu", 30),
                     new Pokemon("Carapuce", "Eau", 20));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(135, niveau);
    }

    @Test
    void calculerNiveau_plusieursElites_cumuleChaqueBonus() {
        // Arrange : 60 + 70 = 130, +10 +10 élites, 1 seul type
        dresseurAvec(new Pokemon("Ectoplasma", "Spectre", 60),
                     new Pokemon("Noctali", "Spectre", 70));

        // Act
        int niveau = dresseurService.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(150, niveau);
    }

    @Test
    void calculerNiveau_dresseurInexistant_leveUneException() {
        // Arrange
        when(dresseurRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(DresseurNotFoundException.class,
                () -> dresseurService.calculerNiveauDresseur(99L));
    }

    @Test
    void getDresseurDto_dresseurExistant_retourneLeDto() {
        // Arrange
        dresseurAvec(new Pokemon("Pikachu", "Électrik", 55),
                     new Pokemon("Salamèche", "Feu", 30),
                     new Pokemon("Carapuce", "Eau", 20));

        // Act
        DresseurDto dto = dresseurService.getDresseurDto(1L);

        // Assert
        assertEquals("Sacha", dto.nomDresseur());
        assertEquals(3, dto.nombrePokemons());
        assertEquals(135, dto.niveauDresseur());
    }
}
