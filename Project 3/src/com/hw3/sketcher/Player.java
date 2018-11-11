package com.hw3.sketcher;

import java.io.Serializable;
import java.util.Collection;

import com.hw3.actionmanager.Clock;
import com.hw3.actionmanager.Record;
import com.hw3.actionmanager.Replay;
import com.hw3.eventManager.Event;
import com.hw3.eventManager.HandleEventDispatch;
import com.hw3.eventManager.types.CharacterCollisionEvent;
import com.hw3.eventManager.types.UserInputEvent;

import processing.core.PApplet;
import processing.core.PConstants;

public class Player extends GameObject implements Movable, Renderable, Serializable {
	private static final long serialVersionUID = 1L;
	public float[] speed = { 10, 0 };
	int diameter;
	float gravity = 0.2f;
	Color color;
	public transient GameObject connectedObject = null;
	int move_x, move_y;
	public int dir_x, dir_y;
	boolean isAlive;
	public int prev_x,prev_y;
	Clock clock;
	HandleEventDispatch dispatchHandler;

	public Player(PApplet sketcher, int x, int y, int diameter, Color color, Clock clock) {
		this.x_pos = x;
		this.y_pos = y;
		this.diameter = diameter;
		this.sketcher = sketcher;
		speed[1] = 10; // this makes the player to reach the ground initially
		this.color = color;
		this.isAlive = true;
		this.clock = clock;
		dispatchHandler = new HandleEventDispatch();
	}

	
	
	
	public void setMovement(int x, int y) {
		move_x = x;
		move_y = y;
	}
	
	public void setDir(int x, int y) {
		dir_x = x;
		dir_y = y;
	}
	

	@Override
	public void render() {
		// TODO Auto-generated method stub
		if (!isAlive)
			return;
		sketcher.fill(color.r, color.g, color.b);
		sketcher.ellipseMode(PConstants.CENTER);
		sketcher.ellipse(x_pos, y_pos, diameter, diameter);

	}

	@Override
	public void step(int x_dir, int y_dir) {
		// TODO Auto-generated method stub
		// Adding an event only when pressed for the first time
		// If There is a change in the direction
		// Then it should be recorded as a event
		if (x_dir != prev_x || y_dir != prev_y) {
			// if recording is on, we have to add this event
			// and if the replay is off we add this event to the replay
			if (Record.isRecording() && !Replay.isReplaying()) {
				// This action creates a user input type event
				Event userInput = new UserInputEvent(x_dir, y_dir, this, clock.getSystemTime());
				Replay.addEvent(userInput);
			}
		}

		x_pos += x_dir * speed[0];
		// If the object is free falling
		if (connectedObject == null) {
			y_pos += speed[1];
			if (speed[1] != 0)
				speed[1] += gravity;
		} else {
			y_pos = connectedObject.y_pos - diameter / 2;
		}
		
		// If the object Jumps when connected
		if (y_dir != 0 && connectedObject != null) {
			// If jumped give it an initial upward speed
			speed[1] = -10;
			connectedObject = null;
		}
		// If We move horizontally and trip over the edge
		if (x_dir != 0 && connectedObject != null) {
			if (!isConnected(connectedObject)) {
				connectedObject = null;
				speed[1] = 5;
			}
		}
		wrap();		
		prev_x = x_dir;
		prev_y = y_dir;
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

	public boolean isConnected(GameObject gameObject) {
		// if object is connected
		if (Math.abs(y_pos + diameter / 2 - gameObject.y_pos) <= speed[1] + 0.1 && x_pos >= gameObject.x_pos
				&& x_pos <= gameObject.x_pos + gameObject.length) {
			speed[1] = 0;
			if (gameObject instanceof Movable)
				x_pos += (((Movable) gameObject).getSpeed())[0];
			if (gameObject instanceof DeathZone) {
				teleport();
				return false;
			}

			// The below statement makes sure the event gets created only when the object
			// makes contact
			if (connectedObject != gameObject) {
				if (!Replay.isReplaying()) {
					Event userInput = new CharacterCollisionEvent(this, gameObject, clock.getSystemTime());
					Replay.addEvent(userInput);
				}
			}
			connectedObject = gameObject;
			return true;
		}
		return false;
	}

	public void resolveCollision(Collection<GameObject> gameObjects) {
		for (GameObject gameObject : gameObjects) {
			isConnected(gameObject);
		}
	}

	public String getConnectedObjectID() {
		if (connectedObject == null)
			return null;
		return connectedObject.GAME_OBJECT_ID;
	}

	public void setConnectedObject(GameObject object) {
		connectedObject = object;
	}

	@Override
	public float[] getSpeed() {
		// TODO Auto-generated method stub
		return speed;
	}

	public void kill() {
		isAlive = false;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public static int[] spawnPlayerPosition(PApplet sketcher) {
		int w = (int) sketcher.random((float) (sketcher.width * 0.1), (float) (sketcher.width * 0.9));
		int h = (int) sketcher.random((float) (sketcher.height * 0.4), (float) (sketcher.height * 0.9));
		return new int[] { w, h };
	}

	public void teleport() {
		int[] pos = spawnPlayerPosition(sketcher);
		speed[1] = 10;
		x_pos = pos[0];
		y_pos = pos[1];
	}

	@Override
	public String toGameObjectString() {
		// TODO Auto-generated method stub
		return "PLAYER~" + GAME_OBJECT_ID + "~" + x_pos + "~" + y_pos + "~" + diameter + "~" + color.r + "~" + color.g
				+ "~" + color.b + "~" + isAlive;

	}

	@Override
	public void updateGameObject(String[] vals) {
		// TODO Auto-generated method stub
		x_pos = Integer.parseInt(vals[2]);
		y_pos = Integer.parseInt(vals[3]);
		isAlive = Boolean.parseBoolean(vals[8]);

	}

}