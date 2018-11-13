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

	public static void record(Collection<Player> players, Collection<GameObject> scene) {
		if(recordingOn)
			return;
		new StartRecordingEvent(scene, players);
		recordingOn = true;
		recordingStartTime = Clock.getSystemTime();
		System.out.println("Recording started at : "+recordingStartTime);
	}

	public static void stopRecording() {
		if(!recordingOn)
			return;
		Event stop = new StopRecordingEvent();
		recordingOn = false;
		// setting to default tic
		Clock.setTic(Clock.DEFAULT_TIC_SIZE);
		events.add(stop);
	}

	public static boolean isRecording() {
		return recordingOn;
	}

}
