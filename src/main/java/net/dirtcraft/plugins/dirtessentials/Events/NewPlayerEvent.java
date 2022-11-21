package net.dirtcraft.plugins.dirtessentials.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NewPlayerEvent extends Event {
	private static final HandlerList handlerList = new HandlerList();
	private final String name;
	private final UUID uniqueId;

	public NewPlayerEvent(String name, UUID uniqueId) {
		this.name = name;
		this.uniqueId = uniqueId;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public String getName() {
		return name;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}
}
