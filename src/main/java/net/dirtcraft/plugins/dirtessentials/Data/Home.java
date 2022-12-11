package net.dirtcraft.plugins.dirtessentials.Data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class Home {
	private final UUID uuid;
	private final String name;
	private final String world;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;

	public Home(UUID uuid, String name, String world, double x, double y, double z, float yaw, float pitch) {
		this.uuid = uuid;
		this.name = name;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public String getName() {
		return name;
	}

	public String getWorld() {
		return world;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public Location getLocation() {
		World world = Bukkit.getWorld(getWorld());
		if (world == null) return null;

		return new Location(world, getX(), getY(), getZ(), getYaw(), getPitch());
	}

	public UUID getUuid() {
		return uuid;
	}
}
