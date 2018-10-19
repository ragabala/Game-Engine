package com.hw1.sketcher;

import java.io.Serializable;

import processing.core.PApplet;

public class Shape implements Serializable, Renderable{
	private static final long serialVersionUID = 1L;
	transient PApplet sketcher;
	Color color;
	int x_speed, y_speed;
	int x, y; // centers of the objects
	int side_a, side_b;
	boolean movable;
	int jump_limit;


	
	public void setSketcher(PApplet sketcher) {
		this.sketcher = sketcher;
	}

	/**
	 *  This is used for setting the objects back in view when it 
	 *  scrolls out. Also used for bringing back objects that jumped.
	 */
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

	@Override
	public void step(int x_dir, int y_dir) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
	}

}
