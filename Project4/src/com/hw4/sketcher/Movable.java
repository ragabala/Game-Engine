package com.hw4.sketcher;

public interface Movable {
	public void step(int x_dir, int y_dir);
	public void step();
	public void stop();
	public void reset();
	public double[] getSpeed();

}
