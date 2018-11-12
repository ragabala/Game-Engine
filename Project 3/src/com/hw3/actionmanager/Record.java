package com.hw3.actionmanager;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.hw3.eventManager.Event;
import com.hw3.eventManager.types.StartRecordingEvent;
import com.hw3.eventManager.types.StopRecordingEvent;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class Record {

	public static boolean recordingOn;
	public static long recordingStartTime;
	public static Queue<Event> events = new LinkedList<>();
	
	public static void addEvent(Event event) {
		events.add(event);
	}

	public static void record(long recordingStartTimeVal, Collection<Player> players, Collection<GameObject> scene) {
		recordingStartTime = recordingStartTimeVal;
		System.out.println("Recording started at : "+recordingStartTimeVal);
		new StartRecordingEvent(scene, players, recordingStartTimeVal);
		recordingOn = true;
	}

	public static void stopRecording(Clock clock) {
		recordingOn = false;
		Event stop = new StopRecordingEvent(clock.getSystemTime());
		// setting to default tic
		clock.setTic(Clock.DEFAULT_TIC_SIZE);
		events.add(stop);
	}

	public static boolean isRecording() {
		return recordingOn;
	}

}
