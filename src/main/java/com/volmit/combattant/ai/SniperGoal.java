package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Stray;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.volmit.combattant.services.SoundSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.nms.NMSSVC;
import com.volmit.volume.bukkit.task.S;
import com.volmit.volume.bukkit.task.SR;
import com.volmit.volume.bukkit.util.particle.ParticleEffect;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.bukkit.util.sound.GSound;
import com.volmit.volume.bukkit.util.world.Area;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.math.M;

public class SniperGoal implements AIGoal
{
	@Override
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage)
	{
		fleeFrom(from, c);

		if(c.getType().equals(EntityType.BLAZE))
		{
			burstBlaze(from, c);
		}

		if(c.getType().equals(EntityType.STRAY))
		{
			if(M.r(0.6))
			{
				warpStray(from, c, (int) 1);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void warpStray(Location from, LivingEntity c, int itr)
	{
		if(c.getLocation().distance(from) < 6)
		{
			return;
		}

		c.setHealth(c.getMaxHealth());
		Vector vv = Vector.getRandom().subtract(Vector.getRandom()).normalize();
		vv.add(VectorMath.direction(c.getLocation(), from).normalize().multiply(2));
		Location dest = c.getLocation().clone().add(vv.clone().setY(0).normalize());

		for(int i = 0; i < 57; i++)
		{
			ParticleEffect.VILLAGER_HAPPY.display(Vector.getRandom().subtract(Vector.getRandom()), 0.6f, c.getEyeLocation().clone().add(Vector.getRandom().subtract(Vector.getRandom())), 32);
		}

		Area a = new Area(c.getLocation(), 44);
		U.getService(SoundSVC.class).makeSound(c.getLocation(), 100d);
		for(Entity i : a.getNearbyEntities())
		{
			if(i instanceof Stray)
			{
				continue;
			}

			if(i instanceof LivingEntity && M.r(0.35))
			{
				for(int j = 0; j < 2; j++)
				{
					new S((long) (2 * j + (Math.random() * 100)))
					{
						@Override
						public void run()
						{
							if(c.isDead())
							{
								return;
							}

							Arrow a = M.r(0.5) ? c.launchProjectile(Arrow.class, VectorMath.direction(c.getEyeLocation(), ((LivingEntity) i).getEyeLocation()).normalize().multiply(5.5)) : c.launchProjectile(Arrow.class, VectorMath.direction(c.getEyeLocation(), ((LivingEntity) i).getLocation()).normalize().multiply(3.5));
							c.teleport(c.getLocation().clone().setDirection(VectorMath.direction(c.getEyeLocation(), ((LivingEntity) i).getEyeLocation()).normalize()));
							new GSound(Sound.ENTITY_ENDERDRAGON_SHOOT, 1f, 1.9f).play(c.getLocation());

							new SR()
							{
								@Override
								public void run()
								{
									if(a.isDead())
									{
										cancel();
										return;
									}

									if(a.getTicksLived() > 15)
									{
										a.remove();
										return;
									}

									ParticleEffect.FIREWORKS_SPARK.display(0, 1, a.getLocation(), 32);
								}
							};
						}
					};
				}

				break;
			}
		}

		GSound g1 = new GSound(Sound.BLOCK_ENDERCHEST_OPEN, 1f, 0.5f);
		g1.play(c.getLocation());
		c.teleport(dest);
	}

	public void fireballAt(Location l, LivingEntity c)
	{
		if(l.distance(c.getLocation()) < 6)
		{
			return;
		}

		if(c.isDead())
		{
			return;
		}

		l.setDirection(new Vector());

		Vector vx = Vector.getRandom().subtract(Vector.getRandom()).setY(0.3).normalize();

		if(c.getType().equals(EntityType.GHAST))
		{
			vx.setY(-7);
			vx.normalize();
		}

		SmallFireball fe = c.launchProjectile(SmallFireball.class, vx);
		fe.setIsIncendiary(true);
		fe.setFireTicks(10);
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

				if(fe.getTicksLived() > 46)
				{
					fe.remove();
					return;
				}

				if(M.r(0.7))
				{
					ParticleEffect.FLAME.display(0.1f, 2, fe.getLocation(), 32);
					new GSound(Sound.BLOCK_FIRE_EXTINGUISH, 0.15f, (float) (Math.random() * 2)).play(fe.getLocation());
				}

				fe.setVelocity(fe.getVelocity().clone().add(VectorMath.reverse(VectorMath.direction(l, fe.getLocation())).clone().multiply(0.5)).clone().normalize().multiply(1.8));
				fe.setVelocity(fe.getVelocity().clone().add(Vector.getRandom().subtract(Vector.getRandom()).normalize().multiply(0.1)));
			}
		};

		ParticleEffect.LAVA.display(1.5f, 6, c.getLocation(), 32);
		new GSound(Sound.ENTITY_BLAZE_SHOOT, 1.7f, 1.9f + (float) (Math.random() * 0.3)).play(c.getLocation());
		U.getService(SoundSVC.class).makeSound(c.getLocation(), 100d);
	}

	@Override
	public void onSoundDiscovered(Location sound, LivingEntity c)
	{
		if(c.getType().equals(EntityType.BLAZE))
		{
			if(((Creature) c).getTarget() != null && M.r(0.25))
			{
				burstBlaze(((Creature) c).getTarget().getLocation(), c);
			}
		}

		if(c.getType().equals(EntityType.BLAZE) && M.r(0.15))
		{
			burstBlaze(sound, c);
		}

		if(c.getType().equals(EntityType.GHAST) && M.r(0.75))
		{
			burstGhast(sound, c);
		}

		if(c.getType().equals(EntityType.STRAY) && M.r(0.15))
		{
			warpStray(sound, c, (int) (int) 1);
		}

		if(c.getType().equals(EntityType.BLAZE))
		{
			if(c.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType().isSolid())
			{
				c.setVelocity(new Vector(0, 0.9, 0));
			}
		}

		if(sound.distanceSquared(c.getLocation()) < Math.pow(7, 2))
		{
			if(M.r(0.25))
			{
				fleeFrom(sound, c);
			}
		}

		Area ss = new Area(sound, 7);
		GList<Entity> g1 = new GList<Entity>(ss.getNearbyEntities(Monster.class));

		if(!g1.isEmpty())
		{
			if(c instanceof Creature)
			{
				((Creature) c).setTarget((Monster) g1.pickRandom());
			}
		}
	}

	private void burstBlaze(Location sound, LivingEntity c)
	{
		Area aa = new Area(sound, 24);

		if(M.r(0.55))
		{
			try
			{
				if(c.getLocation().distance(sound) > 24)
				{
					c.setVelocity(c.getVelocity().clone().add(VectorMath.direction(c.getLocation(), sound).normalize().multiply(0.1).normalize()).setY(0.25).multiply(0.25));

				}

				else
				{
					c.setVelocity(c.getVelocity().clone().add(VectorMath.reverse(VectorMath.direction(c.getLocation(), sound)).normalize().multiply(0.1).normalize()).setY(0.25).multiply(0.25));

				}

			}

			catch(Exception e)
			{

			}
		}

		if(sound.distance(c.getLocation()) > 6)
		{
			int m = 0;

			for(Entity i : aa.getNearbyEntities())
			{
				if(i instanceof LivingEntity)
				{
					m += 20;

					if(M.r(0.15))
					{
						new S((long) (Math.random() * 80) + m)
						{
							@Override
							public void run()
							{
								fireballAt(((LivingEntity) i).getEyeLocation().clone().add(i.getVelocity()), c);

								new S((long) (Math.random() * 11))
								{
									@Override
									public void run()
									{
										if(M.r(0.5))
										{
											fireballAt(((LivingEntity) i).getEyeLocation().clone().add(i.getVelocity()), c);

											if(M.r(0.5))
											{
												new S((long) (Math.random() * 11))
												{
													@Override
													public void run()
													{
														if(M.r(0.5))
														{
															fireballAt(((LivingEntity) i).getEyeLocation().clone().add(i.getVelocity()), c);
														}
													}
												};
											}
										}
									}
								};
							}
						};

						break;
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void burstGhast(Location sound, LivingEntity c)
	{
		if(M.r(0.7))
		{
			return;
		}

		c.setMaxHealth(100);
		c.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 8));
		Area aa = new Area(sound, 32);
		if(M.r(0.55))
		{
			try
			{
				if(c.getLocation().distance(sound) > 24)
				{
					c.setVelocity(c.getVelocity().clone().add(VectorMath.direction(c.getLocation(), sound).normalize().multiply(0.1).normalize()).setY(0.25).multiply(0.25));

				}

				else
				{
					c.setVelocity(c.getVelocity().clone().add(VectorMath.reverse(VectorMath.direction(c.getLocation(), sound)).normalize().multiply(0.1).normalize()).setY(0.25).multiply(0.25));

				}

			}

			catch(Exception e)
			{

			}
		}

		if(sound.distance(c.getLocation()) > 6)
		{
			int m = 0;

			for(Entity i : aa.getNearbyEntities())
			{
				if(i instanceof LivingEntity)
				{
					m += 5;

					if(M.r(0.15))
					{
						for(int vv = 0; vv < 1 + Math.random() * 12; vv++)
						{
							new S((long) (Math.random() * 40) + m)
							{
								@Override
								public void run()
								{
									fireballAt(((LivingEntity) i).getEyeLocation().clone(), c);
								}
							};
						}

						break;
					}
				}
			}
		}
	}

	public void fleeFrom(Location sound, LivingEntity c)
	{
		Vector direction = VectorMath.direction(sound, c.getLocation());
		direction = direction.clone().add(Vector.getRandom().subtract(Vector.getRandom()).clone().multiply(0.47)).clone().multiply((2 * Math.random()) + 2);
		Location dest = c.getLocation().clone().add(direction);
		U.getService(NMSSVC.class).pathFind(c, dest, true, 1.4);
	}

	public void lungeTo(Location sound, LivingEntity c)
	{
		Vector direction = VectorMath.direction(c.getLocation(), sound);
		direction = direction.clone().add(Vector.getRandom().subtract(Vector.getRandom()).clone().multiply(0.27)).clone().multiply(4);
		Location dest = c.getLocation().clone().add(direction).clone().add(Math.random(), Math.random(), Math.random());
		U.getService(NMSSVC.class).pathFind(c, dest, true, 1.1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public double getListeningPower(LivingEntity c)
	{
		return 1.7 + (c.getHealth() > c.getMaxHealth() ? 0.4 : 0);
	}
}
