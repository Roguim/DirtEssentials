package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.ABManager;
import net.dirtcraft.plugins.dirtessentials.Manager.AfkManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {
	private static final List<String> subCommands = new ArrayList<>(Arrays.asList(
			"all",
			"autobroadcast",
			"cjm",
			"config",
			"help",
			"kits",
			"worth"
	));

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.RELOAD)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0 || !subCommands.contains(args[0].toLowerCase())) {
			sender.sendMessage(Strings.USAGE + "/reload <all|autobroadcast|cjm|config|help|kits|worth>");
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "autobroadcast":
				try {
					Utilities.loadAB();
					ABManager.loadABManager();
				} catch (Exception e) {
					sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "An error occurred while reloading the Autobroadcasts!");
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(Strings.PREFIX + "Autobroadcasts reloaded!");
				break;
			case "cjm":
				try {
					Utilities.loadCjm();
				} catch (Exception e) {
					sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "An error occurred while reloading the CJMs!");
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(Strings.PREFIX + "CJMs reloaded!");
				break;
			case "config":
				try {
					Utilities.loadConfig();
					AfkManager.loadAfkCheck();
				} catch (Exception e) {
					sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "An error occurred while reloading the config!");
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(Strings.PREFIX + "Config reloaded!");
				break;
			case "kits":
				try {
					Utilities.loadKits();
				} catch (Exception e) {
					sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "An error occurred while reloading the kits!");
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(Strings.PREFIX + "Kits reloaded!");
				break;
			case "worth":
				try {
					Utilities.loadConfig();
				} catch (Exception e) {
					sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "An error occurred while reloading the worth!");
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(Strings.PREFIX + "Worths reloaded!");
				break;
			case "help":
				try {
					Utilities.loadHelp();
				} catch (Exception e) {
					sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "An error occurred while reloading the help!");
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(Strings.PREFIX + "Help reloaded!");
				break;
			case "all":
				try {
					Utilities.loadConfig();
					AfkManager.loadAfkCheck();
					Utilities.loadKits();
					Utilities.loadHelp();
					Utilities.loadCjm();
					Utilities.loadAB();
					ABManager.loadABManager();
				} catch (Exception e) {
					sender.sendMessage(Strings.PREFIX + ChatColor.DARK_RED + "An error occurred while reloading the configs!");
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(Strings.PREFIX + "All Configs reloaded!");
				break;
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.RELOAD))
			return arguments;

		if (args.length == 1) {
			arguments.addAll(subCommands);
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
