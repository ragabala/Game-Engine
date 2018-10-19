package com.hw2.sketcher;

import processing.core.PApplet;

public class Floor extends GameObject implements Renderable{
	public Floor(PApplet sketcher) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.x_pos = 0;
		this.y_pos = (int) (sketcher.height * 0.95);
		this.length = sketcher.width;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		sketcher.stroke(255);
		sketcher.line(x_pos, y_pos, length, y_pos);
	}
	
	
	 
}
