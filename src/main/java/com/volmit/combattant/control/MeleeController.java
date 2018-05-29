package com.volmit.combattant.control;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.volmit.combattant.Gate;
import com.volmit.combattant.object.ValueControl;
import com.volmit.combattant.services.StaminaSVC;
import com.volmit.volume.bukkit.U;
import com.volmit.volume.bukkit.pawn.IPawn;
import com.volmit.volume.bukkit.util.particle.ParticleEffect;
import com.volmit.volume.bukkit.util.sound.GSound;
import com.volmit.volume.bukkit.util.world.Cuboid;
import com.volmit.volume.bukkit.util.world.Cuboid.CuboidDirection;
import com.volmit.volume.lang.collections.GList;
import com.volmit.volume.math.M;

public class MeleeController implements IPawn
{
	@EventHandler
	public void on(EntityDamageByEntityEvent e)
	{
		if(e.getDamager() instanceof Player)
		{
			on(new PlayerInteractEvent((Player) e.getDamager(), Action.LEFT_CLICK_AIR, ((Player) e.getDamager()).getInventory().getItemInMainHand(), e.getDamager().getLocation().getBlock(), BlockFace.UP));
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(PlayerInteractEvent e)
	{
		if(e.getHand().equals(EquipmentSlot.HAND))
		{
			if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
			{
				ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
				ValueControl s = U.getService(StaminaSVC.class).get(e.getPlayer());
				e.getPlayer().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1000);
				int dmg = 0;
				if(is != null && is.getType().name().contains("_SWORD") && !e.getPlayer().hasCooldown(is.getType()))
				{
					if(e.getPlayer().isSprinting())
					{
						if(s.current > Gate.SWORD_STAB_MIN_STAMINA)
						{
							e.getPlayer().setCooldown(is.getType(), 8);
							s.rate(-Gate.SWORD_STAB_ATTEMPT_CONSUME_STAMINA, Gate.SWORD_STAB_ATTEMPT_CONSUME_STAMINA_TICKS);
							ParticleEffect.CRIT.display(0.3f, 12, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(3.65)).add(0, -0.5, 0), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.65)).add(0, -0.5, 0), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.55)).add(0, -0.3, 0), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.45)).add(0, -0.4, 0), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.35)).add(0, -0.2, 0), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.75)).add(0, -0.1, 0), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.85)).add(0.1, -0.5, 0), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.55)).add(0, -0.5, 0.1), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.35)).add(0, -0.5, -0.1), 32);
							ParticleEffect.CRIT.display(e.getPlayer().getLocation().getDirection(), 0.3f, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.75)).add(-0.1, -0.5, 0), 32);
							new GSound(Sound.ENTITY_PLAYER_ATTACK_STRONG, 1f, 0.35f).play(e.getPlayer().getEyeLocation());

							for(Entity i : e.getPlayer().getWorld().getNearbyEntities(e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(2.65)), 0.75, 0.75, 0.75))
							{
								if(i instanceof LivingEntity && !i.equals(e.getPlayer()))
								{
									double d = 0;

									switch(is.getType())
									{
										case DIAMOND_SWORD:
											d += Gate.SWORD_STAB_DAMAGE_DIAMOND;
											break;
										case IRON_SWORD:
											d += Gate.SWORD_STAB_DAMAGE_IRON;
											break;
										case GOLD_SWORD:
											d += Gate.SWORD_STAB_DAMAGE_GOLD;
											break;
										case WOOD_SWORD:
											d += Gate.SWORD_STAB_DAMAGE_WOOD;
											break;
										case STONE_SWORD:
											d += Gate.SWORD_STAB_DAMAGE_STONE;
											break;
										default:
											break;
									}

									((LivingEntity) i).damage(d, e.getPlayer());
									e.getPlayer().setCooldown(is.getType(), Gate.SWORD_STAB_SUCCESS_COOLDOWN);
									s.rate(-Gate.SWORD_STAB_SUCCESS_CONSUME_STAMINA, Gate.SWORD_STAB_SUCCESS_CONSUME_STAMINA_TICKS);
									dmg += Gate.SWORD_STAB_SUCCESS_DURABILITY;
									ParticleEffect.DAMAGE_INDICATOR.display(1.5f, 55, i.getLocation(), 32);
									new GSound(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1f, 0.55f).play(i.getLocation());
									new GSound(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1f, 0.85f).play(i.getLocation());
									new GSound(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1f, 1.25f).play(i.getLocation());
								}
							}

							e.getPlayer().setCooldown(is.getType(), Gate.SWORD_STAB_ATTEMPT_COOLDOWN);
							new GSound(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4f, 1.655f).play(e.getPlayer().getLocation());
						}
					}

					else
					{
						if(s.current > Gate.SWORD_SWEEP_MIN_STAMINA && !e.getPlayer().hasCooldown(is.getType()))
						{
							e.getPlayer().setCooldown(is.getType(), Gate.SWORD_SWEEP_BASE_COOLDOWN);
							ParticleEffect.SWEEP_ATTACK.display(0.1f, 1, e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(1.25)).add(0, -0.5, 0), 32);
							new GSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, (float) (Math.random() / 2) + 0.65f).play(e.getPlayer().getEyeLocation());
							Location ctr = e.getPlayer().getEyeLocation().clone().add(e.getPlayer().getLocation().getDirection().clone().multiply(2.25)).add(0, -0.5, 0);

							Cuboid c = new Cuboid(ctr);
							c = c.expand(CuboidDirection.Up, 1);
							c = c.expand(CuboidDirection.Down, 1);
							c = c.expand(CuboidDirection.North, 1);
							c = c.expand(CuboidDirection.South, 1);
							c = c.expand(CuboidDirection.East, 1);
							c = c.expand(CuboidDirection.West, 1);

							if(dmg > 0)
							{
								return;
							}

							for(Block i : new GList<Block>(c.iterator()))
							{
								if(M.r(2.27 / (i.getLocation().distanceSquared(ctr))))
								{
									if(i.getType().equals(Material.LONG_GRASS) || i.getType().equals(Material.CACTUS) || i.getType().equals(Material.SUGAR_CANE_BLOCK) || i.getType().equals(Material.CROPS) || i.getType().equals(Material.CARROT) || i.getType().equals(Material.POTATO) || i.getType().equals(Material.NETHER_WARTS) || i.getType().equals(Material.VINE) || i.getType().equals(Material.RED_ROSE) || i.getType().equals(Material.LEAVES) || i.getType().equals(Material.LEAVES_2) || i.getType().equals(Material.DEAD_BUSH) || i.getType().equals(Material.DOUBLE_PLANT) || i.getType().equals(Material.YELLOW_FLOWER))
									{
										BlockBreakEvent ee = new BlockBreakEvent(i, e.getPlayer());
										Bukkit.getPluginManager().callEvent(ee);

										if(!ee.isCancelled())
										{
											dmg += 1;

											for(int j = 0; j < 8; j++)
											{
												i.getWorld().playEffect(i.getLocation().clone().add(0.5, 0.5, 0.5).clone().add(Vector.getRandom().subtract(Vector.getRandom()).normalize().multiply(0.5)), Effect.TILE_BREAK, i.getTypeId(), i.getData());
											}

											s.rate(-0.6, 16);
											i.breakNaturally();
											new GSound(Sound.BLOCK_GRASS_BREAK, 0.4f, (float) Math.random() * 1.85f).play(i.getLocation());
										}
									}
								}
							}
						}
					}

					if(dmg > 0)
					{
						dmg = (int) (dmg * Gate.SWORD_SWEEP_DURABILITY_MULTIPLIER);
						e.getPlayer().setCooldown(is.getType(), (int) ((dmg + 5) * Gate.SWORD_SWEEP_COOLDOWN_MULTIPLIER));

						if(is.getDurability() + dmg + 1 > is.getType().getMaxDurability())
						{
							e.getPlayer().getInventory().setItemInMainHand(null);
							new GSound(Sound.ENTITY_ITEM_BREAK, 1f, 1f).play(e.getPlayer().getLocation());
						}

						else
						{
							is.setDurability((short) (is.getDurability() + dmg));
						}
					}
				}
			}
		}
	}
}
