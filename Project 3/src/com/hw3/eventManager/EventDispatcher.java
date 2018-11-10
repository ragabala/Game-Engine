package com.hw3.eventManager;

public class EventDispatcher {

	private Event event;
	private long eventTimeStamp;

	public EventDispatcher(Event event, long time) {
		// TODO Auto-generated constructor stub
		this.event = event;
		eventTimeStamp = time;
	}

	public void dispatch(Event.Type type, EventHandler handler) {
		if (event.handled)
			return;

		if (event.getType() == type)
			event.handled = handler.onEvent(event);
	}

}
