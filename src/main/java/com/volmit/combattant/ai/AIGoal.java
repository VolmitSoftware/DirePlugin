package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.volmit.volume.lang.collections.GList;

public interface AIGoal
{
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage);

	public void onSoundDiscovered(Location nearestSound, Location furthestSound, GList<Location> allSounds, LivingEntity c, GList<LivingEntity> nearbyEntities, GList<LivingEntity> nearbyEntitiesLOS);

	public double getListeningPower(LivingEntity c);

	public void onPityTick(LivingEntity c);
}
