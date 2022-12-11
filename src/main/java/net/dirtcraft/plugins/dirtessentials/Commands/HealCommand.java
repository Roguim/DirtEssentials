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
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HealCommand implements CommandExecutor, TabCompleter {
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.HEAL)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (!(sender instanceof Player) && args.length < 1) {
			sender.sendMessage(Strings.USAGE + "/heal <player>");
			return false;
		}

		if (args.length > 0 && !sender.hasPermission(Permissions.HEAL_OTHERS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player target = args.length < 1 ? (Player) sender : Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return false;
		}

		target.setHealth(20);
		target.setFireTicks(0);
		for (PotionEffect effect : target.getActivePotionEffects()) {
			target.removePotionEffect(effect.getType());
		}

		target.sendMessage(Strings.PREFIX + "You have been healed!");

		if (!(sender instanceof Player) || !sender.getName().equals(target.getName())) sender.sendMessage(Strings.PREFIX + "You have healed " + ChatColor.GOLD + target.getName());
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.HEAL_OTHERS)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
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
