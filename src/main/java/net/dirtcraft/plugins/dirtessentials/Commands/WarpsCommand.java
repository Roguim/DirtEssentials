package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Data.Warp;
import net.dirtcraft.plugins.dirtessentials.Manager.WarpManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class WarpsCommand implements CommandExecutor, Listener {
	private static Inventory warpMenu;
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.WARPS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (WarpManager.getWarps().isEmpty()) {
			sender.sendMessage(Strings.PREFIX + "There are no warps to display.");
			return true;
		}

		Player player = (Player) sender;
		int guiSize;
		int warpSize = WarpManager.getWarps().size();
		if (warpSize > 9 && warpSize <= 18) {
			guiSize = 18;
		} else if (warpSize > 18 && warpSize <= 27) {
			guiSize = 27;
		} else if (warpSize > 27 && warpSize <= 36) {
			guiSize = 36;
		} else if (warpSize > 36 && warpSize <= 45) {
			guiSize = 45;
		} else if (warpSize > 45 && warpSize <= 54) {
			guiSize = 54;
		} else {
			guiSize = 9;
		}

		warpMenu = Bukkit.createInventory(null, guiSize, ChatColor.RED + "DirtCraft " + ChatColor.YELLOW + "Warps");

		for (int i = 0; i < WarpManager.getWarps().size(); i++) {
			warpMenu.setItem(i, WarpManager.getWarps().get(i).getIcon());
		}

		player.openInventory(warpMenu);
		return true;
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null) return;
		if (!event.getInventory().equals(warpMenu)) return;

		event.setCancelled(true);
		if (event.getClickedInventory().equals(event.getView().getTopInventory())) {
			if (event.getCurrentItem() == null) return;

			Warp warp = WarpManager.getWarps().get(event.getSlot());
			Player player = (Player) event.getWhoClicked();
			player.teleport(warp.getLocation());
			player.sendMessage(Strings.PREFIX + "You have teleported to warp " + ChatColor.YELLOW + warp.getName());
			player.closeInventory();
		}
	}
}
