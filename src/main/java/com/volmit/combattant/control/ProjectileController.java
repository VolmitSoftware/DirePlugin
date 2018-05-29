package com.volmit.combattant.control;

import org.bukkit.Sound;
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
import com.volmit.volume.bukkit.task.S;
import com.volmit.volume.bukkit.util.particle.ParticleEffect;
import com.volmit.volume.lang.collections.GMap;

public class ProjectileController implements IPawn
{
	private GMap<Player, Vector> velocities;

	public ProjectileController()
	{
		velocities = new GMap<Player, Vector>();
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
