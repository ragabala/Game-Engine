package com.hw3.actionmanager;

import java.util.concurrent.ConcurrentMap;

import com.hw3.eventManager.Event;
import com.hw3.eventManager.types.UserInputEvent;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class ManageAction {

	public static void manage(int action, ConcurrentMap<String, GameObject> scene,
			ConcurrentMap<String, Player> playerMap) {
		// Pausing
		if (action == 1)
			Clock.pause();
		// recording
		else if (action == 2)
			Record.record(playerMap.values(), scene.values());
		// set replay speed
		else if (action >= 3 && action <= 5)
			Replay.startReplay(action, scene.values(), playerMap.values());
		else if (action == 6)
			Clock.unPause();
		else if (action == 7)
			Record.stopRecording();
		
	}
	
	public static void addInputEvent(int x, int y, Player player) {
			// if recording is on, we have to add this event
			// and if the replay is off we add this event to the replay
			
				// This action creates a user input type event
			
		Event userInput = new UserInputEvent(x, y, player);
		if (Record.isRecording() && !Replay.isReplaying()) {
		Record.addEvent(userInput);
			}

	}

}
