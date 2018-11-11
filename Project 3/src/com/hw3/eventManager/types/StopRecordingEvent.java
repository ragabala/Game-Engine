package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;

public class StopRecordingEvent extends Event {

	public StopRecordingEvent(long timestamp) {
		super(Event.Type.STOP_RECORDING, timestamp);
		// TODO Auto-generated constructor stub
	}

}
