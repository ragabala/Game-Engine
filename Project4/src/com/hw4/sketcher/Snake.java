package com.hw4.sketcher;

import processing.core.PApplet;

public class Snake extends GameObject implements Movable, Renderable {

	int side;
	Color color;
	boolean isAlive = true;
	public Snake(PApplet skecher,int x, int y, int side,Color color) {
		// TODO Auto-generated constructor stub
		this.sketcher = skecher;
		this.x_pos = x;
		this.y_pos = y;
		this.side = side;
		this.color = color;

	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		if(!isAlive) return;
		sketcher.fill(color.r, color.g,color.b);
		sketcher.rect(x_pos,y_pos, side, side);
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
	public double[] getSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "SNAKE~" + GAME_OBJECT_ID + "~"  +x_pos + "~" + y_pos + "~" + side + "~" + color.r + "~" + color.g + "~"
		+ color.b+"~"+isAlive;
	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		x_pos = Integer.parseInt(vals[2]);
		y_pos = Integer.parseInt(vals[3]);
		isAlive = Boolean.parseBoolean(vals[8]);
		
	}

}
