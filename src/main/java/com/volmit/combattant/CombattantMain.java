package com.volmit.combattant;

import com.volmit.combattant.control.AIController;
import com.volmit.combattant.control.BaubleController;
import com.volmit.combattant.control.HydrationController;
import com.volmit.combattant.control.MeleeController;
import com.volmit.combattant.control.ProjectileController;
import com.volmit.combattant.control.StaminaController;
import com.volmit.combattant.control.WeightController;
import com.volmit.volume.bukkit.pawn.Control;
import com.volmit.volume.bukkit.pawn.IPawn;

public class CombattantMain implements IPawn
{
	@Control
	public StaminaController staminaController;

	@Control
	public HydrationController hydrationController;

	@Control
	public BaubleController baubleController;

	@Control
	public ProjectileController arrowKBController;

	@Control
	public AIController aiController;

	@Control
	public MeleeController meleeController;

	@Control
	public WeightController weightController;
}
