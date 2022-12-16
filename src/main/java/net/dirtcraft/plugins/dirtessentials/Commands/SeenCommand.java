package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Data.PlayerData;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SeenCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.SEEN)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage(Strings.USAGE + "/seen <player>");
			return true;
		}

		UUID uuid = PlayerManager.getUuid(args[0]);
		if (uuid == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		PlayerData player = PlayerManager.getPlayerData(uuid);

		if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
			player.setLastLocation(Bukkit.getPlayer(uuid).getLocation());
			printOnlineSeen(sender, player);
			return true;
		}

		printOfflineSeen(sender, player);
		return true;
	}

	private void printOfflineSeen(CommandSender sender, PlayerData player) {
		LocalDateTime lastSeen = player.getLeaveDate();
		LocalDateTime now = LocalDateTime.now();

		long difference = lastSeen.until(now, ChronoUnit.SECONDS);
		long days = difference / 86400;
		difference %= 86400;
		long hours = difference / 3600;
		difference %= 3600;
		long minutes = difference / 60;
		difference %= 60;
		long seconds = difference;

		StringBuilder builder = new StringBuilder();
		if (days == 1)
			builder.append(days).append(" day ");
		if (days > 1)
			builder.append(days).append(" days ");
		if (hours == 1)
			builder.append(hours).append(" hour ");
		if (hours > 1)
			builder.append(hours).append(" hours ");
		if (minutes == 1)
			builder.append(minutes).append(" minute ");
		if (minutes > 1)
			builder.append(minutes).append(" minutes ");
		if (seconds == 1)
			builder.append(seconds).append(" second");
		if (seconds > 1)
			builder.append(seconds).append(" seconds");

		String time = builder.toString();

		BaseComponent[] uuidComponent = new ComponentBuilder()
				.append(ChatColor.GRAY + player.getUuid().toString())
				.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, player.getUuid().toString()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.RED + " Click to copy!")))
				.create();

		BaseComponent[] ipComponent = new ComponentBuilder()
				.append(ChatColor.GRAY + player.getLastIpAddress())
				.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, player.getLastIpAddress()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.YELLOW + " Click to copy!")))
				.create();

		BaseComponent[] locationComponent = new ComponentBuilder()
				.append(ChatColor.GRAY + player.getLastLocation().getWorld().getName() + ", " + ChatColor.AQUA + player.getLastLocation().getBlockX() + ", " + player.getLastLocation().getBlockY() + ", " + player.getLastLocation().getBlockZ())
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tppos " + player.getLastLocation().getWorld().getName() + " " + player.getLastLocation().getX() + " " + player.getLastLocation().getY() + " " + player.getLastLocation().getZ() + " " + player.getLastLocation().getYaw() + " " + player.getLastLocation().getPitch()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.DARK_AQUA + " Click to teleport!")))
				.create();

		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Player " + ChatColor.GOLD + player.getUsername() + ChatColor.GRAY + " has been " + ChatColor.RED + "offline" + ChatColor.GRAY + " since " + ChatColor.DARK_AQUA + time + ChatColor.GRAY + ".");
		sender.sendMessage("");
		if (sender.hasPermission(Permissions.STAFF)) {
			sender.spigot().sendMessage(new ComponentBuilder().append(ChatColor.GOLD + " UUID" + ChatColor.GRAY + ": ").event((HoverEvent) null).event((ClickEvent) null).append(uuidComponent).create());
			sender.spigot().sendMessage(new ComponentBuilder().append(ChatColor.GOLD + " IP Address" + ChatColor.GRAY + ": ").event((HoverEvent) null).event((ClickEvent) null).append(ipComponent).create());
			sender.spigot().sendMessage(new ComponentBuilder().append(ChatColor.GOLD + " Last Location" + ChatColor.GRAY + ": ").event((HoverEvent) null).event((ClickEvent) null).append(locationComponent).create());
			sender.sendMessage("");
		}
	}

	private void printOnlineSeen(CommandSender sender, PlayerData player) {
		BaseComponent[] uuidComponent = new ComponentBuilder()
				.append(ChatColor.GRAY + player.getUuid().toString())
				.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, player.getUuid().toString()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.RED + " Click to copy!")))
				.create();

		BaseComponent[] ipComponent = new ComponentBuilder()
				.append(ChatColor.GRAY + player.getLastIpAddress())
				.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, player.getLastIpAddress()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.YELLOW + " Click to copy!")))
				.create();

		BaseComponent[] locationComponent = new ComponentBuilder()
				.append(ChatColor.GRAY + player.getLastLocation().getWorld().getName() + ", " + ChatColor.AQUA + player.getLastLocation().getBlockX() + ", " + player.getLastLocation().getBlockY() + ", " + player.getLastLocation().getBlockZ())
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getUsername()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.DARK_AQUA + " Click to teleport!")))
				.create();

		sender.sendMessage("");
		sender.sendMessage(ChatColor.GRAY + "Player " + ChatColor.GOLD + player.getUsername() + ChatColor.GRAY + " is currently " + ChatColor.GREEN + "online" + ChatColor.GRAY + ".");
		sender.sendMessage("");
		if (sender.hasPermission(Permissions.STAFF)) {
			sender.spigot().sendMessage(new ComponentBuilder().append(ChatColor.GOLD + " UUID" + ChatColor.GRAY + ": ").event((HoverEvent) null).event((ClickEvent) null).append(uuidComponent).create());
			sender.spigot().sendMessage(new ComponentBuilder().append(ChatColor.GOLD + " IP Address" + ChatColor.GRAY + ": ").event((HoverEvent) null).event((ClickEvent) null).append(ipComponent).create());
			sender.spigot().sendMessage(new ComponentBuilder().append(ChatColor.GOLD + " Last Location" + ChatColor.GRAY + ": ").event((HoverEvent) null).event((ClickEvent) null).append(locationComponent).create());
			sender.sendMessage("");
		}
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.SEEN)) return arguments;

		if (args.length == 1) {
			arguments.addAll(PlayerManager.getAllPlayerNames());
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.toLowerCase().contains(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
