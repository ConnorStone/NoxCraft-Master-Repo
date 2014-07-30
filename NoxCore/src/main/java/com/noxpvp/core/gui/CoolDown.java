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

package com.noxpvp.core.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.utils.TimeUtils;
import org.bukkit.configuration.serialization.SerializableAs;

public class CoolDown implements ConfigurationSerializable {
	private final String name;
	private final boolean nanoTime;
	private final long expires;

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Constructors
	//~~~~~~~~~~~~~~~~~~~~~~~~~

	public CoolDown(String name, Time time) {
		this(name, time.toFutureStamp(), time.usesNanos());
	}

	public CoolDown(String name, int seconds) {
		this(name, seconds * 1000, NoxCore.isUsingNanoTime());
	}

	public CoolDown(String name, int length, boolean nanoTime) {
		this(name, length + (nanoTime? System.nanoTime() : System.currentTimeMillis()), nanoTime);
	}

	public CoolDown(String name, long actualExpireTime, boolean nanoTime) {
		this.name = name;
		this.expires = actualExpireTime;
		this.nanoTime = nanoTime;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Instanced Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~


	public boolean expired() {
		long stamp = TimeUtils.getStamp(nanoTime);

		return stamp >= expires;
	}

	public final String getName() {
		return name;
	}

	/**
	 * Gets the actual expire time in mili / nano seconds depending on {@link NoxCore#isUsingNanoTime()}
	 *
	 * @return long Exact expire time
	 */
	public final long getExpiryStamp() {
		return expires;
	}

	/**
	 * Gets the time left on this cooldown in mili / nano seconds depending on {@link NoxCore#isUsingNanoTime()}
	 *
	 * @return long Time left
	 */
	public final long getTimeLeft() {
		long stamp = TimeUtils.getStamp(nanoTime);
		return Math.max(expires - stamp, 0);
	}

	public String getReadableTimeLeft() {
		if (isNanoTime())
			return TimeUtils.getReadableSecTime(getTimeLeft() / 1000 / 1000);
		else
			return TimeUtils.getReadableMillisTime(getTimeLeft() / 1000);
	}


	/**
	 * If this cooldown is using nano time
	 *
	 * @return {@link Boolean} nanoTime
	 */
	public final boolean isNanoTime() {
		return nanoTime;
	}

	public Map<String, Object> serialize() {
		Map<String, Object> obs = new HashMap<String, Object>();

		obs.put("name", getName());
		obs.put("expires", getExpiryStamp());
		obs.put("ns", isNanoTime());

		return obs;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Special Data Types
	//~~~~~~~~~~~~~~~~~~~~~~~~~
	@SerializableAs(value = "CD-Time")
	public static class Time implements ConfigurationSerializable {
		public int days, hours, minutes, seconds, millis, micros, nanos;

		public Time() {
			days = 0;
			hours = 0;
			minutes = 0;
			seconds = 0;
			millis = 0;
			micros = 0;
			nanos = 0;
		}

		public Time(Map<String, Object> data) {
			this();

			if (data.containsKey("days"))
				days = (Integer) data.get("days");
			if (data.containsKey("hours"))
				hours = (Integer) data.get("hours");
			if (data.containsKey("minutes"))
				minutes = (Integer) data.get("minutes");
			if (data.containsKey("seconds"))
				seconds = (Integer) data.get("seconds");
			if (data.containsKey("millis"))
				millis = (Integer) data.get("millis");
			if (data.containsKey("micros"))
				micros = (Integer) data.get("micros");
			if (data.containsKey("nanos"))
				nanos = (Integer) data.get("micros");
		}

		public Time days(int days) {
			this.days = days;
			return this;
		}

		public Time hours(int hours) {
			this.hours = hours;
			return this;
		}

		public Time minutes(int minutes) {
			this.minutes = minutes;
			return this;
		}

		public Time seconds(int seconds) {
			this.seconds = seconds;
			return this;
		}

		public Time millis(int millis) {
			this.millis = millis;
			return this;
		}

		public Time micros(int micros) {
			this.micros = micros;
			return this;
		}

		public Time nanos(int nanos) {
			this.nanos = nanos;
			return this;
		}

		public boolean usesNanos() {
			return nanos != 0 || micros != 0;
		}

		public long toFutureStamp() {
			long current = System.nanoTime();
			final boolean useMillis = !usesNanos();

			final long micromult = 1000;
			final long millimult = micromult * 1000;
			final long secmult = millimult * 1000;
			final long minmult = secmult * 60;
			final long hourmult = minmult * 12;
			final long daymult = hourmult * 24;


			long add = 0;
			add += nanos;
			add += micros * micromult;
			add += millis * millimult;
			add += seconds * secmult;
			add += minutes * minmult;
			add += hours * hourmult;
			add += days * daymult;

			add += current;
			return (useMillis ? add / millimult : add);
		}

		public long toStamp() {
			final boolean useMillis = !usesNanos();

			final long micromult = 1000;
			final long millimult = micromult * 1000;
			final long secmult = millimult * 1000;
			final long minmult = secmult * 60;
			final long hourmult = minmult * 12;
			final long daymult = hourmult * 24;


			long add = 0;
			add += nanos;
			add += micros * micromult;
			add += millis * millimult;
			add += seconds * secmult;
			add += minutes * minmult;
			add += hours * hourmult;
			add += days * daymult;

			return (useMillis ? add / millimult : add);
		}

		public Map<String, Object> serialize() {
			Map<String, Object> data = new HashMap<String, Object>();

			data.put("days", days);
			data.put("hours", hours);
			data.put("minutes", minutes);
			data.put("seconds", seconds);
			data.put("millis", millis);
			data.put("micros", micros);
			data.put("nanos", nanos);

			return data;
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~
	//Static Methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~

	public static CoolDown deserialize(Map<String, Object> obs) {
		if (!obs.containsKey("name") || !obs.containsKey("expires") || !obs.containsKey("ns"))
			throw new IllegalArgumentException("(name | expires | ns) should not be null!");

		return new CoolDown((String) obs.get("name"), (Long) obs.get("expires"), (Boolean) obs.get("ns"));
	}
}
