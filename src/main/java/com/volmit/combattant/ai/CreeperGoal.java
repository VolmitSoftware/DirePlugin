package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.volmit.combattant.Gate;
import com.volmit.volume.bukkit.task.S;
import com.volmit.volume.bukkit.task.SR;
import com.volmit.volume.bukkit.util.particle.ParticleEffect;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.lang.collections.FinalInteger;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.math.M;

public class CreeperGoal extends GOAL
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{

	}

	@Override
	public void onSoundDiscovered(Location near, Location far, GList<Location> sounds, LivingEntity c, GList<LivingEntity> nearbyEntities, GList<LivingEntity> nearbyEntitiesLOS)
	{
		if(!nearbyEntities.isEmpty())
		{
			LivingEntity le = nearbyEntities.pickRandom();
			Location t = le.getLocation();
			Location a = c.getLocation();
			Location h = a.clone();
			h.setY(t.getY());

			double verticalGoal = t.getY() - a.getY();
			double distanceLateral = h.distance(t);

			if(verticalGoal < -Gate.AI_GOAL_CREEPER_DIVEBOMB_MINIMUM_HEIGHT_SEPERATION && distanceLateral < Gate.AI_GOAL_CREEPER_DIVEBOMB_MAXIMUM_LATERAL_SEPERATION && distanceLateral > Gate.AI_GOAL_CREEPER_DIVEBOMB_MINIMUM_LATERAL_SEPERATION)
			{
				diveBomb(c, le);
			}

			else if(Math.abs(verticalGoal) < Gate.AI_GOAL_CREEPER_RUSHBOMB_MINIMUM_HEIGHT_SEPERATION && distanceLateral < Gate.AI_GOAL_CREEPER_RUSHBOMB_MAXIMUM_LATERAL_SEPERATION && distanceLateral > Gate.AI_GOAL_CREEPER_RUSHBOMB_MINIMUM_LATERAL_SEPERATION)
			{
				rushBomb(c, le);
			}
		}
	}

	private void rushBomb(LivingEntity c, LivingEntity le)
	{
		pathfind(le.getLocation(), c, Gate.AI_GOAL_CREEPER_RUSHBOMB_CHARGE_SPEED);
		ignite(c, le);
		((Creeper) c).setMaxFuseTicks(Gate.AI_GOAL_CREEPER_RUSHBOMB_FUSE_TICKS_CONTACT);
		ParticleEffect.VILLAGER_ANGRY.display(1.2f, 1, c.getEyeLocation(), 32);
	}

	private void diveBomb(LivingEntity c, LivingEntity le)
	{
		if(c.getFallDistance() < Gate.AI_GOAL_CREEPER_DIVEBOMB_ATTEMPT_IGNITE_DISTANCE)
		{
			diveAttempt(c, le);
		}

		else
		{
			speak(c, Sound.BLOCK_FIRE_EXTINGUISH, (float) (1.9 * M.clip(c.getFallDistance() / 10.0, 0.1, 1.9)));
			speak(c, Sound.ENTITY_CREEPER_HURT, (float) (1.9 * M.clip(c.getFallDistance() / 10.0, 0.1, 1.9)));
		}
	}

	private void diveAttempt(LivingEntity c, LivingEntity le)
	{
		Vector direction = VectorMath.direction(c.getEyeLocation(), le.getEyeLocation());
		direction.setY(0);
		direction.multiply(Gate.AI_GOAL_CREEPER_DIVEBOMB_LUNGE_SPEED);
		move(c, direction);
		lookAt(c, direction);
		ParticleEffect.VILLAGER_ANGRY.display(1.2f, 1, c.getEyeLocation(), 32);
		checkDive(c);
	}

	private void ignite(LivingEntity c, LivingEntity target)
	{
		((Creeper) c).setTarget(target);
	}

	private void ignite(LivingEntity c)
	{
		ArmorStand aa = (ArmorStand) c.getLocation().getWorld().spawnEntity(c.getLocation(), EntityType.ARMOR_STAND);
		aa.setAI(false);
		aa.setGravity(false);
		aa.setVisible(false);
		aa.setCollidable(false);
		aa.setInvulnerable(true);
		((Creeper) c).setMaxFuseTicks(Gate.AI_GOAL_CREEPER_RUSHBOMB_FUSE_TICKS);
		((Creeper) c).setTarget(aa);
		FinalInteger fe = new FinalInteger(8);

		new SR()
		{
			@Override
			public void run()
			{
				fe.sub(1);
				if(c.isDead() || fe.get() < 0)
				{
					aa.remove();
					cancel();
				}
			}
		};
	}

	private void checkDive(LivingEntity c)
	{
		new S(2)
		{
			@Override
			public void run()
			{
				if(c.getFallDistance() > 0)
				{
					((Creeper) c).setMaxFuseTicks(10000);
					ignite(c);

					new SR()
					{
						@Override
						public void run()
						{
							if(c.isOnGround())
							{
								Location at = ((Creeper) c).getEyeLocation();
								at.getWorld().createExplosion(at, ((Creeper) c).getExplosionRadius());
								c.remove();
								cancel();
								speak(c, Sound.ENTITY_GENERIC_EXPLODE, (float) 1.9);
								speak(c, Sound.ENTITY_GENERIC_EXPLODE, (float) 0.3);
							}
						}
					};
				}
			}
		};
	}

	@Override
	public double getListeningPower(LivingEntity c)
	{
		return Gate.AI_GOAL_CREEPER_LISTENING_POWER;
	}

	@Override
	public void onPityTick(LivingEntity c)
	{

	}
}
