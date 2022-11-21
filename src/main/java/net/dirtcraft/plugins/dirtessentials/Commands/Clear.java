package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Clear implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.CLEAR)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (!(sender instanceof Player) && args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/clear <player> [item]");
			return true;
		}

		Player target = args.length == 0 ? (Player) sender : Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		if (args.length == 0) {
			target.getInventory().clear();
			sender.sendMessage(Strings.PREFIX + "Your inventory has been cleared.");
			return true;
		}

		if (args.length == 1) {
			target.getInventory().clear();
			sender.sendMessage(Strings.PREFIX + "Cleared " + ChatColor.GOLD + target.getName() + "s " + ChatColor.GRAY + "inventory.");
			target.sendMessage(Strings.PREFIX + "Your inventory has been cleared.");
			return true;
		}

		Material material = Material.matchMaterial(args[1]);
		if (material == null) {
			sender.sendMessage(Strings.PREFIX + "Invalid item.");
			return true;
		}

		target.getInventory().remove(material);
		sender.sendMessage(Strings.PREFIX + "Cleared " + ChatColor.AQUA + material.name() + ChatColor.GRAY + " from " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + ".");
		target.sendMessage(Strings.PREFIX + "Cleared " + ChatColor.AQUA + material.name() + ChatColor.GRAY + " from your inventory.");

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.CLEAR)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
		} else if (args.length == 2) {
			Arrays.stream(Material.values()).map(Material::name).forEach(arguments::add);
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
