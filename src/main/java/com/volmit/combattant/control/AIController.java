package com.volmit.combattant.control;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.volmit.combattant.ai.AIGoal;
import com.volmit.combattant.ai.AgressiveMeleeGoal;
import com.volmit.combattant.ai.BlazeGoal;
import com.volmit.combattant.ai.PassiveGoal;
import com.volmit.combattant.services.SoundSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.pawn.Start;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.util.physics.VectorMath;
import com.volmit.volume.bukkit.util.world.RayTrace;
import com.volmit.volume.lang.collections.FinalInteger;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.lang.collections.GMap;
import com.volmit.volume.math.M;

public class AIController implements IPawn
{
	private GList<LivingEntity> queue;
	private GMap<EntityType, AIGoal> goalMap;

	@Start
	public void start()
	{
		queue = new GList<LivingEntity>();
		goalMap = new GMap<EntityType, AIGoal>();
		PassiveGoal pg = new PassiveGoal();
		AgressiveMeleeGoal ag = new AgressiveMeleeGoal();

		goalMap.put(EntityType.COW, pg);
		goalMap.put(EntityType.PIG, pg);
		goalMap.put(EntityType.SHEEP, pg);
		goalMap.put(EntityType.MUSHROOM_COW, pg);
		goalMap.put(EntityType.CHICKEN, pg);
		goalMap.put(EntityType.BAT, pg);
		goalMap.put(EntityType.RABBIT, pg);
		goalMap.put(EntityType.POLAR_BEAR, pg);

		goalMap.put(EntityType.ZOMBIE, ag);
		goalMap.put(EntityType.ZOMBIE_VILLAGER, ag);
		goalMap.put(EntityType.HUSK, ag);
		goalMap.put(EntityType.CAVE_SPIDER, ag);
		goalMap.put(EntityType.GIANT, ag);
		goalMap.put(EntityType.MAGMA_CUBE, ag);
		goalMap.put(EntityType.SLIME, ag);
		goalMap.put(EntityType.SILVERFISH, ag);
		goalMap.put(EntityType.SPIDER, ag);
		goalMap.put(EntityType.VEX, ag);
		goalMap.put(EntityType.SQUID, ag);

		goalMap.put(EntityType.BLAZE, new BlazeGoal());

		// goalMap.put(EntityType.SKELETON, sg);
		// goalMap.put(EntityType.STRAY, sg);
		// goalMap.put(EntityType.GHAST, sg);
		// goalMap.put(EntityType.WITCH, sg);
		// goalMap.put(EntityType.SHULKER, sg);
	}

	@EventHandler
	public void on(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof LivingEntity)
		{
			handledmg((LivingEntity) e.getEntity(), null, e.getEntity().getLocation(), e.getDamage());
		}
	}

	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getEntity() instanceof LivingEntity && e.getDamager() instanceof LivingEntity)
		{
			handledmg((LivingEntity) e.getEntity(), (LivingEntity) e.getDamager(), e.getDamager().getLocation(), e.getDamage());
		}
	}

	@Tick
	public void tick()
	{
		if(queue.isEmpty())
		{
			for(World i : Bukkit.getWorlds())
			{
				queue.addAll(i.getEntitiesByClass(LivingEntity.class));
			}
		}

		long ns = M.ns();
		long time = Duration.ofMillis(2).toNanos();

		while(!queue.isEmpty() && M.ns() - ns < time)
		{
			handle(queue.pop());
		}
	}

	private void handledmg(LivingEntity entity, LivingEntity dmr, Location ll, double dmg)
	{
		if(goalMap.containsKey(entity.getType()))
		{
			goalMap.get(entity.getType()).onHurt(ll, dmr, entity, dmg);
		}
	}

	private void handle(LivingEntity pop)
	{
		if(goalMap.containsKey(pop.getType()))
		{
			AIGoal g = goalMap.get(pop.getType());
			Location furthest = null;
			Location nearest = null;
			double n = Double.MAX_VALUE;
			double f = Double.MIN_VALUE;
			g.onPityTick(pop);
			GList<Location> sounds = U.getService(SoundSVC.class).listenFor(pop.getLocation(), g.getListeningPower(pop));
			GList<LivingEntity> entitiesLOS = new GList<LivingEntity>();
			GList<LivingEntity> entities = new GList<LivingEntity>();
			double radius = 18.5 * g.getListeningPower(pop);

			for(Entity i : pop.getLocation().getWorld().getNearbyEntities(pop.getLocation(), radius, radius, radius))
			{
				if(i instanceof LivingEntity)
				{
					if(!(g instanceof PassiveGoal) && i.getType().equals(pop.getType()))
					{
						continue;
					}

					entities.add((LivingEntity) i);

					if(hasLineOfSight(pop, ((LivingEntity) i).getEyeLocation()))
					{
						entitiesLOS.add((LivingEntity) i);
					}
				}
			}

			for(Location j : sounds)
			{
				double d = j.distanceSquared(pop.getLocation());

				if(d > f)
				{
					f = d;
					furthest = j;
				}

				if(d < n)
				{
					n = d;
					nearest = j;
				}
			}

			if(nearest != null && furthest != null)
			{
				g.onSoundDiscovered(nearest, furthest, sounds, pop, entities, entitiesLOS);
			}
		}
	}

	private boolean hasLineOfSight(LivingEntity seeker, Location position)
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
}
