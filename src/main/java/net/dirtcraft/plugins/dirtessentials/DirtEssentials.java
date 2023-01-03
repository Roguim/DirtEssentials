package net.dirtcraft.plugins.dirtessentials;

import net.dirtcraft.plugins.dirtessentials.Database.Database;
import net.dirtcraft.plugins.dirtessentials.Economy.DirtEconomy;
import net.dirtcraft.plugins.dirtessentials.Economy.VaultHook;
import net.dirtcraft.plugins.dirtessentials.Manager.*;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class DirtEssentials extends JavaPlugin {
	private static DirtEssentials plugin;
	private static DirtEconomy dirtEconomy;
	private static VaultHook vaultHook;
	private static Chat chat;

	@Override
	public void onEnable() {
		plugin = this;
		Utilities.loadConfig();
		Utilities.loadKits();
		Utilities.loadHelp();
		Utilities.loadCjm();
		Utilities.loadAB();
		Database.initialiseDatabase();
		Utilities.registerCommands();
		Utilities.registerListeners();
		dirtEconomy = new DirtEconomy();
		vaultHook = new VaultHook();
		HomeManager.init();
		PlayerManager.init();
		WarpManager.init();
		AfkManager.loadAfkCheck();
		SpawnManager.init();
		KitManager.init();
		ABManager.init();
		NoteManager.init();
		GcManager.init();

		RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
	}

	@Override
	public void onDisable() {
		Database.closeDatabase();
		GcManager.stopTpsCalculator();
		vaultHook.unhook();
	}

	public static DirtEconomy getDirtEconomy() {
		return dirtEconomy;
	}

	public static DirtEssentials getPlugin() {
		return plugin;
	}

	public static Chat getChat() {
		return chat;
	}
}
