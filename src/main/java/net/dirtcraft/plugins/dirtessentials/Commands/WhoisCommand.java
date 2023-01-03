package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Manager.ABManager;
import net.dirtcraft.plugins.dirtessentials.Manager.HomeManager;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
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
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhoisCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.WHOIS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/whois <player>");
			return true;
		}

		String player = args[0];
		Player target = Bukkit.getPlayer(player);

		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		sender.sendMessage(Strings.WHOIS_BAR_TOP);
		sender.sendMessage(ChatColor.GOLD + " - Player" + ChatColor.GRAY + ": " + target.getName());
		sender.sendMessage(ChatColor.GOLD + " - DisplayName" + ChatColor.GRAY + ": " + target.getDisplayName());
		sender.spigot().sendMessage(
				new ComponentBuilder(ChatColor.GOLD + " - UUID" + ChatColor.GRAY + ": ")
						.append(target.getUniqueId().toString())
						.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, target.getUniqueId().toString()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.GRAY + " Click to copy!"))).create()
		);
		sender.spigot().sendMessage(
				new ComponentBuilder(ChatColor.GOLD + " - IP" + ChatColor.GRAY + ": ")
						.append(target.getAddress().getAddress().getHostAddress())
						.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, target.getAddress().getAddress().getHostAddress()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.GRAY + " Click to copy!"))).create()
		);
		sender.sendMessage(ChatColor.GOLD + " - Gamemode" + ChatColor.GRAY + ": " + target.getGameMode().name());
		sender.sendMessage(ChatColor.GOLD + " - Health" + ChatColor.GRAY + ": " + target.getHealth());
		sender.sendMessage(ChatColor.GOLD + " - Food" + ChatColor.GRAY + ": " + target.getFoodLevel() + " / 20  (" + target.getSaturation() + " saturation)");
		sender.spigot().sendMessage(
				new ComponentBuilder(ChatColor.GOLD + " - Location" + ChatColor.GRAY + ": ")
						.append(ChatColor.BLUE + target.getLocation().getWorld().getName() + " " + ChatColor.AQUA + target.getLocation().getBlockX() + " " + target.getLocation().getBlockY() + " " + target.getLocation().getBlockZ())
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tppos " + target.getLocation().getX() + " " + target.getLocation().getY() + " " + target.getLocation().getZ() + " " + target.getLocation().getWorld().getName() + " " + target.getLocation().getYaw() + " " + target.getLocation().getPitch()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BLUE + "\u2139" + ChatColor.GRAY + " Click to teleport!"))).create()
		);
		sender.sendMessage(ChatColor.GOLD + " - Staff" + ChatColor.GRAY + ": " + ChatColor.DARK_PURPLE + PlayerManager.getPlayerData(target.getUniqueId()).isStaff());
		sender.sendMessage(ChatColor.GOLD + " - Flying" + ChatColor.GRAY + ": " + ChatColor.DARK_PURPLE + target.isFlying());
		sender.sendMessage(ChatColor.GOLD + " - Fly Speed" + ChatColor.GRAY + ": " + target.getFlySpeed());
		sender.sendMessage(ChatColor.GOLD + " - Walk Speed" + ChatColor.GRAY + ": " + target.getWalkSpeed());
		sender.sendMessage(ChatColor.GOLD + " - Money" + ChatColor.GRAY + ": " + ChatColor.GREEN + DirtEssentials.getDirtEconomy().getBalance(target) + Utilities.config.economy.currencySymbol);
		sender.sendMessage(ChatColor.GOLD + " - God Mode" + ChatColor.GRAY + ": " + ChatColor.DARK_PURPLE + GodCommand.getGodPlayers().contains(target.getUniqueId()));
		sender.sendMessage(ChatColor.GOLD + " - Home Limit" + ChatColor.GRAY + ": " + ChatColor.AQUA + HomeManager.getAvailableHomeCount(target.getUniqueId()));
		sender.sendMessage(ChatColor.GOLD + " - Autobroadcasts" + ChatColor.GRAY + ": " + (ABManager.hasABDisabled(target.getUniqueId()) ? ChatColor.RED + "Disabled" : ChatColor.GREEN + "Enabled"));
		sender.sendMessage("");

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.WHOIS)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
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
