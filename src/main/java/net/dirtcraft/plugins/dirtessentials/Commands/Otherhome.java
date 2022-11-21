package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Data.Home;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.BackManager;
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

public class Otherhome implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.OTHERHOME)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 3) {
			sender.sendMessage(Strings.USAGE + "/otherhome <player> <teleport|delete> <name>");
			return true;
		}

		UUID uuid = PlayerManager.getUuid(args[0]);
		if (uuid == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		String action = args[1].toLowerCase();
		if (!action.equalsIgnoreCase("teleport") && !action.equalsIgnoreCase("delete")) {
			sender.sendMessage(Strings.USAGE + "/otherhome <player> <teleport|delete> <name>");
			return true;
		}

		String name = args[2];
		List<Home> homes = HomeManager.getHomes(uuid);
		if (homes == null || homes.isEmpty()) {
			sender.sendMessage(Strings.PREFIX + "That player doesn't have any homes!");
			return true;
		}

		if (homes.stream().noneMatch(home -> home.getName().equalsIgnoreCase(name))) {
			sender.sendMessage(Strings.PREFIX + "That player doesn't have a home with that name!");
			return true;
		}

		Home home = homes.stream().filter(h -> h.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		if (home == null) {
			sender.sendMessage(Strings.PREFIX + "Something went wrong!");
			return true;
		}

		Player player = (Player) sender;

		switch (action) {
			case "teleport":
				player.teleport(home.getLocation());
				player.sendMessage(Strings.PREFIX + "Teleported to " + ChatColor.GOLD + args[0] + ChatColor.GRAY + "'s home " + ChatColor.AQUA + home.getName() + ChatColor.GRAY + "!");
				break;
			case "delete":
				HomeManager.removeHome(uuid, home.getName());
				DatabaseOperations.removeHome(player.getUniqueId(), name);
				player.sendMessage(Strings.PREFIX + "Deleted " + ChatColor.GOLD + args[0] + ChatColor.GRAY + "'s home " + ChatColor.AQUA + home.getName() + ChatColor.GRAY + "!");
				break;
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.OTHERHOME))
			return arguments;

		if (args.length == 1) {
			arguments.addAll(PlayerManager.getAllPlayerNames());
		} else if (args.length == 2) {
			arguments.add("teleport");
			arguments.add("delete");
		} else if (args.length == 3) {
			UUID uuid = PlayerManager.getUuid(args[0]);
			if (uuid == null)
				return arguments;

			List<Home> homes = HomeManager.getHomes(uuid);
			if (homes == null || homes.isEmpty())
				return arguments;

			homes.forEach(home -> arguments.add(home.getName()));
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
