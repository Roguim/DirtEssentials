package net.dirtcraft.plugins.dirtessentials.Database;

import net.dirtcraft.plugins.dirtessentials.Data.Home;
import net.dirtcraft.plugins.dirtessentials.Data.PlayerData;
import net.dirtcraft.plugins.dirtessentials.Data.PlayerKitTracker;
import net.dirtcraft.plugins.dirtessentials.Database.Callbacks.*;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DatabaseOperations {
	public static void initHomes(final GetHomes getHomesCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM homes")) {
				ResultSet resultSet = statement.executeQuery();

				Map<UUID, List<Home>> map = new HashMap<>();
				while (resultSet.next()) {
					UUID uuid = UUID.fromString(resultSet.getString("uuid"));
					String name = resultSet.getString("home");
					String world = resultSet.getString("world");
					double x = resultSet.getDouble("x");
					double y = resultSet.getDouble("y");
					double z = resultSet.getDouble("z");
					float yaw = resultSet.getFloat("yaw");
					float pitch = resultSet.getFloat("pitch");

					Home home = new Home(name, world, x, y, z, yaw, pitch);
					if (map.containsKey(uuid)) {
						map.get(uuid).add(home);
					} else {
						List<Home> list = new ArrayList<>();
						list.add(home);
						map.put(uuid, list);
					}
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> getHomesCallback.onSuccess(map));
			} catch (SQLException ignored) { }
		});
	}

	public static void addHome(final UUID uniqueId, final String name, final Location location, final AddHome addHomeCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("INSERT INTO homes VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
				statement.setString(1, uniqueId.toString());
				statement.setString(2, name);
				statement.setString(3, location.getWorld().getName());
				statement.setDouble(4, location.getX());
				statement.setDouble(5, location.getY());
				statement.setDouble(6, location.getZ());
				statement.setFloat(7, location.getYaw());
				statement.setFloat(8, location.getPitch());
				statement.executeUpdate();

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), addHomeCallback::onSuccess);
			} catch (SQLException ignored) { }
		});
	}

	public static void removeHome(final UUID uniqueId, final String name) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("DELETE FROM homes WHERE uuid = ? AND home = ?")) {
				statement.setString(1, uniqueId.toString());
				statement.setString(2, name);
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void initPlayers(final GetAllPlayers getAllPlayersCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM players")) {
				ResultSet resultSet = statement.executeQuery();

				Map<UUID, PlayerData> map = new HashMap<>();
				while (resultSet.next()) {
					PlayerData playerData = new PlayerData(
							UUID.fromString(resultSet.getString("uuid")),
							resultSet.getString("username"),
							resultSet.getString("nickname"),
							resultSet.getString("lastIpAddress"),
							resultSet.getBoolean("isStaff"),
							LocalDateTime.parse(resultSet.getString("leaveDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
							new Location(
									Bukkit.getWorld(resultSet.getString("locWorld")),
									resultSet.getDouble("locX"),
									resultSet.getDouble("locY"),
									resultSet.getDouble("locZ"),
									resultSet.getFloat("locYaw"),
									resultSet.getFloat("locPitch")
							)
					);
					map.put(playerData.getUuid(), playerData);
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> getAllPlayersCallback.onSuccess(map));
			} catch (SQLException ignored) { }
		});
	}

	public static void updatePlayer(final UUID uniqueId, final String name, final String nickname, final String address, final boolean isStaff, final LocalDateTime leaveDate, final Location location) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement insertStatement = connection.prepareStatement("MERGE INTO players KEY (uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

				insertStatement.setString(1, uniqueId.toString());
				insertStatement.setString(2, name);
				insertStatement.setString(3, nickname);
				insertStatement.setString(4, address);
				insertStatement.setBoolean(5, isStaff);
				insertStatement.setString(6, leaveDate != null ? leaveDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
				insertStatement.setString(7, location != null ? location.getWorld().getName() : null);
				insertStatement.setDouble(8, location != null ? location.getX() : 0);
				insertStatement.setDouble(9, location != null ? location.getY() : 0);
				insertStatement.setDouble(10, location != null ? location.getZ() : 0);
				insertStatement.setFloat(11, location != null ? location.getYaw() : 0);
				insertStatement.setFloat(12, location != null ? location.getPitch() : 0);
				insertStatement.executeUpdate();

			} catch (SQLException ignored) { }
		});
	}

	public static void initKits(final InitKits initKitsCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM kit_tracker")) {
				ResultSet resultSet = statement.executeQuery();

				List<PlayerKitTracker> map = new ArrayList<>();
				while (resultSet.next()) {
					PlayerKitTracker playerKitTracker = new PlayerKitTracker(
							UUID.fromString(resultSet.getString("uuid")),
							resultSet.getString("kit"),
							LocalDateTime.parse(resultSet.getString("lastClaimed"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
					);

					map.add(playerKitTracker);
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> initKitsCallback.onSuccess(map));
			} catch (SQLException ignored) { }
		});
	}

	public static void setKitCooldown(final String kitName, final UUID uniqueId) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("MERGE INTO kit_tracker (uuid, kit, lastClaimed) KEY (uuid, kit) VALUES (?, ?, ?)")) {
				statement.setString(1, uniqueId.toString());
				statement.setString(2, kitName);
				statement.setString(3, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void setNickname(final UUID uniqueId, final String nickname) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE players SET nickname = ? WHERE uuid = ?")) {
				statement.setString(1, nickname);
				statement.setString(2, uniqueId.toString());
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}
}
