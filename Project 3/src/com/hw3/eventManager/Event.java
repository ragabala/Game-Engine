package com.hw3.eventManager;

import com.hw3.actionmanager.Clock;
import com.hw3.actionmanager.Record;

public class Event {

	public enum Type {
		USER_INPUT,
		CHARACTER_COLLSION, 
		CHARACTER_DEATH,
		CHARACTER_SPAWN,
		START_RECORDING,
		STOP_RECORDING,
	}

	private Type type;
	public boolean handled;
	// The `timestamp` that is saved should be real time
	// so that Event can be converted for any timeline.
	private double tics;

	protected Event(Type type) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.tics = Clock.getTics(Record.recordingStartTime, Clock.getSystemTime());
	}

	public Type getType() {
		return type;
	}
	
	public double getTics() {return tics;}

}
