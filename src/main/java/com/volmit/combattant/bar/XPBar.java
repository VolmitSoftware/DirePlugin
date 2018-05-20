package com.volmit.combattant.bar;

import org.bukkit.entity.Player;

import com.volmit.combattant.Gate;
import com.volmit.volume.math.Average;
import com.volmit.volume.math.M;

public class XPBar implements IBar
{
	private Player bar;
	private Average roll;
	private double value;
	private double tail;

	public XPBar(Player bar)
	{
		tail = 0;
		this.bar = bar;
		roll = new Average(Gate.BAR_AVERAGE_RADIUS);
		value = bar.getExp();
	}

	@Override
	public void setProgress(double progress)
	{
		value = progress;
	}

	@Override
	public void setProgress(double v, double of)
	{
		setProgress(v / of);
	}

	@Override
	public void setProgress(int v, int of)
	{
		setProgress((double) v, (double) of);
	}

	@Override
	public void stop()
	{

	}

	@Override
	public void update()
	{
		roll.put(value);

		if(Math.abs(tail - roll.getAverage()) > 0.001)
		{
			if(tail > roll.getAverage())
			{
				tail -= Math.abs(tail - roll.getAverage()) / 7.0;
			}

			else if(tail < roll.getAverage())
			{
				tail += Math.abs(tail - roll.getAverage()) / 7.0;
			}
		}

		bar.setExp((float) M.clip(tail, 0.0, 1.0));
	}

	@Override
	public double get()
	{
		return tail;
	}
}
