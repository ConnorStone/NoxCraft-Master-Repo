package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class SeveringStrikesAbility extends BasePlayerAbility implements PassiveAbility<EntityDamageByEntityEvent>{
	
	//TODO make this
	
	public static final String ABILITY_NAME = "Severing Strikes";
	public static final String PERM_NODE = "severing-strikes";
	
	private int bleed;
	
	/**
	 * 
	 * @param player The user of the ability instance
	 */
	public SeveringStrikesAbility(Player player){
		super(ABILITY_NAME, player);

	}

	public boolean execute() {
		return true;
	}
	
	public boolean execute(EntityDamageByEntityEvent event) {
		if (!mayExecute() || event.getDamager() != getPlayer())
			return false;
		
		Entity damaged = event.getEntity();		
		Player p = getPlayer();
		
		if (!(damaged instanceof Damageable))
			return false;
		
		int levels = PlayerManager.getInstance().getPlayer(p).getPrimaryClass().getTotalLevel();
		this.bleed = (20 * levels) / 16;
		
		new DamageRunnable((Damageable) damaged, p, 1*(1+((bleed / 20) / 6)), (bleed / 20) / 3).runTaskTimer(NoxMMO.getInstance(), 30, 30);
		
		return true;
	}
	
}
