package net.dirtcraft.plugins.dirtessentials.Economy;

import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.logging.Level;

public class VaultHook {
	private Economy provider;

	public VaultHook() {
		hook();
	}

	public void hook() {
		provider = DirtEssentials.getDirtEconomy();
		Bukkit.getServicesManager().register(Economy.class, provider, DirtEssentials.getPlugin(), ServicePriority.Highest);
		Utilities.log(Level.INFO, "Vault hooked into DirtEssentials!");
	}

	public void unhook() {
		Bukkit.getServicesManager().unregister(Economy.class, provider);
		Utilities.log(Level.INFO, "Vault unhooked from DirtEssentials!");
	}
}
