package com.hw2.sketcher;

import processing.core.PApplet;

public class Platform extends GameObject implements Movable, Renderable {

	int breadth;
	boolean isMovable;
	int x_speed, y_speed;
	int iter = 1, limit = 200;
	Color color;

	public Platform(PApplet sketcher, int x_pos, int y_pos, int length, int breadth) {
		// TODO Auto-generated constructor stub
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.length = length;
		this.breadth = breadth;
		this.sketcher = sketcher;
		color = new Color(255, 255, 255);
	}

	public void setMotion(int x, int y) {
		x_speed = x;
		y_speed = y;
		isMovable = true;
	}

	@Override
	public void step(int x_dir, int y_dir) {

	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (!isMovable)
			return;
		x_pos += x_speed;
		y_pos += y_speed;
		iter = (iter + 1) % limit;
		if (iter == 0) {
			x_speed *= -1;
			y_speed *= -1;
		}
		wrap();
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
		sketcher.fill(color.r,color.g,color.b);
		sketcher.rect(x_pos, y_pos, length, breadth);

	}

	public void changeColor() {
		color = new Color((int)sketcher.random(255),
				(int)sketcher.random(255),
				(int)sketcher.random(255));
	}
	
}
