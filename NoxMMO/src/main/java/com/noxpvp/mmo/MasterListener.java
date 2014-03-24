package com.noxpvp.mmo;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.Event;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.noxpvp.mmo.handlers.GenericMMOListener;
import com.noxpvp.mmo.handlers.MMOEventHandler;

@SuppressWarnings("unchecked")
public class MasterListener {
	private static ModuleLogger log;
	
	private Map<String, GenericMMOListener<?>> listeners = new HashMap<String, GenericMMOListener<?>>();
	
	public <T extends Event> GenericMMOListener<T> getNoxListener(Class<T> eventClass, String eventName)
	{
		GenericMMOListener<T> listener = (GenericMMOListener<T>)listeners.get(eventName);
		if (listener == null) {
			listener = GenericMMOListener.newListener(NoxMMO.getInstance(), eventClass);
			if (listener != null)
				listeners.put(eventName, listener);
		}

		return listener;
	}
	
	public <T extends Event> GenericMMOListener<T> getNoxListener(T event)
	{
		return getNoxListener((Class<T>)event.getClass(), event.getEventName());
	}
	
	public <T extends Event> boolean unregisterHandler(MMOEventHandler<T> handler)
	{
		String eventName = handler.getEventName();
		if (!listeners.containsKey(eventName))
			return false;
		try {
			((GenericMMOListener<T>)listeners.get(eventName)).unregisterHandler(handler);
			return true;
		} catch (IllegalStateException e) {
			return false;
		} catch (NullPointerException e) { 
			return false;
		}
	}
	
	public <T extends Event> boolean registerHandler(MMOEventHandler<T> handler)
	{
		String eventName = handler.getEventName();
		GenericMMOListener<T> listener = getNoxListener(handler.getEventType(), eventName);
		
		if (listener == null) {
			log.severe("There is no event listener class for event '" + eventName + "'");
			return false;
		}
		
		try { 
			listener.registerHandler(handler);
			return true;
		} catch (IllegalStateException e) {
			return false;
		} 
	}

	public void unregisterAll() {
		for (GenericMMOListener<?> listener : listeners.values())
			listener.unregister();
	}

	public static void init() {
		log = NoxMMO.getInstance().getModuleLogger("MasterListener");
	}
}
