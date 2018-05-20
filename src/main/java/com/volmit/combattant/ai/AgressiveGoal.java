package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.nms.NMSSVC;
import com.volmit.volume.bukkit.task.S;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.bukkit.util.world.Area;

public class AgressiveGoal implements AIGoal
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{
		lungeTo(from, c);
	}

	@Override
	public void onSoundDiscovered(Location sound, LivingEntity c)
	{
		lungeTo(sound, c);
		Area ss = new Area(sound, 17);
		int mm = 0;
		for(Entity i : ss.getNearbyEntities(LivingEntity.class))
		{
			new S(5 + mm)
			{
				@Override
				public void run()
				{
					if(c instanceof Creature)
					{
						((Creature) c).setTarget((LivingEntity) i);
					}
				}
			};
			mm += 30;
		}
	}

	public void lungeTo(Location sound, LivingEntity c)
	{
		Vector direction = VectorMath.direction(c.getLocation(), sound);
		direction = direction.clone().add(Vector.getRandom().subtract(Vector.getRandom()).clone().multiply(0.27)).clone().multiply(4);
		Location dest = c.getLocation().clone().add(direction).clone().add(Math.random(), Math.random(), Math.random());
		U.getService(NMSSVC.class).pathFind(c, dest, true, 1.2);
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getListeningPower(LivingEntity c)
	{
		return 1.2 + (c.getHealth() > c.getMaxHealth() ? 0.4 : 0);
	}
}
