package com.ragabala.sketcher;

import processing.core.PApplet;

class Rectangle extends Shape {
	public Rectangle(PApplet sketcher, int side_a, int side_b) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.side_a = side_a;
		this.side_b = side_b;
		x_speed = 5;
		y_speed = 0;
		x = 10;
		y = (int) sketcher.height/2;
		jump_limit = (int) sketcher.height/2 - 200;
	}

	public void init_color(Color c) {
		this.color = c;
	}

	
	public void step() {
		x = x + x_speed;
		y = y + y_speed;
		wrap();
	}
	
	public void step(int x_dir, int y_dir) {
		if(x_dir!=0)
			x_speed = Math.abs(x_speed)*x_dir;
		if(y_dir !=0 && y_speed == 0 )
		{
			y_speed=-Math.abs(x_speed);
			System.out.println("setting y speed"+y_speed);
	
		}
	}

	public void render() {
		sketcher.fill(color.r, color.g, color.b);
		sketcher.rectMode(sketcher.CENTER);
		sketcher.rect(x, y, side_a, side_b);
	}

}
