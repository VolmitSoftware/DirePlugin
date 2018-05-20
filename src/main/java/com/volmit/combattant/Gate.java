package com.volmit.combattant;

public class Gate
{
	/**
	 * The maximum hydration points
	 */
	public static double MAX_HYDRATION = 10000;

	/**
	 * Breaking and placing over ticks
	 */
	public static int STAMINA_DRAIN_BREAK_PLACE_TICKS = 3;

	/**
	 * Breaking and placing amount
	 */
	public static double STAMINA_DRAIN_BREAK_PLACE = 10;

	/**
	 * The maximum stamina points
	 */
	public static double MAX_STAMINA = 6000;

	/**
	 * Average radius. Higher the radius, the longer it takes to tail the true
	 * value. For example, if the radius is set to 20, it will take 20 ticks to
	 * reach its true value assuming no further changes within that time interval.
	 */
	public static int BAR_AVERAGE_RADIUS = 10;

	/**
	 * The base dehydration per dehydration interval. Set this as positive.
	 */
	public static double DEHYDRATION_BASE = 0.1;

	/**
	 * The dehydration per interval while sprinted (added to the base)
	 */
	public static double DEHYDRATION_SPRINT_ADDITIVE = 0.7;

	/**
	 * Reduce dehydration if the saturation is above DEHYDRATION_SATURATION_MINIMUM
	 */
	public static double DEHYDRATION_SATURATION_REDUCTION = 0.2;

	/**
	 * If saturation is above this value, DEHYDRATION_SATURATION_REDUCTION will be
	 * applied to the DEHYDRATION_BASE
	 */
	public static int DEHYDRATION_SATURATION_MINIMUM = 1;

	/**
	 * Drain hydration per interval by FIRE_TICKS / DEHYDRATE_FIRE_RATIO. If the
	 * dehydrate ratio is set to 20, then fireticks at 20 will drain you 1 hydration
	 * point.
	 */
	public static double DEHYDRATE_FIRE_RATIO = 14;

	/**
	 * How much hydration per interval while in water
	 */
	public static int HYDRATE_IN_WATER_TICKS = 1;

	/**
	 * How long should we hydrate while in water per interval of amt
	 * (HYDRATE_IN_WATER_TICKS)
	 */
	public static double HYDRATE_IN_WATER_AMOUNT = 10;

	/**
	 * Trigger draining hydration in exchange for stamina once stamina drops below
	 * this percent
	 */
	public static double STAMINA_REGEN_WATER_MIN_STAMINA_PERCENT = 0.25;

	/**
	 * Regenerate this much stamina when generating stamina from hydration
	 */
	public static double STAMINA_REGEN_WATER_AMOUNT = 3.8;

	/**
	 * Drain this much hydration while generating stamina (from hydration)
	 */
	public static double STAMINA_REGEN_WATER_DRAIN = 1.4;

	/**
	 * How many ticks should hydration stream into stamina
	 */
	public static int STAMINA_REGEN_WATER_TICKS = 5;

	/**
	 * The base drain rate of hydration for temperature calc. This is not how much
	 * we are taking away. THIS IS A PERCENT multiplier on any drains that hit the
	 * hydration value.
	 */
	public static double HYDRATION_DRAIN_RATE_BASE = 0.5;

	/**
	 * The base drain multiplier of hydration while in the nether. (121% drain rate)
	 */
	public static double HYDRATION_DRAIN_HELL_BONUS = 1.21;

	/**
	 * The generation multiplier of hydration
	 */
	public static double HYDRATION_GEN_RATE_BASE = 1;

	/**
	 * The generation multiplier of hydration while in water
	 */
	public static double HYDRATION_GEN_RATE_IN_WATER_BUNUS = 1.21;

	/**
	 * The gen multiplier of hydration is reduced by the temperature (0 - 2) /
	 * DIVISOR
	 */
	public static double HYDRATION_GEN_RATE_TEMP_DIVISOR = 4;

	/**
	 * The drain multiplier is boosted by the temp (0 - 2) / DIVISOR
	 */
	public static double HYDRATION_DRAIN_RATE_TEMP_DIVISOR = 2;

	/**
	 * The stamina generation multiplier (base) while in the end.
	 */
	public static double STAMINA_GEN_RATE_END_BASE = 0.15;

	/**
	 * The stamina generation multiplier (base).
	 */
	public static double STAMINA_GEN_RATE_BASE = 0.21;

	/**
	 * The drain multiplier (base)
	 */
	public static double STAMINA_DRAIN_RATE_BASE = 1.5;

	/**
	 * Hydration drain ticks
	 */
	public static int HYDRATION_DRAIN_TICKS = 1;

	/**
	 * Hydration tick interval (dont use 0)
	 */
	public static int HYDRATION_DRAIN_TICK_INTERVAL = 1;

	/**
	 * Stamina tick interval (dont use 0)
	 */
	public static int STAMINA_GEN_TICK_INTERVAL = 3;

	/**
	 * Stamina saturation consumption. Consumes X saturation in exchange for
	 * stamina. Remember, health regenerates from saturation also. Sharing is
	 * caring.
	 */
	public static double STAMINA_SATURATION_CONSUME = 0.5;

	/**
	 * Generate stamina base from saturation
	 */
	public static double STAMINA_SATURATION_GEN = 0.62;

	/**
	 * Generate stamina base from food level (after saturation is depleted)
	 */
	public static double STAMINA_FOOD_GEN = 0.94;

	/**
	 * The minimum food level to consume food for stamina (after saturation is
	 * eaten)
	 */
	public static double STAMINA_MIN_FOOD_LEVEL = 8;

	/**
	 * Generate stamina base
	 */
	public static double STAMINA_GEN_BASE = 5.71;

	/**
	 * While holding right click with bow draw how much stamina
	 */
	public static double STAMINA_DRAIN_BOW_DRAW = 3.2;
	/**
	 * While holding right click with bow draw how much stamina
	 */
	public static double STAMINA_DRAIN_SHIELD_ARMED = 1.1;

	/**
	 * Generate stamina over ticks
	 */
	public static int STAMINA_GEN_OVER_TICKS = 30;

	/**
	 * Stamina drain on damage multiplier
	 */
	public static double STAMINA_DRAIN_ON_DAMAGED = 7.4;

	/**
	 * Stamina drain over ticks from damage
	 */
	public static int STAMINA_DRAIN_ON_DAMAGED_TICKS = 1;

	/**
	 * Stamina drain damage persist (small amt over large time)
	 */
	public static double STAMINA_DRAIN_ON_DAMAGED_PERSIST = 0.01;

	/**
	 * Damage reduction from using stamina
	 */
	public static double STAMINA_ON_DAMAGED_REDUCTION = 1.2;

	/**
	 * Stamina drain damage persist (small amt over large time) ticks
	 */
	public static int STAMINA_DRAIN_ON_DAMAGED_PERSIST_TICKS = 100;

	/**
	 * Stamina drain on damage multiplier
	 */
	public static double STAMINA_DRAIN_ON_DAMAGE = 7.4;

	/**
	 * Stamina drain over ticks from damage
	 */
	public static int STAMINA_DRAIN_ON_DAMAGE_TICKS = 1;

	/**
	 * Stamina drain damage persist (small amt over large time)
	 */
	public static double STAMINA_DRAIN_ON_DAMAGE_PERSIST = 0.01;

	/**
	 * Damage increase from using stamina
	 */
	public static double STAMINA_ON_DAMAGE_MULTIPLIER = 1.2;

	/**
	 * Stamina drain damage persist (small amt over large time) ticks
	 */
	public static int STAMINA_DRAIN_ON_DAMAGE_PERSIST_TICKS = 100;

	/**
	 * Adds and removes stamina based on how your food level changes. This means if
	 * your hunger goes down by 1 point, you would loose MULTIPLIER * hunger change.
	 * However this also goes in the other direction. If you eat food, you will gain
	 * stamina.
	 */
	public static double STAMINA_FOLLOW_FOOD_MULTIPLIER = 1.0;

	/**
	 * When modifying stamina from food level changes, how long to add/remove
	 * stamina in ticks
	 */
	public static int STAMINA_FOLLOW_FOOD_TICKS = 50;

	/**
	 * Stamina food burst ticks
	 */
	public static int STAMINA_FOLLOW_FOOD_BURST_TICKS = 30;

	/**
	 * When eating food, gain food points multiplied by BURST across
	 * STAMINA_FOLLOW_FOOD_BURST_TICKS
	 */
	public static double STAMINA_FOLLOW_FOOD_MULTIPLIER_BURST = 2.3;

	/**
	 * After eating food, trickle add stamina for a longer period of time with this
	 * amount.
	 */
	public static double STAMINA_GAIN_FOOD_TRICKLE = 2.6;

	/**
	 * Trickle food to stamina over this duration MULTIPLIED by the food points
	 * gained. If you eat and gain 3 food points, that is 150 ticks (7 seconds)
	 */
	public static int STAMINA_GAIN_FOOD_TRICKLE_TICKS_MULTIPLIER = 130;

	/**
	 * Hydration gained from consuming water (burst)
	 */
	public static double HYDRATION_CONSUME_WATER = 450;

	/**
	 * Hydration gained from consuming water over time (ticks)
	 */
	public static int HYDRATION_CONSUME_WATER_TICKS = 5;

	/**
	 * Stamina sprint drain
	 */
	public static double STAMINA_SPRINT_DRAIN = 0.95;

	/**
	 * Stamina sprint drain for ticks. (stacks)
	 */
	public static int STAMINA_SPRINT_DRAIN_TICKS = 11;
}
