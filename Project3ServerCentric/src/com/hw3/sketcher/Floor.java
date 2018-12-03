package com.hw3.sketcher;

import processing.core.PApplet;

public class Floor extends GameObject implements Renderable {
	private static final long serialVersionUID = 8753250229013264818L;
	int height;
	int width;

	public Floor(PApplet sketcher, int height, int width) {
		// TODO Auto-generated constructor stub
		this.height = height;
		this.width = width;
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


	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "FLOOR~"+
				GAME_OBJECT_ID+"~"+
				height+"~"+
				width;
	}
	
	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub		
	}

}
