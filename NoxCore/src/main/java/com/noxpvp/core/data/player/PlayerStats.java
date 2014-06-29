package com.noxpvp.core.data.player;

import com.noxpvp.core.Persistent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerStats implements Persistent {

	private final UUID uuid;

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~

	public PlayerStats(Map<String, Object> data) {
		this(UUID.fromString((String) data.get("uuid")));
	}

	public PlayerStats(Player player) {
		this(player.getUniqueId());
	}

	public PlayerStats(UUID uuid) {
		this.uuid = uuid;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~

	public UUID getPersistentID() {
		return uuid;
	}

	/**
	 * {@inheritDoc}
	 * <hr/>
	 * <p>This specific implementation takes the {@link #getType()} and appends {@literal "-stats"} as a path.</p>
	 */
	public final String getPersistenceNode() {
		return getType() + "-stats";
	}

	//Required base implementation.
	public abstract String getType();

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("uuid", getPersistentID().toString());

		return data;
	}
}
