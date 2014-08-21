package com.noxpvp.mmo.gui;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.bergerkiller.bukkit.common.utils.ItemUtil;
import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.gui.CoreBoxRegion;
import com.noxpvp.core.gui.ToggleCoreBoxItem;
import com.noxpvp.core.utils.BukkitUtil;
import com.noxpvp.mmo.AbilityCycler;
import com.noxpvp.mmo.abilities.internal.PlayerAbility;
import com.noxpvp.mmo.abilities.internal.TieredAbility;

public class AbilityBindMenu extends CoreBox {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static final String	MENU_NAME	= "Ability Binding";
	public static final int		MENU_SIZE	= 18;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final AbilityCycler	abilityCycler;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AbilityBindMenu(Player p, AbilityCycler cycler) {
		this(p, cycler, null);
	}
	
	public AbilityBindMenu(Player p, AbilityCycler cycler,
			@Nullable CoreBox backButton) {
		super(p, MENU_NAME, MENU_SIZE, backButton);
		
		abilityCycler = cycler;
		
		final List<PlayerAbility> curBindedAbs = cycler.getAbilityList();
		final Set<PlayerAbility> playerAbs = cycler.getMMOPlayer().getAbilities();
		final CoreBoxRegion abilityRegion = new CoreBoxRegion(this, new Vector(1, 0,
				0), 2, 9);
		
		// Make menu bigger if there are more abilities
		if (playerAbs.size() > 9) {
			setBox(BukkitUtil.createInventory(null, 9 * 3, MENU_NAME));
		}
		
		// Cycler Display
		updateCyclerItem();
		
		for (final PlayerAbility ab : playerAbs) {
			final AbilityBindToggle abt = new AbilityBindToggle(this, ab
					.getIdentifiableItem(), ab, curBindedAbs.contains(ab)) {
				
				@Override
				public void onToggle(InventoryClickEvent click) {
					
					// Add / remove ability from cycler
					if (getToggleState()) {
						abilityCycler.add(getAbility().getName());
					} else {
						abilityCycler.remove(getAbility().getName());
					}
					
					// Update item
					final ItemStack newItem = ab.getIdentifiableItem();
					ItemUtil.setDisplayName(newItem,
							(getToggleState() ? ChatColor.GREEN + "Binded: "
									: ChatColor.RED + "Unbound: ")
									+ ab.getName());
					
					setItem(newItem);
					
					// Update Main menu item
				}
			};
		}
		
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private void updateCyclerItem() {
		addMenuItem(4, new CoreBoxItem(this, abilityCycler.getIdentifiableItem()) {
			
			public boolean onClick(InventoryClickEvent click) {
				return false;
			}
		});
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Ability Bind item
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private abstract class AbilityBindToggle extends ToggleCoreBoxItem {
		
		private final boolean		onOff;
		private final TieredAbility	ability;
		
		public AbilityBindToggle(CoreBox parent, ItemStack item,
				TieredAbility ability, boolean startingPosition) {
			super(parent, item);
			
			this.ability = ability;
			onOff = startingPosition;
		}
		
		public TieredAbility getAbility() {
			return ability;
		}
		
		public boolean getToggleState() {
			return onOff;
		}
		
	}
	
}
