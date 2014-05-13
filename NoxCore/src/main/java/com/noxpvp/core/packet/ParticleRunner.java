package com.noxpvp.core.packet;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.noxpvp.core.NoxCore;

/**
 * @author NoxPVP
 *
 */
public class ParticleRunner extends BukkitRunnable {
	
	private String name;
	
	private Object loc;
	private boolean offSet;
	
	private float data;
	private int amount;
	private int runs;
	
	int i;
	
	public ParticleRunner(ParticleType type, Entity tracker, boolean offSet, float data, int amount, int runs) {
		this(type.name(), tracker, offSet, data, amount, runs);
	}
	
	public ParticleRunner(ParticleType type, LivingEntity tracker, boolean offSet, float data, int amount, int runs) {
		this(type.name(), tracker, offSet, data, amount, runs);
	}
	
	public ParticleRunner(ParticleType type, Location loc, boolean offSet, float data, int amount, int runs) {
		this(type.name(), loc, offSet, data, amount, runs);
	}
	
	public ParticleRunner(String name, Object loc, boolean offSet, float data, int amount, int runs) {
		this.name = name;
		
		this.loc = loc;
		this.offSet = offSet;
		
		this.data = data;
		this.amount = amount;
		this.runs = runs;
		
		this.i = 0;
	}

	public void start(int delay) {
		start(delay, 5);
	}
	
	public void start(int delay, int period) {
		runTaskTimer(NoxCore.getInstance(), delay, period);
	}
	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	
	public void run(){
		Location loc = null;
		
		if (this.loc instanceof LivingEntity)
			loc = ((LivingEntity) this.loc).getEyeLocation();
		else if (this.loc instanceof Entity)
			loc = ((Entity) this.loc).getLocation();
		else if (this.loc instanceof Location)
			loc = (Location) this.loc;
		else throw new IllegalArgumentException("Location must a type of location or entity");
		
		if ((runs > 0 && i >= runs) || ((this.loc instanceof Entity) && !((Entity) this.loc).isValid())) {
			safeCancel();
			return;
		}
		i++;
		
		try {
			CommonPacket commonEffect = new CommonPacket(PacketType.OUT_WORLD_PARTICLES);
			
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.effectName, name);
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.speed, data);			
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.particleCount, amount);
			
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.x, (float) ((Location) loc).getX());
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.y, (float) ((Location) loc).getY());
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.z, (float) ((Location) loc).getZ());
			
			if (offSet) {
				commonEffect.write(PacketType.OUT_WORLD_PARTICLES.randomX, RandomUtils.nextFloat());
				commonEffect.write(PacketType.OUT_WORLD_PARTICLES.randomY, RandomUtils.nextFloat());
				commonEffect.write(PacketType.OUT_WORLD_PARTICLES.randomZ, RandomUtils.nextFloat());
			}
			
			NoxPacketUtil.broadcastPacketSpigotVisibility(commonEffect, loc);
			
		} catch (Exception e) {
			e.printStackTrace();
			safeCancel();
			return;
		}
	}

}
