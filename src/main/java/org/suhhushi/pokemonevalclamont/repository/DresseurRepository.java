package org.suhhushi.pokemonevalclamont.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.suhhushi.pokemonevalclamont.model.Dresseur;

import java.util.List;
import java.util.Optional;

public interface DresseurRepository extends JpaRepository<Dresseur, Long> {

    List<Dresseur> findByRegion(String region);

    Optional<Dresseur> findByNom(String nom);
}
