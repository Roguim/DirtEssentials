package net.dirtcraft.plugins.dirtessentials.Commands;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.dirtcraft.plugins.dirtessentials.Config.Kit;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowkitCommand implements CommandExecutor, TabCompleter, Listener {
	private static Inventory inventory;
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage(Strings.USAGE + "/showkit <name>");
			return true;
		}

		if (!sender.hasPermission(Permissions.SHOWKIT + "." + args[0]) && !sender.hasPermission(Permissions.SHOWKIT + ".*")) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		Player player = (Player) sender;
		String kitName = args[0];
		Kit kit = Utilities.kits.kits.stream().filter(k -> k.name.equalsIgnoreCase(kitName)).findFirst().orElse(null);
		if (kit == null) {
			sender.sendMessage(Strings.PREFIX + "This kit does not exist!");
			return true;
		}

		int kitSize;
		int giveSize = (int) kit.commands.stream().filter(k -> k.contains("minecraft:give")).count();
		if (giveSize > 9 && giveSize <= 18) {
			kitSize = 18;
		} else if (giveSize > 18 && giveSize <= 27) {
			kitSize = 27;
		} else if (giveSize > 27 && giveSize <= 36) {
			kitSize = 36;
		} else if (giveSize > 36 && giveSize <= 45) {
			kitSize = 45;
		} else if (giveSize > 45 && giveSize <= 54) {
			kitSize = 54;
		} else {
			kitSize = 9;
		}

		inventory = Bukkit.createInventory(null, kitSize, ChatColor.BLUE + "Contents of kit " + ChatColor.YELLOW + kit.name + ChatColor.GRAY + ":");
		try {
			kit.commands.stream().filter(k -> k.contains("minecraft:give")).forEach(k -> {
				String[] split = k.split(" ");
				String itemString = split[2];
				String nbt = "{}";
				int amount = Integer.parseInt(split[split.length - 1]);

				if (itemString.contains("{")) {
					nbt = itemString.substring(itemString.indexOf("{")).replace("\\", "");
					itemString = itemString.substring(0, itemString.indexOf("{"));
				}

				NamespacedKey item = NamespacedKey.fromString(itemString);
				Material material = Arrays.stream(Material.values()).filter(m -> m.getKey().equals(item)).findFirst().orElse(null);
				if (material == null) return;

				NBTContainer nbtContainer = new NBTContainer(nbt);
				ItemStack itemStack = new ItemStack(material, amount);

				NBTItem nbtItem = new NBTItem(itemStack);
				nbtItem.mergeCompound(nbtContainer);

				ItemStack finalItemStack = nbtItem.getItem();

				inventory.addItem(finalItemStack);
			});
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "An error occurred while trying to get the items for this kit!");
			return true;
		}

		player.openInventory(inventory);
		return true;
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		if (!event.getInventory().equals(inventory)) return;
		if (event.getClickedInventory() == null) return;
		event.setCancelled(true);
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		List<String> kitNames = new ArrayList<>();
		Utilities.kits.kits.forEach(kit -> kitNames.add(kit.name));

		for (String kitName : kitNames) {
			if (sender.hasPermission(Permissions.SHOWKIT + "." + kitName) || sender.hasPermission(Permissions.SHOWKIT + ".*")) {
				arguments.add(kitName);
			}
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
