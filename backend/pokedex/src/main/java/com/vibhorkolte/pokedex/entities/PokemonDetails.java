package com.vibhorkolte.pokedex.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PokemonDetails {
    private String name;
    private int id;
    //private boolean is_default;
    private String location_area_encounters;
    private Species species;
    private int order;
    private Cries cries;
    private Integer base_experience;
    private Integer weight;
    private Integer height;

    private List<Type> types;
    private List<Ability> abilities;
    private List<Forms> forms;

    private Sprites sprites;

    private List<GameIndex> game_indices;
    private List<Object> held_items; // Adjust type if structure is known
    private List<Stat> stats;

    private List<Ability> past_abilities;
    private List<Type> past_types;

    @Data
    public static class GameIndex {
        private int game_index;
        private Version version;
    }

    @Data
    public static class Version {
        private String name;
        private String url;
    }

    @Data
    public static class Species {
        private String name;
        private String url;
    }

    @Data
    public static class Stat {
        private int base_stat;
        private int effort;
        private StatDetail stat;
    }

    @Data
    public static class StatDetail {
        private String name;
        private String url;
    }

    @Data
    public static class Cries{
        private String latest;
        private String legacy;
    }

    @Data
    public static class Forms{
        private String name;
        private String url;
    }

    @Data
    public static class Type {
        private int slot;
        private TypeDetail type;

        @Data
        public static class TypeDetail {
            private String name;
            private String url;
        }
    }

    @Data
    public static class Ability {
        private AbilityDetail ability;
        private boolean is_hidden;
        private int slot;

        @Data
        public static class AbilityDetail {
            private String name;
            private String url;
        }
    }
}
