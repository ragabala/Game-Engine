package com.hw3.actionmanager;

import java.util.concurrent.ConcurrentMap;

import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class ManageAction {

	public static void manage(int action, Clock clock, ConcurrentMap<String, GameObject> scene,
			ConcurrentMap<String, Player> playerMap) {
		// Pausing
		if (action == 1)
			if (clock.isPaused())
				clock.unPause();
			else
				clock.pause();

		// recording
		else if (action == 2)
			if (Record.isRecording()) {
				Record.stopRecording(clock);
			} else {
				Record.record(clock.getSystemTime(), playerMap.values(), scene.values());
			}
		// set replay speed
		else if (action >= 3 && action <= 5) {
			Replay.startReplay(clock, action, scene.values(), playerMap.values());
		}

	}

}
