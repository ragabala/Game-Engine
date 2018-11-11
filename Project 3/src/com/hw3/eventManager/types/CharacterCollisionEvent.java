package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class CharacterCollisionEvent extends Event {

	// This Event can have two intrested objects
	// One is the Game objects that collides
	// Other one is the Game object that is collided with
	Player collider;
	GameObject collided;

	public CharacterCollisionEvent(Player collider, GameObject collided, long timestamp) {
		// TODO Auto-generated constructor stub
		super(Event.Type.CHARACTER_COLLSION, timestamp);
		this.collider = collider;
		this.collided = collided;
	}

}