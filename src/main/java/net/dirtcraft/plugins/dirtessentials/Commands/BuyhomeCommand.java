package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Manager.HomeManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BuyhomeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.BUYHOME)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;

		if (!Utilities.config.home.buyHomeEnabled) {
			player.sendMessage(Strings.PREFIX + ChatColor.RED + "Buying more homes is currently disabled.");
			return true;
		}

		switch (Utilities.config.home.buyHomeType.toLowerCase()) {
			case "item":
				Material material = Arrays.stream(Material.values()).filter(m -> m.getKey().equals(Utilities.config.home.getBuyHomeItem())).findFirst().orElse(null);
				if (material == null) {
					player.sendMessage(Strings.PREFIX + ChatColor.RED + "The item you are trying to buy homes with is invalid.");
					return true;
				}

				int amount = Utilities.config.home.getBuyHomeItemAmount();
				if (amount <= 0 || amount > 64) {
					player.sendMessage(Strings.PREFIX + ChatColor.RED + "The amount of the item you are trying to buy homes with is invalid.");
					return true;
				}

				ItemStack item = new ItemStack(material, amount);
				if (player.getInventory().containsAtLeast(item, amount)) {
					player.getInventory().removeItem(item);
					HomeManager.addHomeBalance(player.getUniqueId(), 1);
					player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "You have successfully bought a home. Your new home balance is " + ChatColor.GREEN + HomeManager.getHomeBalance(player.getUniqueId()));
				} else {
					player.sendMessage(Strings.PREFIX + ChatColor.RED + "You need " + ChatColor.AQUA + amount + "x " + ChatColor.GREEN + Utilities.config.home.getBuyHomeItem() + ChatColor.RED + " to buy a home.");
				}
				break;
			case "money":
				if (!DirtEssentials.getDirtEconomy().has(player, Utilities.config.home.buyHomeMoney)) {
					player.sendMessage(Strings.PREFIX + ChatColor.RED + "You do not have enough money to buy a home.");
					return true;
				}

				DirtEssentials.getDirtEconomy().withdrawPlayer(player, Utilities.config.home.buyHomeMoney);
				HomeManager.addHomeBalance(player.getUniqueId(), 1);
				player.sendMessage(Strings.PREFIX + ChatColor.GRAY + "You have successfully bought a home. Your new home balance is " + ChatColor.GREEN + HomeManager.getHomeBalance(player.getUniqueId()));
				break;
			default:
				player.sendMessage(Strings.PREFIX + ChatColor.RED + "The buyhome type is invalid. Please contact an administrator.");
				break;
		}

		return true;
	}
}
