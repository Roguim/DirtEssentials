package net.dirtcraft.plugins.dirtessentials.Database.Callbacks;

import java.util.Map;
import java.util.UUID;

public interface GetBalance {
	void onSuccess(Map<UUID, Double> balances);
}
