package net.dirtcraft.plugins.dirtessentials.Data;

import java.util.ArrayList;

public class Mods {
	private final String mod;
	private final ArrayList<Recipe> recipes;

	public Mods(String key) {
		this.mod = key;
		this.recipes = new ArrayList<>();
	}

	public String getMod() {
		return mod;
	}

	public ArrayList<Recipe> getRecipes() {
		return recipes;
	}
}
