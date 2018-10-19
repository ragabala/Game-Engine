package com.hw2.sketcher;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class Game extends PApplet {
	List<GameObject> gameObjects;
	Player player;
	int playerDiameter = 30;
	int noOfPlatforms = 5;
	int[] keys = { 0, 0 };

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		gameObjects = new ArrayList<>();

		System.out.println("hello world");
		float _temp_x = (float) (width * 0.9) / noOfPlatforms;
		float _temp_y = (float) (height * 0.9) / noOfPlatforms;
		// Let us construct 5 platforms of different colors
		// three platforms has to move and two are static
		// Adding Platforms
		//randomSeed(1);
		for (int i = 0; i < noOfPlatforms; i++) {
			int x_pos = (int) random(_temp_x * i, _temp_x * (i + 1));
			int y_pos = (int) random(_temp_y * i, _temp_y * (i + 1));
			Platform temp = new Platform(this, x_pos, y_pos, 60, 10);
			temp.changeColor();
			gameObjects.add(temp);
			if (i == 0)
				temp.setMotion(1, 0);
			if (i == noOfPlatforms / 2)
				temp.setMotion(0, 1);
		}

		Floor temp = new Floor(this);
		gameObjects.add(new Floor(this));
		System.out.println(gameObjects.size());
		int playerPos = (int) random(10, width);
		player = new Player(this, playerPos, temp.y_pos - (int) playerDiameter / 2, playerDiameter);
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
