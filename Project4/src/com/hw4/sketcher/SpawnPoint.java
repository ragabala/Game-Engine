package com.hw4.sketcher;

import processing.core.PApplet;

public class SpawnPoint extends GameObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpawnPoint(PApplet sketcher, Player player) {
		// TODO Auto-generated constructor stub
			x_pos = (int) sketcher.random((float) (sketcher.width * 0.1), (float) (sketcher.width * 0.9));
			this.y_pos = (int) (sketcher.height * 0.95) - player.side/2;
			player.x_pos = x_pos;
			player.y_pos = y_pos;
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
