package com.noxpvp.mmo.handlers.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.GenericMMOListener;

public class EntityRegainHealthListener extends GenericMMOListener<EntityRegainHealthEvent> {

	public EntityRegainHealthListener(NoxMMO plugin) {
		super(plugin, EntityRegainHealthEvent.class);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onLowest(EntityRegainHealthEvent event) {
		onEventLowest(event);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onLow(EntityRegainHealthEvent event) {
		onEventLow(event);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onNormal(EntityRegainHealthEvent event) {
		onEventNormal(event);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public void onHigh(EntityRegainHealthEvent event) {
		onEventHigh(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onHighest(EntityRegainHealthEvent event) {
		onEventHighest(event);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onMonitor(EntityRegainHealthEvent event) {
		onEventMonitor(event);
	}
}
