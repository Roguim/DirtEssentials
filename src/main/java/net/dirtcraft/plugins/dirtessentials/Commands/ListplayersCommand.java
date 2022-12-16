package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Manager.AfkManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.dirtcraft.plugins.dirtessentials.Utils.gradient.GradientHandler;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListplayersCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.LISTPLAYERS)){
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		if (players.isEmpty()) {
			sender.spigot().sendMessage(TextComponent.fromLegacyText(Strings.LIST_BAR_TOP));
			sender.sendMessage("");
			sender.sendMessage(ChatColor.RED + "There are no players online!");
			return true;
		}

		List<Player> staff = new ArrayList<>();
		List<Player> playersList = new ArrayList<>();

		for (Player player : players) {
			if (player.hasPermission(Permissions.STAFF)) staff.add(player);
			else playersList.add(player);
		}

		sender.spigot().sendMessage(TextComponent.fromLegacyText(Strings.LIST_BAR_TOP));
		sender.sendMessage(ChatColor.GRAY + "There " + (players.size() == 1 ? "is " : "are ") + ChatColor.AQUA + players.size() + ChatColor.GRAY + (players.size() == 1 ? " player" : " players") + " online right now!");
		sender.sendMessage("");
		if (staff.size() > 0) {
			sender.sendMessage(ChatColor.GOLD + " Staff" + ChatColor.GRAY + ": " + ChatColor.AQUA + staff.size());

			ComponentBuilder staffPlayers = new ComponentBuilder();
			for (int i = 0; i < staff.size(); i++) {
				BaseComponent[] staffComponent = new ComponentBuilder()
						.append(
								AfkManager.isPlayerAfk(staff.get(i).getUniqueId()) ?
										ChatColor.GRAY + "" + ChatColor.ITALIC + "AFK " + ChatColor.RESET + staff.get(i).getDisplayName() :
										staff.get(i).getDisplayName()
						).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + staff.get(i).getName() + " "))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Name" + ChatColor.GRAY + ": " + ChatColor.AQUA + staff.get(i).getName() + "\n" +
								ChatColor.GOLD + "Rank" + ChatColor.GRAY + ": " + Utilities.translate(DirtEssentials.getChat().getPlayerPrefix(staff.get(i)), false)))).create();

				staffPlayers.append(staffComponent);
				if (i != staff.size() - 1) staffPlayers.append(ChatColor.GRAY + ", ");
			}
			sender.spigot().sendMessage(staffPlayers.create());
		}

		if (!staff.isEmpty() && !playersList.isEmpty()) sender.sendMessage("");

		if (playersList.size() > 0) {
			sender.sendMessage(ChatColor.GOLD + " Players" + ChatColor.GRAY + ": " + ChatColor.AQUA + playersList.size());

			ComponentBuilder normalPlayers = new ComponentBuilder();
			for (int i = 0; i < playersList.size(); i++) {
				BaseComponent[] playerComponent = new ComponentBuilder()
						.append(
								AfkManager.isPlayerAfk(playersList.get(i).getUniqueId()) ?
										ChatColor.GRAY + "" + ChatColor.ITALIC + "AFK " + ChatColor.RESET + playersList.get(i).getDisplayName() :
										playersList.get(i).getDisplayName()
						)
						.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + playersList.get(i).getName() + " "))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Name" + ChatColor.GRAY + ": " + ChatColor.AQUA + playersList.get(i).getName() + "\n" +
								ChatColor.GOLD + "Rank" + ChatColor.GRAY + ": " + Utilities.translate(DirtEssentials.getChat().getPlayerPrefix(playersList.get(i)), false)))).create();

				normalPlayers.append(playerComponent);
				if (i != playersList.size() - 1) normalPlayers.append(ChatColor.GRAY + ", ");
			}
			sender.spigot().sendMessage(normalPlayers.create());
		}

		sender.sendMessage("");

		String gradient1 = GradientHandler.hsvGradient("----------------------", new Color(251, 121, 0), new Color(247,0,0), GradientHandler::linear, net.md_5.bungee.api.ChatColor.STRIKETHROUGH);
		String gradient2 = GradientHandler.hsvGradient("----------------------", new Color(247,0,0), new Color(251, 121, 0), GradientHandler::linear, net.md_5.bungee.api.ChatColor.STRIKETHROUGH);
		sender.spigot().sendMessage(TextComponent.fromLegacyText(Utilities.translate(gradient1 + gradient2, false)));

		return true;
	}
}
