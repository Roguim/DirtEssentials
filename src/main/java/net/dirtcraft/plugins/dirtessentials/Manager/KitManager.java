package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Config.Kit;
import net.dirtcraft.plugins.dirtessentials.Data.PlayerKitTracker;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class KitManager {
	private static final List<PlayerKitTracker> kitTracker = new ArrayList<>();

	public static void init() {
		DatabaseOperations.initKits(kitTracker::addAll);
	}

	public static String getTimeLeft(String kitName, UUID uniqueId) {
		PlayerKitTracker tracker = kitTracker.stream().filter(kit -> kit.getUuid().equals(uniqueId) && kit.getKitName().equalsIgnoreCase(kitName)).findFirst().orElse(null);
		if (tracker == null) return "Something went wrong! :( Please contact an admin!";

		LocalDateTime lastClaimed = tracker.getLastClaimed();
		LocalDateTime now = LocalDateTime.now();

		Kit kit = Utilities.kits.kits.stream().filter(k -> k.name.equalsIgnoreCase(kitName)).findFirst().orElse(null);
		if (kit == null) return "Kit not found";

		int cooldown = kit.cooldown;
		String cooldownType = kit.cooldownType;
		if (cooldown < 0) return "You can only claim this kit once!";

		long secondsLeft = 0;
		switch (cooldownType) {
			case "second":
				secondsLeft = now.until(lastClaimed.plusSeconds(cooldown), ChronoUnit.SECONDS);
				break;
			case "minute":
				secondsLeft = now.until(lastClaimed.plusMinutes(cooldown), ChronoUnit.SECONDS);
				break;
			case "hour":
				secondsLeft = now.until(lastClaimed.plusHours(cooldown), ChronoUnit.SECONDS);
				break;
			case "day":
				secondsLeft = now.until(lastClaimed.plusDays(cooldown), ChronoUnit.SECONDS);
				break;
		}

		int hours = (int) Math.floor(secondsLeft / 3600F);
		int minutes = (int) Math.floor((secondsLeft - (hours * 3600)) / 60F);
		int seconds = (int) Math.floor(secondsLeft % 60F);

		StringBuilder builder = new StringBuilder();
		if (hours == 1)
			builder.append(hours).append(" hour ");
		if (hours > 1)
			builder.append(hours).append(" hours ");
		if (minutes == 1)
			builder.append(minutes).append(" minute ");
		if (minutes > 1)
			builder.append(minutes).append(" minutes ");
		if (seconds == 1)
			builder.append(seconds).append(" second");
		if (seconds > 1)
			builder.append(seconds).append(" seconds");

		return builder.toString();
	}

	public static boolean isKitClaimable(String kitName, UUID uniqueId) {
		PlayerKitTracker tracker = kitTracker.stream().filter(kit -> kit.getUuid().equals(uniqueId) && kit.getKitName().equalsIgnoreCase(kitName)).findFirst().orElse(null);
		if (tracker == null) return true;

		LocalDateTime lastClaimed = tracker.getLastClaimed();
		LocalDateTime now = LocalDateTime.now();
		Kit kit = Utilities.kits.kits.stream().filter(k -> k.name.equalsIgnoreCase(kitName)).findFirst().orElse(null);
		if (kit == null) return false;

		int cooldown = kit.cooldown;

		if (cooldown < 0) return false;

		switch (kit.cooldownType) {
			case "second":
				return lastClaimed.plusSeconds(cooldown).isBefore(now);
			case "minute":
				return lastClaimed.plusMinutes(cooldown).isBefore(now);
			case "hour":
				return lastClaimed.plusHours(cooldown).isBefore(now);
			case "day":
				return lastClaimed.plusDays(cooldown).isBefore(now);
			default:
				return false;
		}
	}

	public static void setCooldown(String name, UUID uniqueId) {
		if (kitTracker.stream().noneMatch(kit -> kit.getUuid().equals(uniqueId) && kit.getKitName().equalsIgnoreCase(name))) {
			kitTracker.add(new PlayerKitTracker(uniqueId, name, LocalDateTime.now()));
		} else {
			kitTracker.stream().filter(kit -> kit.getUuid().equals(uniqueId) && kit.getKitName().equalsIgnoreCase(name)).findFirst().ifPresent(kit -> kit.setLastClaimed(LocalDateTime.now()));
		}
	}
}
