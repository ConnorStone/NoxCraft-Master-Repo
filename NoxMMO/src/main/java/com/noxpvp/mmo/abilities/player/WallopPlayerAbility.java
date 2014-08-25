package com.noxpvp.mmo.abilities.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.noxpvp.mmo.abilities.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PlayerAbilityTier;
import com.noxpvp.mmo.abilities.internal.DamagingAbility;
import com.noxpvp.mmo.abilities.internal.DamagingTier;
import com.noxpvp.mmo.abilities.internal.PVPAbility;
import com.noxpvp.mmo.abilities.internal.RangedAbility;
import com.noxpvp.mmo.abilities.internal.RangedTier;
import com.noxpvp.mmo.abilities.player.WallopPlayerAbility.WallopAbilityTier;

public class WallopPlayerAbility extends BasePlayerAbility<WallopAbilityTier>
		implements PVPAbility, DamagingAbility, RangedAbility {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static final String	ABILITY_NAME	= "Wallop";
	private static final String	PERM_NODE		= "wallop";
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public WallopPlayerAbility(OfflinePlayer player) {
		super(ABILITY_NAME, player);
		
		// Tier 1
		addTier(new WallopAbilityTier(this, 1, 6D, 4D) {
			
			@Override
			public AbilityResult<WallopPlayerAbility> execute() {
				
				Bukkit.broadcastMessage("Bam, you hit them");
				
				return new AbilityResult<WallopPlayerAbility>(
						WallopPlayerAbility.this, true);
			}
		});
		
		// Tier 2
		addTier(new WallopAbilityTier(this, 2, 8D, 6D) {
			
			@Override
			public AbilityResult<WallopPlayerAbility> execute() {
				// execution code
				
				return new AbilityResult<WallopPlayerAbility>(
						WallopPlayerAbility.this, true);
			}
		});
		
		// Tier 3
		addTier(new WallopAbilityTier(this, 3, 10D, 6D) {
			
			@Override
			public AbilityResult<WallopPlayerAbility> execute() {
				// execution code
				
				return new AbilityResult<WallopPlayerAbility>(
						WallopPlayerAbility.this, true);
			}
		});
		
		// Tier 4
		addTier(new WallopAbilityTier(this, 4, 12D, 8D) {
			
			@Override
			public AbilityResult<WallopPlayerAbility> execute() {
				// execution code
				
				return new AbilityResult<WallopPlayerAbility>(
						WallopPlayerAbility.this, true);
			}
		});
		
	}
	
	@Override
	public AbilityResult<WallopPlayerAbility> execute() {
		return execute(1);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public AbilityResult<WallopPlayerAbility> execute(int tierLevel) {
		return getTier(tierLevel).execute();
	};
	
	public double getDamage(int tierLevel) {
		return getTier(tierLevel).getDamage();
	}
	
	public double getRange(int tierLevel) {
		return getTier(tierLevel).getRange();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Tier class
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public abstract class WallopAbilityTier extends
			PlayerAbilityTier<WallopPlayerAbility> implements DamagingTier,
			RangedTier {
		
		private final double	damage;
		private final double	range;
		
		public WallopAbilityTier(WallopPlayerAbility ab, int level, double damage,
				double range) {
			super(ab, level);
			
			this.damage = damage;
			this.range = range;
		}
		
		public double getDamage() {
			return damage;
		}
		
		public double getRange() {
			return range;
		}
		
	}
}
