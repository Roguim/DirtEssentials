package net.dirtcraft.plugins.dirtessentials.Commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Repair implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		if (!sender.hasPermission(Permissions.REPAIR)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return false;
		}

		Player player = (Player) sender;
		ItemStack hand = player.getInventory().getItemInMainHand();

		if (hand.getType() == Material.AIR) {
			player.sendMessage(Strings.PREFIX + "You must be holding an item to repair it!");
			return true;
		}

		NBTItem nbtItem = new NBTItem(hand);
		if (!nbtItem.hasKey("Damage")) {
			player.sendMessage(Strings.PREFIX + "This item cannot be repaired!");
			return true;
		}

		if (nbtItem.getDouble("Damage") == 0) {
			player.sendMessage(Strings.PREFIX + "This item is already fully repaired!");
			return true;
		}

		nbtItem.setDouble("Damage", 0.0);
		player.getInventory().setItemInMainHand(nbtItem.getItem());
		player.sendMessage(Strings.PREFIX + ChatColor.GREEN + "Your item has been repaired!");

		return true;
	}
}
