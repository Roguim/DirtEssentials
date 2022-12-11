package net.dirtcraft.plugins.dirtessentials.Data;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class Worth {
	@JsonProperty("server")
	private String server;
	@JsonProperty("display_name")
	private String displayName;
	@JsonProperty("logo")
	private String logo;
	@JsonProperty("mods")
	private List<Mod> mods;

	@JsonProperty("server")
	public String getServer() {
		return server;
	}

	@JsonProperty("mods")
	public List<Mod> getMods() {
		return mods;
	}
}
