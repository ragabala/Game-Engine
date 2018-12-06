package com.hw4.sketcher;

import processing.core.PApplet;

public class SpawnPoint extends GameObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// This is used for aligning snake and the food in the same grid
	private static int gridVal = 20;
	public SpawnPoint(PApplet sketcher, Player player) {
		// TODO Auto-generated constructor stub
			int[] pos = getRandomPos(sketcher);
			x_pos = pos[0];
			this.y_pos = pos[1];
			player.x_pos = x_pos;
			player.y_pos = y_pos;
	}
	
	public static int[] getRandomPos(PApplet sketcher) {
		int[] temp = new int[2];
		temp[0] =  PApplet.floor(sketcher.random((float) (sketcher.width * 0.1), (float) (sketcher.width * 0.9)) / gridVal) * gridVal ;
		temp[1] =  PApplet.floor(sketcher.random((float) (sketcher.height * 0.1), (float) (sketcher.height * 0.9)) / gridVal) * gridVal ;
	return temp;
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
