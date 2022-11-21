package net.dirtcraft.plugins.dirtessentials.Config;

import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

public class General {
	public boolean debug;
	public String broadcastPrefix;
	public int maxPlayers;
	public boolean staffJoinFull;
	private String fullMessage;

	public String getFullMessage() {
		return Utilities.translate(this.fullMessage, false);
	}
}
