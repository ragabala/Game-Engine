package com.hw2.sketcher;

import processing.core.PApplet;

public class Platform extends GameObject implements Movable,Renderable{
	
	int length, breadth;
	boolean isMovable;
	int x_speed, y_speed;
	public Platform(PApplet sketcher, int x_pos, int y_pos, int length, int breadth) {
		// TODO Auto-generated constructor stub
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.length = length;
		this.breadth = breadth;
		this.sketcher = sketcher;
	}
	
	
	public void setMotion() {
		
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
	public void render() {
		// TODO Auto-generated method stub
		sketcher.fill(255);
		sketcher.rect(x_pos, y_pos, length, breadth);
		
	}
	
}
