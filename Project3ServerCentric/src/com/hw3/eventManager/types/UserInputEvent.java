package com.hw3.eventManager.types;

import com.hw3.eventManager.Event;
import com.hw3.sketcher.Player;

public class UserInputEvent extends Event {

	// Only the User Inputs are 
	public int x, y;
	public int pos_x, pos_y;
	public Player player;

	public UserInputEvent(int x, int y, Player player) {
		super(Event.Type.USER_INPUT);
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		this.pos_x = player.x_pos;
		this.pos_y = player.y_pos;
		this.player = player;
	}

}
