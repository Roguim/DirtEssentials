package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.TeleportManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpacancelCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.TPACANCEL)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (!TeleportManager.hasTpaRequest(player.getUniqueId()) && !TeleportManager.hasTpahereRequest(player.getUniqueId())) {
			player.sendMessage(ChatColor.RED + "You do not have any pending teleport requests!");
			return true;
		}

		if (TeleportManager.hasTpaRequest(player.getUniqueId())) {
			Player target = Bukkit.getPlayer(TeleportManager.getTpaRequest(player.getUniqueId()));
			target.sendMessage(ChatColor.RED + "Your teleport request to " + player.getName() + " has been cancelled!");
			player.sendMessage(ChatColor.RED + "You have cancelled your teleport request to " + target.getName() + "!");
		} else {
			Player target = Bukkit.getPlayer(TeleportManager.getTpahereRequest(player.getUniqueId()));
			target.sendMessage(ChatColor.RED + "Your teleport request to " + player.getName() + " has been cancelled!");
			player.sendMessage(ChatColor.RED + "You have cancelled your teleport here request with " + target.getName() + "!");
		}

		TeleportManager.removeOpenTpRequest(player.getUniqueId());

		return true;
	}
}
