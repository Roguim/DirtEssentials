package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RadiusZapCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (!sender.hasPermission(Permissions.RADIUSZAP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length < 5 || args.length > 6 || !Utilities.isInteger(args[1]) || !Utilities.isInteger(args[2]) || !Utilities.isInteger(args[3]) || !Utilities.isInteger(args[4])) {
			sender.sendMessage(Strings.USAGE + "/radiuszap <world> <x> <y> <z> <radius> [block]");
			return false;
		}

		int x = Integer.parseInt(args[1]);
		int y = Integer.parseInt(args[2]);
		int z = Integer.parseInt(args[3]);
		int radius = Integer.parseInt(args[4]);
		World world = Bukkit.getWorld(args[0]);
		NamespacedKey block = null;
		if (args.length == 6) {
			block = NamespacedKey.fromString(args[5]);
		}

		if (world == null) {
			sender.sendMessage(Strings.WORLD_NOT_FOUND);
			return false;
		}

		if (radius < 0 || radius > 25) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Invalid radius! Radius must be between 0 and 25!");
			return false;
		}

		if (args.length == 6 && block == null) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Invalid block!");
			return false;
		}

		int removedBlocks = 0;
		for (int i = x - radius; i <= x + radius; i++) {
			for (int j = y - radius; j <= y + radius; j++) {
				for (int k = z - radius; k <= z + radius; k++) {
					if (block == null) {
						if (world.getBlockAt(i, j, k).getType() != Material.AIR) {
							world.getBlockAt(i, j, k).setType(Material.AIR);
							removedBlocks++;
						}
					} else {
						Block b = world.getBlockAt(i, j, k);
						if (b.getType().getKey().equals(block)) {
							b.setType(Material.AIR);
							removedBlocks++;
						}
					}
				}
			}
		}

		if (block == null) {
			sender.sendMessage(Strings.PREFIX + Utilities.translate("&aRemoved &b" + removedBlocks + " &ablocks in a radius of &b" + radius + " &aaround&b " + x + "&a, &b" + y + "&a, &b" + z, false));
		} else {
			sender.sendMessage(Strings.PREFIX + Utilities.translate("&aRemoved &b" + removedBlocks + " &9" + block + " &ain a radius of &b" + radius + " &aaround&b " + x + "&a, &b" + y + "&a, &b" + z, false));
		}

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
			Arrays.stream(Material.values()).map(Material::getKey).map(NamespacedKey::toString).forEach(arguments::add);
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
