package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.TeleportManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpallCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.TPALL)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (TeleportManager.isTpDisabled(player.getUniqueId())) continue;

			player.teleport(((Player) sender).getLocation());
			player.sendMessage(Strings.PREFIX + "You have been teleported to " + ChatColor.GOLD + sender.getName());
		}

		sender.sendMessage(Strings.PREFIX + "Teleported all players to you.");
		return true;
	}
}
