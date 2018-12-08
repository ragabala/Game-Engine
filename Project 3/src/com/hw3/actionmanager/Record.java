package com.hw3.actionmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.hw3.eventManager.Event;
import com.hw3.eventManager.EventManager;
import com.hw3.eventManager.types.StartRecordingEvent;
import com.hw3.eventManager.types.StopRecordingEvent;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;

public class Record {

	public static boolean recordingOn;
	public static long recordingStartTime;
	public static Queue<Event> events = new LinkedList<>();
	public static Map<String, String> positionMap = new HashMap<>();
	public static Map<String, GameObject> relationMap = new HashMap<>();
	
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
		EventManager.register(Event.Type.STOP_RECORDING);
	}

	public static boolean isRecording() {
		return recordingOn;
	}

}
