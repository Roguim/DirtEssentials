package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.MessageManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReplyCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.REPLY)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(Strings.USAGE + "/reply <message>");
			return true;
		}

		Player player = (Player) sender;

		if (MessageManager.getLastMsg(player.getUniqueId()) == null) {
			sender.sendMessage(Strings.PREFIX + "You have no one to reply to!");
			return true;
		}

		Player target = Bukkit.getPlayer(MessageManager.getLastMsg(player.getUniqueId()));
		if (target == null) {
			sender.sendMessage(Strings.PREFIX + "The player you were messaging is no longer online!");
			return true;
		}

		if (!MessageManager.canReceiveMsg(target.getUniqueId())) {
			sender.sendMessage(Strings.PREFIX + "This player has disabled private messages!");
			return true;
		}

		if (target == sender) {
			sender.sendMessage(Strings.PREFIX + "You can't message yourself!");
			return true;
		}

		StringBuilder messageString = new StringBuilder();
		for (String arg : args) {
			messageString.append(arg).append(" ");
		}

		MessageManager.message(player, target, messageString.toString());
		MessageManager.setLastMsg(target.getUniqueId(), player.getUniqueId());
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.ENDERCHEST)) return arguments;

		if (args.length > 0) {
			arguments.add("<message>");
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
