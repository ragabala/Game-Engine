package com.hw1.sketcher;

/**
 * @author ragbalak
 * Renderable interface contains the methods that are responsible
 * for rendering and moving objects in the screen.
 * 
 */
public interface Renderable {
	public void step(int x_dir, int y_dir);
	public void step();
	public void stop();
	public void reset();	
	public void render();
}
