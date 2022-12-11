package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.TeleportManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TptoggleCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.TPTOGGLE)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (TeleportManager.isTpDisabled(player.getUniqueId())) {
			TeleportManager.setTpDisabled(player.getUniqueId(), false);
			player.sendMessage(Strings.PREFIX + "Players can now teleport to you again.");
		} else {
			TeleportManager.setTpDisabled(player.getUniqueId(), true);
			player.sendMessage(Strings.PREFIX + "Players can no longer teleport to you.");
		}

		return true;
	}
}
