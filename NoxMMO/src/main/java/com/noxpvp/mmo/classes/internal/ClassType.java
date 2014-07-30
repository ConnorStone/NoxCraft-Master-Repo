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

package com.noxpvp.mmo.classes.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ClassType {
	Melee,
	Axes(Melee),
	Swords(Melee),
	Stealth(Melee),
	Ranged,
	Magic(Ranged),
	Healing(Magic),
	Archery(Ranged),
	Gathering,
	Farming(Gathering),
	Pets(null, Melee, Ranged),
	Scientific;

	private ClassType parent;
	private ClassType[] inheritance;

	private List<ClassType> children;

	private ClassType() {
		this.children = new ArrayList<ClassType>();
		this.parent = null;
		this.inheritance = new ClassType[0];
	}

	private ClassType(ClassType parent) {
		this.children = new ArrayList<ClassType>();
		if (parent != null)
			parent.children.add(this);
		this.parent = parent;
		this.inheritance = new ClassType[0];
	}

	private ClassType(ClassType parent, ClassType... inherits) {
		this.children = new ArrayList<ClassType>();
		if (parent != null)
			parent.children.add(this);
		this.parent = parent;
		this.inheritance = inherits;
	}

	public boolean inherits(ClassType type) {
		if (getParent() != null && getParent().equals(type))
			return true;

		for (ClassType t : inheritance)
			if (t.equals(type))
				return true;

		//Nothing matches return false
		return false;
	}

	public List<ClassType> getChildren() {
		return Collections.unmodifiableList(children); //Should never be edited!
	}

	public ClassType getRoot() {
		if (parent == null)
			return this;
		else
			return parent.getRoot();
	}

	public ClassType getParent() {
		return parent;
	}
}