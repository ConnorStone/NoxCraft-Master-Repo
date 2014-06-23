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

package com.noxpvp.noxchat;

import java.util.List;

import com.noxpvp.core.data.OldBaseNoxPlayerAdapter;
import org.bukkit.OfflinePlayer;

import com.noxpvp.core.data.OldNoxPlayerAdapter;
import com.noxpvp.core.utils.gui.MessageUtil;

public class OldChatPlayer extends OldBaseNoxPlayerAdapter implements Targetable {

	private Targetable target;
	private List<String> mutes;

	public OldChatPlayer(OldNoxPlayerAdapter player) {
		super(player);
	}

	public OldChatPlayer(OfflinePlayer player) {
		super(player);
	}
	public OldChatPlayer(String name) {
		super(name);
	}

	public Targetable getTarget() {
		return this.target;
	}

	public void setTarget(Targetable target) {
		this.target = target;
	}

	public void sendMessage(String message) {
		if (isOnline())
			MessageUtil.sendMessage(getPlayer(), message);
	}

	public void sendMessage(String... messages) {
		if (isOnline())
			MessageUtil.sendMessage(getPlayer(), messages);
	}

	public void sendTargetMessage(String message) {
		if (getTarget() != null)
			getTarget().sendMessage(message);
	}

	public void sendTargetMessage(String... messages) {
		if (getTarget() != null)
			getTarget().sendMessage(messages);
	}

	public final String getName() {
		return getPlayerName();
	}

	public final String getType() {
		return "Player";
	}

	public boolean isMuted(Targetable target) {
		if (!(target instanceof OldChatPlayer))
			return false;

		return mutes.contains(target.getName());
	}

	public void sendTargetMessage(Targetable from, String message) {
		if (getTarget() != null)
			getTarget().sendMessage(from, message);
	}

	public void sendTargetMessage(Targetable from, String... messages) {
		if (getTarget() != null)
			getTarget().sendMessage(from, messages);
	}

	public void sendMessage(Targetable from, String message) {
		if (!isMuted(from))
			sendMessage(message);
	}

	public void sendMessage(Targetable from, String... messages) {
		if (!isMuted(from))
			sendMessage(messages);
	}

	@Override
	public void save() {
		Targetable target = getTarget();
		if (target == null)
			getPersistantData().remove("chat.target");
		else {
			getPersistantData().set("chat.target.type", target.getType());
			getPersistantData().set("chat.target.name", target.getName());
		}
	}

	@Override
	public void load() {

	}
}
