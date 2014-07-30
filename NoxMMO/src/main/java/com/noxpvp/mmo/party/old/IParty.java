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

import java.util.List;

import org.bukkit.entity.Player;

/*
 * Partys are a way for encouraging players to work together more often, and provide a means of chat for a group outside of tc and local.
 * when 2 or more players are in a party the exp gains for each player is boosted by .1 or 10%. Each player is entitled to they're own party
 * and cannot create randomly named partys like is other plugins. This way player can invite people to join they're party or ask another player to join theirs.
 * 
 * If a player sets the password on they're party and gives it to anther player, that player can then join the party without the owner having to accept.
 * 
 * PVP can be toggled on / off and will override any towny setting for ally pvp, allowing for fighting, or preventing mis-conduct during a raid.
 */
public interface IParty {

	public Player getOwner();

	public List<String> getMembers();

	public boolean hasMember(String player);

	public void addMember(String player);

	public String getPartyName();

	public void setPartyName(String name);

	public String getPassword();

	public void setPassword(String pass);

	public boolean getPVPState();

	public void setPVPState(boolean state);

	public SharingType getSharingState();

	public void setSharingState(SharingType state);

	public List<Player> getExpGroup();
}
