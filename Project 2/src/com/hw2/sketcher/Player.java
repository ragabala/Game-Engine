package com.hw2.sketcher;

import processing.core.PApplet;
import processing.core.PConstants;

public class Player extends GameObject implements Movable,Renderable{
	
	int diameter;
	public Player(PApplet sketcher, int x, int y , int diameter) {
		// TODO Auto-generated constructor stub
		this.x_pos = x;
		this.y_pos = y;
		this.diameter = diameter;
		this.sketcher = sketcher;
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		sketcher.fill(255,0,0);
		sketcher.ellipseMode(PConstants.CENTER);
		sketcher.ellipse(x_pos, y_pos, diameter, diameter);
		
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
	

	
	
}
