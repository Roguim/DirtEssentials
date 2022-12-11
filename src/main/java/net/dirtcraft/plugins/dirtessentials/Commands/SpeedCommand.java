package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpeedCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.SPEED)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (!(sender instanceof Player) && args.length < 3) {
			sender.sendMessage(Strings.USAGE + "/speed <speed|reset> <type> <player>");
			return true;
		}

		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (args.length == 0) {
				player.sendMessage(Strings.USAGE + "/speed <speed|reset> [type] [player]");
				return true;
			}

			if (!Utilities.isFloat(args[0]) && !args[0].equalsIgnoreCase("reset")) {
				player.sendMessage(Strings.USAGE + "/speed <speed|reset> [type] [player]");
				return true;
			}

			if (Utilities.isFloat(args[0]) && (Float.parseFloat(args[0]) > 10 || Float.parseFloat(args[0]) < 0)) {
				player.sendMessage(Strings.PREFIX + ChatColor.RED + "Speed must be between 0 and 10!");
				return true;
			}

			String speed = args[0];

			if (args.length == 1) {
				if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
					if (speed.equalsIgnoreCase("reset")) {
						player.setFlySpeed(0.1f);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Fly Speed reset!");
					} else {
						player.setFlySpeed(Float.parseFloat(args[0]) / 10);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Fly Speed set to " + ChatColor.AQUA + args[0] + ChatColor.GRAY + "!");
					}
				} else {
					if (speed.equalsIgnoreCase("reset")) {
						player.setWalkSpeed(0.2f);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Walk Speed reset!");
					} else {
						player.setWalkSpeed(Float.parseFloat(args[0]) / 10);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Walk Speed set to " + ChatColor.AQUA + args[0] + ChatColor.GRAY + "!");
					}
				}

				return true;
			}

			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("walk")) {
					if (speed.equalsIgnoreCase("reset")) {
						player.setWalkSpeed(0.2f);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Walk Speed reset!");
					} else {
						player.setWalkSpeed(Float.parseFloat(args[0]) / 10);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Walk Speed set to " + ChatColor.AQUA + args[0] + ChatColor.GRAY + "!");
					}
				} else if (args[1].equalsIgnoreCase("fly")) {
					if (speed.equalsIgnoreCase("reset")) {
						player.setFlySpeed(0.1f);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Fly Speed reset!");
					} else {
						player.setFlySpeed(Float.parseFloat(args[0]) / 10);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Fly Speed set to " + ChatColor.AQUA + args[0] + ChatColor.GRAY + "!");
					}
				} else {
					player.sendMessage(Strings.USAGE + "/speed <speed|reset> [type] [player]");
				}

				return true;
			}

			if (args.length == 3) {
				Player target = Bukkit.getPlayer(args[2]);
				if (target == null) {
					player.sendMessage(Strings.PLAYER_NOT_FOUND);
					return true;
				}

				if (args[1].equalsIgnoreCase("walk")) {
					if (speed.equalsIgnoreCase("reset")) {
						player.setWalkSpeed(0.2f);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Walk Speed reset!");
					} else {
						player.setWalkSpeed(Float.parseFloat(args[0]) / 10);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Walk Speed set to " + ChatColor.AQUA + args[0] + ChatColor.GRAY + "!");
					}
				} else if (args[1].equalsIgnoreCase("fly")) {
					if (speed.equalsIgnoreCase("reset")) {
						player.setFlySpeed(0.1f);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Fly Speed reset!");
					} else {
						player.setFlySpeed(Float.parseFloat(args[0]) / 10);
						player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Fly Speed set to " + ChatColor.AQUA + args[0] + ChatColor.GRAY + "!");
					}
				} else {
					player.sendMessage(Strings.USAGE + "/speed <speed|reset> [type] [player]");
				}

				return true;
			}
		}

		Player target = Bukkit.getPlayer(args[2]);
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		if (args[1].equalsIgnoreCase("walk")) {
			target.setWalkSpeed(Float.parseFloat(args[0]) / 10);
		} else if (args[1].equalsIgnoreCase("fly")) {
			target.setFlySpeed(Float.parseFloat(args[0]) / 10);
		} else {
			sender.sendMessage(Strings.USAGE + "/speed <speed|reset> [type] [player]");
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.SPEED)) return arguments;

		if (args.length == 1) {
			arguments.add("0 - 10");
			arguments.add("reset");
		} else if (args.length == 2) {
			arguments.add("walk");
			arguments.add("fly");
		} else if (args.length == 3) {
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
