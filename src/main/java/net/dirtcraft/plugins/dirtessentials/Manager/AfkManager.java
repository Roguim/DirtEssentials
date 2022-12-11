package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class AfkManager implements Listener {
	private static final Map<UUID, Long> afkPlayers = new HashMap<>();
	private static final Map<UUID, Long> activePlayers = new HashMap<>();
	private static BukkitTask afkTask;

	public static void setAfk(UUID uuid) {
		if (afkPlayers.containsKey(uuid)) return;

		afkPlayers.put(uuid, System.currentTimeMillis());
		activePlayers.remove(uuid);

		Player player = Bukkit.getPlayer(uuid);
		if (player == null) return;

		player.sendMessage(ChatColor.GRAY + "You are now AFK!");
		Bukkit.getOnlinePlayers().stream().filter(p -> !p.getName().equals(player.getName())).forEach(p -> p.sendMessage(ChatColor.GRAY + player.getName() + " is now AFK!"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + player.getName() + " is now AFK!");
	}

	public static void removeAfk(UUID uuid) {
		if (!afkPlayers.containsKey(uuid)) return;

		afkPlayers.remove(uuid);
		activePlayers.put(uuid, System.currentTimeMillis());

		Player player = Bukkit.getPlayer(uuid);
		if (player == null) return;

		player.sendMessage(ChatColor.GRAY + "You are no longer AFK!");
		Bukkit.getOnlinePlayers().stream().filter(p -> !p.getName().equals(player.getName())).forEach(p -> p.sendMessage(ChatColor.GRAY + player.getName() + " is no longer AFK!"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + player.getName() + " is no longer AFK!");
	}

	public static boolean isPlayerAfk(UUID uuid) {
		return afkPlayers.containsKey(uuid);
	}

	@EventHandler
	public static void onPlayerJoin(PlayerJoinEvent event) {
		activePlayers.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
		afkPlayers.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public static void onPlayerLeave(PlayerQuitEvent event) {
		activePlayers.remove(event.getPlayer().getUniqueId());
		afkPlayers.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public static void onPlayerMove(PlayerMoveEvent event) {
		if (isPlayerAfk(event.getPlayer().getUniqueId())) {
			removeAfk(event.getPlayer().getUniqueId());
		} else {
			activePlayers.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
		}
	}

	@EventHandler
	public static void onUseCommand(PlayerCommandPreprocessEvent event) {
		if (isPlayerAfk(event.getPlayer().getUniqueId())) {
			removeAfk(event.getPlayer().getUniqueId());
		} else {
			activePlayers.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
		}
	}

	@EventHandler
	public static void onPlayerChat(AsyncPlayerChatEvent event) {
		if (isPlayerAfk(event.getPlayer().getUniqueId())) {
			if (event.isAsynchronous()) {
				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> removeAfk(event.getPlayer().getUniqueId()));
			} else {
				removeAfk(event.getPlayer().getUniqueId());
			}
		} else {
			if (event.isAsynchronous()) {
				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> activePlayers.put(event.getPlayer().getUniqueId(), System.currentTimeMillis()));
			} else {
				activePlayers.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
			}
		}
	}

	public static void loadAfkCheck() {
		if (afkTask != null) afkTask.cancel();

		checkAfk();
	}

	private static void checkAfk() {
		afkTask = Bukkit.getScheduler().runTaskTimer(DirtEssentials.getPlugin(), () -> {
			for (Map.Entry<UUID, Long> entry : activePlayers.entrySet()) {
				if (System.currentTimeMillis() - entry.getValue() >= Utilities.config.general.afkTime * 1000L) {
					setAfk(entry.getKey());
				}
			}

			if (!Utilities.config.general.afkKick) return;

			for (Map.Entry<UUID, Long> entry : afkPlayers.entrySet()) {
				if (System.currentTimeMillis() - entry.getValue() >= Utilities.config.general.afkKickTime * 1000L) {
					Player player = Bukkit.getPlayer(entry.getKey());
					if (player == null) continue;

					if (player.hasPermission(Permissions.AFK_BYPASSKICK)) continue;

					player.kickPlayer(Utilities.translate(Utilities.config.general.afkKickMessage, false));
				}
			}
		}, 0, 20);
	}
}
