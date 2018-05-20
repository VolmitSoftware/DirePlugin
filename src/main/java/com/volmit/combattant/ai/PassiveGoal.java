package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.volmit.combattant.services.SoundSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.nms.NMSSVC;
import com.volmit.volume.bukkit.util.physics.VectorMath;

public class PassiveGoal implements AIGoal
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{
		fleeFrom(from, c);
	}

	@Override
	public void onSoundDiscovered(Location sound, LivingEntity c)
	{
		fleeFrom(sound, c);
	}

	public void fleeFrom(Location sound, LivingEntity c)
	{
		Vector direction = VectorMath.direction(sound, c.getLocation());
		direction = direction.clone().add(Vector.getRandom().subtract(Vector.getRandom()).clone().multiply(0.17)).clone().multiply((4 * Math.random()) + 8);
		Location dest = c.getLocation().clone().add(direction);
		U.getService(NMSSVC.class).pathFind(c, dest, true, 1.9);
		U.getService(SoundSVC.class).makeSound(c.getLocation(), 100d);
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getListeningPower(LivingEntity c)
	{
		return 1.0 + (c.getHealth() > c.getMaxHealth() ? 0.4 : 0);
	}
}
