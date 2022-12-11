package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Data.Home;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.HomeManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
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

public class DelhomeCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.DELHOME)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			if (HomeManager.hasHome(player.getUniqueId(), "home")) {
				player.performCommand("delhome home");
			} else {
				player.sendMessage(Strings.USAGE + "/delhome <name>");
			}
			return true;
		}

		String name = args[0];

		if (!HomeManager.hasHome(player.getUniqueId(), name)) {
			player.sendMessage(Strings.PREFIX + "You don't have a home with that name!");
			return true;
		}

		HomeManager.removeHome(player.getUniqueId(), name);
		player.sendMessage(Strings.PREFIX + "Home " + ChatColor.YELLOW + name + ChatColor.GRAY + " has been deleted!");
		DatabaseOperations.removeHome(player.getUniqueId(), name);

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.DELHOME)) return arguments;

		if (args.length == 1) {
			List<Home> homes = HomeManager.getHomes(((Player) sender).getUniqueId());
			if (homes == null) return arguments;

			arguments.addAll(homes.stream().map(net.dirtcraft.plugins.dirtessentials.Data.Home::getName).collect(Collectors.toList()));
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
