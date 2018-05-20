package com.volmit.combattant.services;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;

import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.nms.IPacketHandler;
import com.volmit.volume.bukkit.nms.NMSSVC;
import com.volmit.volume.bukkit.pawn.Start;
import com.volmit.volume.bukkit.pawn.Stop;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.service.IService;
import com.volmit.volume.bukkit.util.particle.ParticleEffect;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.lang.collections.GMap;
import com.volmit.volume.reflect.V;

public class SoundSVC implements IService
{
	private GMap<Location, Double> sounds = new GMap<Location, Double>();

	@Start
	public void start()
	{
		sounds = new GMap<Location, Double>();

		U.getService(NMSSVC.class).addPacketHandler(new IPacketHandler()
		{
			@Override
			public Object onPacketOutAsync(Player reciever, Object packet)
			{
				if(packet.getClass().getSimpleName().equals("PacketPlayOutNamedSoundEffect") || packet.getClass().getSimpleName().equals("PacketPlayOutCustomSoundEffect"))
				{
					int x = new V(packet).get("c");
					int y = new V(packet).get("d");
					int z = new V(packet).get("e");
					x /= 8;
					y /= 8;
					z /= 8;
					float v = new V(packet).get("f");
					sounds.put(new Location(reciever.getWorld(), x, y, z), (double) v * 20);
				}

				return packet;
			}

			@Override
			public Object onPacketInAsync(Player sender, Object packet)
			{
				return packet;
			}
		});
	}

	public void makeSound(Location l, Double volume)
	{
		if(volume == null)
		{
			return;
		}

		sounds.put(l, volume);
		ParticleEffect.CLOUD.display(0.1f, 1, l, 50);
	}

	@EventHandler
	public void on(BlockPlaceEvent e)
	{
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
		{
			return;
		}

		makeSound(e.getBlock().getLocation(), 100.0);
	}

	@EventHandler
	public void on(BlockBreakEvent e)
	{
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
		{
			return;
		}

		makeSound(e.getBlock().getLocation(), 100D);
	}

	@EventHandler
	public void on(EntityDamageEvent e)
	{
		makeSound(e.getEntity().getLocation(), 90d);
	}

	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		makeSound(e.getDamager().getLocation(), 90d);
	}

	@EventHandler
	public void on(PlayerItemConsumeEvent e)
	{
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
		{
			return;
		}

		makeSound(e.getPlayer().getLocation(), 40d);
	}

	@EventHandler
	public void on(EntityPickupItemEvent e)
	{
		makeSound(e.getEntity().getLocation(), 30d);
	}

	@EventHandler
	public void on(PlayerTeleportEvent e)
	{
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
		{
			return;
		}

		makeSound(e.getPlayer().getLocation(), 70d);
	}

	@EventHandler
	public void on(PlayerInteractEvent e)
	{
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
		{
			return;
		}

		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			makeSound(e.getPlayer().getLocation(), 25d);
		}
	}

	@EventHandler
	public void on(PortalCreateEvent e)
	{
		for(Block i : e.getBlocks())
		{
			makeSound(i.getLocation(), 50d);
		}
	}

	@EventHandler
	public void on(ProjectileHitEvent e)
	{
		makeSound(e.getEntity().getLocation(), 50d);
	}

	@EventHandler
	public void on(PlayerMoveEvent e)
	{
		if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE) || e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
		{
			return;
		}

		if(e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())
		{
			return;
		}

		if(e.getPlayer().isSprinting())
		{
			makeSound(e.getPlayer().getLocation(), 150d);
		}

		if(e.getPlayer().isSneaking())
		{
			makeSound(e.getPlayer().getLocation(), 20d);
		}

		else
		{
			makeSound(e.getPlayer().getLocation(), 50d);
		}
	}

	@Tick
	public void soundTick()
	{
		for(Location i : sounds.k())
		{
			if(sounds.get(i) == null)
			{
				sounds.remove(i);
				continue;
			}

			sounds.put(i, sounds.get(i) - 1.25);

			if(sounds.get(i) < 0.25)
			{
				sounds.remove(i);
			}
		}
	}

	@Stop
	public void stop()
	{

	}

	public Location listenForSingle(Location position, double listeningPower)
	{
		for(Location i : sounds.k())
		{
			if(i.getWorld().equals(position.getWorld()))
			{
				double amp = sounds.get(i);
				double maxdist = listeningPower * (amp / 2);

				if(Math.pow(maxdist, 2) > i.distanceSquared(position))
				{
					return i;
				}
			}
		}

		return null;
	}

	public GList<Location> listenFor(Location position, double listeningPower)
	{
		GList<Location> found = new GList<Location>();

		for(Location i : sounds.k())
		{
			if(i.getWorld().equals(position.getWorld()))
			{
				if(sounds.get(i) == null)
				{
					sounds.remove(i);
					continue;
				}

				double amp = sounds.get(i);
				double maxdist = listeningPower * (amp / 2);

				if(Math.pow(maxdist, 2) > i.distanceSquared(position))
				{
					found.add(i);
				}
			}
		}

		return found;
	}
}
