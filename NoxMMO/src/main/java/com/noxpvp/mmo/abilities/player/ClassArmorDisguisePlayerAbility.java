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

package com.noxpvp.mmo.abilities.player;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutEntityEquipment;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.manager.MMOPlayerManager;

public class ClassArmorDisguisePlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Armor Disguise";
	public static final String PERM_NODE = "armor-disguise";

	private CommonPacket packet;

	public ClassArmorDisguisePlayerAbility(Player p, CommonPacket packet) {
		super(ABILITY_NAME, p);

		this.packet = packet;
	}

	public AbilityResult execute() {
		NMSPacketPlayOutEntityEquipment nms = new NMSPacketPlayOutEntityEquipment();

		ItemStack stack = packet.read(nms.item);

		if (stack != null && packet.read(nms.slot) != 0) {
			String name = stack.getType().name();
			stack.setType(Material.AIR);

			if (name.contains("HELMET")) {
				stack.setType(Material.LEATHER_HELMET);

			} else if (name.contains("CHESTPLATE")) {
				stack.setType(Material.LEATHER_CHESTPLATE);

			} else if (name.contains("LEGGING")) {
				stack.setType(Material.LEATHER_LEGGINGS);

			} else if (name.contains("BOOT")) {
				stack.setType(Material.LEATHER_BOOTS);

			}

			if (stack.getType() != Material.AIR) {

				IPlayerClass clazz = MMOPlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();
				if (clazz == null)
					return new AbilityResult(this, false);

				LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();

				if (clazz.getRBGColor() != null)
					meta.setColor(clazz.getRBGColor());

				if (clazz.getLevel() == clazz.getMaxLevel())
					meta.addEnchant(Enchantment.OXYGEN, 1, true);//fake enchant for maxed tiers

				stack.setItemMeta(meta);
				packet.write(nms.item, stack);

			} else return new AbilityResult(this, false);
		} else return new AbilityResult(this, false);

		return new AbilityResult(this, true);

	}

}
