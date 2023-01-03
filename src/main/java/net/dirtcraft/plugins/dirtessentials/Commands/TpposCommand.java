package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

public class TpposCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.TPPOS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length < 3) {
			sender.sendMessage(Strings.USAGE + "/tppos <x> <y> <z> [world] [yaw] [pitch]");
			return true;
		}

		String worldString = args.length > 3 ? args[3] : ((Player) sender).getWorld().getName();
		World world = Bukkit.getWorld(worldString);
		if (world == null) {
			sender.sendMessage(Strings.WORLD_NOT_FOUND);
			return true;
		}

		String xString = args[0];
		String yString = args[1];
		String zString = args[2];

		if (!Utilities.isDouble(xString) || !Utilities.isDouble(yString) || !Utilities.isDouble(zString)) {
			sender.sendMessage(Strings.INVALID_COORDINATES);
			return true;
		}

		double x = Double.parseDouble(xString);
		double y = Double.parseDouble(yString);
		double z = Double.parseDouble(zString);

		float yaw = 0;
		float pitch = 0;

		if (args.length >= 5) {
			String yawString = args[4];
			if (!Utilities.isFloat(yawString)) {
				sender.sendMessage(Strings.INVALID_COORDINATES);
				return true;
			}
			yaw = Float.parseFloat(yawString);
		}

		if (args.length >= 6) {
			String pitchString = args[5];
			if (!Utilities.isFloat(pitchString)) {
				sender.sendMessage(Strings.INVALID_COORDINATES);
				return true;
			}
			pitch = Float.parseFloat(pitchString);
		}

		Player player = (Player) sender;
		player.teleport(new Location(world, x, y, z, yaw, pitch));

		sender.sendMessage(Strings.PREFIX + "Teleported to " + ChatColor.AQUA + x + ", " + y + ", " + z + ChatColor.GRAY + " in world " + ChatColor.GOLD + worldString);
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.TPPOS)) return arguments;

		if (args.length == 1) {
			arguments.add("<x>");
		} else if (args.length == 2) {
			arguments.add("<y>");
		} else if (args.length == 3) {
			arguments.add("<z>");
		} else if (args.length == 4) {
			arguments.addAll(Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toCollection(ArrayList::new)));
		} else if (args.length == 5) {
			arguments.add("[yaw]");
		} else if (args.length == 6) {
			arguments.add("[pitch]");
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
