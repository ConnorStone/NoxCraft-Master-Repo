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

package com.noxpvp.core.proxies;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.bergerkiller.bukkit.common.proxies.Proxy;

public class WeakProxyBase<T> implements Proxy<T> {
	protected Reference<T> base;

	public WeakProxyBase(T base) {
		setProxyBase(base);
	}

	public void setProxyBase(T base) {
		this.base = new WeakReference<T>(base);
	}

	public T getProxyBase() {
		if (this.base != null)
			return this.base.get();
		return null;
	}

	@Override
	public String toString() {
		return base.toString();
	}

	@Override
	public int hashCode() {
		return base.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		return base.equals(unwrap(object));
	}

	/**
	 * If the object is a Proxy class, it's base is unwrapped and returned instead
	 *
	 * @param object to check
	 * @return unwrapped Proxy data, or the input object
	 */
	public static Object unwrap(Object object) {
		if (object instanceof Proxy) {
			return ((Proxy<?>) object).getProxyBase();
		} else {
			return object;
		}
	}

	/**
	 * Validates that all methods are properly overrided.
	 * Logs a warning if this is not the case.
	 *
	 * @param proxy to check
	 * @return True if validation was successful, False if not
	 */
	public static boolean validate(Class<? extends Proxy<?>> proxy) {
		try {
			boolean succ = true;
			boolean loggedHeader = false;
			for (Method method : proxy.getDeclaredMethods()) {
				if (method.getDeclaringClass() != proxy) {
					succ = false;
					if (!loggedHeader) {
						loggedHeader = true;
						Bukkit.getLogger().log(Level.WARNING, "[Proxy] Some method(s) are not overrided in '" + proxy.getName() + "':");
					}
					Bukkit.getLogger().log(Level.WARNING, "    - '" + method.toGenericString());
				}
			}
			return succ;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

}
