package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class GodCommand implements CommandExecutor, TabCompleter, Listener {
	private static final Set<UUID> godPlayers = new HashSet<>();

	public static Set<UUID> getGodPlayers() {
		return godPlayers;
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (godPlayers.contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
		}
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.GOD)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (!(sender instanceof Player) && args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/god <player>");
			return true;
		}

		Player target = args.length == 0 ? (Player) sender : sender.getServer().getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		if (!sender.hasPermission(Permissions.GOD_OTHERS) && !sender.getName().equalsIgnoreCase(target.getName())) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (godPlayers.contains(target.getUniqueId())) {
			godPlayers.remove(target.getUniqueId());
			target.sendMessage(Strings.PREFIX + "God mode is now " + ChatColor.RED + "disabled");
			if (!sender.getName().equalsIgnoreCase(target.getName())) {
				sender.sendMessage(Strings.PREFIX + "God mode is now " + ChatColor.RED + "disabled" + ChatColor.GRAY + " for " + ChatColor.GOLD + target.getName());
			}
		} else {
			godPlayers.add(target.getUniqueId());
			target.sendMessage(Strings.PREFIX + "God mode is now " + ChatColor.GREEN + "enabled");
			if (!sender.getName().equalsIgnoreCase(target.getName())) {
				sender.sendMessage(Strings.PREFIX + "God mode is now " + ChatColor.GREEN + "enabled" + ChatColor.GRAY + " for " + ChatColor.GOLD + target.getName());
			}
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.GOD_OTHERS)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.toLowerCase().contains(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
