package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class CharacterCollisionEvent extends Event {

	// This Event can have two intrested objects
	// One is the Game objects that collides
	// Other one is the Game object that is collided with
	public Player collider;
	public GameObject collided;

	public CharacterCollisionEvent(Player collider, GameObject collided) {
		// TODO Auto-generated constructor stub
		super(Event.Type.CHARACTER_COLLSION);
		this.collider = collider;
		this.collided = collided;
	}

}
