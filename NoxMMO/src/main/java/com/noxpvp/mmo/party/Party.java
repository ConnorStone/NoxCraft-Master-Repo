package com.noxpvp.mmo.party;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;

public class Party implements IParty {

	ConfigurationNode partyData;

	private String owner;
	private List<String> members;
	private String focusedEXPMember;

	private String name;
	private String password;
	private boolean pvpState;
	private SharingType sharing;


	public Party(Player owner, @Nullable String name, @Nullable String pass) {
		this.owner = owner.getName();

		MMOPlayer player = MMOPlayerManager.getInstance().getPlayer(owner);

		partyData = player.getPersistantData().getNode("party");

		this.name = name != null ? name : partyData.get("name", "Party");

		this.members = partyData.getList("members", String.class);
		this.focusedEXPMember = partyData.get("focused-xp-member", this.owner);
		this.password = pass != null ? pass : partyData.get("password", "NULL");
		this.pvpState = partyData.get("pvp", false);
		this.sharing = partyData.get("sharing", SharingType.SHARED);
	}

	public Player getOwner() {
		return Bukkit.getPlayerExact(owner);
	}

	//START MEMBERS
	public List<String> getMembers() {
		return this.members;
	}

	public boolean hasMember(String player) {
		if (player != null)
			return this.members.contains(player);

		return false;
	}

	public void addMember(String player) {
		if (hasMember(player))
			return;

		if (Bukkit.getPlayerExact(player) != null)
			this.addMember(player);
	}
	/*
	 * END MEMBERS
	 */

	/*
	 * Start getters
	 */
	public String getPartyName() {
		return this.name;
	}

	/*
	 * Setters start
	 */
	public void setPartyName(String name) {
		if (name != null && name != this.name) {
			partyData.set("name", name);
			this.name = name;
		}
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pass) {
		if (pass != null && pass != this.password) {
			partyData.set("password", pass);
			this.password = pass;
		}
	}

	public boolean getPVPState() {
		return this.pvpState;
	}
	/*
	 * End Getters
	 */

	public void setPVPState(boolean state) {
		if (state != this.pvpState) {
			partyData.set("pvp", state);
			this.pvpState = state;
		}
	}

	public SharingType getSharingState() {
		return this.sharing;
	}

	public void setSharingState(SharingType state) {
		if (state != null && state != this.sharing) {
			partyData.set("sharing", state);
			this.sharing = state;
		}
	}

	public List<Player> getExpGroup() {
		List<Player> members = new ArrayList<Player>();

		if (this.sharing == SharingType.FOCUSED)
			members.add(Bukkit.getPlayerExact(focusedEXPMember));

		else {
			for (String m : this.members) {
				if (Bukkit.getPlayerExact(m) != null)
					members.add(Bukkit.getPlayerExact(m));
			}
		}

		return members;
	}
	/*
	 * Setters end
	 */

}
