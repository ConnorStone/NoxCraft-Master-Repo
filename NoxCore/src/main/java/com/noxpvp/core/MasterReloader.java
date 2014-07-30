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

package com.noxpvp.core;

import com.noxpvp.core.reloader.BaseReloader;


public final class MasterReloader extends BaseReloader {
	private static final String root = "Master";
	private static MasterReloader instance;

	private MasterReloader() {
		super(null, root);
	}

	public static MasterReloader getInstance() {
		if (instance == null)
			instance = new MasterReloader();

		return instance;
	}

	/**
	 * @deprecated Method does nothing.
	 * <br/><br/>
	 * <p/>
	 * Must specify a module in {@link #reload(String)} instead or use {@link #reloadAll()} to reload all modules.
	 * <br/><br/>
	 * <p/>
	 * <b> Warning using {@link #reloadAll()} May be intensive due to fact any plugin hooked into this will reload its configs all at once.</b>
	 */
	public boolean reload() {
		return false;
	}

}
