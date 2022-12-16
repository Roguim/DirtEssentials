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

public class WeatherCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.WEATHER)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return false;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		int argLength = args.length;
		if (argLength == 0) {
			sender.sendMessage(Strings.USAGE + "/weather <clear|rain|thunder> [duration] [world]");
			return false;
		}

		if (argLength > 1 && !Utilities.isInteger(args[1])) {
			sender.sendMessage(Strings.USAGE + "/weather <clear|rain|thunder> [duration] [world]");
			return false;
		}

		Player player = (Player) sender;
		World world = argLength == 3 ? Bukkit.getWorld(args[2]) : player.getWorld();
		String weather = args[0];
		int duration = 6000;
		if (argLength > 1 && Utilities.isInteger(args[1])) {
			duration = Integer.parseInt(args[2]) * 20;

			if (duration > 20000000 || duration < 0) {
				sender.sendMessage(Strings.INVALID_DURATION);
				return false;
			}
		}

		if (world == null) {
			sender.sendMessage(Strings.INVALID_WORLD);
			return false;
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
			default:
				sender.sendMessage(Strings.USAGE + "/weather <clear|rain|thunder> [duration] [world]");
				return false;
		}

		sender.sendMessage(Strings.PREFIX + "Weather set to " + ChatColor.YELLOW + weather + ChatColor.GRAY + " for " + ChatColor.GREEN + duration / 20 + "s " + ChatColor.GRAY + "in world " + ChatColor.AQUA + args[0] + ChatColor.GRAY + ".");
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.WEATHER))
			return arguments;

		if (args.length == 1) {
			arguments.add("clear");
			arguments.add("rain");
			arguments.add("thunder");
		} else if (args.length == 2) {
			arguments.add("duration");
		} else if (args.length == 3) {
			arguments.addAll(Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toCollection(ArrayList::new)));
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
