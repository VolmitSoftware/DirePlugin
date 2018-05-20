package com.volmit.combattant.object;

import com.volmit.volume.lang.collections.GMap;

public class ValueControl
{
	public double max;
	public double current;
	public double rate;
	public double genRate;
	public double drainRate;
	public GMap<Double, Integer> rates;

	public ValueControl(double max)
	{
		genRate = 1;
		drainRate = 1;
		this.max = max;
		this.current = max;
		this.rate = 0;
		this.rates = new GMap<Double, Integer>();
	}

	public void mod(double v)
	{
		if(v > 0)
		{
			v = v * genRate;
		}

		if(v < 0)
		{
			v = v * drainRate;
		}

		this.current += v;
		this.current = current > max ? max : current;
		this.current = current < 0 ? 0 : current;
	}

	public void rate(double r, int ticks)
	{
		if(rates.containsKey(r))
		{
			int oldTicks = rates.get(r);
			rates.remove(r);
			rates.put(r * 2, Math.min(ticks, oldTicks));
			rates.put(r, Math.max(ticks, oldTicks) - Math.min(ticks, oldTicks));
		}

		else
		{
			rates.put(r, ticks);
		}
	}

	public void update()
	{
		double finalRate = 0;

		for(double i : rates.k())
		{
			finalRate += i;
			rates.put(i, rates.get(i) - 1);

			if(rates.get(i) <= 0)
			{
				rates.remove(i);
			}
		}

		mod(rate = finalRate);
	}

	public double getMax()
	{
		return max;
	}

	public void setMax(double max)
	{
		this.max = max;
	}

	public double getCurrent()
	{
		return current;
	}

	public void setCurrent(double current)
	{
		this.current = current;
	}

	public GMap<Double, Integer> getRates()
	{
		return rates;
	}

	public void setRates(GMap<Double, Integer> rates)
	{
		this.rates = rates;
	}
}
