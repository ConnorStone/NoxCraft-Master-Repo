package com.noxpvp.mmo.abilities.player;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;

//FIXME not made yet

public class JoustAbility extends BasePlayerAbility implements IPVPAbility {

	public final static String ABILITY_NAME = "Joust";
	public final static String PERM_NODE = "joust";
	
	private EntityDamageByEntityEvent event;
	
	public JoustAbility(Player p, EntityDamageByEntityEvent event) {
		super(ABILITY_NAME, p);
		
		this.event = event;
	}

	public boolean execute() {
		
		throw new NotImplementedException();
		
//		MMOPlayer mmoP;
//		PlayerClass clazz = (mmoP = PlayerManager.getInstance().getPlayer(getPlayer())) != null? mmoP.getPrimaryClass() : null;
//		
//		if (clazz == null) return false;
//		
//		event.setDamage(event.getDamage() + (((clazz.getTotalLevel() * 100) + clazz.getLevel()) / 75));
//		
//		return true;
	}

}
