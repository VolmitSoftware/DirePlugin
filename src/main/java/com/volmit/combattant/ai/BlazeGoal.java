package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.volmit.combattant.fx.TracerFireball;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.math.M;

public class BlazeGoal extends GOAL
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{
		moveAwayFromXZ(c, from, 0.75);
		move(c, Vector.getRandom().subtract(Vector.getRandom()).normalize().setY(0));
	}

	@Override
	public void onSoundDiscovered(Location near, Location far, GList<Location> sounds, LivingEntity c, GList<LivingEntity> nearbyEntities, GList<LivingEntity> nearbyEntitiesLOS)
	{
		if(M.r(0.025) && !nearbyEntitiesLOS.isEmpty())
		{
			attack(c, nearbyEntitiesLOS);
		}

		hover(c);
	}

	private void hover(LivingEntity c)
	{
		if(heightOffGround(c) < 3.85)
		{
			ascend(c, 0.45);
		}
	}

	private void firingAt(LivingEntity c, LivingEntity t)
	{
		target(c, t);
		double d = c.getEyeLocation().distance(t.getEyeLocation());
		lookAt(c, t);

		if(d > 14.5)
		{
			moveCloserToXZ(c, t.getEyeLocation(), 0.47);
			speak(c, Sound.ENTITY_BLAZE_AMBIENT, 0.5f);
		}

		else
		{
			moveAwayFromXZ(c, t.getEyeLocation(), 0.87);
		}
	}

	private void attack(LivingEntity c, GList<LivingEntity> nearbyEntitiesLOS)
	{
		LivingEntity le = closest(c, nearbyEntitiesLOS);

		if(M.r(0.45))
		{
			le = nearbyEntitiesLOS.pickRandom();
		}

		firingAt(c, le);
		new TracerFireball(c, le.getEyeLocation()).fireTicks(0).speed(1.8).gforce(0.5).fire();
	}

	@Override
	public double getListeningPower(LivingEntity c)
	{
		return 1.7;
	}

	@Override
	public void onPityTick(LivingEntity c)
	{

	}
}
