package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemnameCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.ITEMNAME)){
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;
		final ItemStack item = player.getInventory().getItemInMainHand();
		if (item.getType().isAir()) {
			player.sendMessage(Strings.PREFIX + "You must be holding an item to use this command!");
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(Strings.USAGE + "/itemname <name>");
			return true;
		}

		String name = String.join(" ", args);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utilities.translate(name, false));
		item.setItemMeta(meta);

		player.sendMessage(Strings.PREFIX + "Your item's name has been set to " + Utilities.translate(name, false) + ChatColor.GRAY + "!");
		return true;
	}
}
