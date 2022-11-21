package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Enchant implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.ENCHANT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;
		ItemStack hand = player.getInventory().getItemInMainHand();

		if (hand.getType() == Material.AIR) {
			player.sendMessage(Strings.PREFIX + "You must be holding an item to enchant!");
			return true;
		}

		if (args.length != 2) {
			player.sendMessage(Strings.USAGE + "/enchant <enchantment> <level>");
			return true;
		}

		if (!Utilities.isInteger(args[1])) {
			player.sendMessage(Strings.PREFIX + "Level must be a number!");
			return true;
		}

		if (Integer.parseInt(args[1]) > 10 || Integer.parseInt(args[1]) < 1) {
			player.sendMessage(Strings.PREFIX + "Level must be between 1 and 10!");
			return true;
		}

		NamespacedKey enchant = NamespacedKey.fromString(args[0]);
		if (enchant == null) {
			player.sendMessage(Strings.PREFIX + "Invalid enchantment!");
			return true;
		}

		Enchantment enchantment = Enchantment.getByKey(enchant);
		if (enchantment == null) {
			player.sendMessage(Strings.PREFIX + "Invalid enchantment!");
			return true;
		}

		hand.addUnsafeEnchantment(enchantment, Integer.parseInt(args[1]));
		player.sendMessage(Strings.PREFIX + "Enchanted item with " + ChatColor.AQUA + enchantment.getKey().getKey() + " " + ChatColor.DARK_AQUA + args[1]);

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.ENCHANT)) return arguments;

		if (args.length == 1) {
			Enchantment[] enchantments = Enchantment.values();
			for (Enchantment enchantment : enchantments) {
				arguments.add(enchantment.getKey().toString());
			}
		} else if (args.length == 2) {
			arguments.add("1 - 10");
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
