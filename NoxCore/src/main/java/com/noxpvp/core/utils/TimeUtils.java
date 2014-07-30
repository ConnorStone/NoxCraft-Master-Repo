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

package com.noxpvp.core.utils;

import java.util.HashMap;
import java.util.Map;

public final class TimeUtils {
	private TimeUtils() {
	}

	public static String getReadableSecTime(long sec) {

		int seconds = (int) (sec % 60);
		sec /= 60;

		int minutes = (int) (sec % 60);
		sec /= 60;

		int hours = (int) (sec % 24);
		sec /= 24;

		int days = (int) (sec % Integer.MAX_VALUE);

		StringBuilder sb = new StringBuilder();
		if (days > 0)
			sb.append("D: ").append(days + " ");
		if (hours > 0)
			sb.append("H: ").append(hours + " ");
		if (minutes > 0)
			sb.append("M: ").append(minutes + " ");
		if (seconds > 0)
			sb.append("S: ").append(seconds);

		if (sb.length() < 1)
			sb.append("S: 0");
		
		return sb.toString();
	}

	public static String getReadableMillisTime(long millis) {
		int ms = (int) (millis % 1000);
		millis /= 1000;

		int seconds = (int) (millis % 60);
		millis /= 60;

		int minutes = (int) (millis % 60);
		millis /= 60;

		int hours = (int) (millis % 24);
		millis /= 24;

		int days = (int) (millis % Integer.MAX_VALUE);

		StringBuilder sb = new StringBuilder();
		if (days > 0)
			sb.append(days).append("D ");
		if (hours > 0)
			sb.append(hours).append("H ");
		if (minutes > 0)
			sb.append(minutes).append("M ");
		if (seconds > 0)
			sb.append(seconds).append("S ");
		if (ms > 0)
			sb.append(ms).append("MS ");

		return sb.toString();
	}

	//XXX: This is actually not millis seconds on the MS entry. It is actually Micro Seconds.
	public static String getReadableNanosTime(long nanos) {
		int ns = (int) (nanos % 1000);
		nanos /= 1000;
		int ms = (int) (nanos % 1000);
		nanos /= 1000;

		int seconds = (int) (nanos % 60);
		nanos /= 60;

		int minutes = (int) (nanos % 60);
		nanos /= 60;

		int hours = (int) (nanos % 24);
		nanos /= 24;

		int days = (int) (nanos % Integer.MAX_VALUE);

		StringBuilder sb = new StringBuilder();
		if (days > 0)
			sb.append("D: ").append(days + " ");
		if (hours > 0)
			sb.append("H: ").append(hours + " ");
		if (minutes > 0)
			sb.append("M: ").append(minutes + " ");
		if (seconds > 0)
			sb.append("S: ").append(seconds + " ");
		if (ms > 0)
			sb.append("MS: ").append(ms + " ");
		if (ns > 0)
			sb.append("NS: ").append(ns);

		return sb.toString();
	}

	public static long getStamp(boolean nanos) {
		return (nanos) ? System.nanoTime() : System.currentTimeMillis();
	}

	public static class StopWatch {
		private final Map<String, Long> elapses;
		private final Map<String, Long> starts;
		private final Map<String, Long> stops;
		private boolean isNanos = false;

		public StopWatch() {
			elapses = new HashMap<String, Long>();
			stops = new HashMap<String, Long>();
			starts = new HashMap<String, Long>();
		}

		public StopWatch(boolean nanos) {
			this();
			isNanos = nanos;
		}

		private static long getStamp(boolean nanos) {
			return (nanos) ? System.nanoTime() : System.currentTimeMillis();
		}

		private void convert(boolean nanos) {
			for (String key : starts.keySet())
				if (nanos)
					starts.put(key, starts.get(key) * 1000);
				else
					starts.put(key, starts.get(key) / 1000);

			for (String key : elapses.keySet())
				if (nanos)
					elapses.put(key, elapses.get(key) * 1000);
				else
					elapses.put(key, elapses.get(key) / 1000);
		}

		public boolean isNanos() {
			return isNanos;
		}

		public boolean setUsingNanos(boolean useNanos) {
			boolean last = isNanos();
			isNanos = useNanos;

			if (last != isNanos())
				convert(isNanos());
			return last;
		}

		public void start(String name) {
			long stamp = getStamp(isNanos());
			if (starts.containsKey(name))
				elapses.put(name, time(name));

			starts.put(name, stamp);
		}

		public long stop(String name) {
			long mark = getStamp(isNanos());

			if (!starts.containsKey(name))
				return 0;

			long diff = mark - starts.get(name);
			stops.put(name, mark);
			starts.remove(name);
			elapses.put(name, diff);

			return diff;
		}

		public long time(String name) {
			long mark = getStamp(isNanos());
			if (starts.containsKey(name) && !stops.containsKey(name))
				return mark - starts.get(name);
			else if (stops.containsKey(name) && elapses.containsKey(name))
				return elapses.get(name);
			else
				return 0;
		}
	}
}
