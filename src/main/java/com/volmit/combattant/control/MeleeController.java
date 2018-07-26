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
import org.bukkit.entity.FallingBlock;
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
import com.volmit.volume.bukkit.task.S;
import com.volmit.volume.bukkit.task.SR;
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
			new S()
			{
				@Override
				public void run()
				{
					on(new PlayerInteractEvent((Player) e.getDamager(), Action.LEFT_CLICK_AIR, ((Player) e.getDamager()).getInventory().getItemInMainHand(), e.getDamager().getLocation().getBlock(), BlockFace.UP));
				}
			};
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(BlockBreakEvent e)
	{
		ItemStack is = e.getPlayer().getItemInHand();

		if(e.getBlock().getType().equals(Material.LOG) || e.getBlock().getType().equals(Material.LOG_2) && is != null && is.getType().name().endsWith("_AXE") && !e.getPlayer().hasCooldown(is.getType()))
		{
			shred(e.getBlock(), 3, is, e.getPlayer());
		}
	}

	private int shred(Block bx, int maxf, ItemStack is, Player p)
	{
		int dmg = 0;

		if(maxf < 0)
		{
			return dmg;
		}

		if(bx.getType().equals(Material.LOG) || bx.getType().equals(Material.LOG_2) || bx.getType().equals(Material.LEAVES) || bx.getType().equals(Material.LEAVES_2))
		{
			int mm = (int) (4 * Math.random());

			for(int i = bx.getY(); i < bx.getY() + 5; i++)
			{
				dmg++;
				Block bb = bx.getWorld().getBlockAt(new Location(bx.getWorld(), bx.getX(), i, bx.getZ()));

				if(i > bx.getY() && !(bb.getType().equals(Material.LOG) || bb.getType().equals(Material.LOG_2) || bb.getType().equals(Material.LEAVES) || bb.getType().equals(Material.LEAVES_2)))
				{
					break;
				}

				int id = i;
				new S((int) ((mm)))
				{
					@SuppressWarnings("deprecation")
					@Override
					public void run()
					{
						Block bb = bx.getWorld().getBlockAt(new Location(bx.getWorld(), bx.getX(), id, bx.getZ()));

						if(bb.getType().equals(Material.LOG) || bb.getType().equals(Material.LOG_2) || bb.getType().equals(Material.LEAVES) || bb.getType().equals(Material.LEAVES_2))
						{
							if(p.getItemInHand() != null && p.getItemInHand().getType().name().endsWith("_AXE"))
							{
								p.setCooldown(is.getType(), p.getCooldown(is.getType()) + 2);
								is.setDurability((short) (is.getDurability() + (M.r(0.25) ? 1 : 0)));
								if(is.getDurability() > is.getType().getMaxDurability())
								{
									p.setItemInHand(null);
								}

								process(bb);
								shred(bb.getRelative(BlockFace.NORTH), maxf - 1, is, p);
								shred(bb.getRelative(BlockFace.SOUTH), maxf - 1, is, p);
								shred(bb.getRelative(BlockFace.EAST), maxf - 1, is, p);
								shred(bb.getRelative(BlockFace.WEST), maxf - 1, is, p);
							}

							else
							{
								return;
							}
						}
					}
				};

				mm += (Math.random() * 4) + 2;
			}
		}

		return dmg;
	}

	private void process(Block bb)
	{
		Material t = bb.getType();
		@SuppressWarnings("deprecation")
		byte da = bb.getData();
		bb.setType(Material.AIR);
		@SuppressWarnings("deprecation")
		FallingBlock fb = bb.getWorld().spawnFallingBlock(bb.getLocation().clone().add(0.5, 0.0, 0.5), t, da);
		fb.setDropItem(true);
		Location ls = fb.getLocation();
		new SR()
		{
			@Override
			public void run()
			{
				if(fb.isDead())
				{
					cancel();
					new S(2)
					{
						@Override
						public void run()
						{
							if(ls.getBlock().getType().toString().endsWith("LOG"))
							{
								new GSound(Sound.BLOCK_WOOD_BREAK, 0.4f, 0.75f).play(ls);
							}

							else
							{
								if(M.r(0.25))
								{
									new GSound(Sound.BLOCK_GRASS_BREAK, 0.4f, 1.75f).play(ls);
								}

								else
								{
									new GSound(Sound.BLOCK_GRASS_STEP, 0.9f, 1.75f).play(ls);
								}
							}

							ls.getBlock().breakNaturally();
						}
					};

					return;
				}

				else
				{
					ls.setX(fb.getLocation().getX());
					ls.setY(fb.getLocation().getY());
					ls.setZ(fb.getLocation().getZ());
				}
			}
		};
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(PlayerInteractEvent e)
	{
		if(e.getHand() == null)
		{
			return;
		}

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
									new GSound(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1f, 0.55f).play(i.getLocation());
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

				if(is != null && is.getType().name().contains("_AXE") && !e.getPlayer().hasCooldown(is.getType()))
				{
					if(e.getPlayer().getVelocity().getY() < -0.11)
					{
						if(s.current > Gate.AXE_SMASH_MIN_STAMINA)
						{
							e.getPlayer().setCooldown(is.getType(), 8);
							s.rate(-Gate.AXE_SMASH_ATTEMPT_CONSUME_STAMINA, Gate.AXE_SMASH_ATTEMPT_CONSUME_STAMINA_TICKS);
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
											d += Gate.AXE_SMASH_DAMAGE_DIAMOND;
											break;
										case IRON_SWORD:
											d += Gate.AXE_SMASH_DAMAGE_IRON;
											break;
										case GOLD_SWORD:
											d += Gate.AXE_SMASH_DAMAGE_GOLD;
											break;
										case WOOD_SWORD:
											d += Gate.AXE_SMASH_DAMAGE_WOOD;
											break;
										case STONE_SWORD:
											d += Gate.AXE_SMASH_DAMAGE_STONE;
											break;
										default:
											break;
									}

									((LivingEntity) i).damage(d, e.getPlayer());
									e.getPlayer().setCooldown(is.getType(), Gate.AXE_SMASH_SUCCESS_COOLDOWN);
									s.rate(-Gate.AXE_SMASH_SUCCESS_CONSUME_STAMINA, Gate.AXE_SMASH_SUCCESS_CONSUME_STAMINA_TICKS);
									dmg += Gate.AXE_SMASH_SUCCESS_DURABILITY;
									ParticleEffect.DAMAGE_INDICATOR.display(1.5f, 55, i.getLocation(), 32);
									new GSound(Sound.BLOCK_ANVIL_FALL, 1f, 0.55f).play(i.getLocation());
									new GSound(Sound.BLOCK_ANVIL_FALL, 1f, 0.85f).play(i.getLocation());
									new GSound(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1f, 1.25f).play(i.getLocation());
								}
							}

							e.getPlayer().setCooldown(is.getType(), Gate.AXE_SMASH_ATTEMPT_COOLDOWN);
							new GSound(Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 0.4f, 1.655f).play(e.getPlayer().getLocation());
							new GSound(Sound.BLOCK_IRON_TRAPDOOR_OPEN, 0.4f, 0.655f).play(e.getPlayer().getLocation());
						}
					}

					if(dmg > 0)
					{
						dmg = (int) (dmg * Gate.AXE_SWEEP_DURABILITY_MULTIPLIER);
						e.getPlayer().setCooldown(is.getType(), (int) ((dmg + 5) * Gate.AXE_SWEEP_COOLDOWN_MULTIPLIER));

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
