/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.handlers;

import java.util.Comparator;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

public abstract class BaseMMOEventHandler<T extends Event> implements MMOEventHandler<T>, Comparator<MMOEventHandler<?>> {
	private final String id;
	private final EventPriority eventPriority;
	private int priority;

	public BaseMMOEventHandler(String id, EventPriority eventPriority, int priority) {
		this.id = id;
		this.eventPriority = eventPriority;
		this.priority = priority;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MMOEventHandler)) return false;

		MMOEventHandler that = (MMOEventHandler) o;

		if (priority != that.getPriority()) return false;
		if (eventPriority != that.getEventPriority()) return false;
		if (!id.equals(that.getID())) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + eventPriority.hashCode();
		result = 31 * result + priority;
		return result;
	}

	public final String getID() {
		return id;
	}

	public int compareTo(MMOEventHandler o) {
		return compare(this, o);
	}

	public int compare(MMOEventHandler<?> o1, MMOEventHandler<?> o2) {
		EventPriority ep1 = o1.getEventPriority(), ep2 = o2.getEventPriority();
		int d1 = ep1.getSlot(), d2 = ep2.getSlot();

		if (d1 > d2)
			return 1;
		else if (d1 < d2)
			return -1;

		int p1 = o1.getPriority(), p2 = o2.getPriority();
		if (p1 > p2)
			return 1;
		else if (p1 < p2)
			return -1;
		else
			return 0;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int value) {
		priority = value;
	}

	public EventPriority getEventPriority() {
		return eventPriority;
	}
}
