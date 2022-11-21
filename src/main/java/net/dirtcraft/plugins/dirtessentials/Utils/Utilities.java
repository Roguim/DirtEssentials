package net.dirtcraft.plugins.dirtessentials.Utils;

import com.moandjiezana.toml.Toml;
import net.dirtcraft.plugins.dirtessentials.Commands.Back;
import net.dirtcraft.plugins.dirtessentials.Config.Config;
import net.dirtcraft.plugins.dirtessentials.Config.Kits;
import net.dirtcraft.plugins.dirtessentials.Config.Worth;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Commands.*;
import net.dirtcraft.plugins.dirtessentials.Listener.ConnectionListener;
import net.dirtcraft.plugins.dirtessentials.Manager.BackManager;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
	public static Config config;
	public static Kits kits;
	public static Worth worth;

	public static String translate(String message, boolean stripColor) {
		Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
		Matcher matcher = pattern.matcher(message);

		while (matcher.find()) {
			String color = message.substring(matcher.start(), matcher.end());
			message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color.replace("&", "")) + "");
			matcher = pattern.matcher(message);
		}

		if (stripColor) return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message));

		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void loadConfig() {
		if (!DirtEssentials.getPlugin().getDataFolder().exists()) {
			DirtEssentials.getPlugin().getDataFolder().mkdirs();
		}
		File file = new File(DirtEssentials.getPlugin().getDataFolder(), "config.toml");
		if (!file.exists()) {
			try {
				Files.copy(DirtEssentials.getPlugin().getResource("config.toml"), file.toPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		config = new Toml(new Toml().read(DirtEssentials.getPlugin().getResource("config.toml"))).read(file).to(Config.class);
	}

	public static void loadKits() {
		if (!DirtEssentials.getPlugin().getDataFolder().exists()) {
			DirtEssentials.getPlugin().getDataFolder().mkdirs();
		}
		File file = new File(DirtEssentials.getPlugin().getDataFolder(), "kits.toml");
		if (!file.exists()) {
			try {
				Files.copy(DirtEssentials.getPlugin().getResource("kits.toml"), file.toPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		kits = new Toml(new Toml().read(DirtEssentials.getPlugin().getResource("kits.toml"))).read(file).to(Kits.class);
	}

	public static void loadWorth() {
		if (!DirtEssentials.getPlugin().getDataFolder().exists()) {
			DirtEssentials.getPlugin().getDataFolder().mkdirs();
		}
		File file = new File(DirtEssentials.getPlugin().getDataFolder(), "worths.toml");
		if (!file.exists()) {
			try {
				Files.copy(DirtEssentials.getPlugin().getResource("worths.toml"), file.toPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		worth = new Toml(new Toml().read(DirtEssentials.getPlugin().getResource("worths.toml"))).read(file).to(Worth.class);
	}

	public static void registerCommands() {
		DirtEssentials.getPlugin().getCommand("repair").setExecutor(new Repair());
		DirtEssentials.getPlugin().getCommand("nbt").setExecutor(new NBT());
		DirtEssentials.getPlugin().getCommand("weather").setExecutor(new Weather());
		DirtEssentials.getPlugin().getCommand("weather").setTabCompleter(new Weather());
		DirtEssentials.getPlugin().getCommand("enderchest").setExecutor(new Enderchest());
		DirtEssentials.getPlugin().getCommand("enderchest").setTabCompleter(new Enderchest());
		DirtEssentials.getPlugin().getCommand("top").setExecutor(new Top());
		DirtEssentials.getPlugin().getCommand("broadcast").setExecutor(new Broadcast());
		DirtEssentials.getPlugin().getCommand("smite").setExecutor(new Smite());
		DirtEssentials.getPlugin().getCommand("smite").setTabCompleter(new Smite());
		DirtEssentials.getPlugin().getCommand("feed").setExecutor(new Feed());
		DirtEssentials.getPlugin().getCommand("feed").setTabCompleter(new Feed());
		DirtEssentials.getPlugin().getCommand("heal").setExecutor(new Heal());
		DirtEssentials.getPlugin().getCommand("heal").setTabCompleter(new Heal());
		DirtEssentials.getPlugin().getCommand("unalive").setExecutor(new Unalive());
		DirtEssentials.getPlugin().getCommand("fly").setTabCompleter(new Fly());
		DirtEssentials.getPlugin().getCommand("fly").setExecutor(new Fly());
		DirtEssentials.getPlugin().getCommand("kill").setExecutor(new Kill());
		DirtEssentials.getPlugin().getCommand("kill").setTabCompleter(new Kill());
		DirtEssentials.getPlugin().getCommand("hat").setExecutor(new Hat());
		DirtEssentials.getPlugin().getCommand("clear").setExecutor(new Clear());
		DirtEssentials.getPlugin().getCommand("clear").setTabCompleter(new Clear());
		DirtEssentials.getPlugin().getCommand("trash").setExecutor(new Trash());
		DirtEssentials.getPlugin().getCommand("gmc").setExecutor(new Gmc());
		DirtEssentials.getPlugin().getCommand("gmc").setTabCompleter(new Gmc());
		DirtEssentials.getPlugin().getCommand("gms").setExecutor(new Gms());
		DirtEssentials.getPlugin().getCommand("gms").setTabCompleter(new Gms());
		DirtEssentials.getPlugin().getCommand("gma").setExecutor(new Gma());
		DirtEssentials.getPlugin().getCommand("gma").setTabCompleter(new Gma());
		DirtEssentials.getPlugin().getCommand("gmsp").setExecutor(new Gmsp());
		DirtEssentials.getPlugin().getCommand("gmsp").setTabCompleter(new Gmsp());
		DirtEssentials.getPlugin().getCommand("time").setExecutor(new Time());
		DirtEssentials.getPlugin().getCommand("time").setTabCompleter(new Time());
		DirtEssentials.getPlugin().getCommand("bal").setExecutor(new Bal());
		DirtEssentials.getPlugin().getCommand("bal").setTabCompleter(new Bal());
		DirtEssentials.getPlugin().getCommand("pay").setExecutor(new Pay());
		DirtEssentials.getPlugin().getCommand("pay").setTabCompleter(new Pay());
		DirtEssentials.getPlugin().getCommand("eco").setExecutor(new Eco());
		DirtEssentials.getPlugin().getCommand("eco").setTabCompleter(new Eco());
		DirtEssentials.getPlugin().getCommand("baltop").setExecutor(new Baltop());
		DirtEssentials.getPlugin().getCommand("baltop").setTabCompleter(new Baltop());
		DirtEssentials.getPlugin().getCommand("craft").setExecutor(new Craft());
		DirtEssentials.getPlugin().getCommand("anvil").setExecutor(new Anvil());
		DirtEssentials.getPlugin().getCommand("cartographytable").setExecutor(new Cartographytable());
		DirtEssentials.getPlugin().getCommand("grindstone").setExecutor(new Grindstone());
		DirtEssentials.getPlugin().getCommand("loom").setExecutor(new Loom());
		DirtEssentials.getPlugin().getCommand("smithingtable").setExecutor(new Smithingtable());
		DirtEssentials.getPlugin().getCommand("stonecutter").setExecutor(new Stonecutter());
		DirtEssentials.getPlugin().getCommand("skull").setExecutor(new Skull());
		DirtEssentials.getPlugin().getCommand("skull").setTabCompleter(new Skull());
		DirtEssentials.getPlugin().getCommand("god").setExecutor(new God());
		DirtEssentials.getPlugin().getCommand("god").setTabCompleter(new God());
		DirtEssentials.getPlugin().getCommand("xp").setExecutor(new Xp());
		DirtEssentials.getPlugin().getCommand("xp").setTabCompleter(new Xp());
		DirtEssentials.getPlugin().getCommand("break").setExecutor(new Break());
		DirtEssentials.getPlugin().getCommand("speed").setExecutor(new Speed());
		DirtEssentials.getPlugin().getCommand("speed").setTabCompleter(new Speed());
		DirtEssentials.getPlugin().getCommand("enchant").setExecutor(new Enchant());
		DirtEssentials.getPlugin().getCommand("enchant").setTabCompleter(new Enchant());
		DirtEssentials.getPlugin().getCommand("reload").setExecutor(new Reload());
		DirtEssentials.getPlugin().getCommand("reload").setTabCompleter(new Reload());
		DirtEssentials.getPlugin().getCommand("createkit").setExecutor(new Createkit());
		DirtEssentials.getPlugin().getCommand("createkit").setTabCompleter(new Createkit());
		DirtEssentials.getPlugin().getCommand("delkit").setExecutor(new Delkit());
		DirtEssentials.getPlugin().getCommand("delkit").setTabCompleter(new Delkit());
		DirtEssentials.getPlugin().getCommand("kits").setExecutor(new net.dirtcraft.plugins.dirtessentials.Commands.Kits());
		DirtEssentials.getPlugin().getCommand("kit").setExecutor(new Kit());
		DirtEssentials.getPlugin().getCommand("kit").setTabCompleter(new Kit());
		DirtEssentials.getPlugin().getCommand("showkit").setExecutor(new Showkit());
		DirtEssentials.getPlugin().getCommand("showkit").setTabCompleter(new Showkit());
		DirtEssentials.getPlugin().getCommand("sethome").setExecutor(new Sethome());
		DirtEssentials.getPlugin().getCommand("home").setExecutor(new Home());
		DirtEssentials.getPlugin().getCommand("home").setTabCompleter(new Home());
		DirtEssentials.getPlugin().getCommand("back").setExecutor(new Back());
		DirtEssentials.getPlugin().getCommand("homes").setExecutor(new Homes());
		DirtEssentials.getPlugin().getCommand("delhome").setExecutor(new Delhome());
		DirtEssentials.getPlugin().getCommand("delhome").setTabCompleter(new Delhome());
		DirtEssentials.getPlugin().getCommand("otherhome").setExecutor(new Otherhome());
		DirtEssentials.getPlugin().getCommand("otherhome").setTabCompleter(new Otherhome());
		DirtEssentials.getPlugin().getCommand("tppos").setExecutor(new Tppos());
		DirtEssentials.getPlugin().getCommand("tppos").setTabCompleter(new Tppos());
		DirtEssentials.getPlugin().getCommand("seen").setExecutor(new Seen());
		DirtEssentials.getPlugin().getCommand("seen").setTabCompleter(new Seen());
		DirtEssentials.getPlugin().getCommand("sudo").setExecutor(new Sudo());
		DirtEssentials.getPlugin().getCommand("sudo").setTabCompleter(new Sudo());
		DirtEssentials.getPlugin().getCommand("nick").setExecutor(new Nick());
		DirtEssentials.getPlugin().getCommand("nick").setTabCompleter(new Nick());
		DirtEssentials.getPlugin().getCommand("othernick").setExecutor(new Othernick());
		DirtEssentials.getPlugin().getCommand("othernick").setTabCompleter(new Othernick());
		DirtEssentials.getPlugin().getCommand("itemname").setExecutor(new Itemname());
		DirtEssentials.getPlugin().getCommand("lore").setExecutor(new Lore());


	}

	public static void registerListeners() {
		DirtEssentials.getPlugin().getServer().getPluginManager().registerEvents(new Fly(), DirtEssentials.getPlugin());
		DirtEssentials.getPlugin().getServer().getPluginManager().registerEvents(new ConnectionListener(), DirtEssentials.getPlugin());
		DirtEssentials.getPlugin().getServer().getPluginManager().registerEvents(new God(), DirtEssentials.getPlugin());
		DirtEssentials.getPlugin().getServer().getPluginManager().registerEvents(new Showkit(), DirtEssentials.getPlugin());
		DirtEssentials.getPlugin().getServer().getPluginManager().registerEvents(new BackManager(), DirtEssentials.getPlugin());
	}

	public static void log(Level level, String msg) {
		DirtEssentials.getPlugin().getLogger().log(level, msg);
	}

	public static void disablePlugin() {
		DirtEssentials.getPlugin().getServer().getPluginManager().disablePlugin(DirtEssentials.getPlugin());
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}

		return true;
	}

	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}

		return true;
	}

	public static boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}

		return true;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
