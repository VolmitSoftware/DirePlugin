package com.volmit.combattant.control;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.volmit.combattant.Gate;
import com.volmit.combattant.services.SoundSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.task.S;
import com.volmit.volume.bukkit.util.particle.ParticleEffect;
import com.volmit.volume.bukkit.util.sound.GSound;
import com.volmit.volume.bukkit.util.world.Cuboid;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.lang.collections.GMap;
import com.volmit.volume.math.M;

public class ProjectileController implements IPawn
{
	private GMap<Player, Vector> velocities;

	public ProjectileController()
	{
		velocities = new GMap<Player, Vector>();
	}

	@Tick
	public void onTick()
	{
		for(World i : Bukkit.getWorlds())
		{
			for(Arrow j : i.getEntitiesByClass(Arrow.class))
			{
				Block aa = j.getLocation().clone().add(j.getVelocity()).getBlock();
				Block bb = j.getLocation().clone().add(j.getVelocity().clone().multiply(0.2)).getBlock();
				Block cc = j.getLocation().clone().add(j.getVelocity().clone().multiply(0.5)).getBlock();
				Block dd = j.getLocation().clone().getBlock();

				GList<Block> gb = new GList<Block>();
				gb.add(aa);
				gb.add(bb);
				gb.add(cc);
				gb.add(dd);
				gb.removeDuplicates();

				Cuboid cv = new Cuboid(j.getLocation(), j.getLocation().clone().add(j.getVelocity().multiply(2)));

				for(Block k : new GList<Block>(cv.iterator()))
				{
					gb.add(k);
				}

				for(Block k : gb)
				{
					if(k.getType().equals(Material.LEAVES) || k.getType().equals(Material.LEAVES_2))
					{
						k.breakNaturally();
						if(M.r(0.25))
						{
							new GSound(Sound.BLOCK_GRASS_BREAK, 0.4f, 1.75f).play(k.getLocation());
						}

						else
						{
							new GSound(Sound.BLOCK_GRASS_STEP, 0.9f, 1.75f).play(k.getLocation());
						}
					}

					if(k.getType().equals(Material.GLASS) || k.getType().equals(Material.GLOWSTONE) || k.getType().equals(Material.STAINED_GLASS_PANE) || k.getType().equals(Material.STAINED_GLASS) || k.getType().equals(Material.THIN_GLASS))
					{
						k.breakNaturally();
						if(M.r(0.25))
						{
							new GSound(Sound.BLOCK_GLASS_BREAK, 0.4f, 1.75f).play(k.getLocation());
						}

						else
						{
							new GSound(Sound.BLOCK_GLASS_BREAK, 0.9f, 1.45f).play(k.getLocation());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onKb(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Projectile)
		{
			if(e.getEntity() instanceof LivingEntity)
			{
				LivingEntity c = (LivingEntity) e.getEntity();
				double size = Gate.BOW_HEADSHOT_AABB_SIZE;
				double accuracy = Gate.BOW_HEADSHOT_CHECK_ITERATIONS;
				Vector vv = e.getDamager().getVelocity();

				for(int i = 1; i < accuracy; i++)
				{
					e.getDamager().teleport(e.getDamager().getLocation().clone().add(vv.clone().multiply(1.0 / accuracy)));
					if(c.getWorld().getNearbyEntities(c.getEyeLocation().clone().add(0, 0.6, 0), (size * 1.3) / 2.0, (size * 0.68) / 2.0, (size * 1.3) / 2.0).contains(e.getDamager()))
					{
						c.getEyeLocation().getWorld().playSound(c.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.7f, 1.2f);
						c.getEyeLocation().getWorld().playSound(c.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1.7f, 1.2f);
						c.getEyeLocation().getWorld().playSound(c.getEyeLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.7f, 1.2f);
						c.damage((Gate.BOW_HEADSHOT_DAMAGE_MULTIPLIER - 1) * e.getDamage());
						ParticleEffect.CRIT.display(0.6f, 24, c.getEyeLocation(), 100);
						U.getService(SoundSVC.class).makeSound(c.getLocation(), 100d);
						break;
					}
				}

				try
				{
					new S()
					{
						@Override
						public void run()
						{
							if(velocities.containsKey(e.getEntity()))
							{
								e.getEntity().setVelocity(velocities.get(e.getEntity()));
							}

							else
							{
								e.getEntity().setVelocity(new Vector());
							}
						}
					};
				}

				catch(Throwable ef)
				{

				}
			}
		}
	}

	@EventHandler
	public void on(PlayerMoveEvent e)
	{
		velocities.put(e.getPlayer(), e.getPlayer().getVelocity());
	}
}
