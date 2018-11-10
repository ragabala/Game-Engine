package com.hw3.eventManager;

public class Event {

	public enum Type {
		USER_INPUT,
		CHARACTER_COLLSION, 
		CHARACTER_DEATH,
		CHARACTER_SPAWN
	}

	private Type type;
	public boolean handled;

	protected Event(Type type) {
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	public Type getType() {
		return type;
	}

}
