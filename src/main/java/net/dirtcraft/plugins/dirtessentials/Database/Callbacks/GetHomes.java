package net.dirtcraft.plugins.dirtessentials.Database.Callbacks;

import net.dirtcraft.plugins.dirtessentials.Data.Home;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GetHomes {
	void onSuccess(Map<UUID, List<Home>> homeMap);
}
