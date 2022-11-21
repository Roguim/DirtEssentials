package net.dirtcraft.plugins.dirtessentials.Listener;

import net.dirtcraft.plugins.dirtessentials.Data.PlayerData;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Events.NewPlayerEvent;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConnectionListener implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (PlayerManager.hasPlayerData(player.getUniqueId())) {
			PlayerManager.getPlayerData(player.getUniqueId()).setUsername(player.getName());
		} else {
			NewPlayerEvent newPlayerEvent = new NewPlayerEvent(player.getName(), player.getUniqueId());
			Bukkit.getPluginManager().callEvent(newPlayerEvent);
			System.out.println("New Player Event Called");
			PlayerManager.addPlayerData(player.getUniqueId(), new PlayerData(player.getUniqueId(), player.getName(), player.getDisplayName(), player.getAddress().getAddress().getHostAddress(), player.hasPermission(Permissions.STAFF), null, player.getLocation()));
		}

		DatabaseOperations.updatePlayer(player.getUniqueId(), player.getName(), player.getDisplayName(), player.getAddress().getAddress().getHostAddress(), player.hasPermission(Permissions.STAFF), null, player.getLocation());
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PlayerManager.getPlayerData(player.getUniqueId()).setLeaveDate(LocalDateTime.now());
		DatabaseOperations.updatePlayer(player.getUniqueId(), player.getName(), player.getDisplayName(), player.getAddress().getAddress().getHostAddress(), player.hasPermission(Permissions.STAFF), LocalDateTime.now(), player.getLocation());
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
		int online = Bukkit.getOnlinePlayers().size();
		int max = Math.min(Math.min(Utilities.config.general.maxPlayers, 999), Bukkit.getMaxPlayers());

		UUID uuid = event.getUniqueId();
		boolean isStaff = PlayerManager.isStaff(uuid);

		if (!isStaff && online >= max) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Utilities.config.general.getFullMessage());
			return;
		}

		if (isStaff && online >= max && Utilities.config.general.staffJoinFull) {
			event.allow();
		}
	}
}
