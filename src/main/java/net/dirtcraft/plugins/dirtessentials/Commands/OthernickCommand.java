package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
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

public class OthernickCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.OTHERNICK)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 2) {
			sender.sendMessage(Strings.USAGE + "/othernick <player> <reset|<nick>>");
			return true;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		if (args[1].equalsIgnoreCase("reset")) {
			target.setDisplayName(target.getName());
			target.sendMessage(Strings.PREFIX + ChatColor.GOLD + target.getName() + ChatColor.GRAY + "'s nickname has been reset!");
			return true;
		}

		if (Utilities.translate(args[1], true).length() > 16) {
			target.sendMessage(Strings.PREFIX + "The nickname cannot be longer than 16 characters! (excluding color codes)");
			return true;
		}

		String nickname = Utilities.config.general.nicknamePrefix + Utilities.translate(args[0], false);

		target.setDisplayName(nickname);
		target.sendMessage(Strings.PREFIX + ChatColor.GOLD + target.getName() + ChatColor.GRAY + "'s nickname has been set to " + nickname + ChatColor.GRAY + "!");

		PlayerManager.getPlayerData(target.getUniqueId()).setDisplayName(nickname);
		DatabaseOperations.setNickname(target.getUniqueId(), Utilities.translate(args[1], false));
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.OTHERNICK)) return arguments;

		if (args.length == 1) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				arguments.add(player.getName());
			}
		} else if (args.length == 2) {
			arguments.add("reset");
			arguments.add("<nick>");
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
