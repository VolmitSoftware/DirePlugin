package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.volmit.combattant.services.SoundSVC;
import com.volmit.combattant.services.WindSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.math.M;

public class PassiveGoal extends GOAL
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{
		flee(from, c, 1.4);
	}

	@Override
	public void onSoundDiscovered(Location near, Location far, GList<Location> sounds, LivingEntity c, GList<LivingEntity> nearbyEntities, GList<LivingEntity> nearbyEntitiesLOS)
	{
		if(!nearbyEntities.isEmpty() && !groupUpWithAllies(c, nearbyEntities, far))
		{
			if(far.distanceSquared(c.getLocation()) > 10)
			{
				flee(far, c, 1.5);
			}
		}
	}

	public boolean groupUpWithAllies(LivingEntity c, GList<LivingEntity> e, Location flee)
	{
		Location a = c.getLocation();
		a.clone().add(U.getService(WindSVC.class).getWindDirection().normalize().multiply(5));
		for(LivingEntity i : e)
		{
			if(i.getType().equals(c.getType()) && !i.equals(c))
			{
				target(c, i);
				a.add(VectorMath.direction(a, i.getLocation()).multiply(a.distance(i.getLocation())));
			}
		}

		if(a.distanceSquared(c.getLocation()) < 150)
		{
			return false;
		}

		pathfind(a.clone().add(Vector.getRandom().subtract(Vector.getRandom()).normalize().multiply(5)), c, 1.7);

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getListeningPower(LivingEntity c)
	{
		return 1.0 + (c.getHealth() > c.getMaxHealth() ? 0.4 : 0);
	}

	@Override
	public void onPityTick(LivingEntity c)
	{
		if(M.r(0.01))
		{
			U.getService(SoundSVC.class).makeSound(c.getLocation(), (double) 20);
		}
	}
}
