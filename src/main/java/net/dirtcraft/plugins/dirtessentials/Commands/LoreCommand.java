package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LoreCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.LORE)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.getType().isAir()) {
			player.sendMessage(Strings.PREFIX + "You must be holding an item to add lore to it!");
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(Strings.USAGE + "/lore <add|remove|clear> [line]");
			return true;
		}

		final ItemMeta meta = item.getItemMeta();
		switch (args[0]) {
			case "add":
				if (args.length == 1) {
					player.sendMessage(Strings.USAGE + "/lore add <line>");
					return true;
				}
				List<String> lore = meta.getLore();
				if (lore == null) lore = new ArrayList<>();
				StringBuilder message = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					message.append(args[i]).append(" ");
				}

				lore.add(Utilities.translate(message.toString().trim(), false));
				meta.setLore(lore);
				item.setItemMeta(meta);
				player.sendMessage(Strings.PREFIX + "Added lore to item!");
				break;
			case "remove":
				if (args.length == 1) {
					player.sendMessage(Strings.USAGE + "/lore remove <line>");
					return true;
				}
				lore = meta.getLore();
				if (lore == null) {
					player.sendMessage(Strings.PREFIX + "This item has no lore!");
					return true;
				}
				int line;
				try {
					line = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					player.sendMessage(Strings.PREFIX + "Invalid line number!");
					return true;
				}
				if (line < 1 || line > lore.size()) {
					player.sendMessage(Strings.PREFIX + "Invalid line number!");
					return true;
				}
				lore.remove(line - 1);
				meta.setLore(lore);
				item.setItemMeta(meta);
				player.sendMessage(Strings.PREFIX + "Removed lore from item!");
				break;
			case "clear":
				meta.setLore(null);
				item.setItemMeta(meta);
				player.sendMessage(Strings.PREFIX + "Cleared lore from item!");
				break;
			default:
				player.sendMessage(Strings.USAGE + "/lore <add|remove|clear> [line]");
				break;
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.LORE)) return arguments;

		if (args.length == 1) {
			arguments.add("add");
			arguments.add("remove");
			arguments.add("clear");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
			ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
			if (item.getType().isAir()) return arguments;

			ItemMeta meta = item.getItemMeta();
			if (meta == null) return arguments;

			List<String> lore = meta.getLore();
			if (lore == null) return arguments;

			for (int i = 1; i <= lore.size(); i++) {
				arguments.add(String.valueOf(i));
			}
		} else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
			arguments.add("<line>");
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
