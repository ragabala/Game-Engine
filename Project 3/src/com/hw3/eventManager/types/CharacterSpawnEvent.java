package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;
import com.hw3.sketcher.GameObject;

public class CharacterSpawnEvent extends Event {

	GameObject spawnObject;
	GameObject spawnPoint;
	public CharacterSpawnEvent(GameObject spawnObject, GameObject spawnPoint, long timestamp) {
		// TODO Auto-generated constructor stub
		super(Event.Type.CHARACTER_SPAWN, timestamp);
		this.spawnObject = spawnObject;
		this.spawnPoint = spawnPoint;
	}

}
