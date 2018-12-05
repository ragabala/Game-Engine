package com.hw4.sketcher;

import java.util.Collection;

import processing.core.PApplet;

public class Enemy extends GameObject implements Renderable{

	int side = 10;
	boolean isAlive = true;
	
	public Enemy(PApplet sketcher, int x_pos, int y_pos) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.x_pos =x_pos;
		this.y_pos = y_pos;
	}
	

	@Override
	public void render() {
		// TODO Auto-generated method stub
		if(!isAlive) return; // don't render if not alive 
		sketcher.rectMode(sketcher.CENTER);
		sketcher.fill(255,255,255);
		sketcher.rect(x_pos, y_pos, side, side);
	}
	
	public void changePos() {
		int[] pos = SpawnPoint.getRandomPos(sketcher);
		x_pos = pos[0];
		y_pos = pos[1];
			
	}
	
	
	public void kill() {
		isAlive = false;
	}
	
	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		x_pos = Integer.parseInt(vals[2]);
		y_pos = Integer.parseInt(vals[3]);
		boolean isAlive = Boolean.parseBoolean(vals[4]);
		this.isAlive = isAlive;
	}
	
	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "ENEMY~" + GAME_OBJECT_ID + "~" + x_pos + "~" + y_pos + "~" + isAlive;
	}

}
