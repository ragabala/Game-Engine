package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;

public class UserInputEvent extends Event {

	// Only the User Inputs are 
	public int x, y;

	public UserInputEvent(int x, int y) {
		super(Event.Type.USER_INPUT);
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}

}
