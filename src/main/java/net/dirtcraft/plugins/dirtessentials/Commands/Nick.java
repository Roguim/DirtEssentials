package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Database.DatabaseOperations;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Nick implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.NICK)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		Player player = (Player) sender;

		if (args.length != 1) {
			player.sendMessage(Strings.USAGE + "/nick <reset|<nick>>");
			return true;
		}

		if (args[0].equalsIgnoreCase("reset")) {
			player.setDisplayName(player.getName());
			player.sendMessage(Strings.PREFIX + "Your nickname has been reset!");
			return true;
		}

		if (Utilities.translate(args[0], true).length() > 16) {
			player.sendMessage(Strings.PREFIX + "Your nickname cannot be longer than 16 characters! (excluding color codes)");
			return true;
		}

		player.setDisplayName(Utilities.translate(args[0], false));
		player.sendMessage(Strings.PREFIX + "Your nickname has been set to " + Utilities.translate(args[0], false) + "!");

		DatabaseOperations.setNickname(player.getUniqueId(), Utilities.translate(args[0], false));
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.NICK)) return arguments;

		if (args.length == 1) {
			arguments.add("reset");
			arguments.add("<nick>");
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
