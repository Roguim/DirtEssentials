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

public class SocialspyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.SOCIALSPY)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (MessageManager.isSocialSpyActive(player.getUniqueId())) {
			MessageManager.deactivateSocialSpy(player.getUniqueId());
			sender.sendMessage(Strings.PREFIX + "SocialSpy has been " + ChatColor.RED + "disabled");
			return true;
		}

		MessageManager.activateSocialSpy(player.getUniqueId());
		sender.sendMessage(Strings.PREFIX + "SocialSpy has been " + ChatColor.GREEN + "enabled");
		return true;
	}
}
