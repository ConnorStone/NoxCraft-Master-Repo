package com.noxpvp.mmo;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public interface AbilityCyclerContainer {
	
	public void addAbilityCycler(AbilityCycler cycler);
	
	public AbilityCycler getCycler(ItemStack item);
	
	public AbilityCycler getCycler(UUID id);
	
	public boolean hasAbilityCycler(ItemStack item);
	
	public boolean hasAbilityCycler(UUID id);
	
}
