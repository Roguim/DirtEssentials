package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reload implements CommandExecutor, TabCompleter {
	private static final List<String> subCommands = new ArrayList<>(Arrays.asList("config", "kits", "worth", "all"));

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.RELOAD)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0 || !subCommands.contains(args[0].toLowerCase())) {
			sender.sendMessage(Strings.USAGE + "/reload <config|kits|worth|all>");
			return true;
		}

		switch (args[0]) {
			case "config":
				Utilities.loadConfig();
				sender.sendMessage(Strings.PREFIX + "Config reloaded!");
				break;
			case "kits":
				Utilities.loadKits();
				sender.sendMessage(Strings.PREFIX + "Kits reloaded!");
				break;
			case "worth":
				Utilities.loadWorth();
				sender.sendMessage(Strings.PREFIX + "Worths reloaded!");
				break;
			case "all":
				Utilities.loadConfig();
				Utilities.loadKits();
				Utilities.loadWorth();
				sender.sendMessage(Strings.PREFIX + "All Configs reloaded!");
				break;
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.RELOAD)) return arguments;

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
