package com.hw4.sketcher;

import java.util.Collection;

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
		// false signify that its a enemy bullet
		return  new Bullet(this.sketcher, this.x_pos, this.y_pos, false);
		
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

	
	public void isHit(Collection<GameObject> gameObjects) {
		// if object is connected

		for (GameObject gameObject : gameObjects) {
			// If the colliding object is a bullet and it is by the client,
			// Kill the enemy
			if (gameObject instanceof Bullet  && ((Bullet) gameObject).active && ((Bullet) gameObject).byPlayer())
				if (PApplet.dist(x_pos, y_pos, gameObject.x_pos, gameObject.y_pos) <= side) {
					kill();
					((Bullet) gameObject).deactivate(); 
					((Bullet) gameObject).increasePlayerScore();
				}
		}
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
