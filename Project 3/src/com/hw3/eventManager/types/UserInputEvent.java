package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;
import com.hw3.sketcher.Player;

public class UserInputEvent extends Event {

	// Only the User Inputs are 
	public int x, y;
	public Player player;

	public UserInputEvent(int x, int y, Player player, long timestamp) {
		super(Event.Type.USER_INPUT, timestamp);
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		this.player = player;
	}

}
