package net.dirtcraft.plugins.dirtessentials.Database;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.dirtcraft.plugins.dirtessentials.Data.*;
import net.dirtcraft.plugins.dirtessentials.Database.Callbacks.*;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

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
			     PreparedStatement getHomesStatement = connection.prepareStatement("SELECT * FROM homes NATURAL JOIN homeData")) {
				ResultSet resultSet = getHomesStatement.executeQuery();

				Map<UUID, PlayerHomeData> map = new HashMap<>();
				List<Home> homes = new ArrayList<>();
				while (resultSet.next()) {
					UUID uniqueId = UUID.fromString(resultSet.getString("uuid"));
					if (!map.containsKey(uniqueId)) {
						map.put(uniqueId, new PlayerHomeData(uniqueId, resultSet.getInt("homesAvailable"), new ArrayList<>()));
					}

					homes.add(
							new Home(
									uniqueId,
									resultSet.getString("home"),
									resultSet.getString("world"),
									resultSet.getDouble("x"),
									resultSet.getDouble("y"),
									resultSet.getDouble("z"),
									resultSet.getFloat("yaw"),
									resultSet.getFloat("pitch")
							)
					);
				}

				for (Home home : homes) {
					map.get(home.getUuid()).addHome(home);
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> getHomesCallback.onSuccess(map));
			} catch (SQLException e) { e.printStackTrace(); }
		});
	}

	public static void addHome(final UUID uniqueId, final String name, final Location location, final AddHome addHomeCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement addHomeStatement = connection.prepareStatement("INSERT INTO homes VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
				addHomeStatement.setString(1, uniqueId.toString());
				addHomeStatement.setString(2, name);
				addHomeStatement.setString(3, location.getWorld().getName());
				addHomeStatement.setDouble(4, location.getX());
				addHomeStatement.setDouble(5, location.getY());
				addHomeStatement.setDouble(6, location.getZ());
				addHomeStatement.setFloat(7, location.getYaw());
				addHomeStatement.setFloat(8, location.getPitch());
				addHomeStatement.executeUpdate();

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
							resultSet.getString("leaveDate") != null ? LocalDateTime.parse(resultSet.getString("leaveDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null,
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
				insertStatement.setDouble(7, location != null ? location.getX() : 0);
				insertStatement.setDouble(8, location != null ? location.getY() : 0);
				insertStatement.setDouble(9, location != null ? location.getZ() : 0);
				insertStatement.setFloat(10, location != null ? location.getYaw() : 0);
				insertStatement.setFloat(11, location != null ? location.getPitch() : 0);
				insertStatement.setString(12, location != null ? location.getWorld().getName() : null);
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
			} catch (SQLException e) { e.printStackTrace(); }
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
			} catch (SQLException e) { e.printStackTrace(); }
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

	public static void initWarps(final InitWarps initWarpsCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps")) {
				ResultSet resultSet = statement.executeQuery();

				List<Warp> warps = new ArrayList<>();
				while (resultSet.next()) {
					String item = resultSet.getString("item");
					NBTContainer nbtContainer = new NBTContainer(item);
					ItemStack itemStack = NBTItem.convertNBTtoItem(nbtContainer);

					Warp warp = new Warp(
							resultSet.getString("name"),
							new Location(
									Bukkit.getWorld(resultSet.getString("world")),
									resultSet.getDouble("x"),
									resultSet.getDouble("y"),
									resultSet.getDouble("z"),
									resultSet.getFloat("yaw"),
									resultSet.getFloat("pitch")
							),
							itemStack
					);

					warps.add(warp);
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> initWarpsCallback.onSuccess(warps));
			} catch (SQLException ignored) { }
		});
	}

	public static void addWarp(final Warp warp) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("INSERT INTO warps VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
				statement.setString(1, warp.getName());
				statement.setString(2, warp.getLocation().getWorld().getName());
				statement.setDouble(3, warp.getLocation().getX());
				statement.setDouble(4, warp.getLocation().getY());
				statement.setDouble(5, warp.getLocation().getZ());
				statement.setFloat(6, warp.getLocation().getYaw());
				statement.setFloat(7, warp.getLocation().getPitch());
				statement.setString(8, NBTItem.convertItemtoNBT(warp.getIcon()).toString());
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void deleteWarp(final String warpName) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("DELETE FROM warps WHERE name = ?")) {
				statement.setString(1, warpName);
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void changeWarpIcon(final String warp, final ItemStack item) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE warps SET item = ? WHERE name = ?")) {
				statement.setString(1, NBTItem.convertItemtoNBT(item).toString());
				statement.setString(2, warp);
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void getSpawn(final GetSpawn getSpawnCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM spawn")) {
				ResultSet resultSet = statement.executeQuery();

				if (resultSet.next()) {
					Spawn spawn = new Spawn(
							resultSet.getString("world"),
							resultSet.getDouble("x"),
							resultSet.getDouble("y"),
							resultSet.getDouble("z"),
							resultSet.getFloat("yaw"),
							resultSet.getFloat("pitch")
					);

					Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> getSpawnCallback.onSuccess(spawn));
					return;
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> getSpawnCallback.onSuccess(null));
			} catch (SQLException e) { e.printStackTrace(); }
		});
	}

	public static void setSpawn(final Spawn spawn) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM spawn");
			     PreparedStatement statement = connection.prepareStatement("INSERT INTO spawn VALUES (?, ?, ?, ?, ?, ?)")) {
				deleteStatement.executeUpdate();

				statement.setString(1, spawn.getWorld());
				statement.setDouble(2, spawn.getX());
				statement.setDouble(3, spawn.getY());
				statement.setDouble(4, spawn.getZ());
				statement.setFloat(5, spawn.getYaw());
				statement.setFloat(6, spawn.getPitch());
				statement.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
		});
	}

	public static void setHomeBalance(final UUID uuid, final int homeBalance) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE homeData SET homesAvailable = ? WHERE uuid = ?")) {
				statement.setInt(1, homeBalance);
				statement.setString(2, uuid.toString());
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void initAB(final GetAutobroadcast autobroadcastCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM autobroadcast")) {
				ResultSet resultSet = statement.executeQuery();

				List<UUID> autobroadcasts = new ArrayList<>();

				while (resultSet.next()) {
					autobroadcasts.add(UUID.fromString(resultSet.getString("uuid")));
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> autobroadcastCallback.onSuccess(autobroadcasts));
			} catch (SQLException ignored) { }
		});
	}

	public static void removeAB(final UUID uuid) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("DELETE FROM autobroadcast WHERE uuid = ?")) {
				statement.setString(1, uuid.toString());
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void addAB(UUID uuid) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("INSERT INTO autobroadcast VALUES (?)")) {
				statement.setString(1, uuid.toString());
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void addNewHomeData(final UUID uniqueId) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("INSERT INTO homeData VALUES (?, ?)")) {
				statement.setString(1, uniqueId.toString());
				statement.setInt(2, Utilities.config.home.defaultHomes);
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void initNotes(final GetNotes getNotesCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM notes")) {
				ResultSet resultSet = statement.executeQuery();

				Map<UUID, List<Note>> noteMap = new HashMap<>();
				List<Note> notes = new ArrayList<>();
				while (resultSet.next()) {
					notes.add(new Note(
							UUID.fromString(resultSet.getString("uuid")),
							resultSet.getString("note"),
							UUID.fromString(resultSet.getString("addedBy")),
							LocalDateTime.parse(resultSet.getString("addedOn"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
					));
				}

				for (Note note : notes) {
					if (noteMap.containsKey(note.getUuid())) {
						noteMap.get(note.getUuid()).add(note);
					} else {
						List<Note> noteList = new ArrayList<>();
						noteList.add(note);
						noteMap.put(note.getUuid(), noteList);
					}
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> getNotesCallback.onSuccess(noteMap));
			} catch (SQLException ignored) { }
		});
	}

	public static void addNote(final UUID uuid, final Note note) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("INSERT INTO notes VALUES (?, ?, ?, ?)")) {
				statement.setString(1, uuid.toString());
				statement.setString(2, note.getNote());
				statement.setString(3, note.getAddedBy().toString());
				statement.setString(4, note.getAddedOn().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void removeNote(final UUID uuid, final Note note) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("DELETE FROM notes WHERE uuid = ? AND note = ? AND addedBy = ? AND addedOn = ?")) {
				statement.setString(1, uuid.toString());
				statement.setString(2, note.getNote());
				statement.setString(3, note.getAddedBy().toString());
				statement.setString(4, note.getAddedOn().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
				statement.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void resetAllPlayerData() {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("DELETE FROM players");
			     PreparedStatement statement1 = connection.prepareStatement("DELETE FROM balances");
			     PreparedStatement statement2 = connection.prepareStatement("DELETE FROM kit_tracker");
			     PreparedStatement statement3 = connection.prepareStatement("DELETE FROM homeData");
			     PreparedStatement statement4 = connection.prepareStatement("DELETE FROM homes");
			     PreparedStatement statement5 = connection.prepareStatement("DELETE FROM autobroadcast")) {
				statement.executeUpdate();
				statement1.executeUpdate();
				statement2.executeUpdate();
				statement3.executeUpdate();
				statement4.executeUpdate();
				statement5.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}

	public static void resetPlayerData(final UUID uniqueId) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("DELETE FROM players WHERE uuid = ?");
			     PreparedStatement statement1 = connection.prepareStatement("DELETE FROM balances WHERE uuid = ?");
			     PreparedStatement statement2 = connection.prepareStatement("DELETE FROM kit_tracker WHERE uuid = ?");
			     PreparedStatement statement3 = connection.prepareStatement("DELETE FROM homeData WHERE uuid = ?");
			     PreparedStatement statement4 = connection.prepareStatement("DELETE FROM homes WHERE uuid = ?");
			     PreparedStatement statement5 = connection.prepareStatement("DELETE FROM autobroadcast WHERE uuid = ?")) {
				statement.setString(1, uniqueId.toString());
				statement1.setString(1, uniqueId.toString());
				statement2.setString(1, uniqueId.toString());
				statement3.setString(1, uniqueId.toString());
				statement4.setString(1, uniqueId.toString());
				statement5.setString(1, uniqueId.toString());
				statement.executeUpdate();
				statement1.executeUpdate();
				statement2.executeUpdate();
				statement3.executeUpdate();
				statement4.executeUpdate();
				statement5.executeUpdate();
			} catch (SQLException ignored) { }
		});
	}
}
