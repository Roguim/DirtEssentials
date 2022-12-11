package net.dirtcraft.plugins.dirtessentials.Commands;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NBTCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		if (!sender.hasPermission(Permissions.NBT)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return false;
		}

		Player player = (Player) sender;
		ItemStack hand = player.getInventory().getItemInMainHand();

		NBTCompound itemData = NBTItem.convertItemtoNBT(hand);
		String item = itemData.toString();

		player.sendMessage(Strings.PREFIX + ChatColor.GRAY + item);
		return true;
	}
}
