package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Data.Warp;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Manager.WarpManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SetwarpCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.SETWARP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			sender.sendMessage(Strings.PREFIX + "Usage: /setwarp <name> [item]");
			return true;
		}

		String warpName = args[0];
		if (WarpManager.getWarp(warpName) != null) {
			sender.sendMessage(Strings.PREFIX + "A warp with that name already exists!");
			return true;
		}

		if (args.length == 1 && player.getInventory().getItemInMainHand().getType().isAir()) {
			sender.sendMessage(Strings.PREFIX + "You must be holding an item to set it as the warp item or specify it manually.");
			return true;
		}

		NamespacedKey key = args.length == 1 ? player.getInventory().getItemInMainHand().getType().getKey() : NamespacedKey.fromString(args[1]);
		if (key == null || key.equals(Material.AIR.getKey())) {
			sender.sendMessage(Strings.PREFIX + "Invalid item specified.");
			return true;
		}

		ItemStack item = args.length == 1 ? player.getInventory().getItemInMainHand() : new ItemStack(Arrays.stream(Material.values()).filter(material -> material.getKey().equals(key)).findFirst().orElse(Material.STONE));

		Location location = player.getLocation();
		Warp warp = new Warp(warpName, location, item);
		WarpManager.addWarp(warp);
		sender.sendMessage(Strings.PREFIX + "Warp " + ChatColor.YELLOW + warpName + ChatColor.GRAY + " has been created.");
		DatabaseOperations.addWarp(warp);
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.SETWARP)) return arguments;

		if (args.length == 1) {
			arguments.add("<name>");
		} else if (args.length == 2) {
			arguments.addAll(Arrays.stream(Material.values()).map(material -> material.getKey().toString()).collect(Collectors.toCollection(ArrayList::new)));
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
