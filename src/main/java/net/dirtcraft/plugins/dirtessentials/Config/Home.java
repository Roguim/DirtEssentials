package net.dirtcraft.plugins.dirtessentials.Config;

import org.bukkit.NamespacedKey;

public class Home {
	public int defaultHomes;
	public int homeCooldown;
	public int homesSize;
	public boolean buyHomeEnabled;
	public String buyHomeType;
	private String buyHomeItem;
	public double buyHomeMoney;

	public NamespacedKey getBuyHomeItem() {
		return NamespacedKey.fromString(buyHomeItem.split(" ")[0]);
	}

	public int getBuyHomeItemAmount() {
		return Integer.parseInt(buyHomeItem.split(" ")[1]);
	}
}
