package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Hat implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.HAT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length > 0 && !sender.hasPermission(Permissions.HAT_OTHER)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player target = args.length > 0 ? Bukkit.getPlayer(args[0]) : (Player) sender;
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		ItemStack oldHelmet = target.getInventory().getHelmet();
		ItemStack newHelmet = ((Player) sender).getInventory().getItemInMainHand();
		target.getInventory().setHelmet(newHelmet);
		((Player) sender).getInventory().setItemInMainHand(oldHelmet);

		target.sendMessage(Strings.PREFIX + "Have fun with your new hat!");

		if (!target.getName().equals(sender.getName()))
			sender.sendMessage(Strings.PREFIX + "You gave " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " a new hat!");
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.HAT_OTHER))
			return arguments;

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
