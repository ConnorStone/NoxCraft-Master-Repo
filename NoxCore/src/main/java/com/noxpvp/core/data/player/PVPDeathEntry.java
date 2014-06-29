package com.noxpvp.core.data.player;

import com.bergerkiller.bukkit.common.conversion.Conversion;
import com.noxpvp.core.utils.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@SerializableAs("PlayerPVPDeathEntry")
public class PVPDeathEntry extends DeathEntry {

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization Keys
	//~~~~~~~~~~~~~~~~~~~~~~~~

	protected static final String PLAYER_NAME_KEY = "player-name";
	protected static final String PLAYER_UUID_KEY = "player-uuid";
	protected static final String PLAYER_WEAPON = "player-weapon";

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~

	public PVPDeathEntry(Map<String, Object> data) {
		super(data);

		playerName = (data.containsKey(PLAYER_NAME_KEY)) ? data.get(PLAYER_NAME_KEY).toString() : "UNKNOWN";
		playerUUID = (data.containsKey(PLAYER_UUID_KEY)) ? UUIDUtil.toUUID(data.get(PLAYER_UUID_KEY)) : UUIDUtil.ZERO_UUID; //TODO: Determine whether to use a blank uuid or null.
		playerWeapon = (data.containsKey(PLAYER_WEAPON)) ? Conversion.toItemStack.convert(data.get(PLAYER_WEAPON), null) : null;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Fields
	//~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final UUID playerUUID;
	private final String playerName;
	private final ItemStack playerWeapon;

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public boolean isUUIDValid() {
		return getPlayerUUID() != null && !UUIDUtil.ZERO_UUID.equals(getPlayerUUID());
	}

	public String getPlayerName() {
		return playerName;
	}

	public ItemStack getPlayerWeapon() {
		return playerWeapon;
	}

	public boolean isKillerOnline() {
		OfflinePlayer p = getKiller();
		return p != null && p.isOnline();
	}

	public OfflinePlayer getKiller() {
		if (isUUIDValid()) return Bukkit.getOfflinePlayer(getPlayerUUID());
//		else if (!getPlayerName().equals("UNKNOWN")) return Bukkit.getOfflinePlayer(getPlayerName()); //Not Best Option but its a fallback.
		else return null;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~
	//Serialization
	//~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> data = super.serialize();

		data.put(PLAYER_NAME_KEY, getPlayerName());
		data.put(PLAYER_UUID_KEY, getPlayerUUID());
		data.put(PLAYER_WEAPON, getPlayerWeapon());

		return data;
	}
}
