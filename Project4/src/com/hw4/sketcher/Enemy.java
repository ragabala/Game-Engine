package com.hw4.sketcher;

import processing.core.PApplet;

public class Enemy extends GameObject implements Movable,Renderable,Shootable{

	int side = 10;
	boolean isAlive = true;
	
	public Enemy(PApplet sketcher, int x_pos, int y_pos) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.x_pos =x_pos;
		this.y_pos = y_pos;
	}
	
	@Override
	public GameObject shoot() {
		if(!isAlive) return null; // don't shoot if not alive 
		return null;
	}
	



	@Override
	public void render() {
		// TODO Auto-generated method stub
		if(!isAlive) return; // don't render if not alive 
		sketcher.rectMode(sketcher.CENTER);
		sketcher.fill(255,255,255);
		sketcher.rect(x_pos, y_pos, side, side);
	}

	@Override
	public void step(int x_dir, int y_dir) {
		// TODO Auto-generated method stub
		x_pos += x_dir;
		y_pos += y_dir ;
		
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
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		x_pos = Integer.parseInt(vals[2]);
		y_pos = Integer.parseInt(vals[3]);

	}
	
	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "ENEMY~" + GAME_OBJECT_ID + "~" + x_pos + "~" + y_pos + "~" + isAlive;
	}

}
