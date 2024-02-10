# Darwin Game

This project has been developed for an object-oriented programming course at AGH.

## Grade

Final grade was **27/32** points:

* **12/16** for code architecture, clarity, optimization, exception handling and tests
* **15/16** for functionality

*Grade was based on [this commit](https://github.com/karmatys8/PO_2023_projekt/commit/86ab94e86154a2b34df436e70ab109b2758de43d).*

## Task

### Animal

Animals and plants are characterized by coordinates. Each animal has energy, orientation, and a genotype of length **n**.
There are 8 possible orientations, corresponding to cardinal directions. Animals turn right by **x** * 45°,
where **x** is a value in their genotype. After a turn, an animal tries to move one tile forward.
After the **n**th value in the genotype, the sequence wraps back to the 1st value the next day.

### Eating and reproducing

When an animal encounters a plant, it consumes it, increasing its energy by a set value. Reproduction occurs when two animals have sufficient energy.
After reproduction both parents lose a certain amount of energy that sums up to a starting energy of their child.

Offspring inherits their genotype based on their parents' energy proportions.
For instance if 1st parent has 50 energy and the 2nd one has 150 animals genotype will be 25% of 1st parent and 75% of the 2nd.
It is chosen randomly which parents left side of genotype is chosen, and the other passes down his right part.

### Simulation

The simulation proceeds daily in the following order:

1. Dead animals are removed
2. Animals turn and move
3. Animals eat plants
4. If animals share a tile they try to reproduce
5. New plants grow

---

Every simulation has those parameters:

* height and width
* variant of map
* starting count of plants
* energy for eating a plant
* number of plants growing per day
* starting count of animals
* starting energy of animals
* energy necessary for animal to reproduce
* energy used by animal to reproduce
* minimal and maximal amount of mutation *can be 0*
* variant of mutation
* length of animals genotypes

### Modifications

Students' groups received random modifications from 24 possible combinations.

#### Map

Map is rectangular and made of square tiles.

* **Globe** - If animal tries to go out of up or down bounds it stays at the same tile and turns 180°. It has priority over next rule.
Left and right boundary of map connect, if animal goes out of bound on eater said he arrives at the other.
* **Underground Tunnels** - if animal tries to go out of bounds it stays at the same tile.
On the map appear pairs of tunnels that connect two different places to each other.

#### Plants

With accordance to Pareto principle, plants grow with 80% of possibility on preferred tiles, which cover 20% of the map.

* **Wooded Equators** - plants prefer to grow on the equator

#### Animals

* **Full Randomness** - mutation changes a gene to a random different one

### Requirements

1. Application should contain GUI made with JavaFX.
2. User should be able to peak configurations
   1. Pre-made one
   2. Picking every value by himself
   3. Save it to use later
3. Every simulation should have a separate window. There can be multiple simulations running at once, each one with different map.
4. Plants and animals positions and animals energy should be shown on GUI.
5. Simulation should be possible to pause and start.
6. These stats should be shown in-real time:
   1. Number of alive animals
   2. Number of plants on map
   3. Number of empty tiles
   4. Most common genotype
   5. Average energy level for alive animals
   6. Average life length for dead animals
   7. Average number of children for alive animals
7. When simulation is paused, user can choose an animal to track.
From this points animation should show these stats as long as user does not untrack this animal:
   1. Genotype
   2. Active gene
   3. Energy
   4. Number of plants eaten so far
   5. Number of children
   6. Number of descendants
   7. Days lived *if alive*
   8. Day of death *if dead*
8. When animation is paused, user shod also be able to see which animals have the most common genotype and see tiles preferred by plants.
9. While setting up simulation user can choose to export simulations statistics to CSV file.
They should be written each day and the file needs to be openable.
10. Application has to be buildable and runnable with Gradle.

### Additional info

* Newborn animals' orientation and current gene are chosen randomly.
* Newborn animals are placed on the same tile as their parents.
* UI may restrict simulation parameters.
* Energy ia an integer; 1 energy is used per move.
* If on the same tile animals fight over plant or chance to mate *only one matting per tile* they are picked based on:
  1. Who has more energy
  2. If not decided, who is older
  3. If not decided, who has more children
  4. If not decided, we pick a winner randomly from the strongest.
* Plants can grow where animals are. Animals eat only when entering the tile.
* Plants do not grow without available tiles.