package net.dirtcraft.plugins.dirtessentials.Manager;

import net.dirtcraft.plugins.dirtessentials.Data.Note;
import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.util.*;
import java.util.logging.Level;

public class NoteManager {
	private static final Map<UUID, List<Note>> notes = new HashMap<>();

	public static void init() {
		DatabaseOperations.initNotes(n -> {
			notes.putAll(n);
			Utilities.log(Level.INFO, "Loaded " + n.size() + " notes.");
		});
	}

	public static void addNote(UUID uuid, Note note) {
		notes.computeIfAbsent(uuid, k -> new ArrayList<>()).add(note);
		DatabaseOperations.addNote(uuid, note);
	}

	public static void removeNote(UUID uuid, Note note) {
		notes.computeIfAbsent(uuid, k -> new ArrayList<>()).remove(note);
		DatabaseOperations.removeNote(uuid, note);
	}

	public static List<Note> getNotes(UUID uuid) {
		return notes.getOrDefault(uuid, new ArrayList<>());
	}

	public static boolean hasNotes(UUID uuid) {
		return notes.containsKey(uuid);
	}

	public static Note getNoteByIndex(UUID uuid, int index) {
		return notes.getOrDefault(uuid, new ArrayList<>()).get(index);
	}
}
