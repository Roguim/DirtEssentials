package net.dirtcraft.plugins.dirtessentials.Data;

import java.util.UUID;

public class TeleportData {
	private final UUID uuid;
	private UUID lastTpaRequest;
	private UUID lastTpahereRequest;

	public TeleportData(UUID uuid) {this.uuid = uuid;}

	public UUID getUuid() {
		return uuid;
	}

	public UUID getLastTpaRequest() {
		return lastTpaRequest;
	}

	public void setLastTpaRequest(UUID lastTpaRequest) {
		this.lastTpaRequest = lastTpaRequest;
	}

	public UUID getLastTpahereRequest() {
		return lastTpahereRequest;
	}

	public void setLastTpahereRequest(UUID lastTpahereRequest) {
		this.lastTpahereRequest = lastTpahereRequest;
	}
}
