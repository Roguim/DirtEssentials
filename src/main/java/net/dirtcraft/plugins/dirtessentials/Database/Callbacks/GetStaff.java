package net.dirtcraft.plugins.dirtessentials.Database.Callbacks;

import java.util.Set;
import java.util.UUID;

public interface GetStaff {
	void onSuccess(Set<UUID> staff);
}
