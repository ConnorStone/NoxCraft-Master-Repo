package com.noxpvp.core.gui;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.listeners.NoxListener;

public class AttributeHider extends NoxListener<NoxCore> {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Fields
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private Field	cC;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constructors
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public AttributeHider() {
		super(PacketType.Play.Server.WINDOW_ITEMS,
				PacketType.Play.Server.CUSTOM_PAYLOAD,
				PacketType.Play.Server.SET_SLOT);
		
		try {
			cC = EntityPlayer.class.getDeclaredField("containerCounter");
			cC.setAccessible(true);
		} catch (final NoSuchFieldException e) {
			unRegister();
		} catch (final SecurityException e) {
			unRegister();
		}
		
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Static Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static ItemStack removeAttributes(ItemStack i) {
		if (i == null)
			return i;
		if (i.getType() == Material.BOOK_AND_QUILL)
			return i;
		final ItemStack item = i.clone();
		final net.minecraft.server.v1_7_R3.ItemStack nmsStack = CraftItemStack
				.asNMSCopy(item);
		NBTTagCompound tag;
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}
		else {
			tag = nmsStack.getTag();
		}
		final NBTTagList am = new NBTTagList();
		tag.set("AttributeModifiers", am);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}
	
	public static net.minecraft.server.v1_7_R3.ItemStack removeAttributes(
			net.minecraft.server.v1_7_R3.ItemStack i) {
		if (i == null)
			return i;
		if (net.minecraft.server.v1_7_R3.Item.b(i.getItem()) == 386)
			return i;
		final net.minecraft.server.v1_7_R3.ItemStack item = i.cloneItemStack();
		NBTTagCompound tag;
		if (!item.hasTag()) {
			tag = new NBTTagCompound();
			item.setTag(tag);
		}
		else {
			tag = item.getTag();
		}
		final NBTTagList am = new NBTTagList();
		tag.set("AttributeModifiers", am);
		item.setTag(tag);
		return item;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Instance Methods
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public void onPacketReceiving(PacketEvent event) {
		super.onPacketReceiving(event);
	}
	
	@Override
	public void onPacketSending(PacketEvent event) {
		
		final PacketContainer packet = event.getPacket();
		final PacketType type = packet.getType();
		if (type == PacketType.Play.Server.WINDOW_ITEMS) {
			try {
				final ItemStack[] read = packet.getItemArrayModifier().read(0);
				for (int i = 0; i < read.length; i++) {
					read[i] = removeAttributes(read[i]);
				}
				packet.getItemArrayModifier().write(0, read);
			} catch (final FieldAccessException e) {
				Logger.getLogger(AttributeHider.class.getName()).log(Level.SEVERE,
						null, e);
			}
		}
		else if (type == PacketType.Play.Server.CUSTOM_PAYLOAD) {
			if (!packet.getStrings().read(0).equalsIgnoreCase("MC|TrList"))
				return;
			try {
				final EntityPlayer p = ((CraftPlayer) event.getPlayer()).getHandle();
				final ContainerMerchant cM = (ContainerMerchant) p.activeContainer;
				final Field fieldMerchant = cM.getClass().getDeclaredField(
						"merchant");
				fieldMerchant.setAccessible(true);
				final IMerchant imerchant = (IMerchant) fieldMerchant.get(cM);
				
				final MerchantRecipeList merchantrecipelist = imerchant.getOffers(p);
				final MerchantRecipeList nlist = new MerchantRecipeList();
				for (final Object orecipe : merchantrecipelist) {
					final MerchantRecipe recipe = (MerchantRecipe) orecipe;
					final int uses = recipe.i().getInt("uses");
					final int maxUses = recipe.i().getInt("maxUses");
					final MerchantRecipe nrecipe = new MerchantRecipe(
							removeAttributes(recipe.getBuyItem1()),
							removeAttributes(recipe.getBuyItem2()),
							removeAttributes(recipe.getBuyItem3()));
					nrecipe.a(maxUses - 7);
					for (int i = 0; i < uses; i++) {
						nrecipe.f();
					}
					nlist.add(nrecipe);
				}
				
				final PacketDataSerializer packetdataserializer = new PacketDataSerializer(
						Unpooled.buffer());
				packetdataserializer.writeInt(cC.getInt(p));
				nlist.a(packetdataserializer);
				final byte[] b = packetdataserializer.array();
				packet.getByteArrays().write(0, b);
				packet.getIntegers().write(0, b.length);
			} catch (final NoSuchFieldException e) {
				Logger.getLogger(AttributeHider.class.getName()).log(Level.SEVERE,
						null, e);
			} catch (final SecurityException e) {
				Logger.getLogger(AttributeHider.class.getName()).log(Level.SEVERE,
						null, e);
			} catch (final IllegalArgumentException e) {
				Logger.getLogger(AttributeHider.class.getName()).log(Level.SEVERE,
						null, e);
			} catch (final IllegalAccessException e) {
				Logger.getLogger(AttributeHider.class.getName()).log(Level.SEVERE,
						null, e);
			} catch (final FieldAccessException e) {
				Logger.getLogger(AttributeHider.class.getName()).log(Level.SEVERE,
						null, e);
			}
		}
		else {
			try {
				packet.getItemModifier().write(0,
						removeAttributes(packet.getItemModifier().read(0)));
			} catch (final FieldAccessException e) {
				Logger.getLogger(AttributeHider.class.getName()).log(Level.SEVERE,
						null, e);
			}
		}
	}
	
}
