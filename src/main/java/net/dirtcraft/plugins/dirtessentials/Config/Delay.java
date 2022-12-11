package net.dirtcraft.plugins.dirtessentials.Config;

public class Delay {
	public int delay;

	public int getDelay() {
		return Math.max(this.delay, 60);
	}
}
