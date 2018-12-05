package com.hw4.sketcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import processing.core.PApplet;

public abstract class GameObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String GAME_OBJECT_ID = UUID.randomUUID().toString();
	public int x_pos, y_pos, length, breadth;
	transient PApplet sketcher;


	public void wrap() {
		if (x_pos > sketcher.width)
			x_pos = 0;
		if (x_pos < 0)
			x_pos = sketcher.width;
	}

	public void setSketcher(PApplet sketcher) {
		this.sketcher = sketcher;
	}

	public static GameObject parseGameString(String[] vals) {

		if (vals[0].equals("PLAYER")) {
			int x_pos = Integer.parseInt(vals[2]);
			int y_pos = Integer.parseInt(vals[3]);
			int diameter = Integer.parseInt(vals[4]);
			int r = Integer.parseInt(vals[5]);
			int g = Integer.parseInt(vals[6]);
			int b = Integer.parseInt(vals[7]);
			return new Player(null, x_pos, y_pos, diameter, new Color(r, g, b));

		}  
		
		else if (vals[0].equals("SNAKE")) {
			int x_pos = Integer.parseInt(vals[2]);
			int y_pos = Integer.parseInt(vals[3]);
			int diameter = Integer.parseInt(vals[4]);
			int r = Integer.parseInt(vals[5]);
			int g = Integer.parseInt(vals[6]);
			int b = Integer.parseInt(vals[7]);
			return new Snake(null, x_pos, y_pos, diameter, new Color(r, g, b));
		} 
		
		
		
		else if (vals[0].equals("ENEMY")) {
			int x_pos = Integer.parseInt(vals[2]);
			int y_pos = Integer.parseInt(vals[3]);
			return new Enemy(null, x_pos, y_pos);
		}
		
		
		else if (vals[0].equals("SCORER")) {
			int hits = Integer.parseInt(vals[2]);
			int score = Integer.parseInt(vals[3]);
			Scorer s =  new Scorer();
			s.updateScore(hits, score);
			return s;
		}
		
		

		return null;
	}

	public abstract String toGameObjectString();
	public abstract void updateGameObject(String[] vals);

}
