package com.hw3.eventManager.types;


import java.util.Collection;

import com.hw3.actionmanager.Replay;
import com.hw3.eventManager.Event;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class StartRecordingEvent extends Event{

	public StartRecordingEvent(Collection<GameObject> scene, Collection<Player> players,long timestamp) {
		super(Event.Type.START_RECORDING, timestamp);
		// TODO Auto-generated constructor stub
		for (Player player : players) {
			Replay.positionMap.put(player.GAME_OBJECT_ID, player.toGameObjectString());
		}
		for (GameObject gameObject : scene) {
			Replay.positionMap.put(gameObject.GAME_OBJECT_ID, gameObject.toGameObjectString());
		}
		
		
	}

}
