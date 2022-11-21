package net.dirtcraft.plugins.dirtessentials;

import net.dirtcraft.plugins.dirtessentials.Database.Database;
import net.dirtcraft.plugins.dirtessentials.Economy.DirtEconomy;
import net.dirtcraft.plugins.dirtessentials.Economy.VaultHook;
import net.dirtcraft.plugins.dirtessentials.Manager.HomeManager;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.plugin.java.JavaPlugin;

public final class DirtEssentials extends JavaPlugin {
	private static DirtEssentials plugin;
	private static DirtEconomy dirtEconomy;
	private static VaultHook vaultHook;

	@Override
	public void onEnable() {
		plugin = this;
		Utilities.loadConfig();
		Utilities.loadKits();
		Utilities.loadWorth();
		Database.initialiseDatabase();
		Utilities.registerCommands();
		Utilities.registerListeners();
		dirtEconomy = new DirtEconomy();
		vaultHook = new VaultHook();
		HomeManager.init();
		PlayerManager.init();
	}

	@Override
	public void onDisable() {
		Database.closeDatabase();
		vaultHook.unhook();
	}

	public static DirtEconomy getDirtEconomy() {
		return dirtEconomy;
	}

	public static DirtEssentials getPlugin() {
		return plugin;
	}
}
