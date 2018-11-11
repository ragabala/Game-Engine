package com.hw3.actionmanager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.hw3.eventManager.Event;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class Replay {

	// This Queue will store all the intrested events whenever
	// The Replay button is pressed
	// And will stop holding the events when the stop button is clicked

	// if this is on, then all the actions are recorded
	// Since Replays, Pause and unPause happens at server level
	// It is agnostic of which client has given the request
	// This is a design choice

	public static boolean replayingOn;

	public static Queue<Event> events = new LinkedList<>();

	// Map between GUID and the position of the Objects
	// This is used for setting up the initial Positions during replay
	public static Map<String, String> positionMap;

	public static void addEvent(Event event) {
		events.add(event);
	}

	public static boolean isReplaying() {
		return replayingOn;
	}

	// this should be called when all the events are removed
	public static void stopReplay() {
		replayingOn = false;
	}

	public static void startReplay(Clock clock, int action, Collection<GameObject> scene, Collection<Player> players) {
		replayingOn = true;
		if (action == 3) {
			clock.setTic(30);
		} else if (action == 4) {
			clock.setTic(60);
		} else if (action == 5) {
			clock.setTic(90);
		}
		clock.setCurrentTime();
		
		// Set the initial position of each and every player and scene objects when the replay started
		for (Player player : players) {
			if(positionMap.containsKey(player.GAME_OBJECT_ID))
				player.updateGameObject(positionMap.get(player.GAME_OBJECT_ID).split("~"));
		}
		for (GameObject gameObject : scene) {
			if(positionMap.containsKey(gameObject.GAME_OBJECT_ID))
				gameObject.updateGameObject(positionMap.get(gameObject.GAME_OBJECT_ID).split("~"));
		}
		
		
	}

}