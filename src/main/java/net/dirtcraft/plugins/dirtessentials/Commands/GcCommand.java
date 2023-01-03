package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.GcManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class GcCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.GC)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		int bars = 20;
		float used = GcManager.getUsedMemory();
		float max = GcManager.getMaxMemory();
		float percent = used / max;
		int usedBars = (int) (percent * bars);
		int freeBars = bars - usedBars;
		String usedBar = String.join("", Collections.nCopies(usedBars, ChatColor.YELLOW + "|"));
		String freeBar = String.join("", Collections.nCopies(freeBars, ChatColor.GOLD + "|"));

		String tps;
		if (GcManager.getTps() >= 18) tps = ChatColor.GREEN + String.format("%.1f", GcManager.getTps());
		else if (GcManager.getTps() >= 14) tps = ChatColor.YELLOW + String.format("%.1f", GcManager.getTps());
		else tps = ChatColor.DARK_RED + String.format("%.1f", GcManager.getTps());

		int charSpacer = 15;

		sender.sendMessage(Strings.GC_BAR_TOP);
		sender.sendMessage(ChatColor.RED + "Uptime" + ChatColor.GRAY + ": " + String.join("", Collections.nCopies(charSpacer - 7, "")) + ChatColor.YELLOW + GcManager.getUptime());
		sender.sendMessage(ChatColor.RED + "Memory" + ChatColor.GRAY + ": " + String.join("", Collections.nCopies(charSpacer - 7, "")) + usedBar + freeBar + ChatColor.GRAY + " " + ChatColor.YELLOW + ((int) used) + "MB" + ChatColor.GRAY + " | " + ChatColor.GOLD + ((int) max) + "MB");
		sender.sendMessage(ChatColor.RED + "TPS" + ChatColor.GRAY + ": " + tps);
		sender.sendMessage("");
		if (Bukkit.getWorlds().size() > 20 && (sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Too many worlds to display more info! Use the bot/console for more info.");
			return true;
		}
		for (World world : Bukkit.getWorlds()) {
			sender.sendMessage(
					ChatColor.RED + world.getName() + ChatColor.GRAY + ": " +
							ChatColor.GOLD + world.getLoadedChunks().length + ChatColor.GRAY + " chunks | " +
							ChatColor.GOLD + world.getEntities().size() + ChatColor.GRAY + " entities | " +
							ChatColor.GOLD + world.getPluginChunkTickets().size() + ChatColor.GRAY + " tickets"
			);
		}
		sender.sendMessage(Strings.GC_BAR_BOTTOM);

		return true;
	}
}
