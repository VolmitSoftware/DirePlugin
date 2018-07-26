package com.volmit.combattant;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.pawn.Start;
import com.volmit.volume.bukkit.pawn.Stop;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.util.data.Edgy;
import com.volmit.volume.bukkit.util.event.EventBuffer;
import com.volmit.volume.lang.collections.GMap;

public class CoolStuff implements IPawn
{
	private EventBuffer<PlayerMoveEvent> moveBuffer;
	private GMap<Player, Location> lastPositions;

	@Start
	@Edgy("Lambdas")
	public void onStart()
	{
		lastPositions = new GMap<Player, Location>();
		moveBuffer = new EventBuffer<PlayerMoveEvent>(PlayerMoveEvent.class).priority(EventPriority.HIGH).acceptCancelled().addConsumer(e -> lastPositions.put(e.getPlayer(), e.getTo())).setSorter(e -> e.getPlayer().getUniqueId().toString()).engage();
	}

	@Tick(20)
	public void onTick()
	{
		moveBuffer.consumeTop();
	}

	@Stop
	public void onStop()
	{
		moveBuffer.disengage();
	}
}
