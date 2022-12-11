package net.dirtcraft.plugins.dirtessentials.Data;

import java.util.ArrayList;

public class Recipe {
	private final String key;
	private final int amountCrafted;
	private final ArrayList<Ingredient> ingredients;

	public Recipe(String key, int amount) {
		this.key = key;
		this.amountCrafted = amount;
		this.ingredients = new ArrayList<>();
	}

	public String getKey() {
		return key;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}
}
