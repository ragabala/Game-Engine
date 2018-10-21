package com.hw2.sketcher;

import processing.core.PApplet;

public class Floor extends GameObject implements Renderable{
	private static final long serialVersionUID = 8753250229013264818L;

	public Floor(PApplet sketcher,int height, int width) {
		// TODO Auto-generated constructor stub
		this.sketcher = sketcher;
		this.x_pos = 0;
		this.y_pos = (int) (height * 0.95);
		this.length = width;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		sketcher.stroke(255);
		sketcher.line(x_pos, y_pos, length, y_pos);
	}
	
	
	 
}
