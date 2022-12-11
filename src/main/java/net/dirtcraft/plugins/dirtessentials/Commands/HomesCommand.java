package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Data.Home;
import net.dirtcraft.plugins.dirtessentials.Manager.HomeManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.dirtcraft.plugins.dirtessentials.Utils.gradient.GradientHandler;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomesCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.HOMES)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		List<Home> homes = HomeManager.getHomes(player.getUniqueId());

		if (homes == null || homes.isEmpty()) {
			player.sendMessage(Strings.PREFIX + "You don't have any homes!");
			return true;
		}

		int listEntries = Math.min(Utilities.config.home.homesSize, 15);
		int page = args.length > 0 ? Integer.parseInt(args[0]) : 1;

		int maxPages = (int) Math.ceil((double) homes.size() / (double) listEntries);
		if (page > maxPages || page < 1) {
			sender.sendMessage(Strings.PREFIX + "Page must be smaller or equal to: " + maxPages);
			return true;
		}

		int start = (page - 1) * listEntries;
		int end = page * listEntries;
		if (end > homes.size()) {
			end = homes.size();
		}

		sender.sendMessage("");
		sender.sendMessage(Strings.HOMES_BAR_TOP);
		sender.sendMessage(ChatColor.GRAY + "You currently have " + ChatColor.AQUA + homes.size() + ChatColor.GRAY + " / " + ChatColor.AQUA + HomeManager.getHomeBalance(player.getUniqueId()) + ChatColor.GRAY + " homes set.");
		sender.sendMessage("");
		for (int i = start; i < end; i++) {
			Home home = homes.get(i);

			BaseComponent[] homeComponent = new ComponentBuilder("")
					.append(ChatColor.GOLD + home.getName())
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
							ChatColor.GOLD + "World" + ChatColor.GRAY + ": " + ChatColor.BLUE + home.getWorld() + "\n" +
									ChatColor.GOLD + "Coords" + ChatColor.GRAY + ": " + ChatColor.BLUE + Utilities.round(home.getX(), 2) + " " + Utilities.round(home.getY(), 2) + " " + Utilities.round(home.getZ(), 2) + "\n" +
									ChatColor.GOLD + "Yaw" + ChatColor.GRAY + ": " + ChatColor.BLUE + home.getYaw() + "\n" +
									ChatColor.GOLD + "Pitch" + ChatColor.GRAY + ": " + ChatColor.BLUE + home.getPitch() + "\n" +
									"\n" +
									ChatColor.AQUA + "Click to teleport to this home!")
					))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + home.getName()))
					.create();

			BaseComponent[] deleteComponent = new ComponentBuilder("")
					.append(ChatColor.GRAY + "[" + ChatColor.RED + "\u2715" + ChatColor.GRAY + "]")
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Delete this home")))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delhome " + home.getName()))
					.create();

			ComponentBuilder message = new ComponentBuilder();
			message.append(ChatColor.BLUE + "  - ").event((ClickEvent) null).event((HoverEvent) null);

			if (sender.hasPermission(Permissions.DELHOME)) {
				message.append(deleteComponent);
				message.append(" ").event((ClickEvent) null).event((HoverEvent) null);
			}

			message.append(homeComponent);

			sender.spigot().sendMessage(message.create());
		}

		sender.sendMessage("");
		TextComponent bottomBar = new TextComponent(TextComponent.fromLegacyText(GradientHandler.hsvGradient("-----------------", new java.awt.Color(251, 121, 0), new java.awt.Color(247, 0, 0), GradientHandler::linear, net.md_5.bungee.api.ChatColor.STRIKETHROUGH)));
		TextComponent pagePrev;
		if (page == 1) {
			pagePrev = new TextComponent(ChatColor.GRAY + "  \u25C0 ");
			pagePrev.setBold(true);
			pagePrev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "You are already on the first page!")));
		} else {
			pagePrev = new TextComponent(ChatColor.GREEN + "  \u25C0 ");
			pagePrev.setBold(true);
			pagePrev.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Previous page")));
			pagePrev.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/homes " + (page - 1)));
		}
		bottomBar.addExtra(pagePrev);
		TextComponent pageNext;
		if (page == maxPages) {
			pageNext = new TextComponent(ChatColor.GRAY + " \u25B6  ");
			pageNext.setBold(true);
			pageNext.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "You are already on the last page!")));
		} else {
			pageNext = new TextComponent(ChatColor.GREEN + " \u25B6  ");
			pageNext.setBold(true);
			pageNext.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Next page")));
			pageNext.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/homes " + (page + 1)));
		}
		bottomBar.addExtra(pageNext);
		bottomBar.addExtra(new TextComponent(TextComponent.fromLegacyText(GradientHandler.hsvGradient("-----------------", new java.awt.Color(247, 0, 0), new java.awt.Color(251, 121, 0), GradientHandler::linear, net.md_5.bungee.api.ChatColor.STRIKETHROUGH))));
		sender.spigot().sendMessage(bottomBar);
		sender.sendMessage("");

		return true;
	}
}
