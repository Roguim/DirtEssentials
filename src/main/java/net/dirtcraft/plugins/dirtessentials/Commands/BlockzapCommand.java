package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockzapCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (!sender.hasPermission(Permissions.BLOCKZAP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 4 || !Utilities.isInteger(args[1]) || !Utilities.isInteger(args[2]) || !Utilities.isInteger(args[3])) {
			sender.sendMessage(Strings.USAGE + "/blockzap <world> <x> <y> <z>");
			return false;
		}

		int x = Integer.parseInt(args[1]);
		int y = Integer.parseInt(args[2]);
		int z = Integer.parseInt(args[3]);
		World world = Bukkit.getWorld(args[0]);

		if (world == null) {
			sender.sendMessage(Strings.WORLD_NOT_FOUND);
			return false;
		}

		Block block = world.getBlockAt(x, y, z);
		sender.sendMessage(Strings.PREFIX + Utilities.translate("&aRemoved &b" + block.getType().getKey() + " &aat position &b" + x + "&a, &b" + y + "&a, &b" + z, false));

		block.setType(Material.AIR);

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
