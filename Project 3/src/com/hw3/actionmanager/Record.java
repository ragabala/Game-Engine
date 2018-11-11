package com.hw3.actionmanager;

import java.util.Collection;

import com.hw3.eventManager.Event;
import com.hw3.eventManager.types.StartRecordingEvent;
import com.hw3.eventManager.types.StopRecordingEvent;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class Record {

	public static boolean recordingOn;
	public static long recordingStartTime;

	public static void record(long recordingStartTimeVal, Collection<Player> players, Collection<GameObject> scene) {
		recordingOn = true;
		recordingStartTime = recordingStartTimeVal;
		new StartRecordingEvent(scene, players, recordingStartTimeVal);
	}

	public static void stopRecording(Clock clock) {
		recordingOn = false;
		Event stop = new StopRecordingEvent(clock.getSystemTime());
		// setting to default tic
		clock.setTic(60);
		Replay.events.add(stop);
	}

	public static boolean isRecording() {
		return recordingOn;
	}

}
