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

import java.util.*;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.listeners.*;

public class GenericMMOListener<T extends Event> extends NoxListener<NoxMMO> {

	protected static Map<Class<?>, SafeConstructor<GenericMMOListener<?>>> constructors = new HashMap<Class<?>, SafeConstructor<GenericMMOListener<?>>>();

	static {
		constructors.put(EntityDamageEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(EntityDamageListener.class, NoxMMO.class));
		constructors.put(EntityDamageByEntityEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(EntityDamageByEntityListener.class, NoxMMO.class));
		constructors.put(PlayerInteractEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(PlayerInteractListener.class, NoxMMO.class));
		constructors.put(PlayerPickupItemEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(PlayerPickupItemListener.class, NoxMMO.class));
		constructors.put(ProjectileLaunchEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(ProjectileLaunchListener.class, NoxMMO.class));
		constructors.put(ProjectileHitEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(ProjectileHitListener.class, NoxMMO.class));
		constructors.put(InventoryPickupItemEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(InventoryPickupItemListener.class, NoxMMO.class));
		constructors.put(PlayerInteractEntityEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(PlayerInteractEntityListener.class, NoxMMO.class));
		constructors.put(EntityChangeBlockEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(EntityChangeBlockListener.class, NoxMMO.class));
		constructors.put(EntityRegainHealthEvent.class, (SafeConstructor<GenericMMOListener<?>>) new SafeConstructor(EntityRegainHealthListener.class, NoxMMO.class));
	}

	private Class<T> eventType;
	private WeakHashMap<String, MMOEventHandler<T>> abe_name_cache;
	private Map<EventPriority, SortedSet<MMOEventHandler<T>>> abilityHandlers;

	protected GenericMMOListener(NoxMMO plugin, Class<T> type) {
		super(plugin);
		eventType = type;

		abe_name_cache = new WeakHashMap<String, MMOEventHandler<T>>();
		abilityHandlers = new HashMap<EventPriority, SortedSet<MMOEventHandler<T>>>();
		for (EventPriority pri : EventPriority.values())
			abilityHandlers.put(pri, new TreeSet<MMOEventHandler<T>>());
	}

	public static <T extends Event> GenericMMOListener<T> newListener(NoxMMO plugin, Class<T> type) {
		if (constructors.containsKey(type))
			return (GenericMMOListener<T>) constructors.get(type).newInstance(plugin);
		return null;
	}

	protected void onEventLowest(T event) {
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.LOWEST)) {
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}

	protected void onEventLow(T event) {
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.LOW)) {
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}

	protected void onEventNormal(T event) {
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.NORMAL)) {
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}

	protected void onEventHigh(T event) {
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.HIGH)) {
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}

	protected void onEventHighest(T event) {
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.HIGHEST)) {
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}

	protected void onEventMonitor(T event) {
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.MONITOR)) {
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}

	public Class<T> getEventType() {
		return eventType;
	}

	public void unregisterHandler(MMOEventHandler<T> handler) {
		String name;
		if (!abe_name_cache.containsKey((name = handler.getID())))
			throw new IllegalStateException(name + " is already not registered to this handler.");

		abilityHandlers.get(handler.getEventPriority()).remove(handler);
		abe_name_cache.remove(name);

		boolean empty = true;
		for (EventPriority priority : abilityHandlers.keySet()) {
			if (!abilityHandlers.get(priority).isEmpty()) {
				empty = false;
			}
		}

		if (empty)
			unregister();
	}

	public void registerHandler(MMOEventHandler<T> handler) {
		String name;
		if (abe_name_cache.containsKey((name = handler.getID())))
			throw new IllegalStateException(name + " is already registered!");

		abe_name_cache.put(name, handler);
		abilityHandlers.get(handler.getEventPriority()).add(handler);

		for (EventPriority priority : abilityHandlers.keySet()) {
			if (!abilityHandlers.get(priority).isEmpty()) {
				if (isRegistered())
					return;
				else
					register();
			}
		}
	}
}
