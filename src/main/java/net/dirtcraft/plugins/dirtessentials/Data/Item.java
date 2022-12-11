package net.dirtcraft.plugins.dirtessentials.Data;

import org.bukkit.NamespacedKey;
import org.codehaus.jackson.annotate.JsonProperty;

public class Item {
	@JsonProperty("id")
	private String id;
	@JsonProperty("nbt")
	private String nbt;
	@JsonProperty("hash")
	private String hash;
	@JsonProperty("price")
	private double price;
	@JsonProperty("display_name")
	private String displayName;

	@JsonProperty("id")
	public NamespacedKey getId() {
		return NamespacedKey.fromString(id);
	}

	@JsonProperty("nbt")
	public String getNbt() {
		return nbt;
	}

	@JsonProperty("price")
	public double getPrice() {
		return price;
	}

	@JsonProperty("display_name")
	public String getDisplayName() {
		return displayName;
	}
}
