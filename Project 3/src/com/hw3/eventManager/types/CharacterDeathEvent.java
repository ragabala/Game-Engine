package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;
import com.hw3.sketcher.GameObject;

public class CharacterDeathEvent extends Event {

	// This event involves a single character that is killed 
	// because of the death zone
	
	GameObject deadObject;
	
	public CharacterDeathEvent(GameObject deadObject) {
		// TODO Auto-generated constructor stub
		super(Event.Type.CHARACTER_DEATH);
		this.deadObject = deadObject;
	}

}
