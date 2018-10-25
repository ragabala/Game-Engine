package com.hw2.sketcher;

// This Class wont be rendered on the screen
// Since it doesn't implement renderable
public class DeathZone extends GameObject {

	private static final long serialVersionUID = 1L;

	public DeathZone(int height,int width) {
		// TODO Auto-generated constructor stub
		this.x_pos = 0;
		this.y_pos = 0;
		this.length = width;
		this.breadth = 10;
	}
	
	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		
	}

	
}
