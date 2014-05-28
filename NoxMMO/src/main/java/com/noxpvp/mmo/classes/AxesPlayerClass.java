package com.noxpvp.mmo.classes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.mmo.classes.internal.ClassType;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.IClassTier;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.classes.tiers.*;

public class AxesPlayerClass extends PlayerClass {

	public static final String className = "Axes";

	public static final String uniqueID = "f8c26f34-fc36-427a-b92e-94090b146db1";    //RANDOMLY GENERATED DO NOT CHANGE!
	private ItemStack identiferItem;

	public AxesPlayerClass(Player player) {
		super(uniqueID, className, player);
	}

	public AxesPlayerClass(String playerName, Player player) {
		super(uniqueID, className, playerName, player);
	}

	public AxesPlayerClass(String playerName) {
		super(uniqueID, className, playerName);
	}

	@Override
	public String getDescription() {
		return "Needs description";
	}
	
	public boolean isPrimaryClass() {
		return true;
	}

	public ClassType getPrimaryClassType() {
		return ClassType.Axes;
	}

	public ClassType[] getSubClassTypes() {
		return new ClassType[0];
	}

	public int getHighestPossibleTier() {
		return 4;
	}

	@Override
	public ItemStack getIdentifiableItem() {
		ItemStack s = super.getIdentifiableItem();
		s.setType(Material.DIAMOND_AXE);
		
		return s;
	}

	public Color getRBGColor() {
		return Color.fromRGB(215, 0, 0);
	}

	public Color getBaseArmourColor() {
		return ((LeatherArmorMeta) new ItemStack(Material.LEATHER_HELMET).getItemMeta()).getColor();
	}

	public ChatColor getColor() {
		return ChatColor.RED;
	}

	public ExperienceType[] getExpTypes() {
		return getTier().getExpTypes();
	}

	public boolean canUseTier(int tier) {
		return true;//TODO this
	}

	@Override
	protected Map<Integer, IClassTier> craftClassTiers() {
		this.tiers = new HashMap<Integer, IClassTier>();

		tiers.put(1, new AxesBasherClassTier(this));
		tiers.put(2, new AxesChampionClassTier(this));
		tiers.put(3, new AxesBerserkerClassTier(this));
		tiers.put(4, new AxesWarlordClassTier(this));

		return this.tiers;
	}

	@Override
	public void load(ConfigurationNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(ConfigurationNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	protected FileConfiguration getClassConfig() {
		// TODO Auto-generated method stub
		return null;
	}
}
