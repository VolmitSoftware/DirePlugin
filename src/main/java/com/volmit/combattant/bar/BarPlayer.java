package com.volmit.combattant.bar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import com.volmit.volume.lang.collections.GMap;

public class BarPlayer
{
	private GMap<BarNode, IBar> bars;
	private Player p;

	public BarPlayer(Player p)
	{
		this.p = p;
		bars = new GMap<BarNode, IBar>();
	}

	public void setXPBar(BarNode node, double progress)
	{
		if(bars.containsKey(node))
		{
			IBar bar = bars.get(node);

			if(bar instanceof XPBar)
			{
				((XPBar) bar).setProgress(progress);
			}

			else
			{
				if(bar instanceof BossBarBar)
				{
					((BossBarBar) bar).getBar().removeAll();
				}

				bars.put(node, new XPBar(p));
				setXPBar(node, progress);
			}
		}

		else
		{
			bars.put(node, new XPBar(p));
			setXPBar(node, progress);
		}
	}

	public void setBossBar(BarNode node, String title, BarColor c, BarStyle s, double progress)
	{
		if(bars.containsKey(node))
		{
			IBar bar = bars.get(node);

			if(bar instanceof BossBarBar)
			{
				((BossBarBar) bar).getBar().setColor(c);
				((BossBarBar) bar).getBar().setStyle(s);
				((BossBarBar) bar).setProgress(progress);
				((BossBarBar) bar).getBar().setTitle(title);
				((BossBarBar) bar).getBar().addPlayer(p);
			}

			else
			{
				bars.put(node, new BossBarBar(Bukkit.createBossBar(title, c, s)));
				setBossBar(node, title, c, s, progress);
			}
		}

		else
		{
			bars.put(node, new BossBarBar(Bukkit.createBossBar(title, c, s)));
			setBossBar(node, title, c, s, progress);
		}
	}

	public void stop()
	{
		for(BarNode i : bars.k())
		{
			bars.get(i).stop();
		}
	}

	public void update()
	{
		for(BarNode i : bars.k())
		{
			bars.get(i).update();
		}
	}

	public void hide(BarNode node)
	{
		if(!bars.containsKey(node))
		{
			return;
		}

		((BossBarBar) bars.get(node)).getBar().removePlayer(p);
		bars.remove(node);
	}

	public double get(BarNode node)
	{
		if(!bars.containsKey(node))
		{
			return 1;
		}

		return bars.get(node).get();
	}

	public boolean has(BarNode stamina)
	{
		return bars.containsKey(stamina);
	}
}
