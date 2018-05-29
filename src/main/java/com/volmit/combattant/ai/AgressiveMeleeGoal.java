package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import com.volmit.combattant.Gate;
import com.volmit.volume.lang.collections.GList;

public class AgressiveMeleeGoal extends GOAL
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{
		flee(from, c, Gate.AI_GOAL_AGGRO_FLEE_ON_DAMAGE_SPEED);
	}

	@Override
	public void onSoundDiscovered(Location near, Location far, GList<Location> sounds, LivingEntity c, GList<LivingEntity> nearbyEntities, GList<LivingEntity> nearbyEntitiesLOS)
	{
		if(near != null)
		{
			lunge(near, c, Gate.AI_GOAL_AGGRO_LUNGE_SPEED);
		}

		if(!nearbyEntitiesLOS.isEmpty())
		{
			LivingEntity e = closest(c, nearbyEntitiesLOS);
			attack(c, e);
		}
	}

	public void attack(LivingEntity c, LivingEntity e)
	{
		pathfind(e.getEyeLocation(), c, Gate.AI_GOAL_AGGRO_ATTACK_SPEED);
		target(c, e);
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getListeningPower(LivingEntity c)
	{
		return Gate.AI_GOAL_AGGRO_LISTENING_POWER + (c.getHealth() > c.getMaxHealth() ? 0.4 : 0);
	}

	@Override
	public void onPityTick(LivingEntity c)
	{

	}
}
