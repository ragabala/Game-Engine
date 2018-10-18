package com.hw2.sketcher;

import java.io.Serializable;

/**
 * @author ragbalak
 * This class is used for setting the r,g,b for an object.
 * This will be used for filling the color in the PApplet.
 */
public class Color implements Serializable{
	private static final long serialVersionUID = 1L;
	public int r, g, b;

	public Color(int r, int g, int b) {
		// TODO Auto-generated constructor stub
		this.r = r;
		this.g = g;
		this.b = b;
	}
}
