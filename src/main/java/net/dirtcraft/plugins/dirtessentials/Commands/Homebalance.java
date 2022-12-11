package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.HomeManager;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
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
import java.util.UUID;

public class Homebalance implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.HOMEBALANCE)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/homebalance <add|remove|set> <player> <amount>");
			return true;
		}

		String operation = args[0].toLowerCase();
		UUID uuid = PlayerManager.getUuid(args[1]);
		if (uuid == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		if (!Utilities.isInteger(args[2]) || Integer.parseInt(args[2]) < 1) {
			sender.sendMessage(Strings.INVALID_NUMBER);
			return true;
		}

		int amount = Integer.parseInt(args[2]);

		switch (operation) {
			case "add":
				HomeManager.addHomeBalance(uuid, amount);
				sender.sendMessage(Strings.PREFIX + "Added " + ChatColor.AQUA + amount + ChatColor.GRAY + " homes to " + ChatColor.GOLD + args[1] + ChatColor.GRAY + "!");
				break;
			case "remove":
				if (HomeManager.getHomeBalance(uuid) < amount) {
					HomeManager.setHomeBalance(uuid, 0);
				} else {
					HomeManager.removeHomeBalance(uuid, amount);
				}

				sender.sendMessage(Strings.PREFIX + "Removed " + ChatColor.AQUA + amount + ChatColor.GRAY + " homes from " + ChatColor.GOLD + args[1] + ChatColor.GRAY + "!");
				break;
			case "set":
				HomeManager.setHomeBalance(uuid, amount);
				sender.sendMessage(Strings.PREFIX + "Set " + ChatColor.GOLD + args[1] + ChatColor.GRAY + "'s home balance to " + ChatColor.AQUA + amount + ChatColor.GRAY + "!");
				break;
		}

		DatabaseOperations.setHomeBalance(uuid, HomeManager.getHomeBalance(uuid));

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.HOMEBALANCE)) return arguments;

		if (args.length == 1) {
			arguments.add("add");
			arguments.add("remove");
			arguments.add("set");
		} else if (args.length == 2) {
			arguments.addAll(PlayerManager.getAllPlayerNames());
		} else if (args.length == 3) {
			arguments.add("<amount>");
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
