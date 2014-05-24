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
import com.noxpvp.mmo.locale.MMOLocale;

/**
 * @author NoxPVP
 */
public class FlyPlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Fly";
	public static final String PERM_NODE = "fly";

	public static List<Player> flyers = new ArrayList<Player>();

	private ItemStack reg;
	private int regFreq;

	/**
	 * @param player - This user for this ability instance
	 */
	public FlyPlayerAbility(Player player) {
		super(ABILITY_NAME, player);

		this.reg = new ItemStack(Material.FEATHER, 1);
		this.regFreq = 15;
	}

	/**
	 * @return ItemStack - The currently set Regent for this ability
	 */
	public ItemStack getReg() {
		return reg;
	}

	/**
	 * @param reg - The ItemStack for this ability, including correct amount and type
	 * @return FlyAbility - This instance, used for chaining
	 */
	public FlyPlayerAbility setReg(ItemStack reg) {
		this.reg = reg;
		return this;
	}

	/**
	 * @return Integer - The amount of seconds to wait between regent collecting checks
	 */
	public int getRegFreq() {
		return regFreq;
	}

	/**
	 * @param regFreq - The amount of seconds the ability should wait between collecting regents
	 * @return FlyAbility - This instance, used for chaining
	 */
	public FlyPlayerAbility setRegFreq(int regFreq) {
		this.regFreq = regFreq;
		return this;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();

		if (FlyPlayerAbility.flyers.contains(p)) {
			FlyPlayerAbility.flyers.remove(p);
			p.setAllowFlight(false);
			p.setFlying(false);

			return new AbilityResult(this, true, MMOLocale.ABIL_DEACTIVATED.get(getName()));
		}
		
		Inventory i = p.getInventory();
		if (!i.containsAtLeast(getReg(), getReg().getAmount()))
			return new AbilityResult(this, false,
					MMOLocale.ABIL_NO_TARGET.get(Integer.toString(getReg().getAmount()), getReg().getType().name().toLowerCase()));

		i.removeItem(getReg());
		p.updateInventory();
		FlyPlayerAbility.flyers.add(p);
		p.setAllowFlight(true);

		new FlyRunnable(p, reg).runTaskTimer(NoxMMO.getInstance(), getRegFreq() * 20, getRegFreq() * 20);

		return new AbilityResult(this, true, MMOLocale.ABIL_ACTIVATED.get(getName()));
	}

	private class FlyRunnable extends BukkitRunnable {
		private ItemStack regent;
		private Player p;
		private Inventory i;

		public FlyRunnable(Player p, ItemStack regent) {
			this.p = p;
			this.regent = regent;
			this.i = p.getInventory();
		}

		public void safeCancel() {
			try {
				cancel();
			} catch (IllegalStateException e) {
			}
		}

		public void run() {
			if (p == null || !p.isOnline() || !p.getAllowFlight() || !flyers.contains(p)) {
				safeCancel();
				return;
			}
			if (!i.containsAtLeast(regent, regent.getAmount())) {
				p.setFlying(false);
				p.setAllowFlight(false);

				FlyPlayerAbility.flyers.remove(p);

				safeCancel();
				return;
			} else {
				i.removeItem(regent);
				p.updateInventory();
			}
		}
	}
}