package com.hw3.actionmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Movable;
import com.hw3.sketcher.Player;

public class Replay {

	// This Queue will store all the intrested events whenever
	// The Replay button is pressed
	// And will stop holding the eventsclock when the stop button is clicked

	// if this is on, then all the actions are recorded
	// Since Replays, Pause and unPause happens at server level
	// It is agnostic of which client has given the request
	// This is a design choice

	public static boolean replayingOn;
	public static Map<String, String> positionMap = new HashMap<>();
	public static Map<String, GameObject> relationMap = new HashMap<>();


	// Map between GUID and the position of the Objects
	// This is used for setting up the initial Positions during replay
	

	public static boolean isReplaying() {
		return replayingOn;
	}

	// this should be called when all the events are removed
	public static void stopReplay(Collection<GameObject> scene, Collection<Player> players) {
		if(!replayingOn)
			return;
		revertBackPositions(scene, players);
		replayingOn = false;
		Clock.setTic(Clock.DEFAULT_TIC_SIZE);
	}

	
	public static void revertBackPositions(Collection<GameObject> scene, Collection<Player> players) {
		for (Player player : players) {
			if(positionMap.containsKey(player.GAME_OBJECT_ID)) {
				player.updateGameObject(positionMap.get(player.GAME_OBJECT_ID).split("~"));
				player.connectedObject = relationMap.get(player.GAME_OBJECT_ID);
			}
		}
		for (GameObject gameObject : scene) {
			if(positionMap.containsKey(gameObject.GAME_OBJECT_ID))
			{	
				gameObject.updateGameObject(positionMap.get(gameObject.GAME_OBJECT_ID).split("~"));
			}
		}
	}
	
	public static void saveBeforeReplayPositions( Collection<GameObject> scene, Collection<Player> players) {
		for (Player player : players) {
			positionMap.put(player.GAME_OBJECT_ID, player.toGameObjectString());
			relationMap.put(player.GAME_OBJECT_ID, player.connectedObject);
		}
		for (GameObject gameObject : scene) {
			if(gameObject instanceof Movable)
				positionMap.put(gameObject.GAME_OBJECT_ID, gameObject.toGameObjectString());
		}
		
	}
	
	public static void startReplay(int action, Collection<GameObject> scene, Collection<Player> players) {
		if(replayingOn)
			return;
		replayingOn = true;
		saveBeforeReplayPositions(scene, players);
		if (action == 3) {
			Clock.setTic(Clock.DEFAULT_TIC_SIZE / 2);
		} else if (action == 4) {
			Clock.setTic(Clock.DEFAULT_TIC_SIZE);
		} else if (action == 5) {
			Clock.setTic(Clock.DEFAULT_TIC_SIZE * 2);
		}
		
		// Set the initial position of each and every player and scene objects when the replay started
		for (Player player : players) {
			if(Record.positionMap.containsKey(player.GAME_OBJECT_ID)) {
				player.updateGameObject(Record.positionMap.get(player.GAME_OBJECT_ID).split("~"));
				player.connectedObject = Record.relationMap.get(player.GAME_OBJECT_ID);
			}
		}
		for (GameObject gameObject : scene) {
			if(Record.positionMap.containsKey(gameObject.GAME_OBJECT_ID))
			{	
				gameObject.updateGameObject(Record.positionMap.get(gameObject.GAME_OBJECT_ID).split("~"));
			}
		}
		
	}

}
