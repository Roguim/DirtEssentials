package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Pay implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.PAY)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (args.length != 2) {
			sender.sendMessage(Strings.USAGE + "/pay <player> <amount>");
			return true;
		}

		if (sender.getName().equalsIgnoreCase(args[0])) {
			sender.sendMessage(ChatColor.RED + "Why would you pay yourself?");
			return true;
		}

		UUID uuid = PlayerManager.getUuid(args[0]);
		if (uuid == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);

		if (!Utilities.isDouble(args[1]) || Double.parseDouble(args[1]) < 0.01) {
			sender.sendMessage(Strings.INVALID_AMOUNT);
			return true;
		}

		double amount = Double.parseDouble(args[1]);

		if (!DirtEssentials.getDirtEconomy().has((Player) sender, amount)) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "You do not have enough money to pay that amount!");
			return true;
		}

		DirtEssentials.getDirtEconomy().withdrawPlayer((Player) sender, amount);
		DirtEssentials.getDirtEconomy().depositPlayer(target, amount);

		sender.sendMessage(ChatColor.GREEN + "You have paid " + ChatColor.GOLD + target.getName() + " " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.GREEN + "!");
		if (target.isOnline())
			target.getPlayer().sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.GREEN + " sent you " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.GREEN + "!");

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.PAY))
			return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
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
