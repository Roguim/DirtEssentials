package net.dirtcraft.plugins.dirtessentials.Manager;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackManager implements Listener {
	private static final Map<UUID, Location> backLocations = new HashMap<>();

	public static void setBackLocation(UUID uuid, Location location) {
		backLocations.put(uuid, location);
	}

	public static Location getBackLocation(UUID uuid) {
		return backLocations.get(uuid);
	}

	public static boolean hasBackLocation(UUID uuid) {
		return backLocations.containsKey(uuid);
	}

	@EventHandler
	public static void playerTeleportEvent(PlayerTeleportEvent event) {
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
			setBackLocation(event.getPlayer().getUniqueId(), event.getFrom());
		}
	}
}
