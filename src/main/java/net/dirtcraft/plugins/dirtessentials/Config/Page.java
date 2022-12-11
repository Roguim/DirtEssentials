package net.dirtcraft.plugins.dirtessentials.Config;

import net.dirtcraft.plugins.dirtessentials.Utils.Utilities;

import java.util.List;

public class Page {
	private String name;
	private String title;
	private List<String> content;

	public String getName() {
		return name;
	}

	public String getTitle() {
		return Utilities.translate(title, false);
	}

	public List<String> getContent() {
		return content;
	}
}
