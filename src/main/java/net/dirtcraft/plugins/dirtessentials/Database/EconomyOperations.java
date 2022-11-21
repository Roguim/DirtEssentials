package net.dirtcraft.plugins.dirtessentials.Database;

import net.dirtcraft.plugins.dirtessentials.Database.Callbacks.CreatePlayer;
import net.dirtcraft.plugins.dirtessentials.Database.Callbacks.GetBalance;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class EconomyOperations {
	public static void  getBalances(final GetBalance getBalanceCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("SELECT * FROM balances")) {
				ResultSet resultSet = statement.executeQuery();

				Map<UUID, Double> balances = new HashMap<>();

				while (resultSet.next()) {
					balances.put(UUID.fromString(resultSet.getString("uuid")), resultSet.getDouble("balance"));
				}

				Bukkit.getScheduler().runTask(DirtEssentials.getPlugin(), () -> getBalanceCallback.onSuccess(balances));
			} catch (SQLException e) {
				if (Utilities.config.general.debug) {
					Utilities.log(Level.SEVERE, "Could not execute getBalances query! Shutting down server ...");
					e.printStackTrace();
				}
				Utilities.disablePlugin();
				Bukkit.shutdown();
			}
		});
	}

	public static boolean createPlayerBalance(final UUID playerUuid) {
		try (Connection connection = Database.getConnection();
		     PreparedStatement statement = connection.prepareStatement("INSERT INTO balances VALUES (?, ?)")) {
			System.out.println("Creating player balance for " + playerUuid);
			statement.setString(1, playerUuid.toString());
			statement.setDouble(2, Utilities.config.economy.defaultMoney);
			statement.executeUpdate();

			DirtEssentials.getDirtEconomy().addAccount(playerUuid, Utilities.config.economy.defaultMoney);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void withdrawMoney(final UUID uniqueId, final double amount) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE balances SET balance = balance - ? WHERE uuid = ?")) {
				statement.setDouble(1, amount);
				statement.setString(2, uniqueId.toString());
				statement.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
		});
	}

	public static void depositMoney(final UUID uniqueId, final double amount) {
		Bukkit.getScheduler().runTaskAsynchronously(DirtEssentials.getPlugin(), () -> {
			try (Connection connection = Database.getConnection();
			     PreparedStatement statement = connection.prepareStatement("UPDATE balances SET balance = balance + ? WHERE uuid = ?")) {
				statement.setDouble(1, amount);
				statement.setString(2, uniqueId.toString());
				statement.executeUpdate();
			} catch (SQLException e) { e.printStackTrace(); }
		});
	}
}
