package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityzapCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (!sender.hasPermission(Permissions.ENTITYZAP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 6|| !Utilities.isInteger(args[1]) || !Utilities.isInteger(args[2]) || !Utilities.isInteger(args[3]) || !Utilities.isInteger(args[4])) {
			sender.sendMessage(Strings.USAGE + "/entityzap <world> <x> <y> <z> <radius> <entity>");
			return false;
		}

		int x = Integer.parseInt(args[1]);
		int y = Integer.parseInt(args[2]);
		int z = Integer.parseInt(args[3]);
		int radius = Integer.parseInt(args[4]);
		World world = Bukkit.getWorld(args[0]);
		NamespacedKey entity = NamespacedKey.fromString(args[5]);

		if (world == null) {
			sender.sendMessage(Strings.WORLD_NOT_FOUND);
			return false;
		}

		if (radius < 0 || radius > 50) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Invalid radius! Radius must be between 0 and 50!");
			return false;
		}

		if (entity == null) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Unknown entity!");
			return false;
		}

		int removedEntities = 0;
		for (int i = x - radius; i <= x + radius; i++) {
			for (int j = y - radius; j <= y + radius; j++) {
				for (int k = z - radius; k <= z + radius; k++) {
					for (Entity e : world.getNearbyEntities(new Location(world, i, j, k), radius, radius, radius)) {
						if (e.getType().getKey().equals(entity)) {
							if (e instanceof Player) continue;

							e.remove();
							removedEntities++;
						}
					}
				}
			}
		}

		sender.sendMessage(Strings.PREFIX + Utilities.translate("&aRemoved &b" + removedEntities + " &9" + entity + " &ain a radius of &b" + radius + " &aaround&b " + x + "&a, &b" + y + "&a, &b" + z, false));

		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		List<String> arguments = new ArrayList<>();

		if (args.length == 1) {
			Bukkit.getWorlds().stream().map(World::getName).forEach(arguments::add);
		} else if (args.length == 2) {
			arguments.add("x");
		} else if (args.length == 3) {
			arguments.add("y");
		} else if (args.length == 4) {
			arguments.add("z");
		} else if (args.length == 5) {
			arguments.add("radius");
		} else if (args.length == 6) {
			Arrays.stream(EntityType.values()).forEach(entity -> {
				try {
					arguments.add(entity.getKey().toString());
				} catch (Exception ignored) {}
			});
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
