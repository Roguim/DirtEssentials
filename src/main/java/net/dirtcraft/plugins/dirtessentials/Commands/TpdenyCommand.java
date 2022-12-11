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

public class TpdenyCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.TPDENY)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;
		boolean hasTpaRequest = TeleportManager.hasTpaRequest(player.getUniqueId());
		boolean hasTpahereRequest = TeleportManager.hasTpahereRequest(player.getUniqueId());

		if (!hasTpaRequest && !hasTpahereRequest) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "You do not have any teleport requests!");
			return true;
		}

		Player target = hasTpaRequest ? Bukkit.getPlayer(TeleportManager.getTpaRequest(player.getUniqueId())) : Bukkit.getPlayer(TeleportManager.getTpahereRequest(player.getUniqueId()));
		if (target == null) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "The player who sent you the teleport request is no longer online!");
			TeleportManager.removeOpenTpRequest(player.getUniqueId());
			return true;
		}

		TeleportManager.removeOpenTpRequest(target.getUniqueId());

		player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "You have denied " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + "'s teleport request!");
		target.sendMessage(Strings.PREFIX + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has denied your teleport request!");

		return true;
	}
}
