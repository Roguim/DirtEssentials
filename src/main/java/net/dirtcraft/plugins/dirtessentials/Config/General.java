package net.dirtcraft.plugins.dirtessentials.Config;

import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.util.List;

public class General {
	public boolean debug;
	public String broadcastPrefix;
	public int maxPlayers;
	public boolean staffJoinFull;
	private String fullMessage;

	public String nicknamePrefix;
	public int afkTime;
	public boolean afkKick;
	public int afkKickTime;
	public String afkKickMessage;
	public List<String> backWorlds;
	public boolean backWorldsWhitelist;

	public String getFullMessage() {
		return Utilities.translate(this.fullMessage, false);
	}
}
