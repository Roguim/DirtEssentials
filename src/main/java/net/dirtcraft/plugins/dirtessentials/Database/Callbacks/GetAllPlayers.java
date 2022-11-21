package net.dirtcraft.plugins.dirtessentials.Database.Callbacks;

import net.dirtcraft.plugins.dirtessentials.Data.PlayerData;

import java.util.Map;
import java.util.UUID;

public interface GetAllPlayers {
	void onSuccess(Map<UUID, PlayerData> playerData);
}
