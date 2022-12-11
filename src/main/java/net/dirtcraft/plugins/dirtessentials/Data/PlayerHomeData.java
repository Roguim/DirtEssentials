package net.dirtcraft.plugins.dirtessentials.Data;

import java.util.List;
import java.util.UUID;

public class PlayerHomeData {
	private final UUID uuid;
	private int homesAvailable;
	private final List<Home> homes;

	public PlayerHomeData(UUID uuid, int homesAvailable, List<Home> homes) {
		this.uuid = uuid;
		this.homesAvailable = homesAvailable;
		this.homes = homes;
	}

	public UUID getUuid() {
		return uuid;
	}

	public List<Home> getHomes() {
		return homes;
	}

	public int getHomesAvailable() {
		return homesAvailable;
	}

	public void setHomesAvailable(int homesAvailable) {
		this.homesAvailable = homesAvailable;
	}

	public void addHome(Home home) {
		homes.add(home);
	}
}
