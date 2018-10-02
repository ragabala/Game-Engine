package com.hw1.sketcher;

import processing.core.PApplet;

/**
 * @author ragbalak
 * The square class is used for implementing game objects of 
 * square shape. This also extends the base Shape class.
 */
public class Square extends Shape {
	private static final long serialVersionUID = 1L;

	public Square(PApplet sketcher, int side, Color color) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.side_a = side;
		this.side_b = side;
		x = (int) sketcher.width /2;
		y = (int) sketcher.height /2;
		this.color = color;
		
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
