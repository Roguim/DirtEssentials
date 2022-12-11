package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CjmManager {
	public static void joinMessage(PlayerJoinEvent event, boolean isStaff, boolean isFirst) {
		if (isFirst) {
			List<String> firstJoinMessages = Utilities.cjm.join.firstJoin;
			int size = firstJoinMessages.size();
			int random = ThreadLocalRandom.current().nextInt(0, size);

			String message = Utilities.translate(firstJoinMessages.get(random).replace("{PLAYER}", event.getPlayer().getName()), false);
			event.setJoinMessage(message);
		} else if (isStaff) {
			List<String> staffJoinMessages = Utilities.cjm.join.staff;
			int size = staffJoinMessages.size();
			int random = ThreadLocalRandom.current().nextInt(0, size);

			String message = Utilities.translate(staffJoinMessages.get(random).replace("{PLAYER}", event.getPlayer().getName()), false);
			event.setJoinMessage(message);
		} else {
			List<String> normalJoinMessages = Utilities.cjm.join.normal;
			int size = normalJoinMessages.size();
			int random = ThreadLocalRandom.current().nextInt(0, size);

			String message = Utilities.translate(normalJoinMessages.get(random).replace("{PLAYER}", event.getPlayer().getName()), false);
			event.setJoinMessage(message);
		}
	}

	public static void leaveMessage(PlayerQuitEvent event, boolean isStaff) {
		if (isStaff) {
			List<String> leaveMessages = Utilities.cjm.leave.staff;
			int size = leaveMessages.size();
			int random = ThreadLocalRandom.current().nextInt(0, size);

			String message = Utilities.translate(leaveMessages.get(random).replace("{PLAYER}", event.getPlayer().getName()), false);
			event.setQuitMessage(message);
			return;
		}

		List<String> leaveMessages = Utilities.cjm.leave.normal;
		int size = leaveMessages.size();
		int random = ThreadLocalRandom.current().nextInt(0, size);

		String message = Utilities.translate(leaveMessages.get(random).replace("{PLAYER}", event.getPlayer().getName()), false);
		event.setQuitMessage(message);
	}
}
