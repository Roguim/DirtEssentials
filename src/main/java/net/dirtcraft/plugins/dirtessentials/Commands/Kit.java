package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.KitManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
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

public class Kit implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage(Strings.USAGE + "/kit <name>");
			return true;
		}

		if (!sender.hasPermission(Permissions.KIT + "." + args[0])) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (Utilities.kits.kits.stream().noneMatch(kit -> kit.name.equalsIgnoreCase(args[0]))) {
			sender.sendMessage(Strings.PREFIX + "This kit does not exist!");
			return true;
		}

		net.dirtcraft.plugins.dirtessentials.Config.Kit kit = Utilities.kits.kits.stream().filter(k -> k.name.equalsIgnoreCase(args[0])).findFirst().get();

		Player player = (Player) sender;
		String name = kit.name;
		List<String> kitCommands = kit.commands;

		if (!KitManager.isKitClaimable(name, player.getUniqueId())) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "You cannot claim this kit!");
			return true;
		}

		for (String kitCommand : kitCommands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), kitCommand.replace("{PLAYER}", player.getName()));
		}

		DatabaseOperations.setKitCooldown(name, player.getUniqueId());
		sender.sendMessage(Strings.PREFIX + ChatColor.GREEN + "You have claimed the kit " + ChatColor.GOLD + name + ChatColor.GREEN + "!");
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		List<String> kitNames = new ArrayList<>();
		Utilities.kits.kits.forEach(kit -> kitNames.add(kit.name));

		for (String kitName : kitNames) {
			if (sender.hasPermission(Permissions.KIT + "." + kitName)) {
				arguments.add(kitName);
			}
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
