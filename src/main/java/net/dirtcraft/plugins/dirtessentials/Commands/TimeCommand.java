package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
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

public class TimeCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.TIME)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return false;
		}

		int argLength = args.length;
		if (!(sender instanceof Player) && argLength != 3) {
			sender.sendMessage(Strings.USAGE + "/time <add|set> <time> <world>");
			return false;
		}

		if (argLength == 0 || argLength == 1 || argLength > 3) {
			sender.sendMessage(Strings.USAGE + "/time <add|set> <time> [world]");
			return false;
		}

		String type = args[0].toLowerCase();
		if (!type.equals("add") && !type.equals("set")) {
			sender.sendMessage(Strings.USAGE + "/time <add|set> <time> [world]");
			return false;
		}

		int time;
		if (type.equals("set")) {
			switch (args[1].toLowerCase()) {
				case "day":
					time = 1000;
					break;
				case "noon":
					time = 6000;
					break;
				case "night":
					time = 13000;
					break;
				case "midnight":
					time = 18000;
					break;
				default:
					if (!Utilities.isInteger(args[1])) {
						sender.sendMessage(Strings.INVALID_NUMBER);
						return false;
					}

					time = Integer.parseInt(args[1]);
			}
		} else {
			if (!Utilities.isInteger(args[1])) {
				sender.sendMessage(Strings.INVALID_NUMBER);
				return false;
			}

			time = Integer.parseInt(args[1]);
		}

		World world;
		if (argLength == 3) {
			world = Bukkit.getWorld(args[2]);
			if (world == null) {
				sender.sendMessage(Strings.INVALID_WORLD);
				return false;
			}
		} else {
			world = ((Player) sender).getWorld();
		}

		if (type.equals("add")) {
			world.setTime(world.getTime() + time);
		} else {
			world.setTime(time);
		}

		sender.sendMessage(Strings.PREFIX + "Changed time to " + ChatColor.AQUA + world.getTime() + ChatColor.GRAY + " in world " + ChatColor.GOLD + world.getName());
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.TIME)) return arguments;

		if (args.length == 1) {
			arguments.add("add");
			arguments.add("set");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
			arguments.add("day");
			arguments.add("noon");
			arguments.add("night");
			arguments.add("midnight");
			arguments.add("0 - 24000");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
			arguments.add("0 - 24000");
		} else if (args.length == 3) {
			arguments.addAll(Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
