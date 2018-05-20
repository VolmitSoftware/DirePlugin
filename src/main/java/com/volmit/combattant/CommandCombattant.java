package com.volmit.combattant;

import com.volmit.volume.bukkit.command.Command;
import com.volmit.volume.bukkit.command.PawnCommand;
import com.volmit.volume.bukkit.command.VolumeSender;

public class CommandCombattant extends PawnCommand
{
	@Command
	private CommandReload reload;

	public CommandCombattant()
	{
		super("combattant", "combat", "comb", "cbt", "cb");
	}

	@Override
	public boolean handle(VolumeSender sender, String[] args)
	{
		sender.sendMessage("Try /combat reload");
		return true;
	}
}
