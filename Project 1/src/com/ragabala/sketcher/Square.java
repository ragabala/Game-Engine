package com.ragabala.sketcher;

import processing.core.PApplet;

public class Square extends Shape {
	public Square(PApplet sketcher, int side) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.side_a = side;
		this.side_b = side;
		x = (int) sketcher.width /2;
		y = (int) sketcher.height /2;
		
	}

	public void init_color(Color c) {
		this.color = c;
	}

	public void step(int x_dir, int y_dir) {

	}
	public void step() {

	}

	public void render() {
		sketcher.fill(color.r, color.g, color.b);
		sketcher.rectMode(sketcher.CENTER);
		sketcher.rect(x, y, side_a, side_b);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
