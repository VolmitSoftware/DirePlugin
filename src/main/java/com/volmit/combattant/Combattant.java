package com.volmit.combattant;

import com.volmit.volume.bukkit.VolumePlugin;
import com.volmit.volume.bukkit.command.Command;
import com.volmit.volume.bukkit.command.CommandTag;
import com.volmit.volume.bukkit.pawn.Control;
import com.volmit.volume.bukkit.pawn.Start;
import com.volmit.volume.bukkit.pawn.Stop;

@CommandTag("&8[&cCombattant&8]&7: ")
public class Combattant extends VolumePlugin
{
	public static Combattant inst;

	@Control
	public CombattantMain main;

	@Command
	public CommandCombattant command;

	@Start
	public void start()
	{
		inst = this;
	}

	@Stop
	public void stop()
	{

	}
}
