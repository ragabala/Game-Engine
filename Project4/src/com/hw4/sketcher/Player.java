package com.hw4.sketcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
	List<Snake> snakeBody;
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
		snakeBody = new ArrayList<>();
		addBodyPart(x, y);
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
		for (Snake snake : snakeBody) {
			snake.render();
		}
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
		Snake next = snakeBody.get(0);
		for (int i = 0; i < snakeBody.size() - 1; i++) {
			Snake prev = snakeBody.get(i);
			next = snakeBody.get(i+1);
			prev.x_pos = next.x_pos; prev.y_pos = next.y_pos;
		}
		next.x_pos = x_pos;
		next.y_pos = y_pos;
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
				List<Snake> temp = ((Player) gameObject).snakeBody;
				for (int i=0;i<temp.size()-1;i++) {
					if (PApplet.dist(x_pos, y_pos, temp.get(i).x_pos, temp.get(i).y_pos) <= 0.5) {
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
		addBodyPart(x_pos, y_pos);
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
	
	public  void addBodyPart(int x, int y) {
		// TODO Auto-generated method stub
		snakeBody.add(new Snake(sketcher, x, y,side, color));
	}
	
	public void removeBodyPart() {
		snakeBody.remove(0);
	}

	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (Snake snake : snakeBody) {
			sb.append(snake.toGameObjectString()+"~~");
		}
		return sb.toString();
	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		// This won't be updated since the Client only gets the Snake String
	}

}
