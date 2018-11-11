	package com.hw3.eventManager;

import com.hw3.eventManager.types.CharacterCollisionEvent;
import com.hw3.eventManager.types.CharacterDeathEvent;
import com.hw3.eventManager.types.CharacterSpawnEvent;
import com.hw3.eventManager.types.StartRecordingEvent;
import com.hw3.eventManager.types.StopRecordingEvent;
import com.hw3.eventManager.types.UserInputEvent;

public class HandleEventDispatch implements EventListener {

	// This handles all the Events and how each event has to be processed.
	// This is a generic handler which dispatches the request to the correcsponding
	// functions.
	@Override
	public void onEvent(Event event) {
		EventDispatcher dispatcher = new EventDispatcher(event);
		dispatcher.dispatch(Event.Type.CHARACTER_COLLSION, (Event e) -> (onCharacterCollision((CharacterCollisionEvent) e)));
		dispatcher.dispatch(Event.Type.USER_INPUT, (Event e) -> (onUserInput((UserInputEvent) e)));
		dispatcher.dispatch(Event.Type.CHARACTER_DEATH, (Event e) -> (onCharacterDeath((CharacterDeathEvent) e)));
		dispatcher.dispatch(Event.Type.CHARACTER_SPAWN, (Event e) -> (onCharacterSpawn((CharacterSpawnEvent) e)));
		dispatcher.dispatch(Event.Type.START_RECORDING, (Event e) -> (onStartRecording((StartRecordingEvent) e)));
		dispatcher.dispatch(Event.Type.STOP_RECORDING, (Event e) -> (onStopRecording((StopRecordingEvent) e)));
	}
	
	public boolean onStartRecording(StartRecordingEvent event) {
		// This should Save all the positions of the scenes, along with the 
		// start time of the recording to find the relative events
		return true;
	}
	
	
	public boolean onStopRecording(StopRecordingEvent event) {
		
		return true;
	}
	
		
	public boolean onCharacterCollision(CharacterCollisionEvent event) {
		
		return true;	
	}
	
	public boolean onUserInput(UserInputEvent event) {
		int x = event.x;
		int y = event.y;
		return true;	
	}
	
	public boolean onCharacterDeath(CharacterDeathEvent event) {
		return true;	
	}
	
	public boolean onCharacterSpawn(CharacterSpawnEvent event) {
		return true;	
	}
	
	
	
}
