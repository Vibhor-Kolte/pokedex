package com.vibhorkolte.pokedex.entities;

import lombok.Data;

@Data
public class Sprites {
	private String back_shiny_female;
	private String back_female;
	private String back_default;
	private String front_shiny_female;
	private String front_default;
	private String front_female;
	private String back_shiny;
	private String front_shiny;
	private Other other;

	@Data
	public static class Other{
		private Sprites dream_world;
		private Sprites home;
		private Sprites showdown;
	}
}