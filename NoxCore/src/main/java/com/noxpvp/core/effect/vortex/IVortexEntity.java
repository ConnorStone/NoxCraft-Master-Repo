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

package com.noxpvp.core.effect.vortex;

import java.util.HashSet;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public interface IVortexEntity {

	public HashSet<? extends BaseVortexEntity> tick();

	public BaseVortex getParent();

	public Entity getEntity();

	public void remove();

	public int verticalTicker();

	public int horizontalTicker();

	public Vector getVelo();

	public void setVelo(Vector velo);

	public boolean onRemove();

	public boolean onCreation();

	public HashSet<? extends BaseVortexEntity> onTick();

}
