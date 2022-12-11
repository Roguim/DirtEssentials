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

public class TpacceptCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.TPACCEPT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (!TeleportManager.hasTpaRequest(player.getUniqueId()) && !TeleportManager.hasTpahereRequest(player.getUniqueId())) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "You do not have any teleport requests!");
			return true;
		}

		Player target = TeleportManager.hasTpaRequest(player.getUniqueId()) ? Bukkit.getPlayer(TeleportManager.getTpaRequest(player.getUniqueId())) : Bukkit.getPlayer(TeleportManager.getTpahereRequest(player.getUniqueId()));
		if (target == null) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "The player who sent you the teleport request is no longer online!");
			TeleportManager.removeOpenTpRequest(player.getUniqueId());
			return true;
		}

		if (TeleportManager.hasTpaRequest(player.getUniqueId())) {
			target.teleport(player);
			target.sendMessage(Strings.PREFIX + ChatColor.GRAY + "You have been teleported to " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + "!");
			player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "You have accepted " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + "s teleport request!");
		} else {
			player.teleport(target);
			target.sendMessage(Strings.PREFIX + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has accepted your teleport request!");
			player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "You have been teleported to " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + "!");
		}

		return true;
	}
}
