package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.volmit.volume.lang.collections.GList;

public class AgressiveMeleeGoal extends GOAL
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{
		flee(from, c, 0.85);
	}

	@Override
	public void onSoundDiscovered(Location near, Location far, GList<Location> sounds, LivingEntity c, GList<LivingEntity> nearbyEntities, GList<LivingEntity> nearbyEntitiesLOS)
	{
		lunge(near, c, 0.85);

		if(!nearbyEntitiesLOS.isEmpty())
		{
			LivingEntity e = closest(c, nearbyEntitiesLOS);
			attack(c, e);
		}
	}

	public void attack(LivingEntity c, LivingEntity e)
	{
		pathfind(e.getEyeLocation(), c, 1.25);
		target(c, e);
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
