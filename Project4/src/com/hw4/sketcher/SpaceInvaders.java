package com.hw4.sketcher;

import java.util.concurrent.ConcurrentMap;

import processing.core.PApplet;

/*
 *  This class will be holding all the enemies grouped togethers
 *  This is a special class, and should be having reference to the scene
 *  to add its composition( Enemies) to the scene.
 *  
 *  This though this is a game object, this wont be added to the scene directly 
 *  but through its composition
 * */
public class SpaceInvaders extends GameObject implements Movable, Renderable {

	private static final long serialVersionUID = -7341685468214872858L;
	Enemy[][] enemies;
	int spacing = 30;
	int row, col;
	int x_speed = 3;
	int y_step = 20;
	float shoot_probability = 0.001f;
	ConcurrentMap<String, GameObject> scene;
	public static boolean alive = false;

	public SpaceInvaders(PApplet sketcher, ConcurrentMap<String, GameObject> scene, int x_pos, int y_pos, int row,
			int col) {
		// TODO Auto-generated constructor stub
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.row = row;
		this.col = col;
		this.sketcher = sketcher;
		enemies = new Enemy[row][col];
		this.scene = scene;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Enemy temp = new Enemy(sketcher, x_pos + j * spacing, y_pos + i * spacing);
				enemies[i][j] = temp;
			}
		}
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Enemy temp = enemies[i][j];
				temp.render();
			}
		}
	}

	@Override
	public void step(int x_dir, int y_dir) {
		// TODO Auto-generated method stub

	}

	public void addEnemiesToScene(StringBuffer buffer) {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Enemy temp = enemies[i][j];
				buffer.append(temp.toGameObjectString() + "~~");
			}
		}

	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		if(!alive) return;
		
		boolean changeDir = false;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Enemy temp = enemies[i][j];
				temp.step(x_speed, 0);
				temp.isHit(scene.values());
				if (temp.x_pos > 0.9 * sketcher.width || temp.x_pos < 0.1 * sketcher.width)
					changeDir = true;
				// Each step should be associated with a shoot function, which happens at a
				// probability
				if (temp.isAlive && sketcher.random(1) < shoot_probability) {
					GameObject enemyBullet = shoot(temp);
					scene.put(enemyBullet.GAME_OBJECT_ID, enemyBullet);
				}

			}
		}
		if (changeDir) {
			x_speed *= -1; // change the direction of the field
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					Enemy temp = enemies[i][j];
					temp.step(0, y_step);
				}
			}
		}

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public double[] getSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub

	}

	public GameObject shoot(Enemy enemy) {
		// TODO Auto-generated method stub
		return enemy.shoot();
	}

}
