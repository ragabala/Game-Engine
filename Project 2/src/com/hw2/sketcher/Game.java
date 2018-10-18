package com.hw2.sketcher;

import processing.core.PApplet;

public class Game extends PApplet{
	
	Platform[] platforms;
	Player player;
	Floor floor;
	int noOfPlatforms = 5;
	@Override
	public void setup() {
		// TODO Auto-generated method stub

		System.out.println("hello world");
		platforms = new Platform[noOfPlatforms];
		float _temp_x = (float) (width*0.9)/noOfPlatforms;
		float _temp_y = (float) (height*0.9)/noOfPlatforms;
		// Let us construct 5 platforms of different colors
		// three platforms has to move and two are static
		for (int i = 0; i < noOfPlatforms; i++) {

			int x_pos = (int) random(_temp_x*i ,_temp_x*(i+1));
			int y_pos = (int) random(_temp_y*i,_temp_y*(i+1));
			platforms[i] = new Platform(this, x_pos, y_pos, 60, 10);
		}
		
		floor = new Floor(this);
		int playerPos = (int)random(10, width);
		int playerDiameter = 15;
		player = new Player(this, playerPos , floor.y_pos - (int)playerDiameter/2, playerDiameter);
		
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
		for (Platform platform : platforms) {
			platform.render();
		}
		floor.render();
		player.render();

	}
	
	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) {	
		String[] processingArgs = { "MySketch" };
		Game mySketch = new Game();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
