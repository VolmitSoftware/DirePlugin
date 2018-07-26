package com.volmit.combattant.control;

import static com.volmit.combattant.Gate.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import com.volmit.combattant.object.ValueControl;
import com.volmit.combattant.services.HydrationSVC;
import com.volmit.combattant.services.StaminaSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.task.TICK;
import com.volmit.volume.bukkit.util.sound.GSound;
import com.volmit.volume.bukkit.util.world.Cuboid;
import com.volmit.volume.bukkit.util.world.Cuboid.CuboidDirection;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.math.M;

public class HydrationController implements IPawn
{
	@Tick()
	public void dehydrate()
	{
		if(TICK.tick % HYDRATION_DRAIN_TICK_INTERVAL == 0)
		{
			for(Player i : Bukkit.getServer().getOnlinePlayers())
			{
				Block bb = i.getLocation().getBlock();
				ValueControl s = U.getService(HydrationSVC.class).get(i);
				ValueControl a = U.getService(StaminaSVC.class).get(i);
				double b = DEHYDRATION_BASE;

				if(i.getWorld().getWeatherDuration() > 20 && i.getWorld().hasStorm())
				{
					s.rate(2.85, HYDRATION_DRAIN_TICK_INTERVAL);
				}

				if(i.isSprinting())
				{
					b += DEHYDRATION_SPRINT_ADDITIVE;
				}

				if(i.getSaturation() > DEHYDRATION_SATURATION_MINIMUM)
				{
					b /= DEHYDRATION_SATURATION_REDUCTION;
				}

				b /= (1 + ((double) i.getFoodLevel() / 20.0));

				if(i.getFireTicks() > 0)
				{
					b += i.getFireTicks() / DEHYDRATE_FIRE_RATIO;
				}

				boolean w = false;

				if(i.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || i.getLocation().getBlock().getType().equals(Material.WATER))
				{
					w = true;
					s.rate(HYDRATE_IN_WATER_AMOUNT, HYDRATE_IN_WATER_TICKS);

					if(M.r(0.13) && s.getCurrent() < s.getMax() - 100)
					{
						new GSound(Sound.ITEM_BUCKET_FILL, 1f, 0.25f + (float) (Math.random() * 0.5)).play(i.getLocation());
						consumeWater(i.getLocation());
					}
				}

				double ps = a.getCurrent() / a.getMax();

				if(ps < STAMINA_REGEN_WATER_MIN_STAMINA_PERCENT && s.current > STAMINA_REGEN_FROM_HYDRATION_TICKS * STAMINA_REGEN_WATER_DRAIN)
				{
					a.rate(STAMINA_REGEN_WATER_AMOUNT, STAMINA_REGEN_FROM_HYDRATION_TICKS);
					s.rate(-STAMINA_REGEN_WATER_DRAIN, STAMINA_REGEN_FROM_HYDRATION_TICKS);
				}

				s.drainRate = HYDRATION_DRAIN_RATE_BASE + (bb.getTemperature() / HYDRATION_DRAIN_RATE_TEMP_DIVISOR) + (bb.getBiome().equals(Biome.HELL) ? HYDRATION_DRAIN_HELL_BONUS : 0);
				s.genRate = HYDRATION_GEN_RATE_BASE - (bb.getTemperature() / HYDRATION_GEN_RATE_TEMP_DIVISOR) + (w ? HYDRATION_GEN_RATE_IN_WATER_BUNUS : 0);
				a.genRate = bb.getBiome().equals(Biome.SKY) ? STAMINA_GEN_RATE_END_BASE : (STAMINA_GEN_RATE_BASE + (s.getCurrent() / s.getMax())) - (bb.getTemperature() / 9.6);
				a.drainRate = STAMINA_DRAIN_RATE_BASE - (((s.getCurrent() / s.getMax())) / 2) - (0.5 - (bb.getTemperature() / 8)) + ((bb.getHumidity()));
				s.rate(-b, HYDRATION_DRAIN_TICKS);

				if(i.getLocation().getY() < 63 && i.getLocation().getWorld().getHighestBlockYAt(i.getLocation()) > 63)
				{
					s.drainRate *= 2.223;
					a.drainRate *= 1.62;
				}

				else if(i.getWorld().getWeatherDuration() > 20 && i.getWorld().hasStorm())
				{
					s.drainRate /= 2;
				}
			}
		}
	}

	@EventHandler
	public void on(PlayerBucketFillEvent e)
	{
		consumeWater(e.getBlockClicked().getLocation());
	}

	private void consumeWater(Location l)
	{
		Cuboid cc = new Cuboid(l);
		cc = cc.expand(CuboidDirection.Down, 3);
		cc = cc.expand(CuboidDirection.North, 3);
		cc = cc.expand(CuboidDirection.South, 3);
		cc = cc.expand(CuboidDirection.East, 3);
		cc = cc.expand(CuboidDirection.West, 3);
		cc = cc.expand(CuboidDirection.Up, 3);

		for(Block i : new GList<Block>(cc.iterator()))
		{
			if((i.getType().equals(Material.STATIONARY_WATER) || i.getType().equals(Material.WATER)) && i.getLocation().distance(l) <= 1.95)
			{
				if(M.r(0.76))
				{
					i.setType(Material.AIR);
				}
			}
		}
	}

	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e)
	{
		if(e.getItem().getType().equals(Material.POTION))
		{
			ValueControl v = U.getService(HydrationSVC.class).get(e.getPlayer());
			v.rate(HYDRATION_CONSUME_WATER, HYDRATION_CONSUME_WATER_TICKS);
			v.rate(HYDRATION_CONSUME_WATER / 2, HYDRATION_CONSUME_WATER_TICKS * 2);
			v.rate(HYDRATION_CONSUME_WATER / 4, HYDRATION_CONSUME_WATER_TICKS * 4);
			v.rate(HYDRATION_CONSUME_WATER / 8, HYDRATION_CONSUME_WATER_TICKS * 8);
			v.rate(HYDRATION_CONSUME_WATER / 8, HYDRATION_CONSUME_WATER_TICKS * 16);
			v.rate(HYDRATION_CONSUME_WATER / 16, HYDRATION_CONSUME_WATER_TICKS * 32);

			if(e.getPlayer().getFireTicks() > 0)
			{
				e.getPlayer().setFireTicks(0);
			}
		}

		if(e.getItem().getType().equals(Material.MILK_BUCKET))
		{
			ValueControl v = U.getService(HydrationSVC.class).get(e.getPlayer());
			v.rate(HYDRATION_CONSUME_WATER / 4, HYDRATION_CONSUME_WATER_TICKS * 4);
			v.rate(HYDRATION_CONSUME_WATER / 8, HYDRATION_CONSUME_WATER_TICKS * 8);
		}
	}
}
