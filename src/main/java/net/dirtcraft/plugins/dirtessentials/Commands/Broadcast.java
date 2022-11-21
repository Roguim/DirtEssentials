package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Broadcast implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.BROADCAST)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/broadcast <message>");
			return true;
		}

		StringBuilder message = new StringBuilder();
		for (String arg : args) {
			message.append(arg).append(" ");
		}

		sender.getServer().broadcastMessage(Utilities.translate(Utilities.config.general.broadcastPrefix, false) + " " + ChatColor.RESET + Utilities.translate(message.toString(), false));
		return true;
	}
}
