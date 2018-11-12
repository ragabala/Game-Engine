package com.hw3.actionmanager;

import java.util.concurrent.ConcurrentMap;

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
		;

	}

}
