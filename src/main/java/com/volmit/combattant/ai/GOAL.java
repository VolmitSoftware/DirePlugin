package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.volmit.combattant.Gate;
import com.volmit.combattant.services.SoundSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.nms.NMSSVC;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.bukkit.util.sound.GSound;
import com.volmit.volume.bukkit.util.world.RayTrace;
import com.volmit.volume.lang.collections.FinalInteger;
import com.volmit.volume.lang.collections.GList;

public abstract class GOAL implements AIGoal
{
	public void target(LivingEntity who, LivingEntity target)
	{
		if(who instanceof Creature)
		{
			((Creature) who).setTarget(target);
		}
	}

	public void hover(LivingEntity c, double distance, double speed)
	{
		if(heightOffGround(c) < distance)
		{
			ascend(c, speed);
		}
	}

	public LivingEntity closest(LivingEntity le, GList<LivingEntity> les)
	{
		double min = Double.MAX_VALUE;
		LivingEntity ee = null;

		int cc = 0;
		for(LivingEntity i : les)
		{
			cc++;
			double dd = i.getLocation().distance(le.getLocation());

			if(dd < min)
			{
				ee = i;
				min = dd;
			}

			if(cc > Gate.PERFORMANCE_CLOSEST_MAX_ITERATIONS)
			{
				break;
			}
		}

		return ee;
	}

	public double heightOffGround(LivingEntity c)
	{
		int lvl = c.getLocation().getBlockY();

		Location lc = c.getLocation().clone();

		for(int i = lvl; i > 0; i--)
		{
			lc.setY(i);

			if(!lc.getBlock().isEmpty())
			{
				return c.getLocation().getY() - i;
			}
		}

		return c.getLocation().getY();
	}

	public int rint(int max)
	{
		return rint(0, max);
	}

	public int rint(int min, int max)
	{
		return min + (int) (Math.random() * (max - min));
	}

	public double rdou(double max)
	{
		return rdou(0D, max);
	}

	public double rdou(double min, double max)
	{
		return min + (Math.random() * (max - min));
	}

	public boolean hasLineOfSight(LivingEntity seeker, Location position)
	{
		FinalInteger fe = new FinalInteger(0);

		new RayTrace(seeker.getEyeLocation(), VectorMath.direction(seeker.getEyeLocation(), position), 128D, 1D)
		{
			@Override
			public void onTrace(Location location)
			{
				fe.set(1);
				stop();
			}
		}.trace();

		return fe.get() == 1;
	}

	public void speak(LivingEntity c, Sound sound, float pitch)
	{
		new GSound(sound, 1.7f, pitch).play(c.getLocation());
	}

	public void speakLoudly(LivingEntity c, Sound sound, float pitch)
	{
		new GSound(sound, 3.7f, pitch).play(c.getLocation());
	}

	public void speak(LivingEntity c, Sound sound, float pitch, float osc)
	{
		float oo = (float) (osc * (Math.random() * 2) - 1f);
		new GSound(sound, 1.7f, pitch + oo).play(c.getLocation());
	}

	public void moveCloserTo(LivingEntity c, Location to, double speed)
	{
		move(c, VectorMath.direction(c.getEyeLocation(), to).multiply(speed));
	}

	public void moveAwayFrom(LivingEntity c, Location from, double speed)
	{
		move(c, VectorMath.reverse(VectorMath.direction(c.getEyeLocation(), from)).multiply(speed));
	}

	public void moveCloserToXZ(LivingEntity c, Location to, double speed)
	{
		move(c, VectorMath.direction(c.getEyeLocation(), to).multiply(speed).setY(0));
	}

	public void moveAwayFromXZ(LivingEntity c, Location from, double speed)
	{
		move(c, VectorMath.reverse(VectorMath.direction(c.getEyeLocation(), from)).multiply(speed).setY(0));
	}

	public void lookAt(LivingEntity c, Vector direction)
	{
		c.teleport(c.getLocation().clone().setDirection(direction.normalize()));
	}

	public void lookAt(LivingEntity c, Location at)
	{
		lookAt(c, VectorMath.direction(c.getEyeLocation(), at));
	}

	public void lookAt(LivingEntity c, LivingEntity at)
	{
		lookAt(c, at.getEyeLocation());
	}

	public void ascend(LivingEntity c, double speed)
	{
		move(c, new Vector(0, speed, 0));
	}

	public void descend(LivingEntity c, double speed)
	{
		ascend(c, -speed);
	}

	public void move(LivingEntity c, Vector direction)
	{
		try
		{
			direction.checkFinite();
		}

		catch(IllegalArgumentException e)
		{
			direction.normalize();
		}

		c.setVelocity(c.getVelocity().add(direction));
	}

	public Vector strafe(LivingEntity c, Location from)
	{
		Vector rnd = Vector.getRandom().subtract(Vector.getRandom()).normalize();
		Vector dir = VectorMath.direction(c.getEyeLocation(), from);
		rnd.add(dir).normalize();
		rnd.subtract(dir).normalize();
		rnd.setY(0).normalize();

		return rnd;
	}

	public void pathfind(Location to, LivingEntity c, double speed)
	{
		U.getService(NMSSVC.class).pathFind(c, to, speed > 1, speed);
	}

	public void lunge(Location sound, LivingEntity c, double speed)
	{
		Vector direction = VectorMath.direction(c.getLocation(), sound);
		direction = direction.clone().add(Vector.getRandom().subtract(Vector.getRandom()).clone().multiply(0.27)).clone().multiply(4);
		Location dest = c.getLocation().clone().add(direction).clone().add(Math.random(), Math.random(), Math.random());
		dest.setY(dest.getWorld().getHighestBlockYAt(dest));
		U.getService(NMSSVC.class).pathFind(c, dest, speed > 1, speed);
		U.getService(SoundSVC.class).makeSound(c.getLocation(), 100d);
	}

	public void flee(Location sound, LivingEntity c, double speed)
	{
		Vector direction = VectorMath.direction(sound, c.getLocation());
		direction = direction.clone().add(Vector.getRandom().subtract(Vector.getRandom()).clone().multiply(0.17)).clone().multiply((4 * Math.random()) + 8);
		Location dest = c.getLocation().clone().add(direction);
		dest.setY(dest.getWorld().getHighestBlockYAt(dest));
		U.getService(NMSSVC.class).pathFind(c, dest, true, 1.9);
	}
}
