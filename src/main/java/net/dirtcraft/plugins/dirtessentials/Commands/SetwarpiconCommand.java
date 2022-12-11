package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Data.Warp;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.WarpManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetwarpiconCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.SETWARPICON)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/setwarpicon <warp>");
			return true;
		}

		String warp = args[0];
		if (WarpManager.getWarp(warp) == null) {
			sender.sendMessage(Strings.PREFIX + "There is no warp with that name!");
			return true;
		}

		Player player = (Player) sender;
		ItemStack item = player.getInventory().getItemInMainHand();

		if (item.getType().isAir()) {
			sender.sendMessage(Strings.PREFIX + "You must be holding an item to set it as the warp item.");
			return true;
		}

		WarpManager.getWarp(warp).setIcon(item);
		DatabaseOperations.changeWarpIcon(warp, item);
		sender.sendMessage(Strings.PREFIX + "Changed the warp icon for warp " + ChatColor.YELLOW + warp + ChatColor.GRAY + "!");
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.SETWARPICON)) return arguments;

		if (args.length == 1) {
			arguments.addAll(WarpManager.getWarps().stream().map(Warp::getName).collect(Collectors.toCollection(ArrayList::new)));
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
