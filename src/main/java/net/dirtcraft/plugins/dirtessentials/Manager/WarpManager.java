package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Data.Warp;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class WarpManager {
	private static final List<Warp> warps = new ArrayList<>();

	public static void init() {
		DatabaseOperations.initWarps(warpList -> {
			warps.addAll(warpList);
			Utilities.log(Level.INFO, "Loaded " + warps.size() + " warps.");
		});
	}

	public static void addWarp(Warp warp) {
		warps.add(warp);
	}

	public static void removeWarp(String name) {
		warps.removeIf(warp -> warp.getName().equalsIgnoreCase(name));
	}

	public static Warp getWarp(String name) {
		for (Warp warp : warps) {
			if (warp.getName().equalsIgnoreCase(name)) return warp;
		}

		return null;
	}

	public static List<Warp> getWarps() {
		return warps;
	}
}
