package com.hw4.sketcher;

import processing.core.PApplet;

public class Platform extends GameObject implements Movable, Renderable {
	private static final long serialVersionUID = 394432487520792007L;
	int breadth;
	boolean isMovable;
	double[] speed = { 0, 0 };
	int velocity = 5;
	int iter = 1, limit = 300 / velocity;
	Color color;

	public Platform(PApplet sketcher, int x_pos, int y_pos, int length, int breadth, Color color) {
		// TODO Auto-generated constructor stub
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.length = length;
		this.breadth = breadth;
		this.sketcher = sketcher;
		this.color = color;
	}

	public void setMotion(int x, int y) {
		speed[0] = x * velocity;
		speed[1] = y * velocity;
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
		x_pos += speed[0] ;
		y_pos += speed[1] ;
		if ((x_pos < 0.1 * sketcher.width && speed[0] < 0) || (x_pos > 0.6 * sketcher.width && speed[0] > 0))
			speed[0] *= -1;
		if ((y_pos < 0.1 * sketcher.height && speed[1] < 0) || (y_pos > 0.6 * sketcher.height && speed[1] > 0))
			speed[1] *= -1;

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
		sketcher.fill(color.r, color.g, color.b);
		sketcher.rect(x_pos, y_pos, length, breadth);

	}

	@Override
	public double[] getSpeed() {
		// TODO Auto-generated method stub
		return speed;
	}

	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "PLATFORM~" + GAME_OBJECT_ID + "~" + x_pos + "~" + y_pos + "~" + length + "~" + breadth + "~" + color.r
				+ "~" + color.g + "~" + color.b + "~" + speed[0] + "~" + speed[1];

	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		x_pos = Integer.parseInt(vals[2]);
		y_pos = Integer.parseInt(vals[3]);
		speed[0] = Double.parseDouble(vals[9]);
		speed[1] = Double.parseDouble(vals[10]);

	}
}
