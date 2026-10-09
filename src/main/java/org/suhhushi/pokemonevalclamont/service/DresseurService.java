package org.suhhushi.pokemonevalclamont.service;

import org.springframework.stereotype.Service;
import org.suhhushi.pokemonevalclamont.DresseurNotFoundException;
import org.suhhushi.pokemonevalclamont.dto.DresseurDto;
import org.suhhushi.pokemonevalclamont.model.Dresseur;
import org.suhhushi.pokemonevalclamont.model.Pokemon;
import org.suhhushi.pokemonevalclamont.repository.DresseurRepository;

import java.util.List;

@Service
public class DresseurService {

    static final int NIVEAU_DRESSEUR_SANS_POKEMON = 1;
    static final int NB_TYPES_POUR_BONUS = 3;
    static final int BONUS_DIVERSITE = 20;
    static final int NIVEAU_ELITE = 50;
    static final int BONUS_ELITE = 10;

    private final DresseurRepository dresseurRepository;

    public DresseurService(DresseurRepository dresseurRepository) {
        this.dresseurRepository = dresseurRepository;
    }

    public DresseurDto getDresseurDto(Long id) {
        Dresseur dresseur = trouverDresseur(id);
        int niveau = calculerNiveau(dresseur);
        return new DresseurDto(dresseur.getNom(), dresseur.getPokemons().size(), niveau);
    }

    public int calculerNiveauDresseur(Long dresseurId) {
        return calculerNiveau(trouverDresseur(dresseurId));
    }

    private Dresseur trouverDresseur(Long id) {
        return dresseurRepository.findById(id)
                .orElseThrow(() -> new DresseurNotFoundException(id));
    }

    private int calculerNiveau(Dresseur dresseur) {
        List<Pokemon> pokemons = dresseur.getPokemons();
        if (pokemons.isEmpty()) {
            return NIVEAU_DRESSEUR_SANS_POKEMON;
        }
        return sommeDesNiveaux(pokemons) + bonusDiversite(pokemons) + bonusElite(pokemons);
    }

    private int sommeDesNiveaux(List<Pokemon> pokemons) {
        return pokemons.stream().mapToInt(Pokemon::getNiveau).sum();
    }

    private int bonusDiversite(List<Pokemon> pokemons) {
        long typesDistincts = pokemons
                .stream()
                .map(Pokemon::getType)
                .distinct()
                .count();
        return typesDistincts >= NB_TYPES_POUR_BONUS ? BONUS_DIVERSITE : 0;
    }

    private int bonusElite(List<Pokemon> pokemons) {
        long nbElites = pokemons
                .stream()
                .filter(p -> p.getNiveau() >= NIVEAU_ELITE)
                .count();
        return (int) nbElites * BONUS_ELITE;
    }
}
