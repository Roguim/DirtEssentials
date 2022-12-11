package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlyCommand implements CommandExecutor, TabCompleter, Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (player.hasPermission(Permissions.FLY)) {
			if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
				turnFlightOn(player);
			}
		}
	}

	private void turnFlightOn(Player player) {
		player.setAllowFlight(true);
		player.setFlying(true);
		player.sendMessage(Strings.PREFIX + "Creative Flight is now " + ChatColor.GREEN + "enabled");
	}

	private void turnFlightOff(Player player) {
		player.setAllowFlight(false);
		player.setFlying(false);
		player.sendMessage(Strings.PREFIX + "Creative Flight is now " + ChatColor.RED + "disabled");
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player) && args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/fly <player>");
			return true;
		}

		if (!sender.hasPermission(Permissions.FLY)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length > 0 && !sender.hasPermission(Permissions.FLY_OTHERS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player target = args.length == 0 ? (Player) sender : Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		if (target.getAllowFlight()) {
			turnFlightOff(target);
			if (!(sender instanceof Player) || !sender.getName().equals(target.getName())) sender.sendMessage(Strings.PREFIX + "Creative flight for " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + " is now " + ChatColor.RED + "disabled");
		} else {
			turnFlightOn(target);
			if (!(sender instanceof Player) || !sender.getName().equals(target.getName())) sender.sendMessage(Strings.PREFIX + "Creative flight for " + ChatColor.AQUA + target.getName() + ChatColor.GRAY + " is now " + ChatColor.GREEN + "enabled");
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.FLY_OTHERS)) return arguments;

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
