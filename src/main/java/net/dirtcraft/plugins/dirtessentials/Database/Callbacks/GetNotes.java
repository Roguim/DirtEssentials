package net.dirtcraft.plugins.dirtessentials.Database.Callbacks;

import net.dirtcraft.plugins.dirtessentials.Data.Note;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GetNotes {
	void onSuccess(Map<UUID, List<Note>> notes);
}
