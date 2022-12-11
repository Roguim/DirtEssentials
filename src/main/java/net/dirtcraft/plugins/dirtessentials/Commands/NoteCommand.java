package net.dirtcraft.plugins.dirtessentials.Commands;

import net.dirtcraft.plugins.dirtessentials.Manager.NoteManager;
import net.dirtcraft.plugins.dirtessentials.Manager.PlayerManager;
import net.dirtcraft.plugins.dirtessentials.Utils.Permissions;
import net.dirtcraft.plugins.dirtessentials.Utils.Strings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return true;
		}

		if (!sender.hasPermission(Permissions.NOTE)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage(Strings.USAGE + "/note <add|remove|list> <player> [note|index]");
			return true;
		}

		String operation = args[0].toLowerCase();
		UUID target = PlayerManager.getUuid(args[1]);
		Player player = (Player) sender;
		if (target == null) {
			sender.sendMessage(Strings.PLAYER_NOT_FOUND);
			return true;
		}

		switch (operation) {
			case "add":
				if (args.length < 3) {
					sender.sendMessage(Strings.USAGE + "/note add <player> <note>");
					return true;
				}

				StringBuilder note = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					note.append(args[i]).append(" ");
				}

				NoteManager.addNote(target, new net.dirtcraft.plugins.dirtessentials.Data.Note(
						target,
						note.toString().trim(),
						player.getUniqueId(),
						LocalDateTime.now()
				));
				sender.sendMessage(Strings.PREFIX + "Added note to " + ChatColor.GOLD + args[1]);
				break;
			case "remove":
				if (args.length < 3) {
					sender.sendMessage(Strings.USAGE + "/note remove <player> <index>");
					return true;
				}

				int index;
				try {
					index = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage(Strings.USAGE + "/note remove <player> <index>");
					return true;
				}

				if (NoteManager.getNotes(target).size() < index) {
					sender.sendMessage(Strings.PREFIX + ChatColor.RED + "Invalid Index!");
					return true;
				}

				NoteManager.removeNote(target, NoteManager.getNoteByIndex(target, index));
				sender.sendMessage(Strings.PREFIX + "Removed note " + ChatColor.AQUA + index + ChatColor.GRAY + " from " + ChatColor.GOLD + args[1]);
				break;
			case "list":
				List<net.dirtcraft.plugins.dirtessentials.Data.Note> notes = NoteManager.getNotes(target);
				if (notes.isEmpty()) {
					sender.sendMessage(Strings.PREFIX + "No notes found for " + ChatColor.GOLD + args[1]);
					return true;
				}

				sender.sendMessage("");
				sender.sendMessage(ChatColor.GRAY + "Notes for " + ChatColor.GOLD + args[1] + ChatColor.GRAY + ":");
				sender.sendMessage("");

				for (int i = 0; i < notes.size(); i++) {
					BaseComponent[] removeComponent = new ComponentBuilder()
							.append(ChatColor.GRAY + "[" + ChatColor.RED + "\u2715" + ChatColor.GRAY + "]")
							.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/note remove " + args[1] + " " + i))
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Remove Note"))).create();

					BaseComponent[] noteComponent = new ComponentBuilder()
							.append(ChatColor.BLUE + "- " + ChatColor.GRAY + notes.get(i).getNote())
							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
									ChatColor.GOLD + "Index" + ChatColor.GRAY + ": " + ChatColor.AQUA + i + "\n" +
									ChatColor.GOLD + "Added By" + ChatColor.GRAY + ": " + ChatColor.RED + PlayerManager.getUsername(notes.get(i).getAddedBy()) + "\n" +
									ChatColor.GOLD + "Added On" + ChatColor.GRAY + ": " + ChatColor.DARK_AQUA + notes.get(i).getAddedOn().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
							)))
							.event((ClickEvent) null).create();

					ComponentBuilder builder = new ComponentBuilder();
					builder.append(removeComponent);
					builder.append(" ").event((ClickEvent) null).event((HoverEvent) null);
					builder.append(noteComponent);

					sender.spigot().sendMessage(builder.create());
				}

				sender.sendMessage("");

				break;
			default:
				sender.sendMessage(Strings.USAGE + "/note <add|remove|list> <player> [note|index]");
				break;
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (!sender.hasPermission(Permissions.NOTE)) return arguments;

		if (args.length == 1) {
			arguments.add("add");
			arguments.add("remove");
			arguments.add("list");
		} else if (args.length == 2) {
			arguments.addAll(PlayerManager.getAllPlayerNames());
		} else if (args.length == 3 && args[0].equalsIgnoreCase("remove")) {
			UUID uuid = PlayerManager.getUuid(args[1]);

			int amount = NoteManager.getNotes(uuid).size();

			for (int i = 0; i < amount; i++) {
				arguments.add(String.valueOf(i));
			}
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.toLowerCase().contains(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
