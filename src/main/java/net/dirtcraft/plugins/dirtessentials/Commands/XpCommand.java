package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XpCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.XP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 4) {
			sender.sendMessage(Strings.USAGE + "/xp <add|set|remove> <player> <amount> <levels|points>");
		}

		if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("set") && !args[0].equalsIgnoreCase("remove")) {
			sender.sendMessage(Strings.USAGE + "/xp <add|set|remove> <player> <amount> <levels|points>");
		}

		if (!args[3].equalsIgnoreCase("levels") && !args[3].equalsIgnoreCase("points")) {
			sender.sendMessage(Strings.USAGE + "/xp <add|set|remove> <player> <amount> <levels|points>");
		}

		if (!Utilities.isInteger(args[2])) {
			sender.sendMessage(Strings.USAGE + "/xp <add|set|remove> <player> <amount> <levels|points>");
		}

		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		int amount = Integer.parseInt(args[2]);
		String operation = args[0].toLowerCase();
		String type = args[3].toLowerCase();

		switch (operation) {
			case "add":
				if (type.equals("levels")) {
					target.giveExpLevels(amount);
					target.sendMessage(ChatColor.GRAY + "You have been given " + ChatColor.GREEN + amount + ChatColor.GRAY + " xp levels.");
					if (!sender.getName().equalsIgnoreCase(target.getName())) {
						sender.sendMessage(ChatColor.GRAY + "You have given " + ChatColor.GREEN + amount + ChatColor.GRAY + " xp levels to " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ".");
					}
				} else {
					target.giveExp(amount);
					target.sendMessage(ChatColor.GRAY + "You have been given " + ChatColor.GREEN + amount + ChatColor.GRAY + " xp points.");
					if (!sender.getName().equalsIgnoreCase(target.getName())) {
						sender.sendMessage(ChatColor.GRAY + "You have given " + ChatColor.GREEN + amount + ChatColor.GRAY + " xp points to " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ".");
					}
				}
				break;
			case "set":
				if (type.equals("levels")) {
					target.setLevel(amount);
					target.sendMessage(ChatColor.GRAY + "Your xp level has been set to " + ChatColor.GREEN + amount + ChatColor.GRAY + ".");
					if (!sender.getName().equalsIgnoreCase(target.getName())) {
						sender.sendMessage(ChatColor.GRAY + "You have set " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + "'s xp levels to " + ChatColor.GREEN + amount + ChatColor.GRAY + ".");
					}
				} else {
					target.setExp(amount);
					target.sendMessage(ChatColor.GRAY + "Your xp points have been set to " + ChatColor.GREEN + amount + ChatColor.GRAY + ".");
					if (!sender.getName().equalsIgnoreCase(target.getName())) {
						sender.sendMessage(ChatColor.GRAY + "You have set " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + "'s xp points to " + ChatColor.GREEN + amount + ChatColor.GRAY + ".");
					}
				}
				break;
			case "remove":
				if (type.equals("levels")) {
					target.setLevel(target.getLevel() - amount);
					target.sendMessage(ChatColor.GRAY + "You have lost " + ChatColor.RED + amount + ChatColor.GRAY + " xp levels.");
					if (!sender.getName().equalsIgnoreCase(target.getName())) {
						sender.sendMessage(ChatColor.GRAY + "You have removed " + ChatColor.RED + amount + ChatColor.GRAY + " xp levels from " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ".");
					}
				} else {
					target.setExp(target.getExp() - amount);
					target.sendMessage(ChatColor.GRAY + "You have lost " + ChatColor.RED + amount + ChatColor.GRAY + " xp points.");
					if (!sender.getName().equalsIgnoreCase(target.getName())) {
						sender.sendMessage(ChatColor.GRAY + "You have removed " + ChatColor.RED + amount + ChatColor.GRAY + " xp points from " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ".");
					}
				}
				break;
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.XP)) return arguments;

		if (args.length == 1) {
			arguments.add("add");
			arguments.add("set");
			arguments.add("remove");
		} else if (args.length == 2) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
		} else if (args.length == 3) {
			arguments.add("amount");
		} else if (args.length == 4) {
			arguments.add("levels");
			arguments.add("points");
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
