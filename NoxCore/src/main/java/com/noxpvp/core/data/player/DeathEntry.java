package com.noxpvp.core.data.player;

import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.SafeLocation;
import org.bukkit.Location;
import org.bukkit.Utility;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("PlayerDeathEntry")
public class DeathEntry implements ConfigurationSerializable {

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization Keys
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected static final String KILLED_BOOL_KEY = "was-murdered";
	protected static final String DEATH_LOCATION_KEY = "death-location";
	protected static final String DEATH_TIME_KEY = "death-timestamp";
	protected static final String DEATH_TYPE_KEY = "death-type";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private final SafeLocation location;
	private final boolean murdered;
	private final long deathstamp;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public DeathEntry(Map<String, Object> data) {
		murdered = (data.containsKey(KILLED_BOOL_KEY)) ? Conversion.toBool.convert(data.get(KILLED_BOOL_KEY), true) : true;
		location = (data.containsKey(DEATH_LOCATION_KEY)) ? Conversion.convert(data.get(DEATH_LOCATION_KEY), SafeLocation.class, null) : null;
		deathstamp = (data.containsKey(DEATH_TIME_KEY)) ? Conversion.toLong.convertZero(data.get(DEATH_TIME_KEY)) : 0;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Returns whether or not the person was killed or they just died.
	 *
	 * @return true if murdered false otherwise.
	 *
	 * <p>One example of non murder is falling to death.</p>
	 */
	public final boolean wasMurdered() {
		return murdered;
	}

	/**
	 * Returns a safe location object of where the player died.
	 * <p>It may be used to convert into a bukkit location object. However it may return null if the world does not exist!</p>
	 *
	 * @return {@link com.noxpvp.core.SafeLocation} object
	 */
	public final SafeLocation getSafeDeathLocation() {
		return location;
	}

	/**
	 * Returns a bukkit location object of where the player has died.
	 * <p>Locations may become invalid if the world no longer exists. Other factors may take play but this is the major one that is most likely a cause for such an invalidation.</p>
	 *
	 * @return {@link org.bukkit.Location} object or null if not a valid location.
	 */
	public final Location getDeathLocation() {
		return (location != null) ? location.toLocation() : null;
	}

	/**
	 * Gets the raw timestamp associated with the death time.
	 *
	 * This is in nano's divided by <b>1,000,000</b>
	 *
	 * Please use {@link System#nanoTime()} to set this stamp.
	 *
	 * <i>The milliseconds one is not going to work properly in the possible future as noted in JavaDocs. Use NanoTime dividing to get millis.</i>
	 *
	 * @return long value of a timestamp.
	 */
	public long getDeathStamp() {
		return deathstamp;
	}

	@Utility
	protected String getType() {
		return "Unknown";
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();

		data.put(KILLED_BOOL_KEY, wasMurdered());
		data.put(DEATH_LOCATION_KEY, getSafeDeathLocation());
		data.put(DEATH_TIME_KEY, getDeathStamp());
		data.put(DEATH_TYPE_KEY, getType());

		return data;
	}
}