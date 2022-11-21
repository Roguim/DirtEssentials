package net.dirtcraft.plugins.dirtessentials.Database.Callbacks;

import java.util.Map;
import java.util.UUID;

public interface GetNames {
	void onSuccess(Map<UUID, String> map);
}
