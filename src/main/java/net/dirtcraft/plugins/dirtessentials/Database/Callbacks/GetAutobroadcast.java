package net.dirtcraft.plugins.dirtessentials.Database.Callbacks;

import java.util.List;
import java.util.UUID;

public interface GetAutobroadcast {
	void onSuccess(List<UUID> disabledUsers);
}
