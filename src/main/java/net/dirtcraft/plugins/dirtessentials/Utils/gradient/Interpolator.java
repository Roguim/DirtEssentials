package net.dirtcraft.plugins.dirtessentials.Utils.gradient;

@FunctionalInterface
public interface Interpolator {

	double[] interpolate(double from, double to, int max);
}
