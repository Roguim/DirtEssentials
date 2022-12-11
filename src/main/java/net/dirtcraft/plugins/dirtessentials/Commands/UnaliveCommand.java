package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UnaliveCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		if (!sender.hasPermission(Permissions.UNALIVE)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return false;
		}

		Player player = (Player) sender;

		if (GodCommand.getGodPlayers().contains(player.getUniqueId())) {
			player.sendMessage(Strings.PREFIX + ChatColor.RED + "You cannot unalive yourself while in god mode!");
			return true;
		}

		player.setHealth(0);
		return true;
	}
}
