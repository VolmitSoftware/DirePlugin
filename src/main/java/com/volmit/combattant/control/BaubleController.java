package com.volmit.combattant.control;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.volmit.combattant.bar.BarNode;
import com.volmit.combattant.object.ValueControl;
import com.volmit.combattant.services.BaubleSVC;
import com.volmit.combattant.services.HydrationSVC;
import com.volmit.combattant.services.StaminaSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.bukkit.task.S;
import com.volmit.volume.lang.format.F;

public class BaubleController implements IPawn
{
	@Tick()
	public void update()
	{
		for(Player i : Bukkit.getServer().getOnlinePlayers())
		{
			ValueControl s = U.getService(StaminaSVC.class).get(i);
			ValueControl h = U.getService(HydrationSVC.class).get(i);

			if(s.getCurrent() == s.getMax())
			{
				U.getService(BaubleSVC.class).get(i).hide(BarNode.STAMINA);
			}

			else
			{
				U.getService(BaubleSVC.class).get(i).setBossBar(BarNode.STAMINA, "Stamina " + "+ " + F.pc(s.genRate) + " - " + F.pc(s.drainRate), s.getCurrent() / s.getMax() < 0.3 ? BarColor.RED : BarColor.YELLOW, BarStyle.SEGMENTED_12, s.getCurrent() / s.getMax());
			}

			if(h.getCurrent() == h.getMax())
			{
				U.getService(BaubleSVC.class).get(i).hide(BarNode.HYDRATION);
			}

			else
			{
				U.getService(BaubleSVC.class).get(i).setBossBar(BarNode.HYDRATION, "Hydration " + "+ " + F.pc(h.genRate) + " - " + F.pc(h.drainRate), U.getService(BaubleSVC.class).get(i).get(BarNode.HYDRATION) < 0.35 ? BarColor.RED : BarColor.BLUE, BarStyle.SEGMENTED_6, h.getCurrent() / h.getMax());
			}
		}
	}

	@EventHandler
	public void on(PlayerDeathEvent e)
	{
		new S(1)
		{
			@Override
			public void run()
			{
				ValueControl vs = U.getService(StaminaSVC.class).get(e.getEntity());
				ValueControl vh = U.getService(HydrationSVC.class).get(e.getEntity());
				vs.rates.clear();
				vh.rates.clear();
				vs.setCurrent(vs.getMax());
				vh.setCurrent(vh.getMax());
			}
		};
	}
}
