package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Data.Home;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HomeManager {
	public static final Map<UUID, List<Home>> homes = new HashMap<>();
	public static final Map<UUID, LocalDateTime> cooldowns = new HashMap<>();

	public static void init() {
		DatabaseOperations.initHomes(homes::putAll);
	}

	public static void addHome(UUID uuid, Home home) {
		if (homes.containsKey(uuid)) {
			homes.get(uuid).add(home);
		} else {
			homes.put(uuid, new ArrayList<>(Collections.singletonList(home)));
		}
	}

	public static void removeHome(UUID uuid, String home) {
		homes.get(uuid).removeIf(home1 -> home1.getName().equalsIgnoreCase(home));
	}

	public static List<Home> getHomes(UUID uuid) {
		return homes.get(uuid);
	}

	public static Home getHome(UUID uuid, String name) {
		for (Home home : homes.get(uuid)) {
			if (home.getName().equalsIgnoreCase(name))
				return home;
		}
		return null;
	}

	public static boolean hasHome(UUID uuid, String name) {
		if (!hasHomes(uuid))
			return false;

		for (Home home : homes.get(uuid)) {
			if (home.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	public static boolean hasHomes(UUID uuid) {
		return homes.containsKey(uuid);
	}

	public static int getHomeCount(UUID uuid) {
		if (!hasHomes(uuid))
			return 0;

		return homes.get(uuid).size();
	}

	public static boolean isOnCooldown(UUID uniqueId) {
		int cooldown = Utilities.config.home.homeCooldown;

		if (!cooldowns.containsKey(uniqueId)) {
			cooldowns.put(uniqueId, LocalDateTime.now().plusSeconds(cooldown));
			return false;
		}

		if (cooldowns.get(uniqueId).isBefore(LocalDateTime.now())) {
			cooldowns.put(uniqueId, LocalDateTime.now().plusSeconds(cooldown));
			return false;
		}

		return true;
	}

	public static long getCooldown(UUID uniqueId) {
		return LocalDateTime.now().until(cooldowns.get(uniqueId), ChronoUnit.SECONDS);
	}
}
