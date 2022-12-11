package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.WarpManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DelwarpCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.DELWARP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.PREFIX + "You must specify a warp to delete!");
			return true;
		}

		String warpName = args[0];
		if (WarpManager.getWarp(warpName) == null) {
			sender.sendMessage(Strings.PREFIX + "There is no warp with that name!");
			return true;
		}

		WarpManager.removeWarp(warpName);
		DatabaseOperations.deleteWarp(warpName);
		sender.sendMessage(Strings.PREFIX + "You have deleted the warp " + ChatColor.YELLOW + warpName + ChatColor.GRAY + "!");
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.DELWARP)) return arguments;

		if (args.length == 1) {
			arguments.addAll(WarpManager.getWarps().stream().map(net.dirtcraft.plugins.dirtessentials.Data.Warp::getName).collect(Collectors.toList()));
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
