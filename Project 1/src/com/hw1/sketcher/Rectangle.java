package com.hw1.sketcher;

import processing.core.PApplet;

/**
 * @author ragbalak
 * The Rectangle class is used for implementing game objects of 
 * rectangular shape. This also extends the base Shape class.
 */
public class Rectangle extends Shape {

	private static final long serialVersionUID = 1L;

	// This constructor is used for the renderer class.
	// The x and y are predefined based on the PApplet.
	public Rectangle(PApplet sketcher, int side_a, int side_b, Color color) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.side_a = side_a;
		this.side_b = side_b;
		x_speed = 5;
		y_speed = 0;
		x = 10;
		y = (int) sketcher.height/2;
		jump_limit = (int) sketcher.height/2 - 200;
		this.color = color;
	}
	
	// this is for the client server architecture. This constructor design allows
	// the x,y parameters to be passed.
	public Rectangle(PApplet sketcher, int side_a, int side_b, int x, int y, Color color) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.side_a = side_a;
		this.side_b = side_b;
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	

	@Override
	public void step() {
		x = x + x_speed;
		y = y + y_speed;
		wrap();
	}
	@Override
	public void step(int x_dir, int y_dir) {
		if(x_dir ==0 && y_dir == 0) {
			x_speed = 0; y_speed = 0;
		}
		if(x_dir!=0)
			x_speed = Math.abs(x_speed)*x_dir;
		if(y_dir !=0 && y_speed == 0 )
		{
			y_speed=-Math.abs(x_speed);	
		}
	}
	@Override
	public void render() {
		sketcher.fill(color.r, color.g, color.b);
		sketcher.rectMode(sketcher.CENTER);
		sketcher.rect(x, y, side_a, side_b);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		x_speed = y_speed = 0;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		x_speed = 5;
		y_speed = 0;
		x = 10;
		y = (int) sketcher.height/2;
	}


}

