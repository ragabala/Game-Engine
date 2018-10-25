package com.hw2.sketcher;

import java.io.Serializable;
import java.util.UUID;

import processing.core.PApplet;

public abstract class GameObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String GAME_OBJECT_ID = UUID.randomUUID().toString();
	int x_pos, y_pos, length, breadth;
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

		} else if (vals[0].equals("PLATFORM")) {
			int x_pos = Integer.parseInt(vals[2]);
			int y_pos = Integer.parseInt(vals[3]);
			int length = Integer.parseInt(vals[4]);
			int breadth = Integer.parseInt(vals[5]);
			int r = Integer.parseInt(vals[6]);
			int g = Integer.parseInt(vals[7]);
			int b = Integer.parseInt(vals[8]);
			return new Platform(null, x_pos, y_pos, length, breadth, new Color(r, g, b));

		} else if (vals[0].equals("FLOOR")) {
			int height = Integer.parseInt(vals[2]);
			int width = Integer.parseInt(vals[3]);
			return new Floor(null, height, width);
		}

		return null;
	}

	public abstract String toGameObjectString();
	public abstract void updateGameObject(String[] vals);

}
