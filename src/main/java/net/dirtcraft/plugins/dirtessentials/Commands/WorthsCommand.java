package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WorthsCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.WORTHS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (Utilities.worthData == null) {
			sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "Worth data is not initialized!");
			return true;
		}

		sender.sendMessage("");
		sender.sendMessage(Strings.PREFIX + "You can find a list of all the worths here:");
		sender.sendMessage("");
		sender.spigot().sendMessage(
				new ComponentBuilder()
						.append("   " + ChatColor.GOLD + "https://worth.dirtcraft.net/" + Utilities.worthData.getServer())
						.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://worth.dirtcraft.net/" + Utilities.worthData.getServer()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_AQUA + "\u2139 " + ChatColor.GRAY + "Click to open!"))).create()
		);
		sender.sendMessage("");

		return true;
	}
}
