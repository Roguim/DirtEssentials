package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.MessageManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MsgtoggleCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.MSGTOGGLE)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (MessageManager.canReceiveMsg(player.getUniqueId())) {
			MessageManager.enableMsgToggle(player.getUniqueId());
			sender.sendMessage(Strings.PREFIX + "You have turned private messages " + ChatColor.RED + "off");
			return true;
		}

		MessageManager.disableMsgToggle(player.getUniqueId());
		sender.sendMessage(Strings.PREFIX + "You have turned private messages " + ChatColor.GREEN + "on");
		return true;
	}
}
