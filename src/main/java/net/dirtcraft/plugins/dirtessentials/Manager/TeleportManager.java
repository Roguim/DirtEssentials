package net.dirtcraft.plugins.dirtessentials.Manager;

import java.util.*;

public class TeleportManager {
	private static final Set<UUID> tpDisabled = new HashSet<>();
	public static Map<UUID, UUID> teleportRequests = new HashMap<>();

	public static boolean isTpDisabled(UUID uuid) {
		return tpDisabled.contains(uuid);
	}

	public static void setTpDisabled(UUID uuid, boolean disabled) {
		if (disabled) tpDisabled.add(uuid);
		else tpDisabled.remove(uuid);
	}

	public static boolean hasTpaRequest(UUID uniqueId) {
		return teleportRequests.containsKey(uniqueId);
	}

	public static boolean hasTpahereRequest(UUID uniqueId) {
		return teleportRequests.containsValue(uniqueId);
	}

	public static UUID getTpaRequest(UUID uniqueId) {
		return teleportRequests.get(uniqueId);
	}

	public static UUID getTpahereRequest(UUID uniqueId) {
		for (Map.Entry<UUID, UUID> entry : teleportRequests.entrySet()) {
			if (entry.getValue().equals(uniqueId)) return entry.getKey();
		}
		return null;
	}

	public static void removeOpenTpRequest(UUID uniqueId) {
		if (hasTpaRequest(uniqueId)) teleportRequests.remove(uniqueId);
		else if (hasTpahereRequest(uniqueId)) teleportRequests.remove(getTpahereRequest(uniqueId));
	}

	public static void addRequest(UUID player, UUID target) {
		teleportRequests.put(player, target);
	}
}
