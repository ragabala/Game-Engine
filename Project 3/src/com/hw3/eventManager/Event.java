package com.hw3.eventManager;

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
	private long timestamp;

	protected Event(Type type, long timestamp) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.timestamp = timestamp;
	}

	public Type getType() {
		return type;
	}
	
	public long getTimestamp() {return timestamp;}

}
