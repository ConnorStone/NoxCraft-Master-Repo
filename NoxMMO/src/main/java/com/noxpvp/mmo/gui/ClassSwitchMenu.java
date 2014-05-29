package com.noxpvp.mmo.gui;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.gui.CoreBoxRegion;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassChooseMenu extends CoreBox {

	public static final String MENU_NAME = "Class Selection";
	
	private static final String MENU_MAIN_COLOR = MMOLocale.GUI_MENU_NAME_COLOR.get();
	private static final int size = 18;

	public ClassChooseMenu(Player p, @Nullable CoreBox backButton) {
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size, backButton);

		Inventory box = getBox();
		List<PlayerClass> availableClasses = PlayerClassUtil.getAvailableClasses(p);

		ItemStack primarySign = new ItemStack(Material.SIGN);
		ItemStack secondarySign = new ItemStack(Material.SIGN);

		ItemMeta pMeta = primarySign.getItemMeta();
		ItemMeta sMeta = secondarySign.getItemMeta();

		String lorePrefix = ChatColor.GREEN.toString() + ChatColor.ITALIC;

		pMeta.setDisplayName(MENU_MAIN_COLOR + "Pick a primary class");
		sMeta.setDisplayName(MENU_MAIN_COLOR + "Pick a Secondary class");

		pMeta.setLore(Arrays.asList(lorePrefix + "Click an item on this row", lorePrefix + "to select a primary class"));
		sMeta.setLore(Arrays.asList(lorePrefix + "Click an item on this row", lorePrefix + "to select a secondary class"));

		primarySign.setItemMeta(pMeta);
		secondarySign.setItemMeta(sMeta);

		box.setItem(0, primarySign);
		box.setItem(9, secondarySign);

		CoreBoxRegion primarys = new CoreBoxRegion(this, new Vector(0, 0, 2), 0, 7),
				secondarys = new CoreBoxRegion(this, new Vector(1, 0, 2), 0, 7);

		for (PlayerClass clazz : availableClasses) {

			ClassChooseMenuItem boxItem = new ClassChooseMenuItem(this, clazz.getIdentifiableItem(), clazz) {
				public boolean onClick(InventoryClickEvent click) {

					if (getPlayerClass().isPrimaryClass()) {
						new InnerClassMenu(getPlayer(), getPlayerClass(), ClassChooseMenu.this).show();

						return true;
					} else {
						MMOPlayer mmoPlayer;
						if ((mmoPlayer = MMOPlayerManager.getInstance().getPlayer(getPlayer())) != null) {
							mmoPlayer.setSecondaryClass(getPlayerClass());
							hide();

							return true;
						}

						return false;
					}

				}
			};

			if (clazz.isPrimaryClass()) {
				primarys.add(boxItem);

			} else {
				secondarys.add(boxItem);
			}
		}
	}
	
	@Override
	public ItemStack getIdentifiableItem() {
		ItemStack item = super.getIdentifiableItem();
		item.setType(Material.DIAMOND_CHESTPLATE);
		
		return item;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ClassChooseMenu(getPlayer(), null);

	}

	public abstract class ClassChooseMenuItem extends CoreBoxItem {

		private PlayerClass pClass;

		public ClassChooseMenuItem(ClassChooseMenu parent, ItemStack item, PlayerClass clazz) {
			super(parent, item);

			this.pClass = clazz;
		}

		public PlayerClass getPlayerClass() {
			return this.pClass;
		}

	}
	
}
