package com.hw2.sketcher;

import java.io.Serializable;
import java.util.UUID;

import processing.core.PApplet;

public class GameObject implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String GAME_OBJECT_ID = UUID.randomUUID().toString();
	int x_pos, y_pos, length;
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
	
}
