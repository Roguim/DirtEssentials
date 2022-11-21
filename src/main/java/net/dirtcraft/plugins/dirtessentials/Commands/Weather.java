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

public class Weather implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.WEATHER)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return false;
		}

		int argLength = args.length;
		if (!(sender instanceof Player) && argLength != 3) {
			sender.sendMessage(Strings.USAGE + "/weather <world> <clear|rain|thunder> [duration]");
			return false;
		}

		if (argLength == 0) {
			sender.sendMessage(Strings.USAGE + "/weather <world> <clear|rain|thunder> [duration]");
			return false;
		}

		if (argLength == 1) {
			sender.sendMessage(Strings.USAGE + "/weather <world> <clear|rain|thunder> [duration]");
			return false;
		}

		World world = Bukkit.getWorld(args[0]);
		if (world == null) {
			sender.sendMessage(Strings.INVALID_WORLD);
			return false;
		}

		String weather = args[1].toLowerCase();
		if (!weather.equalsIgnoreCase("clear") && !weather.equalsIgnoreCase("rain") && !weather.equalsIgnoreCase("thunder")) {
			sender.sendMessage(Strings.INVALID_WEATHER);
			return false;
		}

		int duration = 6000;
		if (argLength == 3 && Utilities.isInteger(args[2])) {
			duration = Integer.parseInt(args[2]) * 20;

			if (duration > 20000000 || duration < 0) {
				sender.sendMessage(Strings.INVALID_DURATION);
				return false;
			}
		}

		switch (weather) {
			case "clear":
				world.setStorm(false);
				world.setThundering(false);
				world.setWeatherDuration(duration);
				break;
			case "rain":
				world.setStorm(true);
				world.setThundering(false);
				world.setWeatherDuration(duration);
				break;
			case "thunder":
				world.setStorm(true);
				world.setThundering(true);
				world.setWeatherDuration(duration);
				break;
		}

		sender.sendMessage(Strings.PREFIX + "Weather set to " + ChatColor.YELLOW + weather + ChatColor.GRAY + " for " + ChatColor.GREEN + duration / 20 + "s " + ChatColor.GRAY + "in world " + ChatColor.AQUA + args[0] + ChatColor.GRAY + ".");

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.WEATHER)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toCollection(ArrayList::new)));
		} else if (args.length == 2) {
			arguments.add("clear");
			arguments.add("rain");
			arguments.add("thunder");
		} else if (args.length == 3) {
			arguments.add("duration");
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.contains("duration")) {
				tabResults.add(argument);
				continue;
			}

			if (argument.toLowerCase().contains(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
