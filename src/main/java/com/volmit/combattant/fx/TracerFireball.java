package com.volmit.combattant.fx;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.SmallFireball;
import org.bukkit.util.Vector;

import com.volmit.combattant.services.SoundSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.task.SR;
import com.volmit.volume.bukkit.util.particle.ParticleEffect;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.bukkit.util.sound.GSound;
import com.volmit.volume.math.M;

public class TracerFireball
{
	private Vector compound;
	private int fireTicks;
	private int maxTimeAlive;
	private double gforce;
	private double speed;
	private LivingEntity shooter;
	private Location target;

	public TracerFireball(LivingEntity shooter, Location target)
	{
		this.compound = new Vector();
		this.fireTicks = 10;
		this.maxTimeAlive = 45;
		this.gforce = 0.5;
		this.speed = 1.8;
		this.shooter = shooter;
		this.target = target;
	}

	public TracerFireball compound(Vector compound)
	{
		this.compound = compound;
		return this;
	}

	public TracerFireball fireTicks(int fireTicks)
	{
		this.fireTicks = fireTicks;
		return this;
	}

	public TracerFireball maxTime(int maxTimeAlive)
	{
		this.maxTimeAlive = maxTimeAlive;
		return this;
	}

	public TracerFireball gforce(double gforce)
	{
		this.gforce = gforce;
		return this;
	}

	public TracerFireball speed(double speed)
	{
		this.speed = speed;
		return this;
	}

	public void fire()
	{
		fireball();
	}

	private void fireball()
	{
		Location l = target;
		LivingEntity c = shooter;

		if(!c.getLocation().getWorld().equals(l.getWorld()))
		{
			return;
		}

		if(l.distance(c.getLocation()) < 6)
		{
			return;
		}

		if(c.isDead())
		{
			return;
		}

		l.setDirection(new Vector());

		Vector vx = Vector.getRandom().subtract(Vector.getRandom()).setY(0.3).normalize().clone().add(compound).normalize();
		SmallFireball fe = c.launchProjectile(SmallFireball.class, vx);
		fe.setIsIncendiary(fireTicks > 0);
		fe.setFireTicks(fireTicks);
		fe.setGravity(true);

		new SR()
		{
			@Override
			public void run()
			{
				if(fe.isDead())
				{
					new GSound(Sound.ENTITY_GENERIC_EXPLODE, 0.75f, 1.59f).play(fe.getLocation());
					cancel();
					return;
				}

				if(fe.getTicksLived() > maxTimeAlive)
				{
					fe.remove();
					return;
				}

				if(M.r(0.7))
				{
					ParticleEffect.FLAME.display(0.1f, 2, fe.getLocation(), 32);
					new GSound(Sound.BLOCK_FIRE_EXTINGUISH, 0.15f, (float) (Math.random() * 2)).play(fe.getLocation());
				}

				fe.setVelocity(fe.getVelocity().clone().add(VectorMath.reverse(VectorMath.direction(l, fe.getLocation())).clone().multiply(gforce)).clone().normalize().multiply(speed));
			}
		};

		ParticleEffect.LAVA.display(1.5f, 6, c.getLocation(), 32);
		new GSound(Sound.ENTITY_BLAZE_SHOOT, 1.7f, 1.9f + (float) (Math.random() * 0.3)).play(c.getLocation());
		U.getService(SoundSVC.class).makeSound(c.getLocation(), 100d);
	}
}
