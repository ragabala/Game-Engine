package com.hw3.sketcher;

import java.io.Serializable;

import processing.core.PApplet;

/**
 * @author ragbalak This class is used for setting the r,g,b for an object. This
 *         will be used for filling the color in the PApplet.
 */
public class Color implements Serializable {
	private static final long serialVersionUID = 1L;
	public int r, g, b;

	public Color(int r, int g, int b) {
		// TODO Auto-generated constructor stub
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public static Color getRandomColor() {
		PApplet sketcher = new PApplet();
		return new Color((int) sketcher.random(255), (int) sketcher.random(255), (int) sketcher.random(255));
	}
}
