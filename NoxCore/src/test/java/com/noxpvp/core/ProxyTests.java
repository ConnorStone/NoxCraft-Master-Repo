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

import com.bergerkiller.bukkit.common.proxies.Proxy;
import com.noxpvp.core.data.OldBaseNoxPlayerAdapter;
import org.junit.Test;

import java.lang.reflect.Method;
import static org.junit.Assert.*;

public class ProxyTests {
	private static String newline = System.lineSeparator();

	private static void print(Object... objects) {

		StringBuilder builder = new StringBuilder();
		builder.append("Tester -> ").append(UuidTest.class.getSimpleName()).append(":").append(newline).append('\t');
		builder.append(Thread.currentThread().getStackTrace()[2]);
		boolean first = true;
		for (Object ob : objects)
			if (first) {
				first = false;
				builder.append(ob);
			} else
				builder.append(", ").append(ob);

		System.out.println(builder.toString());
	}

	private static boolean checkClass(Class<? extends Proxy<?>> proxy) {
		try {
			boolean succ = true;
			boolean loggedHeader = false;
			for (Method method : proxy.getDeclaredMethods()) {
				if (method.getDeclaringClass() != proxy) {
					succ = false;
					if (!loggedHeader) {
						loggedHeader = true;
						print("[Proxy] Some method(s) are not overrided in '" + proxy.getName() + "':");
					}
					print("    - '" + method.toGenericString());
				}
			}
			return succ;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

	@Test
	public void checkNoxPlayerAdapter() throws Exception {
		assertTrue(checkClass(OldBaseNoxPlayerAdapter.class));
	}

}
