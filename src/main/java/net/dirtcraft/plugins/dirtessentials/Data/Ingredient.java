package net.dirtcraft.plugins.dirtessentials.Data;

public class Ingredient {
	private final String item;
	private final int amount;

	public Ingredient(String item, int amount) {
		this.item = item;
		this.amount = amount;
	}

	public String getItem() {
		return item;
	}

	public int getAmount() {
		return amount;
	}
}
