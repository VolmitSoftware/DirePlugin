package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Stray;
import org.bukkit.util.Vector;

import com.volmit.combattant.Gate;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.math.M;

public class SkeletonGoal extends GOAL
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{
		moveAwayFromXZ(c, from, Gate.AI_GOAL_SKELETON_DAMAGED_BACKPEDAL_SPEED);
	}

	@Override
	public void onSoundDiscovered(Location near, Location far, GList<Location> sounds, LivingEntity c, GList<LivingEntity> nearbyEntities, GList<LivingEntity> nearbyEntitiesLOS)
	{
		if(!nearbyEntitiesLOS.isEmpty())
		{
			LivingEntity le = closest(c, nearbyEntitiesLOS);
			double ds = le.getLocation().distanceSquared(c.getLocation());

			if(ds > Math.pow(Gate.AI_GOAL_SKELETON_LOCK_TARGET_TRIGGER, 2))
			{
				lunge(le.getLocation(), c, Gate.AI_GOAL_SKELETON_LOCK_TARGET_LUNGE_SPEED);
			}

			else
			{
				flee(le.getLocation(), c, Gate.AI_GOAL_SKELETON_LOCK_TARGET_BACKPEDAL_SPEED);
			}

			target(c, le);

			if(ds < Math.pow(Gate.AI_GOAL_SKELETON_BACKPEDAL_TARGET_TRIGGER, 2) && M.r(Gate.AI_GOAL_SKELETON_BACKPEDAL_TARGET_CHANCE))
			{
				skeletonWoosh(c, VectorMath.direction(le.getLocation(), c.getLocation()).setY(0).normalize());
			}

			else if(M.r(Gate.AI_GOAL_SKELETON_STRAFE_TARGET_CHANCE))
			{
				skeletonWoosh(c, strafe(c, le.getLocation()));
			}
		}
	}

	private void skeletonWoosh(LivingEntity c, Vector direction)
	{
		move(c, direction);

		if(c instanceof Stray)
		{
			speak(c, Sound.ENTITY_STRAY_HURT, 1.74f);
		}

		else
		{
			speak(c, Sound.ENTITY_SKELETON_HURT, 1.74f);
		}
	}

	@Override
	public double getListeningPower(LivingEntity c)
	{
		return Gate.AI_GOAL_SKELETON_LISTENING_POWER;
	}

	@Override
	public void onPityTick(LivingEntity c)
	{

	}
}
