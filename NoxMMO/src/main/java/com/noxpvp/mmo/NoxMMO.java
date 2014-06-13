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

package com.noxpvp.mmo;

import java.util.logging.Level;

import com.noxpvp.mmo.util.PlayerClassUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.permissions.PermissionDefault;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.reflection.SafeConstructor;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.commands.Command;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.permissions.NoxPermission;
import com.noxpvp.core.reloader.*;
import com.noxpvp.core.utils.StaticCleaner;
import com.noxpvp.mmo.abilities.entity.*;
import com.noxpvp.mmo.abilities.player.*;
import com.noxpvp.mmo.abilities.player.AutoToolPlayerAbilities.*;
import com.noxpvp.mmo.abilities.ranged.*;
import com.noxpvp.mmo.abilities.targeted.*;
import com.noxpvp.mmo.classes.*;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.command.AbilityCommand;
import com.noxpvp.mmo.command.ClassCommand;
import com.noxpvp.mmo.command.MMOCommand;
import com.noxpvp.mmo.listeners.*;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.prism.MMOPrismUtil;


public class NoxMMO extends NoxPlugin {
	public static final String PERM_NODE = "nox.mmo";

	private static NoxMMO instance;
	AbilityListener abilityListener;
	DamageListener damageListener;
	HealListener healListener;
	PlayerInteractListener playerTargetListener;
	BlockListener blockListener;
//	ExperienceListener experieneceListener;
	private NoxCore core;
	private PermissionHandler permHandler;
	private FileConfiguration config;
	private FileConfiguration experience;

	private MasterListener masterListener;

	private MMOPlayerManager playerManager = null;

	private Class<Command>[] commands = (Class<Command>[]) new Class[]{ClassCommand.class, AbilityCommand.class, MMOCommand.class};
	private Class<? extends ConfigurationSerializable>[] serializables = new Class[] {AbilityCycler.class, PlayerClass.class};

	public static NoxMMO getInstance() {
		return instance;
	}

	private void setInstance(NoxMMO noxMMO) {
		instance = noxMMO;
	}

	@Override
	public void disable() {
		masterListener.unregisterAll();
		permHandler = null;
		masterListener = null;

		Class<?>[] classes = {
				ShurikenPlayerAbility.class, HammerOfThorPlayerAbility.class,
				HookShotPlayerAbility.class, SeveringStrikesPlayerAbility.class,
				PlayerClass.class, PlayerClassUtil.class,
				AbilityCycler.class, MasterListener.class
		};

		String[] internals = {};

		new StaticCleaner(this, getClassLoader(), internals, classes).resetAll();

		setInstance(null);
	}

	public FileConfiguration getMMOConfig() {
		if (config == null)
			config = new FileConfiguration(this, "config.yml");
		return config;
	}

	public FileConfiguration getExperienceConfig() {
		if (experience == null)
			experience = new FileConfiguration(this, "experience.yml");
		return experience;
	}

	@Override
	public void enable() {
		if (instance != null) {
			log(Level.SEVERE, "This plugin already has an instance running!! Disabling second run.");
			setEnabled(false);
			return;
		}
		setInstance(this);
		Common.loadClasses("com.noxpvp.mmo.classes.internal.DummyClass");
		MasterListener.init();
		masterListener = new MasterListener();


		getPlayerManager();

		core = NoxCore.getInstance();

		PlayerClassUtil.init();
		PlayerClass.init();
		

		abilityListener = new AbilityListener(instance, core.isPrismActive());
		damageListener = new DamageListener(instance);
		healListener = new HealListener(instance);
		playerTargetListener = new PlayerInteractListener(instance);
		blockListener = new BlockListener(instance);
//		experieneceListener = new ExperienceListener(instance);

		abilityListener.register();
		damageListener.register();
		healListener.register();
		playerTargetListener.register();
		blockListener.register();
		permHandler = new PermissionHandler(this);
//		experieneceListener.register();

		Reloader base = new BaseReloader(getMasterReloader(), "NoxMMO") {
			public boolean reload() {
				return true;
			}
		};

		base.addModule(new BaseReloader(base, "config.yml") {

			public boolean reload() {
				reloadConfig();
				return true;
			}
		});

		base.addModule(new BaseReloader(base, "locale") {

			public boolean reload() {
				localization();
				return true;
			}
		});

		base.addModule(new BaseReloader(base, "experience.yml") {

			public boolean reload() {
				getExperienceConfig().load();
				return true;
			}
		});

		registerAllCommands();
		
		//Setup Prism stuff
		try {
			if (core.isPrismActive()) {
				MMOPrismUtil.registerActionTypes();
				MMOPrismUtil.registerCustomHandlers();
			}
		} catch (Exception e) {}
		AbilityCycler.init();
	}

	private void registerAllCommands() {
		for (Class<Command> cls : commands) {
			SafeConstructor<Command> cons = new SafeConstructor<Command>(cls, new Class[0]);
			Command rn = cons.newInstance();
			if (rn != null)
				registerCommand(rn);
		}
	}

	@Override
	public void saveDefaultConfig() {
		//TODO: Add Defaults.
		saveConfig();
	}

	@Override
	public void saveConfig() {

		config.save();
	}

	@Override
	public void reloadConfig() {
		config.load();

	}

	@Override
	public void permissions() {

		//Ability permissions
		addPermission(new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability"), "Base MMO Node", PermissionDefault.FALSE,
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", AutoArmor.PERM_NODE), "Allows usage of the Auto Armor Abilities.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", AutoSword.PERM_NODE), "Allows usage of the Auto Sword Abilities.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", AutoTool.PERM_NODE), "Allows usage of the Auto Tool Abilities.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", BackStabPlayerAbility.PERM_NODE), "Allows usage of the Back Stab Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", BandagePlayerAbility.PERM_NODE), "Allows usage of the Bandage Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", BankShotPlayerAbility.PERM_NODE), "Allows usage of the Bank Shot Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", BoltPlayerAbility.PERM_NODE), "Allows usage of the Bolt Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", CallOfTheWildPlayerAbility.PERM_NODE), "Allows usage of the Call Of The Wild Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", CursePlayerAbility.PERM_NODE), "Allows usage of the Curse Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", DashEntityAbility.PERM_NODE), "Allows usage of the Dash Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", DemoralizingRoarEntityAbility.PERM_NODE), "Allows usage of the Demoralizing Roar Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", DisarmPlayerAbility.PERM_NODE), "Allows usage of the Disarm Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", DrainLifePlayerAbility.PERM_NODE), "Allows usage of the DrainLife Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ExplosiveArrowPlayerAbility.PERM_NODE), "Allows usage of the Explosive Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", FireBallPlayerAbility.PERM_NODE), "Allows usage of the Fire Ball Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", FireNovaEntityAbility.PERM_NODE), "Allows usage of the Fire Nova Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", FireSpinPlayerAbility.PERM_NODE), "Allows usage of the Fire Spin Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ForcePullEntityAbility.PERM_NODE), "Allows usage of the Force Pull Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ThrowPlayerAbility.PERM_NODE), "Allows usage of the Grapple Throw Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", GuardianAngelPlayerAbility.PERM_NODE), "Allows usage of the Guardian Angel Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", HammerOfThorPlayerAbility.PERM_NODE), "Allows usage of the Hammer Of Thor Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", HitVanishedPlayerAbility.PERM_NODE), "Allows usage of the Hit Vanished Players Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", HookShotPlayerAbility.PERM_NODE), "Allows usage of the Hook Shot Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", LeapEntityAbility.PERM_NODE), "Allows usage of the Leap Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MaliciousBiteEntityAbility.PERM_NODE), "Allows usage of the Malicious Bite Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MassDestructionPlayerAbility.PERM_NODE), "Allows usage of the Mass Destruction Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MeasuringTapePlayerAbility.PERM_NODE), "Allows usage of the Measuring Tape Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MedPackPlayerAbility.PERM_NODE), "Allows usage of the Med-Pack Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", MortalWoundPlayerAbility.PERM_NODE), "Allows usage of the Mortal Wound Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", NetArrowPlayerAbility.PERM_NODE), "Allows usage of the Net Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ParryPlayerAbility.PERM_NODE), "Allows usage of the Parry Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", PickPocketPlayerAbility.PERM_NODE), "Allows usage of the Pick Pocket Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", PoisonArrowPlayerAbility.PERM_NODE), "Allows usage of the Poison Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ReincarnatePlayerAbility.PERM_NODE), "Allows usage of the Reincarnation Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", RejuvenationPlayerAbility.PERM_NODE), "Allows usage of the Rejuvenation Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SeveringStrikesPlayerAbility.PERM_NODE), "Allows usage of the Severing Strikes Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ShadowStepPlayerAbility.PERM_NODE), "Allows usage of the Shadow Step Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", ShurikenPlayerAbility.PERM_NODE), "Allows usage of the Shuriken Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SilentWalkingPlayerAbility.PERM_NODE), "Allows usage of the SilentWalking Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", RagePlayerAbility.PERM_NODE), "Allows usage of the Skull Smasher Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SoulStealPlayerAbility.PERM_NODE), "Allows usage of the Soul Steal Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SoothePlayerAbility.PERM_NODE), "Allows usage of the Soothe Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", SuperBreakerPlayerAbility.PERM_NODE), "Allows usage of the Super Breaker Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", TornadoPlayerAbility.PERM_NODE), "Allows usage of the Tornado Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", TracerArrowPlayerAbility.PERM_NODE), "Allows usage of the Tracer Arrow Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", TrackingPlayerAbility.PERM_NODE), "Allows usage of the Tracking Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", VanishPlayerAbility.PERM_NODE), "Allows usage of the Vanish Ability.", PermissionDefault.OP),
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "ability", WhistlePlayerAbility.PERM_NODE), "Allows usage of the Whistle Ability.", PermissionDefault.OP)
		));

		//Class permissions
		addPermission(new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", "*"), "Allow access to all classes.", PermissionDefault.OP,
				new NoxPermission(this, StringUtil.join(".", PERM_NODE, "class", AxesPlayerClass.className), "Allows access to the class named " + AxesPlayerClass.className, PermissionDefault.OP)
		));

	}

	@Override
	public void localization() {

		loadLocales(MMOLocale.class);

//
//		loadLocale("ability.already-active", "&4You cannot use the ability \"%1%\" as it is already active!");
//		loadLocale("ability.activated.default", "&a%1% Activated!");
//
//		//Ability - MedPack
//		loadLocale("ability.medpack.use", "&eMedPack dropped!");
//		loadLocale("ability.medpack.picked-up.other", "&c%1%&e Picked up your dropped medpack!");
//		loadLocale("ability.medpack.picked-up", "&ePicked up %2%'s dropped medpack!");
//
//		//Ability - NetArrow
//		loadLocale("ability.arrow.net.use", getLocale("ability.activated", NetArrowAbility.ABILITY_NAME)); //Dynamic defaults FTW
//		loadLocale("ability.arrow.net.trapped", "&cSomething was caught in the net!"); //%1% should be a list comma delimited of entities or players. Default does not contain it.
//
//		//Ability - Explosive Arrow
//		loadLocale("ability.arrow.explosive", "&eExplosive Arrow Activated!"); //FIXME: Unused
//
		//Ability -

	}

	public MasterListener getMasterListener() {
		return masterListener;
	}

	/**
	 * Gets the player manager.
	 *
	 * @return the player manager
	 * @Deprecated Use {@link MMOPlayerManager#getInstance()} instead
	 */
	public MMOPlayerManager getPlayerManager() {
		MMOPlayerManager c = MMOPlayerManager.getInstance();
		if (playerManager == null)
			playerManager = c;
		else if (playerManager != c)
			playerManager = c;

		return playerManager;
	}

	@Override
	public NoxCore getCore() {
		return core;
	}

	@Override
	public PermissionHandler getPermissionHandler() {
		return permHandler;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends ConfigurationSerializable>[] getSerialiables() {
		return serializables;
	}

}
