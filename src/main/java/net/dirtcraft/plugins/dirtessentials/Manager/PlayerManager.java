package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Data.PlayerData;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.util.*;
import java.util.logging.Level;

public class PlayerManager {
	private static final Map<UUID, PlayerData> playerData = new HashMap<>();

	public static void init() {
		DatabaseOperations.initPlayers(data -> {
			playerData.putAll(data);
			Utilities.log(Level.INFO, "Loaded " + data.size() + " player data entries.");
		});
	}

	public static PlayerData getPlayerData(UUID uuid) {
		return playerData.get(uuid);
	}

	public static void addPlayerData(UUID uuid, PlayerData data) {
		playerData.put(uuid, data);
	}

	public static boolean hasPlayerData(UUID uuid) {
		return playerData.containsKey(uuid);
	}

	public static List<String> getAllPlayerNames() {
		List<String> playerNames = new ArrayList<>();

		for (PlayerData data : playerData.values()) {
			playerNames.add(data.getUsername());
		}

		return playerNames;
	}

	public static UUID getUuid(String username) {
		for (PlayerData data : playerData.values()) {
			if (data.getUsername().equalsIgnoreCase(username)) return data.getUuid();
		}

		return null;
	}

	public static String getUsername(UUID uuid) {
		return playerData.get(uuid).getUsername();
	}

	public static Map<UUID, String> getPlayerMap() {
		Map<UUID, String> playerMap = new HashMap<>();

		for (PlayerData data : playerData.values()) {
			playerMap.put(data.getUuid(), data.getUsername());
		}

		return playerMap;
	}

	public static boolean isStaff(UUID uuid) {
		if (playerData.containsKey(uuid)) {
			return playerData.get(uuid).isStaff();
		}

		return false;
	}
}
