package com.volmit.combattant.services;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import com.volmit.combattant.bar.BarPlayer;
import com.volmit.volume.bukkit.pawn.Stop;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.service.IService;
import com.volmit.volume.lang.collections.GMap;

public class BaubleSVC implements IService
{
	private GMap<Player, BarPlayer> bars;

	public BaubleSVC()
	{
		bars = new GMap<Player, BarPlayer>();
	}

	@Tick
	public void onTick()
	{
		for(Player i : bars.k())
		{
			bars.get(i).update();
		}
	}

	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		bars.remove(e.getPlayer());
	}

	public BarPlayer get(Player p)
	{
		if(!bars.containsKey(p))
		{
			bars.put(p, new BarPlayer(p));
		}

		return bars.get(p);
	}

	@Stop
	public void onStop()
	{
		for(Player i : bars.k())
		{
			bars.get(i).stop();
		}
	}
}
