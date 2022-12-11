package net.dirtcraft.plugins.dirtessentials.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerKitTracker {
	private final UUID uuid;
	private final String kitName;
	private LocalDateTime lastClaimed;

	public PlayerKitTracker(UUID uuid, String kitName, LocalDateTime lastClaimed) {
		this.uuid = uuid;
		this.kitName = kitName;
		this.lastClaimed = lastClaimed;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getKitName() {
		return kitName;
	}

	public LocalDateTime getLastClaimed() {
		return lastClaimed;
	}

	public void setLastClaimed(LocalDateTime lastClaimed) {
		this.lastClaimed = lastClaimed;
	}
}
