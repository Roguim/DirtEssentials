package net.dirtcraft.plugins.dirtessentials.Listener;

import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class MagmaFixListener implements Listener {
	@EventHandler (priority = EventPriority.HIGH)
	public void onWorldChange(PlayerChangedWorldEvent event) {
		event.getPlayer().setDisplayName(PlayerManager.getPlayerData(event.getPlayer().getUniqueId()).getDisplayName());
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void onDeath(PlayerDeathEvent event) {
		event.getEntity().setDisplayName(PlayerManager.getPlayerData(event.getEntity().getUniqueId()).getDisplayName());
	}
}
