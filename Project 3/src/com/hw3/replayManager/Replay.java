package com.hw3.replayManager;

import java.util.LinkedList;
import java.util.Queue;

import com.hw3.eventManager.Event;

public class Replay {
	
	// This Queue will store all the intrested events whenever 
	// The Replay button is pressed 
	// And will stop holding the events when the stop button is clicked
	public static Queue<Event> events = new LinkedList<>();
	
	public static void addEvent(Event event) {
		events.add(event);
	}
	
}
