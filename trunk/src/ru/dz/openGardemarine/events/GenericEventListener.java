package ru.dz.openGardemarine.events;

import java.util.EventListener;

public interface GenericEventListener extends EventListener {

	public abstract void event(final GenericEvent event);
	
}
