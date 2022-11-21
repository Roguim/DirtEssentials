package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Lore implements CommandExecutor {
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

		if (args[0].equalsIgnoreCase("add")) {
			if (args.length == 1) {
				player.sendMessage(Strings.USAGE + "/lore add <line>");
				return true;
			}

			StringBuilder lore = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				lore.append(args[i]).append(" ");
			}

			item.getItemMeta().getLore().add(lore.toString());
			player.sendMessage(Strings.PREFIX + "Lore added!");
			return true;
		}

		if (args[0].equalsIgnoreCase("remove")) {
			if (args.length == 1) {
				player.sendMessage(Strings.USAGE + "/lore remove <line>");
				return true;
			}

			if (!args[1].matches("[0-9]+")) {
				player.sendMessage(Strings.PREFIX + "Line must be a number!");
				return true;
			}

			int line = Integer.parseInt(args[1]);
			if (line > item.getItemMeta().getLore().size()) {
				player.sendMessage(Strings.PREFIX + "Line must be less than or equal to the amount of lines in the lore!");
				return true;
			}

			item.getItemMeta().getLore().remove(line);
			player.sendMessage(Strings.PREFIX + "Lore removed!");
			return true;
		}

		if (args[0].equalsIgnoreCase("clear")) {
			item.getItemMeta().getLore().clear();
			player.sendMessage(Strings.PREFIX + "Lore cleared!");
			return true;
		}

		return true;
	}
}
