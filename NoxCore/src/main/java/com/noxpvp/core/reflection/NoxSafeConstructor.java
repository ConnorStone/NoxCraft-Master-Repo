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

package com.noxpvp.core.reflection;

import com.bergerkiller.bukkit.common.conversion.Converter;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.reflection.SafeField;

import java.lang.reflect.Constructor;

public class NoxSafeConstructor<T> {
	private Constructor<T> constructor;

	public NoxSafeConstructor(SafeConstructor<T> safeConstructor) {
		SafeField<Constructor<T>> field = new SafeField<Constructor<T>>(SafeConstructor.class, "constructor");
		constructor = field.get(safeConstructor);
	}

	public NoxSafeConstructor(Class<T> type, Class<?>... parameterTypes) {
		try {
			constructor = type.getConstructor(parameterTypes);
			constructor.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			//We are ignoring this.
		}
	}

	public NoxSafeConstructor(Constructor<T> constructor) {
		this.constructor = constructor;
	}

	/**
	 * Checks whether this Constructor is in a valid state<br>
	 * Only if this return true can this Constructor be used without problems
	 *
	 * @return True if this constructor is valid, False if not
	 */
	public boolean isValid() {
		return constructor != null && constructor.isAccessible();
	}

	/**
	 * Constructs a new Instance
	 *
	 * @param parameters to use for this Constructor
	 * @return A constructed type
	 * @throws RuntimeException if something went wrong while constructing
	 */
	public T newInstance(Object... parameters) {
		try {
			return constructor.newInstance(parameters);
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	/**
	 * Obtains a new Class Contructor that uses this contructor and converts the output
	 *
	 * @param converter to use for the output
	 * @return translated output
	 */
	@SuppressWarnings("unchecked")
	public <K> SafeConstructor<K> translateOutput(final Converter<K> converter) {
		return new SafeConstructor<K>((Constructor<K>) this.constructor) {
			@Override
			public K newInstance(Object... parameters) {
				return converter.convert(super.newInstance(parameters));
			}
		};
	}
}
