package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KickallCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.KICKALL)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (sender instanceof Player) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.hasPermission(Permissions.KICKALL_EXEMPT)) continue;
				player.kickPlayer(ChatColor.GRAY + "\n\nKicked by " + ChatColor.GOLD + sender.getName() + "\n" + ChatColor.GRAY + "Feel free to rejoin!");
			}
			return true;
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.kickPlayer(ChatColor.GRAY + "\n\nKicked by " + ChatColor.RED + "CONSOLE" + "\n" + ChatColor.GRAY + "Feel free to rejoin!");
		}

		return true;
	}
}
