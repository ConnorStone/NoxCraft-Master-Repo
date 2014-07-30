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

package com.noxpvp.homes.limits;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.noxpvp.homes.managers.HomeLimitManager;
import com.noxpvp.homes.managers.HomesPlayerManager;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

@SerializableAs("HomeLimit")
public abstract class BaseHomeLimit implements HomeLimit {

	private static ModuleLogger log;
	private static Map<String, SafeConstructor<? extends BaseHomeLimit>> constructors = new HashMap<String, SafeConstructor<? extends BaseHomeLimit>>();

	static {

	}

	private static void checkAndRegister(BaseHomeLimit limit) {
		if (constructors.containsKey(limit.getPersistenceNode())) return;

		SafeConstructor<BaseHomeLimit> cons = new SafeConstructor<BaseHomeLimit>((Class<BaseHomeLimit>)limit.getClass(), Map.class);
		if (cons.isValid()) constructors.put(limit.getPersistenceNode(), cons);
	}

	static {
		log = HomeLimitManager.getInstance().getModuleLogger("HomeLimit");
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private final UUID uuid;
	private boolean isCumulative;
	private int limit;
	private int priority;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public BaseHomeLimit(UUID uuid, int limit) {
		this(uuid, limit, false);
	}

	public BaseHomeLimit(UUID uuid, int limit, boolean isCumulative) {
		this(uuid, limit, 1, isCumulative);
	}

	//Deserialization
	protected BaseHomeLimit(Map<String, Object> data) {
		UUID id;
		try { id = UUID.fromString(data.get("uuid").toString()); } catch (Exception e) {
			id = UUID.randomUUID();
			if (e instanceof NullPointerException) {
				log(Level.INFO, "No ID present for data. Generating new UUID");
			} else if (e instanceof IllegalArgumentException) {
				log(Level.WARNING, "UUID for limit was malformed... Generating new UUID");
			}
		}

		this.limit = Conversion.toInt.convert( (data.containsKey("limit") ? data.get("limit") : null), 0);
		this.uuid = id;

		this.isCumulative = Conversion.toBool.convert( (data.containsKey("isCumulative") ? data.get("isCumulative") : null), false);
		checkAndRegister(this);
	}

	public BaseHomeLimit(UUID uuid, int limit, int priority, boolean isCumulative) {
		this.uuid = uuid;
		this.limit = limit;
		this.isCumulative = isCumulative;
		this.priority = priority;

		checkAndRegister(this);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~



	public final int getPriority() {
		return this.priority;
	}

	public final boolean isCumulative() {
		return this.isCumulative;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods: Limits
	//~~~~~~~~~~~~~~~~~~~~~~~~~

	public final int getLimit() {
		return this.limit;
	}

	public final void setLimit(int limit) {
		this.limit = limit;
	}

	public final boolean isPastLimit(Player player) {
		return HomesPlayerManager.getInstance().getPlayer(player).getHomeCount() > getLimit();
	}

	public final boolean isPastLimit(UUID uuid) {
		return HomesPlayerManager.getInstance().getPlayer(uuid).getHomeCount() > getLimit();
	}

	public void log(Level level, String msg) {

	}

	//~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 * <hr/>
	 * This is actually used to determine the limit type.
	 * <br/>
	 * <b>Please keep such values unique!</b>
	 * @return Limit Type
	 */
	public abstract String getPersistenceNode();

	public final UUID getPersistentID() {
		return this.uuid;
	}

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("uuid", getPersistentID());
		data.put("type", getPersistenceNode());
		data.put("limit", getLimit());
		data.put("priority", getPriority());
		data.put("cumulative", isCumulative());

		return data;
	}

	public static HomeLimit valueOf(final Map<String, Object> data) {
		if (data.containsKey("type")) {
			if (constructors.containsKey(data.get("type").toString())) {
				try {
					return constructors.get(data.get("type").toString()).newInstance(data);
				} catch (RuntimeException e) {
					log.severe("There was an exception while constructing HomeLimit. Storing backup for analysis.");
				}
			} else {

				log.severe(data.get("type").toString() + " is not in the constructors for Home Limit Serialization!" + System.lineSeparator() +
						"Data may have been lost!" + System.lineSeparator() +
						"Will attempt to keep data!");
			}
		} else {
			log.severe("Data did not contain a type value for limit. Retaining a backup for analysis.");
		}
		return new BackupHomeLimit(data);
	}

}
