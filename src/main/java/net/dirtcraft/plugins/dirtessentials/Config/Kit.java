package net.dirtcraft.plugins.dirtessentials.Config;

import java.util.List;

public class Kit {
	public Kit(String name, int cooldown, String cooldownType, List<String> commands) {
		this.name = name;
		this.cooldown = cooldown;
		this.cooldownType = cooldownType;
		this.commands = commands;
	}

	public String name;
	public int cooldown;
	public String cooldownType;
	public List<String> commands;
}
