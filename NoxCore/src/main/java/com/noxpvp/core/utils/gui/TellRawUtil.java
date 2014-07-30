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

package com.noxpvp.core.utils.gui;

import java.util.Collection;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.noxpvp.core.json.TrackedJSONStringer;

public class TellRawUtil {

	/**
	 * Much safer version of {@link TellRawUtil#sendRaw(Player, String)} that uses {@link JSONBuilder} objects instead.
	 *
	 * @param player  to send to
	 * @param builder message to send
	 */
	public static void sendRaw(Player player, JSONBuilder builder) {
		sendRaw(player, builder.getJson());
	}

	/**
	 * Much safer version of {@link TellRawUtil#sendRaw(Player, String)} that uses {@link JSONBuilder} objects instead.
	 *
	 * @param players to send to
	 * @param builder message to send
	 */
	public static void sendRaw(Player[] players, JSONBuilder builder) {
		sendRaw(players, builder.getJson());
	}

	/**
	 * Much safer version of {@link TellRawUtil#sendRaw(Player, String)} that uses {@link JSONBuilder} objects instead.
	 *
	 * @param players to send to
	 * @param builder message to send
	 */
	public static void sendRaw(Collection<Player> players, JSONBuilder builder) {
		sendRaw(players, builder.getJson());
	}

	/**
	 * This will send json messages to player.
	 * <br>
	 * <b> This is not safe to use by itself. Please use {@link JSONBuilder}</b>
	 *
	 * @param player to send to
	 * @param raw    message to send
	 */
	public static void sendRaw(Player player, String raw) {
		CommonPacket cp = new CommonPacket(PacketType.OUT_CHAT);

		cp.write(PacketType.OUT_CHAT.isFromServer, true);
		cp.write(PacketType.OUT_CHAT.chatComponent, WrappedChatComponent.fromJson(raw));

		PacketUtil.sendPacket(player, cp);
	}

	/**
	 * This will send json messages to player.
	 * <br>
	 * <b> This is not safe to use by itself. Please use {@link JSONBuilder}</b>
	 *
	 * @param players to send to
	 * @param raw     message to send
	 */
	public static void sendRaw(Player[] players, String raw) {
		CommonPacket cp = new CommonPacket(PacketType.OUT_CHAT);

		cp.write(PacketType.OUT_CHAT.isFromServer, true);
		cp.write(PacketType.OUT_CHAT.chatComponent, WrappedChatComponent.fromJson(raw));

		for (Player player : players)
			PacketUtil.sendPacket(player, cp);
	}

	/**
	 * This will send json messages to player.
	 * <br>
	 * <b> This is not safe to use by itself. Please use {@link JSONBuilder}</b>
	 *
	 * @param players to send to
	 * @param raw     message to send
	 */
	public static void sendRaw(Collection<Player> players, String raw) {
		CommonPacket cp = new CommonPacket(PacketType.OUT_CHAT);

		cp.write(PacketType.OUT_CHAT.isFromServer, true);
		cp.write(PacketType.OUT_CHAT.chatComponent, WrappedChatComponent.fromJson(raw));

		for (Player player : players)
			PacketUtil.sendPacket(player, cp);
	}

	/*
	 *
	 * /tellraw @a { text:"TEST1", color:yellow, bold:true, underlined:true,
	 * italic:true, strikethrough:true, obfuscated:true }
	 */
	public static class JSONBuilder {
		private TrackedJSONStringer json;
		private boolean closed = false;
		private int children = 0;
		public JSONBuilder() {
			this(null);
		}

		public JSONBuilder(String text) {
			this(null, text, 0);
		}

		public JSONBuilder(ChatColor color, String text) {
			this(color, text, 0);
		}

		public JSONBuilder(ChatColor color, int bits) {
			this(color, "", bits);
		}

		public JSONBuilder(ChatColor color, @Nullable String initialText, int bits) {
			if (initialText == null)
				initialText = "";

			//Do not accept format codes. This will be deprecated!
			if (color != null && color.isFormat())
				color = null;

			json = new TrackedJSONStringer();

			json.object();
			json.key("text").value(initialText);

			if (color != null)
				json.key("color").value(color);

			bif(json, bits, 0);

//			json.key("extra").array();
		}

		private void closeCheck() {
			if (closed)
				throw new IllegalStateException("Json builder object is already closed. You must specify a new builder!");
		}

		public final boolean isClosed() {
			return closed;
		}

		/**
		 * USE THIS METHOD FOR EVERY OPERATION ON STRINGER!
		 *
		 * @return safe json or throws IllegalStateException for builder already being closed.
		 * @throws IllegalStateException
		 */
		protected TrackedJSONStringer getStringer() {
			closeCheck();
			return json;
		}

		public JSONBuilder addEnclosedText(String text) {
			return addEnclosedText(null, text);
		}

		public JSONBuilder addEnclosedText(ChatColor color, String text) {
			return addEnclosedText(color, text, 0, 0);
		}

		public JSONBuilder addEnclosedText(ChatColor color, String text, int bits) {
			return addEnclosedText(color, text, bits, 0);
		}

		public JSONBuilder addEnclosedText(ChatColor color, String text, int onBits, int offBits) {
			TrackedJSONStringer b = getStringer();
			if (color != null && color.isFormat()) {
				onBits = FLAGS.combineBits(onBits, FLAGS.valueOf(color));
				color = null;
			}
			if (b.isCurrentlyArray()) {
				b.object();
			} else if (b.isCurrentlyObject()) {
				b.key("extra").array();
				return addEnclosedText(color, text, onBits, offBits);
			}

			b.key("text").value(text);

			if (color != null)
				b.key("color").value(color);

			bif(b, onBits, offBits);
			if (!b.isCurrentlyObject())
				throw new IllegalStateException("Corrupted json code. Was expecting an open object.");

			b.endObject();

			return this;
		}

		public JSONBuilder addOpenText(String text) {
			return addOpenText(null, text);
		}

		public JSONBuilder addOpenText(ChatColor color, String text) {
			return addOpenText(color, text, 0, 0);
		}

		public JSONBuilder addOpenText(ChatColor color, String text, int bits) {
			return addOpenText(color, text, bits, 0);
		}

		public JSONBuilder addOpenText(ChatColor color, String text, int onBits, int offBits) {
			TrackedJSONStringer b = getStringer();
			if (color != null && color.isFormat()) {
				onBits = FLAGS.combineBits(onBits, FLAGS.valueOf(color));
				color = null;
			}
			if (b.isCurrentlyArray()) {
				b.object();
			} else if (b.isCurrentlyObject()) {
				b.key("extra").array();
				return addEnclosedText(color, text, onBits, offBits);
			}

			b.key("text").value(text);

			if (color != null)
				b.key("color").value(color);

			bif(b, onBits, offBits);
			if (!b.isCurrentlyObject())
				throw new IllegalStateException("Corrupted json code. Was expecting an open object.");

			b.key("extra").array();
			children++;

			return this;
		}

		public JSONBuilder closeCurrentText() {
			if (children <= 0)
				return this;

			TrackedJSONStringer b = getStringer();
			if (!b.isCurrentlyArray())
				throw new IllegalStateException("Corrupted json code. Was expecting an open Array.");
			b.endArray();

			children--;
			return this;
		}

		/**
		 * BIF: Build Internal Format<br/><br/>
		 * <b>
		 * INTERNAL METHOD ONLY. DO NOT USE!!!!<br/>
		 * USING WILL RISK DAMAGING YOUR JSON TEXT!
		 * </b>
		 */
		private void bif(TrackedJSONStringer b, int onBits, int offBits) {
			int bv = (offBits & FLAGS.BOLD) - (onBits & FLAGS.BOLD), v = FLAGS.BOLD, nv = -FLAGS.BOLD;
			if (bv == v)
				b.key("bold").value(false);
			else if (bv == nv)
				b.key("bold").value(true);

			v = FLAGS.ITALIC;
			nv = -FLAGS.ITALIC;
			bv = (offBits & v) - (onBits & v);
			if (bv == v)
				b.key("italic").value(false);
			else if (bv == nv)
				b.key("italic").value(true);


			v = FLAGS.STRIKETHROUGH;
			nv = -FLAGS.STRIKETHROUGH;
			bv = (offBits & v) - (onBits & v);
			if (bv == v)
				b.key("strikethrough").value(false);
			else if (bv == nv)
				b.key("strikethrough").value(true);

			v = FLAGS.UNDERLINE;
			nv = FLAGS.UNDERLINE;
			bv = (offBits & v) - (onBits & v);
			if (bv == v)
				b.key("underlined").value(false);
			else if (bv == nv)
				b.key("underlined").value(true);

			v = FLAGS.OBFUSCATED;
			nv = FLAGS.OBFUSCATED;
			bv = (offBits & v) - (onBits & v);
			if (bv == v)
				b.key("obfuscated").value(false);
			else if (bv == nv)
				b.key("obfuscated").value(true);
		}

		public JSONBuilder close() {
			getStringer().close();
			closed = true;

			return this;
		}

		public String getJson() {
			return getJson(true);
		}

		public String getJson(boolean autoClose) {
			if (!closed && autoClose)
				close();

			return json.toString(); //Only time we bypass getStringer()
		}

		public static class FLAGS {
			public static final int BOLD = 1;
			public static final int UNDERLINE = 2;
			public static final int ITALIC = 4;
			public static final int STRIKETHROUGH = 8;
			public static final int OBFUSCATED = 16;

			public static int valueOf(ChatColor color) {
				switch (color) {
					case BOLD:
						return BOLD;
					case UNDERLINE:
						return UNDERLINE;
					case ITALIC:
						return ITALIC;
					case STRIKETHROUGH:
						return STRIKETHROUGH;
					case MAGIC:
						return OBFUSCATED;
					default:
						return 0;
				}
			}

			public static int combineBits(int... bits) {
				int r = 0;
				for (int b : bits)
					r |= b;
				return r;
			}

		}
	}

}
