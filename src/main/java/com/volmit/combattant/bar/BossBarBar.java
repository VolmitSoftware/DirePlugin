package com.volmit.combattant.bar;

import org.bukkit.boss.BossBar;

import com.volmit.combattant.Gate;
import com.volmit.volume.math.Average;

public class BossBarBar implements IBar
{
	private BossBar bar;
	private Average roll;
	private double value;
	private double tail;
	private int c;

	public BossBarBar(BossBar bar)
	{
		c = 0;
		tail = 0;
		this.bar = bar;
		roll = new Average(Gate.BAR_AVERAGE_RADIUS);
		value = bar.getProgress();
	}

	@Override
	public void update()
	{
		c++;
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

		if(c < Gate.BAR_AVERAGE_RADIUS)
		{
			tail = value;
			bar.setProgress(value);
		}

		else
		{
			bar.setProgress(tail);
		}
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

	public BossBar getBar()
	{
		return bar;
	}

	@Override
	public void stop()
	{
		bar.removeAll();
	}

	@Override
	public double get()
	{
		return bar.getProgress();
	}
}
