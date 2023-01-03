package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

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

	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		World world = player.getWorld();

		String worldname = world.getName();
		String environment = world.getEnvironment().toString();

		if (Utilities.config.general.backWorldsWhitelist) {
			if (!Utilities.config.general.backWorlds.contains(worldname + ":" + environment)) return;
		} else {
			if (Utilities.config.general.backWorlds.contains(worldname + ":" + environment)) return;
		}

		BackManager.setBackLocation(player.getUniqueId(), player.getLocation());
		player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.RED + "/back" + ChatColor.GRAY + " to teleport back to where you died!");
	}
}
