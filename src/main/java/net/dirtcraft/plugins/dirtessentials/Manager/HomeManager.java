package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Data.Home;
import net.dirtcraft.plugins.dirtessentials.Data.PlayerHomeData;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HomeManager {
	public static final Map<UUID, PlayerHomeData> homes = new HashMap<>();
	public static final Map<UUID, LocalDateTime> cooldowns = new HashMap<>();

	public static void init() {
		DatabaseOperations.initHomes(homes::putAll);
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

	public static int getAvailableHomeCount(UUID uniqueId) {
		return homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomesAvailable();
	}

	public static void addNewHomeData(UUID uniqueId) {
		homes.put(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>()));
		DatabaseOperations.addNewHomeData(uniqueId);
	}

	public static boolean hasAvailableHomes(UUID uniqueId) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		return homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomes().size() < homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomesAvailable();
	}

	public static boolean hasHome(UUID uniqueId, String home) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		return homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomes().stream().anyMatch(h -> h.getName().equalsIgnoreCase(home));
	}

	public static void addHome(UUID uniqueId, String name, Location location) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).addHome(new Home(uniqueId, name, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
	}

	public static boolean hasHomes(UUID uniqueId) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		return homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomes().size() > 0;
	}

	public static Home getHome(UUID uniqueId, String name) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		return homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomes().stream().filter(h -> h.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public static List<Home> getHomes(UUID uniqueId) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		return homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomes();
	}

	public static void removeHome(UUID uniqueId, String name) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomes().removeIf(h -> h.getName().equalsIgnoreCase(name));
	}

	public static void addHomeBalance(UUID uniqueId, int amount) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).setHomesAvailable(homes.get(uniqueId).getHomesAvailable() + amount);
	}

	public static int getHomeBalance(UUID uniqueId) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		return homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).getHomesAvailable();
	}

	public static void removeHomeBalance(UUID uniqueId, int amount) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).setHomesAvailable(homes.get(uniqueId).getHomesAvailable() - amount);
	}

	public static void setHomeBalance(UUID uniqueId, int amount) {
		if (!homes.containsKey(uniqueId)) addNewHomeData(uniqueId);
		homes.getOrDefault(uniqueId, new PlayerHomeData(uniqueId, Utilities.config.home.defaultHomes, new ArrayList<>())).setHomesAvailable(amount);
	}
}
