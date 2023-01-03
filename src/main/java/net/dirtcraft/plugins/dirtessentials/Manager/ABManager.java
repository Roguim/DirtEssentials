package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Config.Broadcast;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ABManager {
	private static BukkitTask abTask;
	private static final List<UUID> autobroadcastDisabled = new ArrayList<>();

	public static void init() {
		DatabaseOperations.initAB(autobroadcastDisabled::addAll);
		loadABManager();
	}

	public static void loadABManager() {
		if (abTask != null) abTask.cancel();

		startTimer();
	}

	public static boolean hasABDisabled(UUID uuid) {
		return autobroadcastDisabled.contains(uuid);
	}

	public static void toggleAutobroadcast(Player player, UUID uuid) {
		if (autobroadcastDisabled.contains(uuid)) {
			autobroadcastDisabled.remove(uuid);
			DatabaseOperations.removeAB(uuid);
			player.sendMessage(Strings.PREFIX + "You will now " + ChatColor.GREEN + "receive" + ChatColor.GRAY + " autobroadcasts!");
		} else {
			autobroadcastDisabled.add(uuid);
			DatabaseOperations.addAB(uuid);
			player.sendMessage(Strings.PREFIX + "You will " + ChatColor.RED + "no longer" + ChatColor.GRAY + " receive autobroadcasts!");
		}
	}

	private static void startTimer() {
		abTask = Bukkit.getScheduler().runTaskTimerAsynchronously(DirtEssentials.getPlugin(), () -> {
			if (Bukkit.getOnlinePlayers().size() == 0) return;

			List<Broadcast> autoBroadcasts = Utilities.autobroadcast.broadcast;
			int random = ThreadLocalRandom.current().nextInt(0, autoBroadcasts.size());

			Broadcast broadcast = autoBroadcasts.get(random);
			if (broadcast.message.size() == 0) return;

			ComponentBuilder builder = new ComponentBuilder();
			for (String line : broadcast.message) {
				builder.append(TextComponent.fromLegacyText(Utilities.translate(line, false))).append("\n");
			}

			if (!broadcast.clickEvent.equals("") && !broadcast.clickEventValue.equals("")) {
				try {
					builder.event(new ClickEvent(ClickEvent.Action.valueOf(broadcast.clickEvent), broadcast.clickEventValue));
				} catch (IllegalArgumentException e) {
					DirtEssentials.getPlugin().getLogger().warning("Invalid click event type: " + broadcast.clickEvent);
				}
			}

			if (!broadcast.hoverEventText.equals("")) {
				builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Utilities.translate(broadcast.hoverEventText, false))));
			}

			Bukkit.getOnlinePlayers().forEach(player -> {
				if (autobroadcastDisabled.contains(player.getUniqueId())) return;

				player.spigot().sendMessage(builder.create());
			});
		}, 0, 20L * Utilities.autobroadcast.delay.getDelay());
	}
}
