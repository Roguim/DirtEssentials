package net.dirtcraft.plugins.dirtessentials.Commands;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.dirtcraft.plugins.dirtessentials.Config.Kit;
import net.dirtcraft.plugins.dirtessentials.Config.Kits;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Createkit implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.CREATEKIT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length != 3) {
			sender.sendMessage(Strings.USAGE + "/createkit <name> <cooldown> <second|minute|hour|day>");
			return true;
		}

		if (!Utilities.isInteger(args[1])) {
			sender.sendMessage(Strings.USAGE + "/createkit <name> <cooldown> <second|minute|hour|day>");
			return true;
		}

		if (!args[2].equalsIgnoreCase("second") && !args[2].equalsIgnoreCase("minute") && !args[2].equalsIgnoreCase("hour") && !args[2].equalsIgnoreCase("day")) {
			sender.sendMessage(Strings.USAGE + "/createkit <name> <cooldown> <second|minute|hour|day>");
			return true;
		}

		String kitName = args[0];

		if (Utilities.kits.kits.stream().anyMatch(kit -> kit.name.equalsIgnoreCase(kitName))) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "A kit with this name already exists!");
			return true;
		}

		Player player = (Player) sender;
		Block chest = player.getTargetBlock(null, 5);
		if (chest.getType() != Material.CHEST) {
			player.sendMessage(Strings.PREFIX + ChatColor.RED + "You must be looking at a chest!");
			return true;
		}

		Inventory chestInventory = ((Chest) chest.getState()).getInventory();
		if (chestInventory.isEmpty()) {
			player.sendMessage(Strings.PREFIX + ChatColor.RED + "The chest you are looking at is empty!");
			return true;
		}

		List<String> giveCommands = new ArrayList<>();
		for (int i = 0; i < chestInventory.getSize(); i++) {
			ItemStack item = chestInventory.getItem(i);
			if (item == null)
				continue;

			NBTItem nbtItem = new NBTItem(item);
			String commandString = String.format("minecraft:give %s %s%s %s", "{PLAYER}", item.getType().getKey(), nbtItem.toString().equals("{}") ? "" : nbtItem, item.getAmount());

			giveCommands.add(commandString);
		}

		Utilities.kits.kits.add(new Kit(kitName, Integer.parseInt(args[1]), args[2].toLowerCase(), giveCommands));
		Utilities.kits.save();
		Utilities.loadKits();

		sender.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Kit " + ChatColor.YELLOW + kitName + ChatColor.GRAY + " has been created!");

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.CREATEKIT))
			return arguments;

		if (args.length == 1) {
			arguments.add("<name>");
		} else if (args.length == 2) {
			arguments.add("<cooldown>");
		} else if (args.length == 3) {
			arguments.add("second");
			arguments.add("minute");
			arguments.add("hour");
			arguments.add("day");
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
