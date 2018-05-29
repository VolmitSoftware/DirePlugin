package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.volmit.combattant.services.WindSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.lang.collections.GList;

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
		if(near != null)
		{
			if(true)
			{
				flee(near, c, 1.4);
			}
		}

		else if(!nearbyEntities.isEmpty())
		{
			if(true)
			{
				groupUpWithAllies(c, nearbyEntities, far);
			}
		}
	}

	public boolean groupUpWithAllies(LivingEntity c, GList<LivingEntity> e, Location flee)
	{
		Location a = c.getLocation();
		a.clone().add(U.getService(WindSVC.class).getWindDirection().normalize().multiply(9));
		int cc = 0;
		for(LivingEntity i : e)
		{
			cc++;

			if(i.getType().equals(c.getType()) && !i.equals(c))
			{
				target(c, i);
				a.add(VectorMath.direction(a, i.getLocation()).multiply(a.distance(i.getLocation()) * 1.25));
			}

			if(cc > 100)
			{
				break;
			}
		}

		Vector vx = new Vector();

		if(flee != null)
		{
			VectorMath.direction(flee, c.getLocation()).multiply(18);
		}

		vx.clone().add(Vector.getRandom().subtract(Vector.getRandom()).normalize().multiply(5));

		if(a.distanceSquared(c.getLocation()) < 100)
		{
			return false;
		}

		pathfind(a.clone().add(Vector.getRandom().subtract(Vector.getRandom()).normalize().multiply(1)).clone().add(vx), c, 1.4);

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

	}
}
