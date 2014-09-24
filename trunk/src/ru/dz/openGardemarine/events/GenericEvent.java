package ru.dz.openGardemarine.events;

import java.util.EventObject;

public abstract class GenericEvent extends EventObject {


	/**
     * Constructs an Event.
     *
     * @param    source    The object on which the Event initially occurred.
     * @exception  IllegalArgumentException  if source is null.
     */
	public GenericEvent(Object source) {
		super(source);
	}

}
