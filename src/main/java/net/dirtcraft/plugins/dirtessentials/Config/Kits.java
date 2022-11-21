package net.dirtcraft.plugins.dirtessentials.Config;

import com.moandjiezana.toml.TomlWriter;
import net.dirtcraft.plugins.dirtessentials.DirtEssentials;
import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Kits {
	public List<Kit> kits = new ArrayList<>();

	public void save() {
		File file = new File(DirtEssentials.getPlugin().getDataFolder(), "kits.toml");
		if (!DirtEssentials.getPlugin().getDataFolder().exists()) {
			DirtEssentials.getPlugin().getDataFolder().mkdirs();
		}

		if (!file.exists()) {
			try {
				Files.copy(DirtEssentials.getPlugin().getResource("kits.toml"), file.toPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		TomlWriter tomlWriter = new TomlWriter.Builder().indentValuesBy(4).indentTablesBy(8).padArrayDelimitersBy(1).build();
		String tomlString = tomlWriter.write(Utilities.kits);

		try {
			Files.write(file.toPath(), tomlString.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
