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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class FlyAbility extends BasePlayerAbility{
	
	public static final String ABILITY_NAME = "Fly";
	public static final String PERM_NODE = "fly";
	
	public static List<Player> flyers = new ArrayList<Player>();
	
	private ItemStack reg;
	private int regFreq;
	
	/**
	 * 
	 * @return ItemStack - The currently set Regent for this ability
	 */
	public ItemStack getReg() {return reg;}
	
	/**
	 * 
	 * @param reg - The ItemStack for this ability, including correct amount and type
	 * @return FlyAbility - This instance, used for chaining
	 */
	public FlyAbility setReg(ItemStack reg) {this.reg = reg; return this;}
	
	/**
	 * 
	 * @return Integer - The amount of seconds to wait between regent collecting checks
	 */
	public int getRegFreq() {return regFreq;}
	
	/**
	 * 
	 * @param regFreq - The amount of seconds the ability should wait between collecting regents
	 * @return FlyAbility - This instance, used for chaining
	 */
	public FlyAbility setRegFreq(int regFreq) {this.regFreq = regFreq; return this;}
	
	/**
	 * 
	 * @param player - This user for this ability instance
	 */
	public FlyAbility(Player player) {
		super(ABILITY_NAME, player);
		
		this.reg = new ItemStack(Material.FEATHER, 1);
		this.regFreq = 15;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		
		if (FlyAbility.flyers.contains(p)){
			FlyAbility.flyers.remove(p);
			p.setAllowFlight(false);
			p.setFlying(false);
			
			return true;
		}
		Inventory i = p.getInventory();
		if (!i.containsAtLeast(getReg(), getReg().getAmount())) return false;
		
		i.removeItem(getReg());
		p.updateInventory();
		FlyAbility.flyers.add(p);
		p.setAllowFlight(true);
		
		new FlyRunnable(p, reg).runTaskTimer(NoxMMO.getInstance(), getRegFreq() * 20, getRegFreq() * 20);
		
		return true;
	}
	
	private class FlyRunnable extends BukkitRunnable{
		private ItemStack regent;
		private Player p;
		private Inventory i;
		
		public FlyRunnable(Player p, ItemStack regent){
			this.p = p;
			this.regent = regent;
			this.i = p.getInventory();
		}
		
		public void safeCancel() {try { cancel(); } catch (IllegalStateException e) {}}
		
		public void run(){
			if (p == null || !p.isOnline() || !p.getAllowFlight() || !flyers.contains(p)) {
				safeCancel();
				return;
			}
			if (!i.containsAtLeast(regent, regent.getAmount())){
				p.setFlying(false);
				p.setAllowFlight(false);
				
				FlyAbility.flyers.remove(p);
				
				safeCancel();
				return;
			} else {
				i.removeItem(regent);
				p.updateInventory();
			}
		}
	}
}