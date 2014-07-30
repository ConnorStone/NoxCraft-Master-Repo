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

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public abstract class BaseVortexEntity implements IVortexEntity {

	public static final String uniqueMetaKey = "VortexMeta";
	private FixedMetadataValue uniqueMeta;
	private BaseVortex parent;
	private Entity base;

	private double width, height;

	private int ticker_vertical;
	private int ticker_horizontal;

	public BaseVortexEntity(BaseVortex parent, Location loc, Entity base) {
		this.uniqueMeta = new FixedMetadataValue(parent.getPlugin(), parent.hashCode());
		this.parent = parent;
		this.base = base;

		this.width = parent.getWidth();
		this.height = parent.getHeightGain();

		ticker_vertical = 0;
		ticker_horizontal = (int) Math.round((Math.random() * 360));

		onCreation();

		base.setMetadata(uniqueMetaKey, uniqueMeta);
	}

	public HashSet<? extends BaseVortexEntity> tick() {
		double radius = BaseVortex.lookup.get(verticalTicker())[0] * width;
		int horizontal = horizontalTicker();

		double verticalDif = 0;
		if (height == 0D)
			verticalDif = getParent().getUser().getLocation().getY() - getEntity().getLocation().getY();

		Vector v = new Vector(
				radius * BaseVortex.lookup.get(horizontal)[1],
				(verticalDif == 0 ? height : verticalDif),
				radius * BaseVortex.lookup.get(horizontal)[0]);

		setVelo(v);

		return onTick();
	}

	public boolean isVortexEntity(Entity e) {
		return e.getMetadata(uniqueMetaKey).size() > 0;
	}

	public BaseVortex getParent() {
		return this.parent;
	}

	public Entity getEntity() {
		return this.base;
	}

	public void remove() {
		if (onRemove() && isVortexEntity(getEntity())) {
			this.base.remove();
		}
		this.base.removeMetadata(uniqueMetaKey, parent.getPlugin());
	}

	public int verticalTicker() {
		if (ticker_vertical < 90) {
			ticker_vertical += 5;
		}
		return ticker_vertical;
	}

	public int horizontalTicker() {
		ticker_horizontal = (ticker_horizontal + 45) % 360;
		return ticker_horizontal;
	}

	public Vector getVelo() {
		return this.base.getVelocity();
	}

	public void setVelo(Vector velo) {
		this.base.setVelocity(velo);
	}

}
