package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Config.Kit;
import net.dirtcraft.plugins.dirtessentials.Manager.KitManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitsCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.KITS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (Utilities.kits.kits.isEmpty()) {
			sender.sendMessage(Strings.PREFIX + "There are no kits!");
			return true;
		}

		sender.sendMessage("");
		sender.sendMessage(Strings.KITS_BAR_TOP);
		sender.sendMessage("");
		boolean kitFound = false;
		for (Kit kit : Utilities.kits.kits) {
			BaseComponent[] showKitComponent = new ComponentBuilder()
					.append(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + "\u2139" + ChatColor.GRAY + "]")
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Click to show the kits content!")))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/showkit " + kit.name)).create();

			ComponentBuilder kitComponent;
			int cooldown = kit.cooldown;
			String cooldownType = kit.cooldownType;
			String cooldownString = cooldown < 0 ? "One-Time" : cooldown + " " + cooldownType + (cooldown == 1 ? "" : "s");
			if (KitManager.isKitClaimable(kit.name, ((Player) sender).getUniqueId())) {
				kitComponent = new ComponentBuilder()
						.append(ChatColor.GOLD + kit.name + ChatColor.BLUE + " - " + ChatColor.AQUA + "Click to claim!")
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GREEN + "Click to claim this kit!\n" + ChatColor.BLUE + "\u231A " + ChatColor.AQUA + cooldownString)))
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + kit.name));
			} else {
				String timeLeft = KitManager.getTimeLeft(kit.name, ((Player) sender).getUniqueId());
				kitComponent = new ComponentBuilder()
						.append(ChatColor.RED + kit.name + ChatColor.BLUE + " - " + ChatColor.DARK_AQUA + timeLeft)
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(cooldown < 0 ? ChatColor.RED + "You can only claim this kit once!" : ChatColor.GRAY + "You have to wait before claiming this kit again!\n" + ChatColor.BLUE + "\u231A " + ChatColor.AQUA + timeLeft)))
						.event((ClickEvent) null);
			}

			if (sender.hasPermission(Permissions.KIT + "." + kit.name) || sender.hasPermission(Permissions.KIT + ".*")) {
				ComponentBuilder message = new ComponentBuilder()
						.append("  ")
						.event((HoverEvent) null)
						.event((ClickEvent) null);

				if (sender.hasPermission(Permissions.SHOWKIT + "." + kit.name)) {
					message.append(showKitComponent);
					message.append(" ").event((HoverEvent) null).event((ClickEvent) null);
				}

				message.append(kitComponent.create());
				sender.spigot().sendMessage(message.create());
				kitFound = true;
			}
		}

		if (!kitFound) {
			sender.sendMessage(ChatColor.GRAY + "    There are no kits available for you!");
		}

		sender.sendMessage("");
		sender.sendMessage(Strings.KITS_BAR_BOTTOM);
		sender.sendMessage("");

		return true;
	}
}
