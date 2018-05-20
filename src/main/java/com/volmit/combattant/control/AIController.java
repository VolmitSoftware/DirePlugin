package com.volmit.combattant.control;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.volmit.combattant.ai.AIGoal;
import com.volmit.combattant.ai.AgressiveGoal;
import com.volmit.combattant.ai.PassiveGoal;
import com.volmit.combattant.ai.SniperGoal;
import com.volmit.combattant.services.SoundSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.pawn.Start;
import com.volmit.volume.bukkit.pawn.Tick;
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
		AgressiveGoal ag = new AgressiveGoal();
		SniperGoal sg = new SniperGoal();
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

		goalMap.put(EntityType.SKELETON, sg);
		goalMap.put(EntityType.STRAY, sg);
		goalMap.put(EntityType.BLAZE, sg);
		goalMap.put(EntityType.GHAST, sg);
		goalMap.put(EntityType.WITCH, sg);
		goalMap.put(EntityType.SHULKER, sg);
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
		long time = 1000000;

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

			for(Location j : U.getService(SoundSVC.class).listenFor(pop.getLocation(), g.getListeningPower(pop)))
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

			if(g instanceof PassiveGoal && nearest != null)
			{
				g.onSoundDiscovered(nearest, pop);
			}

			if(g instanceof AgressiveGoal && furthest != null)
			{
				g.onSoundDiscovered(furthest, pop);
			}

			if(g instanceof SniperGoal && furthest != null)
			{
				g.onSoundDiscovered(furthest, pop);
			}

			if(g instanceof SniperGoal && nearest != null)
			{
				g.onSoundDiscovered(nearest, pop);
			}
		}
	}
}
