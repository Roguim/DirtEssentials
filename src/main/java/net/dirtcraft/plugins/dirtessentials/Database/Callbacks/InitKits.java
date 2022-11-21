package net.dirtcraft.plugins.dirtessentials.Database.Callbacks;

import net.dirtcraft.plugins.dirtessentials.Data.PlayerKitTracker;

import java.util.List;

public interface InitKits {
	void onSuccess(List<PlayerKitTracker> kits);
}
