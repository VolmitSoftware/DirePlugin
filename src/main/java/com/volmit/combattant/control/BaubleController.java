package com.volmit.combattant.control;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import com.volmit.combattant.bar.BarNode;
import com.volmit.combattant.object.ValueControl;
import com.volmit.combattant.services.BaubleSVC;
import com.volmit.combattant.services.HydrationSVC;
import com.volmit.combattant.services.StaminaSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.pawn.Tick;
import com.volmit.volume.lang.format.F;

public class BaubleController implements IPawn
{
	@SuppressWarnings("deprecation")
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

			if(i.getFireTicks() > 0 || i.getItemInHand().getType().equals(Material.POTION) || U.getService(BaubleSVC.class).get(i).get(BarNode.HYDRATION) < 0.35 || Math.abs(s.rate) > 1)
			{
				U.getService(BaubleSVC.class).get(i).setBossBar(BarNode.HYDRATION, "Hydration " + "+ " + F.pc(h.genRate) + " - " + F.pc(h.drainRate), U.getService(BaubleSVC.class).get(i).get(BarNode.HYDRATION) < 0.35 ? BarColor.RED : BarColor.BLUE, BarStyle.SEGMENTED_6, h.getCurrent() / h.getMax());
			}

			else
			{
				U.getService(BaubleSVC.class).get(i).hide(BarNode.HYDRATION);
			}
		}
	}
}
