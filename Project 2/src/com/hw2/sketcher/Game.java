package com.hw2.sketcher;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class Game extends PApplet {
	List<GameObject> gameObjects;
	Player player;
	int playerDiameter = 30;

	int[] keys = { 0, 0 };

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		gameObjects = new ArrayList<>();

		System.out.println(gameObjects.size());
		player = new Player(this, (int) random(10, width),(int) random(10, height) , playerDiameter);
		gameObjects.add(player);
	}

	@Override
	public void settings() {
		// TODO Auto-generated method stub
		size(800, 800);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		background(0);
		for (GameObject gameObject : gameObjects) {
			
			if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
			if (gameObject instanceof Movable) {
				((Movable) gameObject).step(keys[0], keys[1]);
			}
			if(gameObject!=player)
				player.isConnected(gameObject);
		}
	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		if (keyCode == RIGHT)
			keys[0] = 1;
		if (keyCode == LEFT)
			keys[0] = -1;
		if (keyCode == 32)
			keys[1] = 1;
	}

	@Override
	public void keyReleased() {
		// TODO Auto-generated method stub
		if (keyCode == RIGHT)
			keys[0] = 0;
		if (keyCode == LEFT)
			keys[0] = 0;
		if (keyCode == 32)
			keys[1] = 0;
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Game mySketch = new Game();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
