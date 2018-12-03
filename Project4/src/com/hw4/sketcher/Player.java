package com.hw4.sketcher;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;

public class Player extends GameObject implements Movable, Renderable, Serializable {
	private static final long serialVersionUID = 1L;
	public double[] speed = { 10, 0 };
	int side;
	Color color;
	int move_x, move_y;
	public int dir_x, dir_y;
	boolean isAlive;

	public Player(PApplet sketcher, int x, int y, int diameter, Color color) {
		this.x_pos = x;
		this.y_pos = y;
		this.side = diameter;
		this.sketcher = sketcher;
		speed[0] = 10;
		this.color = color;
		this.isAlive = true;
	}

	public void setMovement(int x, int y) {
		move_x = x;
		move_y = y;
	}


	public void setPos(int x, int y) {
		x_pos = x;
		y_pos = y;
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		if (!isAlive)
			return;
		sketcher.fill(color.r, color.g, color.b);
		sketcher.rectMode(PConstants.CENTER);
		sketcher.rect(x_pos, y_pos, side, side);

	}

	@Override
	public void step(int x_dir, int y_dir) {
		// TODO Auto-generated method stub
		// This current Player doesn't use y_dir
		// Since the player stays in the ground
		x_pos += x_dir * speed[0];
		wrap();
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub
		step(move_x, move_y);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public GameObject shoot() {
		return new Bullet(this.sketcher, this.x_pos, this.y_pos, true);
	}

	public void isHit(Collection<GameObject> gameObjects) {
		// if object is connected

		for (GameObject gameObject : gameObjects) {
			// If the colliding object is a bullet and it is not a bullet by the client,
			// Kill the client
			if (gameObject instanceof Bullet && !((Bullet) gameObject).byPlayer())
				if (PApplet.dist(x_pos, y_pos, gameObject.x_pos, gameObject.y_pos) < 1) {
					isAlive = false;
				}
		}
	}

	@Override
	public double[] getSpeed() {
		// TODO Auto-generated method stub
		return speed;
	}

	public void kill() {
		isAlive = false;
	}

	public void setAlive() {
		isAlive = true;
	}

	public boolean isAlive() {
		return isAlive;
	}

	// public void teleport() {
	// int[] pos = spawnPlayerPosition(sketcher);
	// speed[1] = 10;
	// x_pos = pos[0];
	// y_pos = pos[1];
	// }

	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "PLAYER~" + GAME_OBJECT_ID + "~" + x_pos + "~" + y_pos + "~" + side + "~" + color.r + "~" + color.g + "~"
				+ color.b + "~" + isAlive;

	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		x_pos = Integer.parseInt(vals[2]);
		y_pos = Integer.parseInt(vals[3]);
		isAlive = Boolean.parseBoolean(vals[8]);
	}

}