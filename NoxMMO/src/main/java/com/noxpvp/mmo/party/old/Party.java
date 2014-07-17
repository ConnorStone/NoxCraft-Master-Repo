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

package com.noxpvp.mmo.party.old;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.manager.MMOPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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

//		partyData = player.getPersistantData().getNode("party");

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
