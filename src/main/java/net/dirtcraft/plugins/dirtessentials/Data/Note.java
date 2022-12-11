package net.dirtcraft.plugins.dirtessentials.Data;

import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.time.LocalDateTime;
import java.util.UUID;

public class Note {
	private final UUID uuid;
	private final String note;
	private final UUID addedBy;
	private final LocalDateTime addedOn;

	public Note(UUID uuid, String note, UUID addedBy, LocalDateTime addedOn) {
		this.uuid = uuid;
		this.note = note;
		this.addedBy = addedBy;
		this.addedOn = addedOn;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getNote() {
		return Utilities.translate(note, true);
	}

	public UUID getAddedBy() {
		return addedBy;
	}

	public LocalDateTime getAddedOn() {
		return addedOn;
	}
}
