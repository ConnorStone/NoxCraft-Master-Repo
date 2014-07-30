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

package com.noxpvp.core.packet;

import org.bukkit.entity.EntityType;

public enum ObjectType {

	ITEM_STACK((byte) 2),
	ARROW((byte) 60),
	FISHING_FLOAT((byte) 90);

	private byte id;

	ObjectType(byte id) {
		this.id = id;
	}

	public static byte getByteIdByType(EntityType type) {
		switch (type) {
			case ARROW:
				return 60;
			case BOAT:
				return 1;
			case DROPPED_ITEM:
				return 2;
			default:
				return 0;
		}//TODO finish adding these

	}

	public byte getByteId() {
		return this.id;
	}

}
