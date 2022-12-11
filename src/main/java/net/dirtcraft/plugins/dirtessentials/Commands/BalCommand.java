package net.dirtcraft.plugins.dirtessentials.Commands;

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
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BalCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.BAL)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "Balance" + ChatColor.GRAY + ": " + ChatColor.GRAY + DirtEssentials.getDirtEconomy().getBalance((Player) sender) + Utilities.config.economy.currencySymbol);
			return true;
		}

		if (!sender.hasPermission(Permissions.BAL_OTHERS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		UUID uuid = PlayerManager.getUuid(args[0]);
		if (uuid == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);

		if (DirtEssentials.getDirtEconomy().hasAccount(target)) {
			sender.sendMessage(ChatColor.GREEN + args[0] + "'s " + ChatColor.GOLD + "Balance" + ChatColor.GRAY + ": " + ChatColor.GRAY + DirtEssentials.getDirtEconomy().getBalance(target) + Utilities.config.economy.currencySymbol);
		} else {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.BAL_OTHERS))
			return arguments;

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
