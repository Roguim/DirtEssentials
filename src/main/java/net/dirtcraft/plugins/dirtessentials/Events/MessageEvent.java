package net.dirtcraft.plugins.dirtessentials.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MessageEvent extends Event {
	private static final HandlerList handlerList = new HandlerList();
	private final Player sender;
	private final Player target;
	private final String message;

	public MessageEvent(Player sender, Player target, String message) {
		this.sender = sender;
		this.target = target;
		this.message = message;
	}

	@NotNull
	public static HandlerList getHandlerList() {
		return handlerList;
	}

	public Player getSender() {
		return sender;
	}

	public Player getTarget() {
		return target;
	}

	public String getMessage() {
		return message;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
