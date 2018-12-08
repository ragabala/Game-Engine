package com.hw4.sketcher;

import processing.core.PApplet;

public class Bullet extends GameObject implements Movable,Renderable {

	private static final long serialVersionUID = 1L;
	private boolean byPlayer;
	private int speed = 8;
	private Player player;
	boolean active = true;
	
	public Bullet(PApplet sketcher, int x_pos, int y_pos, boolean byPlayer) {
		// TODO Auto-generated constructor stub
		this.byPlayer = byPlayer;
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.length = 5;
		this.breadth = 10;
		this.sketcher = sketcher;
	}
	
	// The bullets can be generated by both the enemies or the player
	// In case the bullets are generated by the player it has to go up
	// Else if by the enemies it has to come down
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
		if(!active) return;
		
		sketcher.rectMode(sketcher.CENTER);
		sketcher.noStroke();
		
        if(byPlayer()) {
            sketcher.fill(255,255,255);
        } else {
            sketcher.fill(255,0,0);
        }
        sketcher.rect(x_pos, y_pos, length, breadth);
	}

	@Override
	public void step(int x_dir, int y_dir) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPlayerRef(Player player)
	{
		this.player = player;
	}
	
	public void increasePlayerScore()
	{
		if(player != null)
			player.increaseScore();
			
	}
	
	public void deactivate() {active = false;}

	@Override
	public void step() {
		if(!active) return;
		// TODO Auto-generated method stub
		if(byPlayer()) // it has to go up if its from player
			y_pos -= speed;
		else
			y_pos += speed / 3; // Let it go at a slower pace downwards from enemy
	}

	
	public boolean isOutOfBounds(int val) {
		if(y_pos >= sketcher.height+this.breadth + val || y_pos <= -this.breadth - val)
			return true;
		return false;
	}
	
	public boolean byPlayer() {
		return byPlayer;
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
		return "BULLET~" + GAME_OBJECT_ID + "~" + x_pos + "~" + y_pos + "~" + byPlayer+"~"+active;
	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		x_pos = Integer.parseInt(vals[2]);
		y_pos = Integer.parseInt(vals[3]);
		active = Boolean.parseBoolean(vals[5]);
	}
	

}
