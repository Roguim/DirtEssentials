package net.dirtcraft.plugins.dirtessentials.Data;



import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class Mod {
	@JsonProperty("namespace")
	private String namespace;
	@JsonProperty("display_name")
	private String displayName;
	@JsonProperty("icon")
	private String icon;
	@JsonProperty("items")
	private List<Item> items;

	@JsonProperty("namespace")
	public String getNamespace() {
		return namespace;
	}

	@JsonProperty("items")
	public List<Item> getItems() {
		return items;
	}
}
