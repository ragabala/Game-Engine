package com.hw2.sketcher;

import processing.core.PApplet;

public class Game extends PApplet{
	
	Platform[] platforms;
	Player player;
	Floor floor;
	int noOfPlatforms = 5;
	int[] keys = {0,0};
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
		
		platforms[0].setMotion(1, 0);
		platforms[noOfPlatforms/2].setMotion(0, 1);
		
		floor = new Floor(this);
		int playerPos = (int)random(10, width);
		int playerDiameter = 16;
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
			platform.step();
			player.isConnected(platform);
		}
		floor.render();
		player.render();
		player.step(keys[0],keys[1]);
		player.isConnected(floor);
	}
	
	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		if(keyCode == RIGHT)
			keys[0] = 1;
		if(keyCode == LEFT)
			keys[0] = -1;
		if(keyCode == 10)
			player.reset();
		if(keyCode == 32)
			keys[1]= 1;
	}
	
	
	
	@Override
	public void keyReleased() {
		// TODO Auto-generated method stub
		if(keyCode == RIGHT)
			keys[0] = 0;
		if(keyCode == LEFT)
			keys[0] = 0;
		if(keyCode == 32)
			keys[1] = 0;
	}
	
	public static void main(String[] args) {	
		String[] processingArgs = { "MySketch" };
		Game mySketch = new Game();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
