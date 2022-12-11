package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.milkbowl.vault.economy.EconomyResponse;
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

public class EcoCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.ECO)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 3) {
			sender.sendMessage(Strings.USAGE + "/eco <give|remove|set> <player> <amount>");
			return true;
		}

		String type = args[0].toLowerCase();
		if (!type.equalsIgnoreCase("give") && !type.equalsIgnoreCase("remove") && !type.equalsIgnoreCase("set")) {
			sender.sendMessage(Strings.USAGE + "/eco <give|remove|set> <player> <amount>");
			return true;
		}

		UUID uuid = PlayerManager.getUuid(args[1]);

		if (uuid == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

		if (!Utilities.isDouble(args[2]) || Double.parseDouble(args[2]) < 0) {
			sender.sendMessage(Strings.INVALID_AMOUNT);
			return true;
		}

		double amount = Double.parseDouble(args[2]);

		switch (type) {
			case "give":
				EconomyResponse giveResponse = DirtEssentials.getDirtEconomy().depositPlayer(player, amount);
				if (giveResponse.transactionSuccess()) {
					sender.sendMessage(Strings.PREFIX + ChatColor.GREEN + "Successfully gave " + ChatColor.GOLD + player.getName() + ChatColor.GREEN + " " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.GREEN + "!");
					if (player.isOnline())
						player.getPlayer().sendMessage(Strings.PREFIX + ChatColor.GREEN + "You have been given " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.GREEN + "!");
				} else {
					sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Failed to give " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.RED + " to " + ChatColor.GOLD + player.getName() + ChatColor.RED + "!");
				}
				break;
			case "remove":
				EconomyResponse removeResponse = DirtEssentials.getDirtEconomy().withdrawPlayer(player, amount);
				if (removeResponse.transactionSuccess()) {
					sender.sendMessage(Strings.PREFIX + ChatColor.GREEN + "Successfully removed " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.GREEN + " from " + ChatColor.GOLD + player.getName() + ChatColor.GREEN + "!");
					if (player.isOnline())
						player.getPlayer().sendMessage(Strings.PREFIX + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.GREEN + " has been removed from your account!");
				} else {
					sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Failed to remove " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.RED + " from " + ChatColor.GOLD + player.getName() + ChatColor.RED + "!");
				}
				break;
			case "set":
				DirtEssentials.getDirtEconomy().withdrawPlayer(player, DirtEssentials.getDirtEconomy().getBalance(player));
				EconomyResponse setResponse = DirtEssentials.getDirtEconomy().depositPlayer(player, amount);
				if (setResponse.transactionSuccess()) {
					sender.sendMessage(Strings.PREFIX + ChatColor.GREEN + "Successfully set " + ChatColor.GOLD + player.getName() + ChatColor.GREEN + "'s balance to " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.GREEN + "!");
					if (player.isOnline())
						player.getPlayer().sendMessage(Strings.PREFIX + ChatColor.GREEN + "Your balance has been set to " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.GREEN + "!");
				} else {
					sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Failed to set " + ChatColor.GOLD + player.getName() + ChatColor.RED + "'s balance to " + ChatColor.GRAY + amount + Utilities.config.economy.currencySymbol + ChatColor.RED + "!");
				}
				break;
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.ECO))
			return arguments;

		if (args.length == 1) {
			arguments.add("give");
			arguments.add("remove");
			arguments.add("set");
		} else if (args.length == 2) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
		} else if (args.length == 3) {
			arguments.add("amount");
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
