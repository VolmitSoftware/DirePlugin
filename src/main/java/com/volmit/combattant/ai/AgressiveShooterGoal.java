package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.volmit.volume.lang.collections.GList;

public class AgressiveShooterGoal extends GOAL
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{

	}

	@Override
	public void onSoundDiscovered(Location near, Location far, GList<Location> sounds, LivingEntity c, GList<LivingEntity> nearbyEntities, GList<LivingEntity> nearbyEntitiesLOS)
	{

	}

	@SuppressWarnings("deprecation")
	@Override
	public double getListeningPower(LivingEntity c)
	{
		return 1.2 + (c.getHealth() > c.getMaxHealth() ? 0.4 : 0);
	}

	@Override
	public void onPityTick(LivingEntity c)
	{

	}
}
