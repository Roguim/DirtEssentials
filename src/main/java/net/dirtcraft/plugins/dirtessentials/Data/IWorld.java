package net.dirtcraft.plugins.dirtessentials.Data;

import org.bukkit.World;

import java.util.UUID;

public class IWorld {
	private final World world;
	private final UUID uuid;

	public IWorld(World world, UUID uuid) {
		this.world = world;
		this.uuid = uuid;
	}

	public World getWorld() {
		return world;
	}

	public UUID getUuid() {
		return uuid;
	}
}
