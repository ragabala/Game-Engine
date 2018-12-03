package com.hw3.eventManager;

import com.hw3.actionmanager.Clock;
import com.hw3.actionmanager.Record;
import com.hw3.actionmanager.Replay;
import com.hw3.eventManager.types.CharacterCollisionEvent;
import com.hw3.eventManager.types.CharacterDeathEvent;
import com.hw3.eventManager.types.CharacterSpawnEvent;
import com.hw3.eventManager.types.StopRecordingEvent;
import com.hw3.eventManager.types.UserInputEvent;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;
import com.hw3.sketcher.SpawnPoint;

public class EventManager {

	public static void register( Event.Type type,Object... eventParams) {
		// if recording is on, we have to add this event
		// and if the replay is off we add this event to the replay

		// This action creates a user input type event
		Event event;

		switch (type) {
		case CHARACTER_COLLSION:
			event = new CharacterCollisionEvent((Player)eventParams[0], (GameObject)eventParams[1]);
			if (Record.isRecording() && !Replay.isReplaying()) {

				Record.addEvent(event);
			}
			break;
		case CHARACTER_DEATH:
			event = new CharacterDeathEvent((Player)eventParams[0]);
			if (Record.isRecording() && !Replay.isReplaying()) {
				Record.addEvent(event);
			}
			break;
		case CHARACTER_SPAWN:
			event = new CharacterSpawnEvent((Player)eventParams[0], (SpawnPoint)eventParams[1]);
			if (Record.isRecording() && !Replay.isReplaying()) {
				Record.addEvent(event);
			}
			break;
		case START_RECORDING:
			break;
		case STOP_RECORDING:
			event = new StopRecordingEvent();
			Record.recordingOn = false;
			// setting to default tic
			Clock.setTic(Clock.DEFAULT_TIC_SIZE);
			Record.events.add(event);
			break;
		case USER_INPUT:
			event = new UserInputEvent((Integer)eventParams[0], (Integer)eventParams[1], (Player)eventParams[2]);
			if (Record.isRecording() && !Replay.isReplaying()) {
				Record.addEvent(event);
			}

			break;
		default:
			break;

		}

	}

}
