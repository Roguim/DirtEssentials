package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.TeleportManager;
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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TpaCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.TPA)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/tpa <player>");
			return true;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		Player player = (Player) sender;
		if (player == target) {
			sender.sendMessage(Strings.PREFIX + "You can't teleport to yourself!");
			return true;
		}

		if (TeleportManager.isTpDisabled(target.getUniqueId())) {
			sender.sendMessage(Strings.PREFIX + "This player has teleportation disabled!");
			return true;
		}

		if (!sender.hasPermission(Permissions.WORLD + "." + target.getWorld().getName()) && !sender.hasPermission(Permissions.WORLD + ".*")) {
			sender.sendMessage(Strings.NO_WORLD_TELEPORT_PERMISSION);
			return true;
		}

		BaseComponent[] targetComponent = new ComponentBuilder()
				.append(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has requested to teleport to you.\n")
				.append(ChatColor.GRAY + "[" + ChatColor.GREEN + "\u2714 Accept" + ChatColor.GRAY + "]")
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Click to accept the teleport request.")))
				.append(" ").event((HoverEvent) null).event((ClickEvent) null)
				.append(ChatColor.GRAY + "[" + ChatColor.RED + "\u274c Deny" + ChatColor.GRAY + "]")
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Click to deny the teleport request."))).create();

		target.spigot().sendMessage(targetComponent);
		sender.sendMessage(ChatColor.GRAY + "Teleport request sent to " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + "!");
		sender.sendMessage(ChatColor.GRAY + "Use " + ChatColor.AQUA + "/tpacancel" + ChatColor.GRAY + " to cancel the request.");

		TeleportManager.teleportRequests.put(target.getUniqueId(), player.getUniqueId());

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.TPA)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
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
