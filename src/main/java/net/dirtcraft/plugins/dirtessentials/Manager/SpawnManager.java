package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Data.Spawn;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnManager {
	private static Spawn spawn;

	public static void init() {
		DatabaseOperations.getSpawn(s -> {
			if (s == null) {
				spawn = new Spawn(Bukkit.getWorlds().get(0).getSpawnLocation());
			} else {
				spawn = s;
			}

			Bukkit.getWorld(spawn.getWorld()).setSpawnLocation(spawn.getLocation());
		});
	}

	public static Location getLocation() {
		return spawn.getLocation();
	}

	public static void setLocation(Location location) {
		spawn = new Spawn(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		DatabaseOperations.setSpawn(spawn);
	}
}
