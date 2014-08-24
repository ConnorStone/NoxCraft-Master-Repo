package com.noxpvp.mmo.classes.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.Persistent;

public class ClassConfig implements Persistent {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final ModuleLogger		log						= new ModuleLogger(
																	"ClassConfig");
	
	// Data paths -- DO NOT EDIT
	private static final String		PATH_CLASS_NAME			= "class.name";
	private static final String		PATH_CLASS_DESCRIPTION	= "class.description";
	private static final String		PATH_CLASS_MAX_HEALTH	= "class.max-health";
	private static final String		PATH_CLASS_ARMOUR_COLOR	= "class.armour-color";
	private static final String		PATH_CLASS_IS_PRIMARY	= "class.is-primary-class";
	
	private static final String		PATH_ITEMBADGE			= "class.item-badge";
	private static final String		PATH_ITEMBADGE_TYPE		= PATH_ITEMBADGE
																	+ ".type";
	private static final String		PATH_ITEMBADGE_DATA		= PATH_ITEMBADGE
																	+ ".data";
	
	private static final String		PATH_ABILITIES			= "class.abilities";
	private static final String		PATH_MAX_LEVEL			= "experience.max-level";
	private static final String		PATH_FORMULA_BASE		= "experience.formula-base";
	private static final String		PATH_FORMULA_EXPONENT	= "experience.formula-exponent";
	private static final String		PATH_FORMULA_MULTIPLIER	= "experience.formula-multiplier";
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private final FileConfiguration	config;
	
	private final String			className;
	private final String			classDescription;
	private final double			maxHealth;
	private final Color				armourColor;
	private ItemStack				itemBadge;
	private final int				maxLevel;
	private final ExperienceFormula	formula;
	private final List<String>		abilities;
	private final boolean			isPrimaryClass;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public ClassConfig(FileConfiguration config) {
		this.config = config;
		
		config.addHeader(PATH_CLASS_NAME, "The name used in-game for this class");
		className = config.get(PATH_CLASS_NAME, String.class,
				"NO_CLASS_NAME_DEFINED");
		
		classDescription = config.get(PATH_CLASS_DESCRIPTION, String.class,
				"NO_CLASS_DESCRIPTION_DEFINED");
		
		maxHealth = Math.max(1D, config
				.get(PATH_CLASS_MAX_HEALTH, Double.class, 20D));
		
		armourColor = Color.fromRGB(config.get(PATH_CLASS_ARMOUR_COLOR + ".r",
				Integer.class, 160), config.get(PATH_CLASS_ARMOUR_COLOR + ".g",
				Integer.class, 101), config.get(PATH_CLASS_ARMOUR_COLOR + ".b",
				Integer.class, 64));
		
		try {
			itemBadge = new ItemStack(Material.valueOf(config.get(
					PATH_ITEMBADGE_TYPE,
					String.class, "DIAMOND_SWORD")), 1);
		} catch (final IllegalArgumentException e) {
			itemBadge = new ItemStack(Material.DIAMOND_SWORD, 1);
		}
		
		itemBadge.setData(new MaterialData(config.get(PATH_ITEMBADGE_DATA,
				Integer.class, 0)));
		
		maxLevel = config.get(PATH_MAX_LEVEL, Integer.class, 100);
		
		formula = new ExperienceFormula(
				config.get(PATH_FORMULA_EXPONENT, Double.class, 2.5),
				config.get(PATH_FORMULA_MULTIPLIER, Double.class, 1.0),
				config.get(PATH_FORMULA_BASE, Double.class, 2000D));
		
		abilities = config.getList(PATH_ABILITIES, String.class,
				new ArrayList<String>());
		
		isPrimaryClass = config.get(PATH_CLASS_IS_PRIMARY, Boolean.class,
				Boolean.TRUE);
		
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static boolean isClassConfig(FileConfiguration fc) {
		fc.load();
		if (fc.get(PATH_CLASS_NAME, String.class) != null)
			return true;
		
		return false;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public List<String> getAbilities() {
		return abilities;
	}
	
	public Color getArmourColor() {
		return armourColor;
	}
	
	public String getClassDescription() {
		return classDescription;
	}
	
	public String getClassName() {
		return className;
	}
	
	public ExperienceFormula getExpFormula() {
		return formula;
	}
	
	public FileConfiguration getFileConfig() {
		return config;
	}
	
	public ItemStack getItemBadge() {
		return itemBadge;
	}
	
	public double getMaxHealth() {
		return maxHealth;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public String getPersistenceNode() {
		return config.getName().replace(".yml", "");
	}
	
	public UUID getPersistentID() {
		return null;
	}
	
	public boolean isPrimaryClass() {
		return isPrimaryClass;
	}
	
	public void log(Level level, String msg) {
		log.log(level, msg);
	}
	
	public Map<String, Object> serialize() {
		return null;
	}
	
}
