package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DelkitCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.DELKIT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/delkit <name>");
			return true;
		}

		String kitName = args[0];
		if (Utilities.kits.kits.stream().noneMatch(kit -> kit.name.equalsIgnoreCase(kitName))) {
			sender.sendMessage(Strings.PREFIX + "Kit not found!");
			return true;
		}

		Utilities.kits.kits.removeIf(kit -> kit.name.equalsIgnoreCase(kitName));
		Utilities.kits.save();
		Utilities.loadKits();

		sender.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Kit " + ChatColor.YELLOW + kitName + ChatColor.GRAY + " has been deleted!");

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.ENDERCHEST)) return arguments;

		if (args.length == 1) {
			Utilities.kits.kits.stream().map(kit -> kit.name).forEach(arguments::add);
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
