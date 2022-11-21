package net.dirtcraft.plugins.dirtessentials.Data;

import org.bukkit.Location;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerData {
	private UUID uuid;
	private String username;
	private String displayName;
	private String lastIpAddress;
	private boolean isStaff;
	private LocalDateTime leaveDate;
	private Location lastLocation;

	public PlayerData(UUID uuid, String username, String displayName, String lastIpAddress, boolean isStaff, LocalDateTime leaveDate, Location lastLocation) {
		this.uuid = uuid;
		this.username = username;
		this.displayName = displayName;
		this.lastIpAddress = lastIpAddress;
		this.isStaff = isStaff;
		this.leaveDate = leaveDate;
		this.lastLocation = lastLocation;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastIpAddress() {
		return lastIpAddress;
	}

	public void setLastIpAddress(String lastIpAddress) {
		this.lastIpAddress = lastIpAddress;
	}

	public LocalDateTime getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(LocalDateTime leaveDate) {
		this.leaveDate = leaveDate;
	}

	public boolean isStaff() {
		return isStaff;
	}

	public void setStaff(boolean staff) {
		isStaff = staff;
	}

	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
