package com.ragabala.sketcher;

import processing.core.PApplet;

abstract class Shape {
	PApplet sketcher;
	Color color;
	int x_speed, y_speed;
	int x, y; // centers of the objects
	int side_a, side_b;
	boolean movable;
	int jump_limit;

	public abstract void step(int x_dir, int y_dir);
	public abstract void step();
	public abstract void stop();
	public abstract void reset();
	
	public abstract void render();
	
	public abstract void init_color(Color c);

	public void wrap() {
		if (x > sketcher.width)
			x = 0;
		if (x < 0)
			x = sketcher.width;
		if (y < jump_limit)
			y_speed = -y_speed;
		if (y > sketcher.height /2)
		{
			y = (int)sketcher.height /2;
			y_speed = 0;
		}
			
	}
	
}
