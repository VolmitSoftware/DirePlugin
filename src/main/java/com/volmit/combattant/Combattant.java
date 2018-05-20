package com.volmit.combattant;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.volmit.volume.bukkit.VolumePlugin;
import com.volmit.volume.bukkit.command.Command;
import com.volmit.volume.bukkit.command.CommandTag;
import com.volmit.volume.bukkit.pawn.Control;
import com.volmit.volume.bukkit.pawn.Start;
import com.volmit.volume.bukkit.pawn.Stop;

@CommandTag("&8[&cCombattant&8]&7: ")
public class Combattant extends VolumePlugin
{
	@Control
	private CombattantMain main;

	@Command
	private CommandCombattant command;

	@Start
	public void start()
	{
		for(Player i : Bukkit.getOnlinePlayers())
		{
			i.sendMessage("Niceeeee!");
		}
	}

	@Stop
	public void stop()
	{

	}
}
