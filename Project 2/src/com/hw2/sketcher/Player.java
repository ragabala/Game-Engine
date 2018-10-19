package com.hw2.sketcher;

import processing.core.PApplet;
import processing.core.PConstants;

public class Player extends GameObject implements Movable, Renderable {

	float x_speed = 10, y_speed = 0;
	int diameter;
	float gravity = 0.3f;
	boolean connected = false;
	GameObject connectedObject = null;

	public Player(PApplet sketcher, int x, int y, int diameter) {
		// TODO Auto-generated constructor stub
		this.x_pos = x;
		this.y_pos = y;
		this.diameter = diameter;
		this.sketcher = sketcher;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		sketcher.fill(255, 0, 0);
		sketcher.ellipseMode(PConstants.CENTER);
		sketcher.ellipse(x_pos, y_pos, diameter, diameter);

	}

	@Override
	public void step(int x_dir, int y_dir) {
		// TODO Auto-generated method stub
		if (y_dir != 0 && y_speed == 0) {
			y_speed = -10;
			connected = false;
			connectedObject = null;
		}
		x_pos += x_dir * x_speed;
		y_pos += y_speed;
		if (y_speed != 0)
			y_speed += gravity;
		if (connected)
			y_speed = 0;
		if (connectedObject != null)
			if (!isConnected(connectedObject)) {
				connectedObject = null;
				connected = false;
				y_speed = 5;
		}
		wrap();
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

	public void wrap() {
		if (x_pos > sketcher.width)
			x_pos = 0;
		if (x_pos < 0)
			x_pos = sketcher.width;
	}

	public boolean isConnected(GameObject gameObject) {
		if (Math.abs(y_pos + diameter / 2 - gameObject.y_pos) <= y_speed + 0.1 
				&& x_pos >= gameObject.x_pos
				&& x_pos <= gameObject.x_pos + gameObject.length) {
			connected = true;
			y_speed = 0;
			connectedObject = gameObject;
			y_pos = gameObject.y_pos - (int) diameter / 2;
			return true;
		}
		return false;
	}

}
