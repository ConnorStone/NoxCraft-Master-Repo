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

package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

public class NoPermissionException extends RuntimeException {
	private static final long serialVersionUID = 8910717124994314558L;
	private String permission;
	private CommandSender sender;

	public NoPermissionException(CommandSender sender, String permission, String message) {
		super(message);
		this.sender = sender;
		this.permission = permission;
	}

	/**
	 * @return the permission
	 */
	public final synchronized String getPermission() {
		return permission;
	}

	/**
	 * @return the sender
	 */
	public final synchronized CommandSender getSender() {
		return sender;
	}
}
