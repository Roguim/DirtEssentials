package net.dirtcraft.plugins.dirtessentials.Commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.dirtcraft.plugins.dirtessentials.Data.Item;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WorthCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.WORTH)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (Utilities.config.worth == null || Utilities.worthData == null) {
			sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "Worth data is not initialized!");
			return true;
		}

		if (!(sender instanceof Player) && args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/worth <item>");
			return true;
		}

		if (args.length == 0) {
			Player player = (Player) sender;
			ItemStack hand = player.getInventory().getItemInMainHand();

			if (hand.getType().isAir()) {
				sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "You must be holding an item to use this command!");
				return true;
			}

			NamespacedKey key = hand.getType().getKey();
			NBTItem itemData = new NBTItem(hand);
			String nbt = itemData.toString();

			prepareMessage(sender, key, nbt, hand.getAmount());
		} else {
			NamespacedKey key = NamespacedKey.fromString(args[0]);
			if (key == null) {
				sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Invalid item!");
				return true;
			}

			prepareMessage(sender, key, "{}", 1);
		}

		return true;
	}

	private void prepareMessage(CommandSender sender, NamespacedKey key, String nbt, int amount) {
		if (Utilities.worthData.getMods().stream().noneMatch(i -> i.getNamespace().equals(key.getNamespace()))) {
			sender.sendMessage(Strings.PREFIX + ChatColor.RED + "This item does not have a worth!");
			return;
		}

		Utilities.worthData.getMods().stream().filter(i -> i.getNamespace().equals(key.getNamespace())).findFirst().ifPresent(mod -> {
			if (mod.getItems().stream().anyMatch(i -> i.getId().equals(key) && i.getNbt().replace("\\", "").equals(nbt))) {
				mod.getItems().stream().filter(i -> i.getId().equals(key) && i.getNbt().replace("\\", "").equals(nbt)).findFirst().ifPresent(item -> {
					double price = item.getPrice() * amount;
					sendMessage(sender, item, nbt, price, amount);
				});
			} else if (mod.getItems().stream().anyMatch(i -> i.getId().equals(key))) {
				mod.getItems().stream().filter(i -> i.getId().equals(key)).findFirst().ifPresent(item -> {
					double price = item.getPrice() * amount;
					sendMessage(sender, item, nbt, price, amount);
				});
			} else {
				sender.sendMessage(Strings.PREFIX + ChatColor.RED + "This item does not have a worth!");
			}
		});
	}

	private void sendMessage(CommandSender sender, Item item, String nbt, double price, int amount) {
		BaseComponent[] itemComponent = new ComponentBuilder()
				.append(ChatColor.GREEN + (item.getDisplayName().equals("") ? item.getId().toString() : item.getDisplayName()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + nbt))).create();

		ComponentBuilder message = new ComponentBuilder();

		message.append(Strings.PREFIX + ChatColor.AQUA + amount + "x ").event((ClickEvent) null).event((HoverEvent) null);
		message.append(itemComponent);
		message.append(amount > 1 ? ChatColor.GRAY + " are worth " : ChatColor.GRAY + " is worth ").event((ClickEvent) null).event((HoverEvent) null);
		message.append(ChatColor.GOLD + String.valueOf(price) + Utilities.config.economy.currencySymbol + ChatColor.GRAY + "!").event((ClickEvent) null).event((HoverEvent) null);

		sender.spigot().sendMessage(message.create());
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.WORTH)) return arguments;

		if (args.length == 1) {
			Utilities.worthData.getMods().forEach(mod -> mod.getItems().forEach(item -> arguments.add(item.getId().toString())));
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
