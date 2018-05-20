package com.volmit.combattant;

import com.volmit.combattant.control.AIController;
import com.volmit.combattant.control.BaubleController;
import com.volmit.combattant.control.HydrationController;
import com.volmit.combattant.control.MeleeController;
import com.volmit.combattant.control.ProjectileController;
import com.volmit.combattant.control.StaminaController;
import com.volmit.volume.bukkit.pawn.Control;
import com.volmit.volume.bukkit.pawn.IPawn;

public class CombattantMain implements IPawn
{
	@Control
	private StaminaController staminaController;

	@Control
	private HydrationController hydrationController;

	@Control
	private BaubleController baubleController;

	@Control
	private ProjectileController arrowKBController;

	@Control
	private AIController aiController;

	@Control
	private MeleeController meleeController;
}
