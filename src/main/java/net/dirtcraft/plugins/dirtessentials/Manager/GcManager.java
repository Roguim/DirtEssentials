package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

public class GcManager {
	private static long startTime;
	private static float tps;
	private static BukkitTask tpsTask;
	private static long time;

	public static void init() {
		startTime = System.currentTimeMillis();
		startTpsCalculator();
	}

	private static void startTpsCalculator() {
		if (tpsTask != null) tpsTask.cancel();

		time = System.currentTimeMillis();
		tpsTask = Bukkit.getScheduler().runTaskTimer(DirtEssentials.getPlugin(), () -> {
			long now = System.currentTimeMillis();
			long diff = now - time;
			time = now;
			tps = 1000F / diff;
		}, 0, 1);
	}

	public static String getUptime() {
		long uptime = System.currentTimeMillis() - startTime;
		long hours = (uptime % 86400000) / 3600000;
		long minutes = (uptime % 3600000) / 60000;
		long seconds = (uptime % 60000) / 1000;

		StringBuilder builder = new StringBuilder();
		if (hours > 1) builder.append(hours).append(" hours, ");
		if (hours == 1) builder.append(hours).append(" hour, ");
		if (minutes > 1) builder.append(minutes).append(" minutes, ");
		if (minutes == 1) builder.append(minutes).append(" minute, ");
		if (seconds > 1) builder.append(seconds).append(" seconds");
		if (seconds == 1) builder.append(seconds).append(" second");

		return builder.toString();
	}

	public static float getMaxMemory() {
		return Runtime.getRuntime().maxMemory() / 1024F / 1024F;
	}

	public static float getUsedMemory() {
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024F / 1024F;
	}

	public static void stopTpsCalculator() {
		if (tpsTask != null) tpsTask.cancel();
	}

	public static float getTps() {
		return tps;
	}
}
