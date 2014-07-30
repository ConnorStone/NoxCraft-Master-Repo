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

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.*;

import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.utils.CommandUtil;

//@PrepareOnlyThisForTest(Bukkit.class)
public class CoreCommandParser {


	@Mock
	static CommandSender mockSender;
	@Mock
	static Player mockPlayerSender;
	@Mock
	static Server server;


//	@Rule public PowerMockRule rule = new PowerMockRule();
	static String[] testFlagsExistsTestArgLines = {"come --all", "this --all  test", "oh --all"};

	@BeforeClass
	public static void setup() {
		if (Bukkit.getServer() != null)
			server = Bukkit.getServer();
		else {
			server = mock(Server.class);

		}


		if (!Mockito.mockingDetails(server).isMock())
			throw new IllegalStateException("Server is not mocked");

		Mockito.reset(server);
		when(server.getLogger()).thenReturn(Logger.getLogger("minecraft"));
		when(server.getName()).thenReturn("FakeServer");
		when(server.getVersion()).thenReturn("1.6.4");

		when(server.getBukkitVersion()).thenReturn("FakeBukkit 1.6.4-R9000");


		if (Bukkit.getServer() == null)
			Bukkit.setServer(server);

		mockPlayerSender = mock(Player.class);
		mockSender = mock(CommandSender.class);
		doAnswer((new Answer<Void>() {

			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] objs = invocation.getArguments();
				for (Object o : objs)
					System.out.println(o.toString());
				return null;
			}
		})).when(mockSender).sendMessage(anyString());

		doAnswer((new Answer<Void>() {

			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] objs = invocation.getArguments();
				for (Object o : objs)
					System.out.println(o.toString());
				return null;
			}
		})).when(mockPlayerSender).sendMessage(anyString());

//		PowerMockito.mockStatic(Bukkit.class);
//
//		when(Bukkit.getServer()).thenReturn(server);


	}

	public static <V> Matcher<Map<String, V>> hasSize(final int size) {
		return new TypeSafeMatcher<Map<String, V>>() {
			public boolean matchesSafely(Map<String, V> kvMap) {
				return kvMap.size() == size;
			}

			public void describeTo(Description description) {
				description.appendText(" has ").appendValue(size).appendText(" key/value pairs");
			}
		};
	}

	@Test
	public void testFlagsExists() {
		for (String argLine : testFlagsExistsTestArgLines) {
			CommandContext context = CommandUtil.parseCommand(mockSender, argLine);
			boolean all = context.hasFlag("all");
			assertTrue(new StringBuilder().append('"').append(argLine).append('"').append(" has failed...").append("\n").append(context.getFlags()).toString(), all);
			assertTrue(new StringBuilder().append('"').append(argLine).append('"').append(" has failed...").append("\n").append(context.getFlags()).toString(), context.getFlag("all") instanceof Boolean);
		}
	}

	@Test
	public void testBlankFlagArg() {
		String argLine = "test -p  ha";
		CommandContext context = CommandUtil.parseCommand(mockSender, argLine);
		Map<String, Object> res = context.getFlags();
		assertThat(res, allOf(hasSize(1), hasEntry("p", (Object) true)));
		assertThat(context.getArguments(), allOf(hasItemInArray("test"), hasItemInArray("ha")));
		assertTrue("Had Extra arguments!", context.getArgumentCount() < 3);
	}

	@Test
	public void testSingleFlagValue() {
		String argLine = "test -p test";
		CommandContext context = CommandUtil.parseCommand(mockSender, argLine);
		Map<String, Object> res = context.getFlags();
		assertThat(res, allOf(hasSize(1), hasEntry("p", (Object) "test")));
	}

	@Test
	public void testMixedArgFlag() {
		String argLine = "test-poo";
		CommandContext context = CommandUtil.parseCommand(mockSender, argLine);
		Map<String, Object> res = context.getFlags();
		assertThat(res, hasSize(0));
	}

	@Test
	public void testLongSingleFlagValue() {
		String argLine = "test --player test";
		CommandContext context = CommandUtil.parseCommand(mockSender, argLine);
		Map<String, Object> res = context.getFlags();
		assertThat(res, allOf(hasSize(1), hasEntry("player", (Object) "test")));
	}

	@Test
	public void testMultiFlag() {
		String argLine = "oh -test";
		CommandContext context = CommandUtil.parseCommand(mockSender, argLine);

		Map<String, Object> res = context.getFlags();

		assertThat(res, allOf(hasSize(3), //the repeated t is actually removed due to map limitations.
				hasKey("t"),
				hasKey("e"),
				hasKey("s")
		));
	}

	@Test
	public void testMultiLongFlag() {
		String argLine = "dear --test --flag --three";
		CommandContext context = CommandUtil.parseCommand(mockSender, argLine);
		Map<String, Object> res = context.getFlags();

		assertThat(res, allOf(hasSize(3),
				hasKey("test"),
				hasKey("flag"),
				hasKey("three")
		));
	}
}
