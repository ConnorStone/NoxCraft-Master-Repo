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

import static org.junit.Assert.*;

import org.bukkit.ChatColor;
import org.json.JSONStringer;
import org.junit.Ignore;
import org.junit.Test;

import com.noxpvp.core.utils.gui.TellRawUtil;
import com.noxpvp.core.utils.gui.TellRawUtil.JSONBuilder.FLAGS;

public class JSONBuilderTest {

	@Test
	public void testChildlessJSON() {
		String expected = new JSONStringer().object().key("text").value("Test 123").key("color").value(ChatColor.BLUE).endObject().toString();
		assertEquals(expected, new TellRawUtil.JSONBuilder(ChatColor.BLUE, "Test 123").getJson());
	}

	@Test
	public void testEmbeddedFormat() {
		String expected = new JSONStringer().object()
				.key("text").value("")
				.key("extra").array().object()
				.key("text").value("OMG TEST")
				.key("color").value(ChatColor.BLUE)
				.key("italic").value(true)
				.endObject().endArray().endObject().toString();
		String actual = new TellRawUtil.JSONBuilder().addEnclosedText(ChatColor.BLUE, "OMG TEST", FLAGS.ITALIC).getJson();

		assertEquals(expected, actual);
	}

	@Test
	public void bitTest() {
		int expected = 1 | 2 | 4;

		int actual = FLAGS.BOLD | FLAGS.UNDERLINE | FLAGS.ITALIC;
		assertEquals(expected, actual);
	}

	@Test
	public void testChildlessFormattedJSON() {
		String expected = new JSONStringer().object().key("text").value("Test 123").key("color").value(ChatColor.BLUE).key("italic").value(true).endObject().toString();
		assertEquals(expected, new TellRawUtil.JSONBuilder(ChatColor.BLUE, "Test 123", FLAGS.ITALIC).getJson());
	}

	@Test
	public void testChildlessMultiFormattedJSON() {
		String expected = new JSONStringer().object().key("text").value("Test 123").key("color").value(ChatColor.BLUE).key("bold").value(true).key("italic").value(true).endObject().toString();
		assertEquals(expected, new TellRawUtil.JSONBuilder(ChatColor.BLUE, "Test 123", FLAGS.BOLD | FLAGS.ITALIC).getJson());
	}

	@Test
	public void testEmbeddedTest() {
		String expected = new JSONStringer().object()
				.key("text").value("")
				.key("extra").array()
				.object()
				.key("text").value("Embedded")
				.key("color").value(ChatColor.DARK_RED)
				.endObject()
				.endArray()
				.endObject().toString();


		String json = new TellRawUtil.JSONBuilder().addEnclosedText(ChatColor.DARK_RED, "Embedded").getJson();
		assertEquals(expected, json);
	}


	@Test
	@Ignore
	public void test() {
		fail("Not yet implemented");
	}

}
