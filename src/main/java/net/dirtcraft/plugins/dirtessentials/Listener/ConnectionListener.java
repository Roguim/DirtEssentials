package net.dirtcraft.plugins.dirtessentials.Listener;

import net.dirtcraft.plugins.dirtessentials.Data.PlayerData;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Manager.*;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ConnectionListener implements Listener {

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (PlayerManager.hasPlayerData(player.getUniqueId()) && Bukkit.getPlayer(player.getUniqueId()).hasPlayedBefore()) {
			PlayerManager.getPlayerData(player.getUniqueId()).setUsername(player.getName());
			PlayerManager.getPlayerData(player.getUniqueId()).setStaff(player.hasPermission(Permissions.STAFF));
			player.setDisplayName(PlayerManager.getPlayerData(player.getUniqueId()).getDisplayName());
			CjmManager.joinMessage(event, player.hasPermission(Permissions.STAFF), false);
		} else {
			player.teleport(SpawnManager.getLocation());
			player.setBedSpawnLocation(SpawnManager.getLocation(), true);
			CjmManager.joinMessage(event, player.hasPermission(Permissions.STAFF), true);
			DirtEssentials.getDirtEconomy().createPlayerAccount(player);
			HomeManager.addNewHomeData(player.getUniqueId());
			PlayerManager.addPlayerData(player.getUniqueId(), new PlayerData(player.getUniqueId(), player.getName(), player.getDisplayName(), player.getAddress().getAddress().getHostAddress(), player.hasPermission(Permissions.STAFF), null, player.getLocation()));
		}

		if (NoteManager.hasNotes(player.getUniqueId())) {
			List<Player> onlineStaff = Utilities.getOnlineStaff();
			if (!onlineStaff.isEmpty()) {
				List<net.dirtcraft.plugins.dirtessentials.Data.Note> notes = NoteManager.getNotes(player.getUniqueId());

				ComponentBuilder message = new ComponentBuilder();
				message.append("\n");
				message.append(ChatColor.DARK_RED + "\u2666 " + ChatColor.RED + "Found notes for " + ChatColor.GOLD + player.getName() + ChatColor.RED + ":");
				message.append("\n");

				for (int i = 0; i < notes.size(); i++) {
					BaseComponent[] removeComponent = new ComponentBuilder()
							.append(ChatColor.GRAY + "  [" + ChatColor.RED + "\u2715" + ChatColor.GRAY + "]")
							.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/note remove " + player.getName() + " " + i))
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Remove Note"))).create();

					BaseComponent[] noteComponent = new ComponentBuilder()
							.append(ChatColor.BLUE + "- " + ChatColor.GRAY + notes.get(i).getNote())
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
									ChatColor.GOLD + "Index" + ChatColor.GRAY + ": " + ChatColor.AQUA + i + "\n" +
											ChatColor.GOLD + "Added By" + ChatColor.GRAY + ": " + ChatColor.RED + PlayerManager.getUsername(notes.get(i).getAddedBy()) + "\n" +
											ChatColor.GOLD + "Added On" + ChatColor.GRAY + ": " + ChatColor.DARK_AQUA + notes.get(i).getAddedOn().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
							)))
							.event((ClickEvent) null).create();

					message.append(removeComponent);
					message.append(" ").event((ClickEvent) null).event((HoverEvent) null);
					message.append(noteComponent);
				}

				message.append("\n");

				for (Player staff : onlineStaff) {
					staff.spigot().sendMessage(message.create());
				}
			}
		}

		DatabaseOperations.updatePlayer(
				player.getUniqueId(),
				player.getName(),
				player.getDisplayName(),
				player.getAddress().getAddress().getHostAddress(),
				player.hasPermission(Permissions.STAFF),
				null,
				player.getLocation()
		);
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		CjmManager.leaveMessage(event, player.hasPermission(Permissions.STAFF));
		PlayerManager.getPlayerData(player.getUniqueId()).setLeaveDate(LocalDateTime.now());
		PlayerManager.getPlayerData(player.getUniqueId()).setStaff(player.hasPermission(Permissions.STAFF));
		DatabaseOperations.updatePlayer(
				player.getUniqueId(),
				player.getName(),
				player.getDisplayName(),
				player.getAddress().getAddress().getHostAddress(),
				player.hasPermission(Permissions.STAFF),
				LocalDateTime.now(),
				player.getLocation()
		);
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
		int online = Bukkit.getOnlinePlayers().size() - Utilities.getOnlineStaff().size();
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
