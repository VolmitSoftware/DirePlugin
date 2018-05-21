package com.volmit.combattant.services;

import org.bukkit.util.Vector;

import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.service.IService;
import com.volmit.volume.math.M;

public class WindSVC implements IService
{
	private Vector wind = new Vector();
	private double speed = 0.1;

	@Tick
	public void tick()
	{
		wind.add(Vector.getRandom().subtract(Vector.getRandom()).normalize().multiply(0.05));
		wind.setY(0);

		if(wind.length() > Math.abs(speed))
		{
			wind.multiply(0.9);
		}

		else
		{
			wind.multiply(1.1);
		}

		if(speed > 3)
		{
			speed = 3;
		}

		if(speed < 0)
		{
			speed = 0;
		}

		if(M.r(0.05))
		{
			speed += (((Math.random() * 2) - 1) * 0.3);
		}
	}

	public Vector getWindDirection()
	{
		return wind.clone();
	}

	public double getWindSpeed()
	{
		return getWindDirection().length();
	}
}
