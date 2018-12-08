package com.hw3.eventManager;

public class EventDispatcher {

	private Event event;

	public EventDispatcher(Event event) {
		// TODO Auto-generated constructor stub
		this.event = event;
	}

	public void dispatch(Event.Type type, EventHandler handler) {
		if (event.handled)
			return;

		if (event.getType() == type)
			event.handled = handler.onEvent(event);
	}

}
