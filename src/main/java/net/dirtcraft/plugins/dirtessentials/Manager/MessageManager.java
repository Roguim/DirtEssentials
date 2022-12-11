package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Events.MessageEvent;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class MessageManager implements Listener {
	private static final Map<UUID, UUID> lastMsg = new HashMap<>();
	private static final Set<UUID> socialSpy = new HashSet<>();
	private static final Set<UUID> msgDisabled = new HashSet<>();

	public static void disableMsgToggle(UUID uuid) {
		msgDisabled.remove(uuid);
	}

	public static void enableMsgToggle(UUID uuid) {
		msgDisabled.add(uuid);
	}

	public static boolean canReceiveMsg(UUID uuid) {
		return !msgDisabled.contains(uuid);
	}

	public static void setLastMsg(UUID sender, UUID target) {
		lastMsg.put(sender, target);
	}

	public static UUID getLastMsg(UUID sender) {
		return lastMsg.get(sender);
	}

	public static void activateSocialSpy(UUID player) {
		socialSpy.add(player);
	}

	public static void deactivateSocialSpy(UUID player) {
		socialSpy.remove(player);
	}

	public static boolean isSocialSpyActive(UUID player) {
		return socialSpy.contains(player);
	}

	@EventHandler
	private static void onMessage(MessageEvent event) {
		Player sender = event.getSender();
		Player target = event.getTarget();
		String message = event.getMessage();

		ComponentBuilder senderComponent = new ComponentBuilder()
				.append(TextComponent.fromLegacyText(Utilities.translate(sender.getDisplayName(), false)))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Name" + ChatColor.GRAY + ": " + ChatColor.AQUA + sender.getName())));

		ComponentBuilder targetComponent = new ComponentBuilder()
				.append(TextComponent.fromLegacyText(Utilities.translate(target.getDisplayName(), false)))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Name" + ChatColor.GRAY + ": " + ChatColor.AQUA + target.getName())));

		for (UUID playerUuid : socialSpy) {
			Player player = Bukkit.getPlayer(playerUuid);
			if (player == null) continue;
			if (playerUuid.equals(sender.getUniqueId()) || playerUuid.equals(target.getUniqueId())) continue;

			player.spigot().sendMessage(new ComponentBuilder()
					.append(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "SocialSpy" + ChatColor.GRAY + "] [")
					.append(senderComponent.create())
					.append(ChatColor.GRAY + " \u25ba ").event((HoverEvent) null)
					.append(targetComponent.create())
					.append(ChatColor.GRAY + "] ").event((HoverEvent) null)
					.append(TextComponent.fromLegacyText(Utilities.translate(message, false)))
					.create());
		}
	}

	public static void message(Player sender, Player target, String message) {
		ComponentBuilder senderComponent = new ComponentBuilder()
				.append(TextComponent.fromLegacyText(Utilities.translate(sender.getDisplayName(), false)))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Name" + ChatColor.GRAY + ": " + ChatColor.AQUA + sender.getName())));

		ComponentBuilder targetComponent = new ComponentBuilder()
				.append(TextComponent.fromLegacyText(Utilities.translate(target.getDisplayName(), false)))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GOLD + "Name" + ChatColor.GRAY + ": " + ChatColor.AQUA + target.getName())));

		BaseComponent[] senderMessage = new ComponentBuilder()
				.append(ChatColor.GRAY + "[").event((HoverEvent) null)
				.append(ChatColor.GOLD + "Me").event((HoverEvent) null)
				.append(ChatColor.GRAY + " \u25ba ").event((HoverEvent) null)
				.append(targetComponent.create())
				.append(ChatColor.GRAY + "] ").event((HoverEvent) null)
				.append(TextComponent.fromLegacyText(Utilities.translate(message, false))).create();

		BaseComponent[] targetMessage = new ComponentBuilder()
				.append(ChatColor.GRAY + "[").event((HoverEvent) null)
				.append(senderComponent.create())
				.append(ChatColor.GRAY + " \u25ba ").event((HoverEvent) null)
				.append(ChatColor.GOLD + "Me").event((HoverEvent) null)
				.append(ChatColor.GRAY + "] ").event((HoverEvent) null)
				.append(TextComponent.fromLegacyText(Utilities.translate(message, false))).create();

		sender.spigot().sendMessage(senderMessage);
		target.spigot().sendMessage(targetMessage);

		MessageEvent messageEvent = new MessageEvent(sender, target, message);
		Bukkit.getPluginManager().callEvent(messageEvent);
	}
}
