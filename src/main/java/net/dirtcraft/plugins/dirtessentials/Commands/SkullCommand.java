package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
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

public class SkullCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.SKULL)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length > 1) {
			sender.sendMessage(Strings.USAGE + "/skull [player]");
			return true;
		}

		Player player = (Player) sender;
		if (args.length == 0) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " player_head{display:{Name:'{\"text\":\"" + player.getName() + "\\'s Head\",\"color\":\"yellow\"}'},SkullOwner:\"" + player.getName() + "\"} 1");
			sender.sendMessage(Strings.PREFIX + "You have been given your own head!");
			return true;
		}

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " player_head{display:{Name:'{\"text\":\"" + args[0] + "\\'s Head\",\"color\":\"yellow\"}'},SkullOwner:\"" + args[0] + "\"} 1");
		sender.sendMessage(Strings.PREFIX + "You have been given the head of " + ChatColor.GOLD + args[0] + ChatColor.GRAY + "!");
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.SKULL)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
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