package com.hw4.sketcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;

public class Player extends GameObject implements Movable, Renderable, Serializable {
	private static final long serialVersionUID = 1L;
	public int clientId; // used for thread communication
	public double[] speed = { 0, 0};
	int side;
	Color color;
	int move_x, move_y;
	public int dir_x, dir_y;
	boolean isAlive;
	List<int[]> body;
	StringBuilder bodyString;
	int framerate = 5;
	int iter = 0;

	int score = 0, hits = 0;

	public Player(PApplet sketcher, int x, int y, int diameter, Color color) {
		this.x_pos = x;
		this.y_pos = y;
		this.side = diameter;
		speed[0] = diameter;
		speed[1] = diameter;
		
		this.sketcher = sketcher;
		this.color = color;
		this.isAlive = true;
		body = new ArrayList<>();
		body.add(new int[] { x_pos, y_pos });
		
	}

	public void setMovement(int x, int y) {
		// Only change directions between horizontal and vertical
			
		if (x != 0 || y !=0) {
			move_x = x;
			move_y = y;
			}
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
		// The update should be happening at a slower pace
		if (iter++ < framerate)
			return;
		iter = 0;

		x_pos += x_dir * speed[0];
		y_pos += y_dir * speed[1];

		// In order to ensure the snake retains its shape on moving
		// We create a new position at the end
		// and remove the oldest position by one
		body.add(new int[] { x_pos, y_pos });
		body.remove(0);
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

	public void isHit(Collection<GameObject> gameObjects) {
		// if object is connected
		if (!isAlive)
			return;
		for (GameObject gameObject : gameObjects) {
			// If the colliding object is a bullet and it is not a bullet by the client,
			// Kill the client
			if (gameObject instanceof Enemy) {
				if (PApplet.dist(x_pos, y_pos, gameObject.x_pos, gameObject.y_pos) <= side) {
					grow();
					increaseScore();
				}
			}

			// The Snake should not hit any other snakes or itself too
			if (gameObject instanceof Player) {
				for (int[] bodyParts : ((Player) gameObject).body) {
					if (PApplet.dist(x_pos, y_pos, bodyParts[0], bodyParts[1]) <= side) {
						kill();
					}
				}
			}

			if (x_pos > sketcher.width || x_pos < 0 || y_pos > sketcher.height || y_pos < 0)
				kill();

		}
	}

	public void grow() {
		// This is same as step
		// But this doesn't remove the tail node
		x_pos += move_x * speed[0];
		y_pos += move_y * speed[1];
		body.add(new int[] { x_pos, y_pos });
	}

	public void updateScorer(Scorer scorer) {
		scorer.updateScore(hits, score);
	}

	@Override
	public double[] getSpeed() {
		// TODO Auto-generated method stub
		return speed;
	}

	public void kill() {
		isAlive = false;
	}

	public void increaseScore() {
		score++;
	}

	public void setAlive() {
		isAlive = true;
	}

	public boolean isAlive() {
		return isAlive;
	}
	
	public void addBodyPart(int x, int y) {
		// TODO Auto-generated method stub
		body.add(new int[] {x, y});
		bodyString.append(x+"~"+y+"~");
	}
	
	public void removeBodyPart() {
		body.remove(0);
		int index = bodyString.indexOf("~", bodyString.indexOf("~")+1);
		bodyString.replace(0, index, "");
	}

	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "PLAYER~" + GAME_OBJECT_ID + "~" +body.size()+"~"+ x_pos + "~" + y_pos + "~" + side + "~" + color.r + "~" + color.g + "~"
				+ color.b + "~" + isAlive;

	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		x_pos = Integer.parseInt(vals[2]);
		y_pos = Integer.parseInt(vals[3]);
		isAlive = Boolean.parseBoolean(vals[vals.length - 1]);
	}

}
