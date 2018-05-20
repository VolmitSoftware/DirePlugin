package com.volmit.combattant.services;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import com.volmit.combattant.Gate;
import com.volmit.combattant.object.ValueControl;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.service.IService;
import com.volmit.volume.lang.collections.GMap;

public class StaminaSVC implements IService
{
	private GMap<Player, ValueControl> staminas;

	public StaminaSVC()
	{
		staminas = new GMap<Player, ValueControl>();
	}

	@Tick
	public void update()
	{
		for(Player i : staminas.k())
		{
			staminas.get(i).update();
		}
	}

	@EventHandler
	public void on(PlayerQuitEvent e)
	{
		staminas.remove(e.getPlayer());
	}

	public ValueControl get(Player p)
	{
		if(!staminas.containsKey(p))
		{
			staminas.put(p, new ValueControl(Gate.MAX_STAMINA));
		}

		return staminas.get(p);
	}
}
