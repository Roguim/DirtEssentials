package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Config.Broadcast;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListautobroadcastsCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.LISTAUTOBROADCASTS)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (Utilities.autobroadcast.broadcast.isEmpty()) {
			sender.sendMessage(Strings.PREFIX + "There are no autobroadcasts yet!");
			return true;
		}

		sender.sendMessage(Strings.PREFIX + "Autobroadcasts:");
		sender.sendMessage("");

		for (Broadcast broadcast : Utilities.autobroadcast.broadcast) {
			if (broadcast.message.size() == 0)
				continue;

			ComponentBuilder builder = new ComponentBuilder();

			for (String line : broadcast.message) {
				builder.append(TextComponent.fromLegacyText(Utilities.translate(line, false))).append("\n");
			}

			ComponentBuilder message = new ComponentBuilder();
			message.append(builder.create());

			if (!broadcast.clickEvent.equals("") && !broadcast.clickEventValue.equals("")) {
				try {
					message.event(new ClickEvent(ClickEvent.Action.valueOf(broadcast.clickEvent), broadcast.clickEventValue));
				} catch (IllegalArgumentException e) {
					DirtEssentials.getPlugin().getLogger().warning("Invalid click event type: " + broadcast.clickEvent);
					continue;
				}
			}

			if (!broadcast.hoverEventText.equals("")) {
				message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Utilities.translate(broadcast.hoverEventText, false))));
			}

			((Player) sender).spigot().sendMessage(builder.create());
		}

		return false;
	}
}
