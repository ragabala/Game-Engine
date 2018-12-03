package com.hw3.eventManager.types;


import java.util.Collection;

import com.hw3.actionmanager.Record;
import com.hw3.eventManager.Event;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Movable;
import com.hw3.sketcher.Player;

public class StartRecordingEvent extends Event{
	


	public StartRecordingEvent(Collection<GameObject> scene, Collection<Player> players) {
		super(Event.Type.START_RECORDING);
		// TODO Auto-generated constructor stub
		for (Player player : players) {
			Record.positionMap.put(player.GAME_OBJECT_ID, player.toGameObjectString());
			Record.relationMap.put(player.GAME_OBJECT_ID, player.connectedObject);
		}
		for (GameObject gameObject : scene) {
			if(gameObject instanceof Movable)
				Record.positionMap.put(gameObject.GAME_OBJECT_ID, gameObject.toGameObjectString());
		}
		
		
	}

}
