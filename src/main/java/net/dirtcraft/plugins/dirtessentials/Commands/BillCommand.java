package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
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

public class BillCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		if (!sender.hasPermission(Permissions.BILL)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return false;
		}

		if (args.length != 2) {
			sender.sendMessage(Strings.USAGE + "/bill <player> <amount>");
			return false;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return false;
		}

		if (target.getUniqueId().equals(((Player) sender).getUniqueId())) {
			sender.sendMessage(Strings.PREFIX + "You can't bill yourself!");
			return false;
		}

		double amount;
		try {
			amount = Double.parseDouble(args[1]);
		} catch (NumberFormatException e) {
			sender.sendMessage(Strings.USAGE + "/bill <player> <amount>");
			return false;
		}

		if (amount < 0) {
			sender.sendMessage(Strings.INVALID_AMOUNT);
			return false;
		}

		BaseComponent[] messageComponent = new ComponentBuilder()
				.append(Strings.PREFIX + "You have been billed by " + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " for " + ChatColor.GREEN + amount + ChatColor.YELLOW + Utilities.config.economy.currencySymbol + "\n")
				.append("   ")
				.append(ChatColor.GRAY + "[" + ChatColor.GREEN + "\u2714 Pay the bill" + ChatColor.GRAY + "]")
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pay " + sender.getName() + " " + amount))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Click to pay the bill"))).create();

		target.spigot().sendMessage(messageComponent);

		sender.sendMessage(Strings.PREFIX + "You have billed " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " for " + ChatColor.GREEN + amount + ChatColor.YELLOW + Utilities.config.economy.currencySymbol);

		return false;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.BILL)) return arguments;

		if (args.length == 1) {
			arguments.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
		} else if (args.length == 2) {
			arguments.add("amount");
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
