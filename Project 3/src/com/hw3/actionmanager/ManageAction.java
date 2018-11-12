package com.hw3.actionmanager;

import java.util.concurrent.ConcurrentMap;

import com.hw3.eventManager.Event;
import com.hw3.eventManager.types.UserInputEvent;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class ManageAction {

	public static void manage(int action, Clock clock, ConcurrentMap<String, GameObject> scene,
			ConcurrentMap<String, Player> playerMap) {
		// Pausing
		if (action == 1)
			clock.pause();
		// recording
		else if (action == 2)
			Record.record(clock.getSystemTime(), playerMap.values(), scene.values());
		// set replay speed
		else if (action >= 3 && action <= 5)
			Replay.startReplay(clock, action, scene.values(), playerMap.values());
		else if (action == 6)
			clock.unPause();
		else if (action == 7)
			Record.stopRecording(clock);
		
	}
	
	public static void addInputEvent(int x, int y, Player player,Clock clock) {
			// if recording is on, we have to add this event
			// and if the replay is off we add this event to the replay
			if (Record.isRecording() && !Replay.isReplaying()) {
				// This action creates a user input type event
				Event userInput = new UserInputEvent(x, y, player, clock.getSystemTime());
				Record.addEvent(userInput);
			}

	}

}
