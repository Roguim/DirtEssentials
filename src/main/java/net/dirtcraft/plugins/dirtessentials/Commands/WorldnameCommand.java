package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class WorldnameCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		if (!sender.hasPermission(Permissions.WORLDNAME)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return false;
		}

		String worldName = ((Player) sender).getWorld().getName();
		BaseComponent[] message = new ComponentBuilder()
				.append(ChatColor.GOLD + "World" + ChatColor.GRAY + ": ")
				.append(ChatColor.GREEN + worldName)
				.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, worldName))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "Click to copy"))).create();

		sender.spigot().sendMessage(message);
		return true;
	}
}
