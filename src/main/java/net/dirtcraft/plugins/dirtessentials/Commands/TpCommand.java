package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.TeleportManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TpCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.TP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/tp <player> [player]");
			return true;
		}

		if (args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(Strings.PLAYER_NOT_FOUND);
				return true;
			}

			if (TeleportManager.isTpDisabled(target.getUniqueId())) {
				sender.sendMessage(Strings.PREFIX + "This player has teleportation disabled!");
				return true;
			}

			((Player) sender).teleport(target);
			sender.sendMessage(Strings.PREFIX + "Teleported to " + target.getName());
			return true;
		}

		if (args.length == 2) {
			Player target = Bukkit.getPlayer(args[0]);
			Player target2 = Bukkit.getPlayer(args[1]);
			if (target == null || target2 == null) {
				sender.sendMessage(Strings.PLAYER_NOT_FOUND);
				return true;
			}

			if (TeleportManager.isTpDisabled(target.getUniqueId())) {
				sender.sendMessage(Strings.PREFIX + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " has teleportation disabled!");
				return true;
			}

			if (TeleportManager.isTpDisabled(target2.getUniqueId())) {
				sender.sendMessage(Strings.PREFIX + ChatColor.GOLD + target2.getName() + ChatColor.GRAY + " has teleportation disabled!");
				return true;
			}

			target.teleport(target2);
			sender.sendMessage(Strings.PREFIX + "Teleported " + target.getName() + " to " + target2.getName());
			target.sendMessage(Strings.PREFIX + "You have been teleported to " + ChatColor.GOLD + target2.getName());
			return true;
		}

		sender.sendMessage(Strings.USAGE + "/tp <player> [player]");
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.TP)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
		} else if (args.length == 2) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
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
