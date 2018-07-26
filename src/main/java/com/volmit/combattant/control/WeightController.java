package com.volmit.combattant.control;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.volmit.combattant.services.StaminaSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.util.world.PE;
import com.volmit.volume.math.M;

public class WeightController implements IPawn
{
	@Tick
	public void onTick()
	{
		for(World i : Bukkit.getWorlds())
		{
			for(Item j : i.getEntitiesByClass(Item.class))
			{
				if(j.getItemStack().getType().name().contains("GLOWSTONE"))
				{
					for(Entity k : i.getNearbyEntities(j.getLocation(), 1, 1, 1))
					{
						if(k instanceof Creature)
						{
							Creature f = (Creature) k;
							int c = j.getItemStack().getAmount();

							if(f.hasPotionEffect(PotionEffectType.LEVITATION))
							{
								c += f.getPotionEffect(PotionEffectType.LEVITATION).getAmplifier() + 1;
							}

							PE.LEVITATION.a((int) M.clip(c, 0, 255)).d(200).apply((Creature) k);
							j.remove();
						}
					}
				}
			}
		}
	}

	public int getWeight(Material m, byte b)
	{
		int w = 1000;

		if(m.isBlock())
		{
			w += 400;
		}

		if(m.isEdible())
		{
			w += 200;
		}

		if(m.isFuel())
		{
			w += 300;
		}

		if(m.isSolid())
		{
			w += 500;
		}

		w += append(m, "cobble", 30);
		w += append(m, "bow", 11330);
		w += append(m, "arrow", 7730);
		w += append(m, "stone", 1300);
		w += append(m, "bedrock", 11300);
		w += append(m, "ore", 800);
		w += append(m, "ingot", 500);
		w += append(m, "block", 300);
		w += append(m, "iron", 1000);
		w += append(m, "gold", 1500);
		w += append(m, "diamond", 900);
		w += append(m, "wood", 100);
		w += append(m, "log", 700);
		w += append(m, "wool", 300);
		w += append(m, "sword", 1700);
		w += append(m, "axe", 1900);
		w += append(m, "hoe", 1700);
		w += append(m, "shovel", 1200);
		w += append(m, "_block", 17200);
		w += append(m, "shear", 700);
		w += append(m, "raw", 700);
		w += append(m, "cooked", 400);
		w += append(m, "raw", 700);
		w += append(m, "plank", 100);
		w += append(m, "glass", 300);
		w += append(m, "glowstone", -4800);

		return (int) (w * 0.8925);
	}

	private int append(Material m, String s, int k)
	{
		return m.name().toLowerCase().contains(s) ? k : 0;
	}

	public int getWeight(Inventory inv)
	{
		int w = 0;

		for(ItemStack i : inv.getContents())
		{
			if(i != null)
			{
				w += getWeight(i);
			}
		}

		return w;
	}

	@SuppressWarnings("deprecation")
	public int getWeight(ItemStack is)
	{
		double base = 0;

		double mult = 1;
		if(is.getType().getMaxDurability() > 0)
		{
			mult = 1.0 - ((double) is.getDurability() / (double) is.getType().getMaxDurability());
		}

		base += (double) getWeight(is.getType(), is.getData().getData());
		base = (int) ((base * mult) * is.getAmount());

		return (int) (base * 0.113154);
	}

	@EventHandler
	public void on(PlayerMoveEvent e)
	{
		int w = getWeight(e.getPlayer().getInventory());
		double pct = (double) w / 117000.0;
		double min = 0.12;
		double max = 0.27;
		double newSpeed = min + ((max - min) * (1 - pct));
		newSpeed = newSpeed < min ? min : newSpeed;
		newSpeed = newSpeed > max ? max : newSpeed;

		if(pct > 0.6)
		{
			U.getService(StaminaSVC.class).get(e.getPlayer()).rate(-1 * pct, 3);
		}

		if(newSpeed < 0.14 && pct > 0)
		{
			Vector vr = e.getPlayer().getVelocity();

			if(vr.getY() > 0.09)
			{
				vr.setX(vr.getX() / 6);
				vr.setZ(vr.getZ() / 6);
				vr.setY(vr.getY() / 1.5);
				U.getService(StaminaSVC.class).get(e.getPlayer()).rate(-40 * pct, 3);
			}

			e.getPlayer().setVelocity(vr);
		}

		else if(pct < 0)
		{
			if(pct < -0.0025)
			{
				PE.LEVITATION.a(-(int) (pct * 20)).d(100).apply(e.getPlayer());
			}

			else
			{
				PE.LEVITATION.a(255).d(100).apply(e.getPlayer());
			}
		}

		else
		{
			if(e.getPlayer().hasPotionEffect(PotionEffectType.LEVITATION))
			{
				e.getPlayer().removePotionEffect(PotionEffectType.LEVITATION);
			}
		}

		e.getPlayer().setWalkSpeed((float) newSpeed);
	}
}
