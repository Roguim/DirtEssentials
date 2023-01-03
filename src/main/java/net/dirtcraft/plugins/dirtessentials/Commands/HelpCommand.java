package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Config.Page;
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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.HELP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		String gradient1 = GradientHandler.hsvGradient("--------------------------", new Color(251, 121, 0), new Color(247,0,0), GradientHandler::linear, net.md_5.bungee.api.ChatColor.STRIKETHROUGH);
		String gradient2 = GradientHandler.hsvGradient("--------------------------", new Color(247,0,0), new Color(251, 121, 0), GradientHandler::linear, net.md_5.bungee.api.ChatColor.STRIKETHROUGH);

		if (args.length == 0) {
			ComponentBuilder builder = new ComponentBuilder();
			builder.append("\n");

			Utilities.help.page.forEach(page -> {
				BaseComponent[] pageEntry = new ComponentBuilder()
						.append(Utilities.translate(page.getTitle(), false))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_AQUA + "\u2139 " + ChatColor.GRAY + "Click to view this page!")))
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + page.getName())).create();

				builder.append(ChatColor.GRAY + " \u27a4 ").append(pageEntry).append("\n");
			});

			builder.append("\n").event((HoverEvent) null).event((ClickEvent) null);
			builder.append(TextComponent.fromLegacyText(Utilities.translate(gradient1 + gradient2, false)));

			sender.sendMessage(Strings.HELP_BAR_TOP + "\n");
			((Player) sender).spigot().sendMessage(builder.create());
			return true;
		}

		String pageString = args[0];
		if (Utilities.help.page.stream().noneMatch(p -> p.getName().equalsIgnoreCase(pageString))) {
			sender.sendMessage(Strings.PREFIX + "This page does not exist!");
			return true;
		}

		Page page = Utilities.help.page.stream().filter(p -> p.getName().equalsIgnoreCase(pageString)).findFirst().orElse(null);
		if (page == null) return true;

		ComponentBuilder builder = new ComponentBuilder();
		builder.append(ChatColor.GRAY + "Help page for ").append(Utilities.translate(page.getTitle(), false)).append(ChatColor.GRAY + ":\n");
		builder.append("\n");

		Pattern pattern = Pattern.compile("\\[[^]]*]");
		page.getContent().forEach(line -> {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				if (matcher.group().startsWith("[http")) {
					String url = matcher.group().substring(1, matcher.group().length() - 1);
					BaseComponent[] urlEntry = new ComponentBuilder()
							.append(ChatColor.BLUE + "" + ChatColor.ITALIC + url + ChatColor.RESET)
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_AQUA + "\u2139 " + ChatColor.GRAY + "Click to open this link!")))
							.event(new ClickEvent(ClickEvent.Action.OPEN_URL, url)).create();

					builder.append(" ").append(line.substring(0, matcher.start()))
							.event((ClickEvent) null).event((HoverEvent) null)
							.append(urlEntry)
							.append(Utilities.translate(line.substring(matcher.end()), false))
							.event((ClickEvent) null).event((HoverEvent) null)
							.append("\n");
				} else {
					String commandName = matcher.group().substring(1, matcher.group().length() - 1);
					BaseComponent[] commandEntry = new ComponentBuilder()
							.append(Utilities.translate(commandName, false))
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.DARK_AQUA + "\u2139 " + ChatColor.GRAY + "Click to run this command!")))
							.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Utilities.translate(commandName, true))).create();

					builder.append(" ").append(Utilities.translate(line.substring(0, matcher.start()), false))
							.event((ClickEvent) null).event((HoverEvent) null)
							.append(commandEntry)
							.append(Utilities.translate(line.substring(matcher.end()), false))
							.event((ClickEvent) null).event((HoverEvent) null)
							.append("\n");
				}
			} else {
				builder.append(" ").append(Utilities.translate(line, false)).append("\n");
			}
		});

		builder.append("\n");
		builder.append(TextComponent.fromLegacyText(Utilities.translate(gradient1 + gradient2, false)));

		sender.sendMessage(Strings.HELP_BAR_TOP + "\n");
		((Player) sender).spigot().sendMessage(builder.create());

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.HELP)) return arguments;

		if (args.length == 1) {
			Utilities.help.page.stream().map(Page::getName).forEach(arguments::add);
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
