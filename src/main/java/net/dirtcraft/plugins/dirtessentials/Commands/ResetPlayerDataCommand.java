package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Data.PlayerData;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
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

public class ResetPlayerDataCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.RESETPLAYERDATA)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage(Strings.USAGE + "/resetplayerdata <player|all>");
			return true;
		}

		if (args[0].equalsIgnoreCase("all")) {
			if (Bukkit.getOnlinePlayers().size() > 0) {
				sender.sendMessage(Strings.PREFIX + "Please make sure all players are offline before running this command.");
				return true;
			}

			DatabaseOperations.resetAllPlayerData();
			sender.sendMessage(Strings.PREFIX + "All player data has been reset.");
			return true;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		if (target.isOnline()) {
			sender.sendMessage(Strings.PREFIX + "Please make sure the player is offline before running this command.");
			return true;
		}

		DatabaseOperations.resetPlayerData(target.getUniqueId());
		sender.sendMessage(Strings.PREFIX + "Player data for " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " has been reset.");
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.RESETPLAYERDATA)) return arguments;

		if (args.length == 1) {
			arguments.add("all");
			arguments.addAll(PlayerManager.getAllPlayerNames());
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
