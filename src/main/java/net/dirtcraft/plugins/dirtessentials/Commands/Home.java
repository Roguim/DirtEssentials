package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.BackManager;
import net.dirtcraft.plugins.dirtessentials.Manager.HomeManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

public class Home implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.HOME)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (!HomeManager.hasHomes(player.getUniqueId()) || HomeManager.getHomes(player.getUniqueId()) == null || HomeManager.getHomes(player.getUniqueId()).isEmpty()) {
			player.sendMessage(Strings.PREFIX + "You don't have any homes!");
			return true;
		}

		if (args.length == 0) return player.performCommand("home home");

		if (HomeManager.isOnCooldown(player.getUniqueId())) {
			player.sendMessage(Strings.PREFIX + "You can use the command again in " + HomeManager.getCooldown(player.getUniqueId()) + " seconds!");
			return true;
		}

		String name = args[0];

		if (!HomeManager.hasHome(player.getUniqueId(), name)) {
			player.sendMessage(Strings.PREFIX + "You don't have a home with that name!");
			return true;
		}

		net.dirtcraft.plugins.dirtessentials.Data.Home home = HomeManager.getHome(player.getUniqueId(), name);

		Location location = home.getLocation();
		if (location == null) {
			player.sendMessage(Strings.PREFIX + "Home " + ChatColor.YELLOW + name + ChatColor.GRAY + " is not accessible!");
			return true;
		}

		player.teleport(location);
		player.sendMessage(Strings.PREFIX + "Teleported to home " + ChatColor.YELLOW + name + ChatColor.GRAY + "!");

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.HOME)) return arguments;

		if (args.length == 1) {
			List<net.dirtcraft.plugins.dirtessentials.Data.Home> homes = HomeManager.getHomes(((Player) sender).getUniqueId());
			if (homes == null) return arguments;

			arguments.addAll(homes.stream().map(net.dirtcraft.plugins.dirtessentials.Data.Home::getName).collect(Collectors.toCollection(ArrayList::new)));
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
