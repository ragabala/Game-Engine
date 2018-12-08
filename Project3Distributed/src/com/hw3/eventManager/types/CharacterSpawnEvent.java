package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;
import com.hw3.sketcher.Player;
import com.hw3.sketcher.SpawnPoint;

public class CharacterSpawnEvent extends Event {

	public Player spawnObject;
	public SpawnPoint spawnPoint;
	public CharacterSpawnEvent(Player spawnObject, SpawnPoint spawnPoint) {
		// TODO Auto-generated constructor stub
		super(Event.Type.CHARACTER_SPAWN);
		this.spawnObject = spawnObject;
		this.spawnPoint = spawnPoint;
		spawnObject.setAlive();		
	}

}
