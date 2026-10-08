# Pokémon Management API

Microservice Spring Boot de la Ligue Pokémon : consultation d'un dresseur et calcul de son niveau.
Projet de tests (Master 2 I) : suite de tests unitaires sur les 3 couches (Repository, Service, Controller) en respectant la structure **Arrange - Act - Assert**.

## Stack technique

- Java 17
- Spring Boot 3.3 (Web, Data JPA)
- Maven
- MySQL (WAMP) pour l'application
- H2 en mémoire pour les tests
- JUnit 5, Mockito, MockMvc

## Modèle de données

| Entité | Champs |
|--------|--------|
| `Dresseur` | id, nom, region, pokemons (`@OneToMany`) |
| `Pokemon` | id, nom, type, niveau, dresseur (`@ManyToOne`) |

## Règle métier : niveau du dresseur

| Règle | Valeur |
|-------|--------|
| Base | somme des niveaux de tous ses Pokémon |
| Bonus diversité | +20 si au moins 3 types distincts |
| Bonus élite | +10 par Pokémon de niveau ≥ 50 |
| Aucun Pokémon | niveau = 1 |

Exemple : Pikachu (Électrik, 55), Salamèche (Feu, 30), Carapuce (Eau, 20)
→ 105 + 20 (diversité) + 10 (élite) = **135**

## API

### `GET /dresseurs/{id}`

**200 OK**
```json
{
  "nomDresseur": "Sacha",
  "nombrePokemons": 3,
  "niveauDresseur": 135
}
```

**404 Not Found** : le dresseur n'existe pas.

## Architecture

```
src/main/java/com/ligue/pokemon
├── controller/   DresseurController
├── service/      DresseurService
├── repository/   DresseurRepository
├── model/        Dresseur, Pokemon
├── dto/          DresseurDto
└── exception/    DresseurNotFoundException

src/test
├── java/com/ligue/pokemon
│   ├── repository/   DresseurRepositoryTest   (@DataJpaTest, H2)
│   ├── service/      DresseurServiceTest      (@Mock / @InjectMocks)
│   └── controller/   DresseurControllerTest   (@WebMvcTest, MockMvc)
└── resources/
    ├── application.properties   (config H2)
    └── data.sql                 (jeu de données de test)
```

## Installation

### 1. Base de données (WAMP)

1. Démarrer WAMP (MySQL actif).
2. Les identifiants par défaut sont `root` sans mot de passe. Les modifier si besoin dans `src/main/resources/application.properties`.
3. La base `pokemon_db` et les tables sont créées automatiquement au premier lancement.

Données de démonstration (facultatif, à exécuter dans phpMyAdmin après le premier lancement) :

```sql
USE pokemon_db;

INSERT INTO dresseur (id, nom, region) VALUES
(1, 'Sacha', 'Kanto'), (2, 'Ondine', 'Kanto'),
(3, 'Pierre', 'Kanto'), (4, 'Morgane', 'Johto');

INSERT INTO pokemon (id, nom, type_pokemon, niveau, dresseur_id) VALUES
(1, 'Pikachu', 'Électrik', 55, 1),
(2, 'Salamèche', 'Feu', 30, 1),
(3, 'Carapuce', 'Eau', 20, 1),
(4, 'Stari', 'Eau', 25, 2),
(5, 'Staross', 'Eau', 40, 2),
(6, 'Ectoplasma', 'Spectre', 60, 4);
```

### 2. Lancer l'application

```bash
mvn spring-boot:run
```

Puis ouvrir : http://localhost:8080/dresseurs/1

## Lancer les tests

```bash
mvn test
```

Les tests n'ont pas besoin de MySQL : ils utilisent H2 en mémoire, alimentée par `src/test/resources/data.sql`.

| Classe de test | Couche | Outils | Nb de tests |
|----------------|--------|--------|-------------|
| `DresseurRepositoryTest` | Repository | `@DataJpaTest`, H2, `data.sql` | 6 |
| `DresseurServiceTest` | Service | `@Mock`, `@InjectMocks`, `when().thenReturn()` | 10 |
| `DresseurControllerTest` | Controller | `@WebMvcTest`, `MockMvc` | 3 |

## Commandes Git utiles

```bash
git init
git add .
git commit -m "Pokémon API : tests Repository, Service, Controller"
git branch -M main
git remote add origin <url-du-depot>
git push -u origin main
```
