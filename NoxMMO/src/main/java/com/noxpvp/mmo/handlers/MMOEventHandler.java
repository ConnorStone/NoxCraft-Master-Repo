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

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

/**
 * This interface must implement a unique id for its handle.
 * <br/>
 * <br/>
 * The method getID() MUST return a unique ID.
 * <br/><b>Please read the JavaDocs for </b><u>{@link #getID()}</u>
 * <p/>
 * <br/>
 * <br/>
 * It is best to use the Base Implementation of this class. OR copy the base implementations of a compareTo function. As this is required.
 *
 * @param <T> of type Event.
 */
public interface MMOEventHandler<T extends Event> extends Comparable<MMOEventHandler> {

	/**
	 * Gets a unique id to this ability handle.
	 * <br/>
	 * <br/>
	 * <b>When implementing!</b> Be sure to add prefixes such as player names to this. This <b>MUST</b> be a unique name or it will never register!
	 *
	 * @return A unique string id.
	 */
	public String getID();

	/**
	 * Determines the order this ability gets in the ability handles.
	 *
	 * @return int priority no less than 0.
	 */
	public int getPriority();

	/**
	 * Should this event be passed to this handler if its cancelled?
	 *
	 * @return true to not pass and false to always pass.
	 */
	public boolean ignoreCancelled();

	/**
	 * @return EventPriority - to use
	 */
	public EventPriority getEventPriority();

	/**
	 * Executer to ability.
	 *
	 * @param event - to pass.
	 */
	public void execute(T event);

	/**
	 * Tells what event this is registered on.
	 *
	 * @return Class type.
	 */
	public Class<T> getEventType();

	/**
	 * Tells what event name this is under.
	 * <br/>
	 * This is used on the underlying api behind the event listeners.
	 *
	 * @return The name of event
	 */
	public String getEventName();
}
