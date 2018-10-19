package com.hw2.sketcher;

import java.util.UUID;

import processing.core.PApplet; 


public class GameObject {
	private static final UUID GAME_OBJECT_ID = UUID.randomUUID();
	int x_pos, y_pos, length;
	PApplet sketcher;
}
