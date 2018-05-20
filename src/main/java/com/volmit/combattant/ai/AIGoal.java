package com.volmit.combattant.ai;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface AIGoal
{
	public void onHurt(Location from, LivingEntity src, LivingEntity c, double damage);

	public void onSoundDiscovered(Location sound, LivingEntity c);

	public double getListeningPower(LivingEntity c);
}
