package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.HomeManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SethomeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.SETHOME)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (!HomeManager.hasAvailableHomes(player.getUniqueId())) {
			player.sendMessage(Strings.PREFIX + "You have reached the maximum amount of homes!");
			return true;
		}

		if (args.length == 0) {
			if (HomeManager.hasHome(player.getUniqueId(), "home")) {
				sender.sendMessage(Strings.USAGE + "/sethome <name>");
			} else {
				player.performCommand("sethome home");
			}

			return true;
		}

		String name = args[0];

		if (HomeManager.hasHome(player.getUniqueId(), name)) {
			player.sendMessage(Strings.PREFIX + "You already have a home with that name!");
			return true;
		}

		if (name.length() > 50) {
			sender.sendMessage(Strings.PREFIX + "Home name is too long!");
			return true;
		}

		Location location = player.getLocation();

		DatabaseOperations.addHome(player.getUniqueId(), name, location, () -> {
			HomeManager.addHome(player.getUniqueId(), name, location);
			player.sendMessage(Strings.PREFIX + "Home " + ChatColor.YELLOW + name + ChatColor.GRAY + " added!");
		});

		return true;
	}
}
