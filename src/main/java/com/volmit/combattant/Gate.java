package com.volmit.combattant;

public class Gate
{
	/**
	 * Creeper
	 */
	public static double AI_GOAL_CREEPER_DIVEBOMB_MINIMUM_LATERAL_SEPERATION = 0.1;
	public static double AI_GOAL_CREEPER_DIVEBOMB_MAXIMUM_LATERAL_SEPERATION = 4.7;
	public static double AI_GOAL_CREEPER_DIVEBOMB_MINIMUM_HEIGHT_SEPERATION = 5.9;
	public static double AI_GOAL_CREEPER_DIVEBOMB_ATTEMPT_IGNITE_DISTANCE = 1.25;
	public static double AI_GOAL_CREEPER_DIVEBOMB_LUNGE_SPEED = 0.45;
	public static double AI_GOAL_CREEPER_RUSHBOMB_MINIMUM_LATERAL_SEPERATION = 4;
	public static double AI_GOAL_CREEPER_RUSHBOMB_MAXIMUM_LATERAL_SEPERATION = 14;
	public static double AI_GOAL_CREEPER_RUSHBOMB_MINIMUM_HEIGHT_SEPERATION = 2;
	public static double AI_GOAL_CREEPER_RUSHBOMB_CHARGE_SPEED = 1.2;
	public static int AI_GOAL_CREEPER_RUSHBOMB_FUSE_TICKS_CONTACT = 8;
	public static int AI_GOAL_CREEPER_RUSHBOMB_FUSE_TICKS = 55;
	public static double AI_GOAL_CREEPER_LISTENING_POWER = 1.7;

	/**
	 * Blaze
	 */
	public static double AI_GOAL_BLAZE_LISTENING_POWER = 1.7;
	public static double AI_GOAL_BLAZE_TRACER_GFORCE = 0.5;
	public static double AI_GOAL_BLAZE_TRACER_SPEED = 1.8;
	public static double AI_GOAL_BLAZE_STRAFE_TARGET_CHANCE = 0.35;
	public static double AI_GOAL_BLAZE_ATTACK_TARGET_RANDOM_CHANCE = 0.45;
	public static double AI_GOAL_BLAZE_SEPERATION_SPEED = 0.37;
	public static double AI_GOAL_BLAZE_APPROACH_SPEED = 0.47;
	public static double AI_GOAL_BLAZE_PRIME_RANGE = 14.5;
	public static double AI_GOAL_BLAZE_HOVER_ASCEND_SPEED = 0.45;
	public static double AI_GOAL_BLAZE_HOVER_HEIGHT = 3.85;
	public static double AI_GOAL_BLAZE_INITIATE_CHANCE = 0.025;
	public static double AI_GOAL_BLAZE_FLEE_ON_DAMAGE_SPEED = 0.75;
	public static int AI_GOAL_BLAZE_TRACER_FIRE_TICKS = 0;

	/**
	 * Ghast
	 */
	public static double AI_GOAL_GHAST_LISTENING_POWER = 1.9;
	public static double AI_GOAL_GHAST_TRACER_GFORCE = 0.3;
	public static double AI_GOAL_GHAST_TRACER_SPEED = 1.1;
	public static int AI_GOAL_GHAST_TRACER_ALIVE_TIME = 200;
	public static double AI_GOAL_GHAST_STRAFE_TARGET_CHANCE = 0.25;
	public static double AI_GOAL_GHAST_ATTACK_TARGET_RANDOM_CHANCE = 0.75;
	public static double AI_GOAL_GHAST_SEPERATION_SPEED = 0.37;
	public static double AI_GOAL_GHAST_APPROACH_SPEED = 0.77;
	public static double AI_GOAL_GHAST_PRIME_RANGE = 17.5;
	public static double AI_GOAL_GHAST_HOVER_ASCEND_SPEED = 0.25;
	public static double AI_GOAL_GHAST_HOVER_HEIGHT = 12.6;
	public static double AI_GOAL_GHAST_INITIATE_CHANCE = 0.19;
	public static double AI_GOAL_GHAST_FLEE_ON_DAMAGE_SPEED = 0.75;
	public static int AI_GOAL_GHAST_TRACER_FIRE_TICKS = 5;
	public static int AI_GOAL_GHAST_TRACER_VOLLEY_MIN = 2;
	public static int AI_GOAL_GHAST_TRACER_VOLLEY_MAX = 7;
	public static int AI_GOAL_GHAST_TRACER_VOLLEY_TIME_MIN = 1;
	public static int AI_GOAL_GHAST_TRACER_VOLLEY_TIME_MAX = 12;

	/**
	 * Skeleton & Stray
	 */
	public static double AI_GOAL_SKELETON_LISTENING_POWER = 1.7;
	public static double AI_GOAL_SKELETON_STRAFE_TARGET_CHANCE = 0.014;
	public static double AI_GOAL_SKELETON_BACKPEDAL_TARGET_CHANCE = 0.028;
	public static double AI_GOAL_SKELETON_BACKPEDAL_TARGET_TRIGGER = 6;
	public static double AI_GOAL_SKELETON_LOCK_TARGET_TRIGGER = 14;
	public static double AI_GOAL_SKELETON_LOCK_TARGET_LUNGE_SPEED = 0.95;
	public static double AI_GOAL_SKELETON_LOCK_TARGET_BACKPEDAL_SPEED = 0.95;
	public static double AI_GOAL_SKELETON_DAMAGED_BACKPEDAL_SPEED = 0.75;

	/**
	 * Aggressive (Melee)
	 */
	public static double AI_GOAL_AGGRO_FLEE_ON_DAMAGE_SPEED = 0.85;
	public static double AI_GOAL_AGGRO_LUNGE_SPEED = 0.85;
	public static double AI_GOAL_AGGRO_ATTACK_SPEED = 1.25;
	public static double AI_GOAL_AGGRO_LISTENING_POWER = 1.2;

	/**
	 * Performance
	 */
	public static int PERFORMANCE_CLOSEST_MAX_ITERATIONS = 10;
	public static int MAXIMUM_MILLISECONDS = 5;

	/**
	 * Sound Field
	 */
	public static double SOUNDFIELD_BASE_RADIUS = 18.5;
	public static boolean SOUNDFIELD_SHOW_SOUNDS = true;
	public static double SOUNDFIELD_TEMPORAL_DECAY = 1.25;
	public static double SOUNDFIELD_TEMPORAL_LIMIT = 0.15;

	/**
	 * Baubles
	 */
	public static double MAX_STAMINA = 9000;
	public static double MIN_STAMINA = 100;
	public static double MAX_STAMINA_ABSORBTION = 16000;
	public static double MAX_HYDRATION = 5000;
	public static int BAR_AVERAGE_RADIUS = 10;

	/**
	 * Stamina -> Consumption
	 */
	public static int STAMINA_DRAIN_BREAK_PLACE_TICKS = 3;
	public static double STAMINA_DRAIN_RATE_BASE = 1.5;
	public static double STAMINA_DRAIN_BOW_DRAW = 1.9;
	public static double STAMINA_DRAIN_SHIELD_ARMED = 0.1;
	public static double STAMINA_DRAIN_ON_DAMAGED = 7.4;
	public static double STAMINA_ON_DAMAGED_REDUCTION = 1.2;
	public static double STAMINA_DRAIN_ON_DAMAGED_PERSIST = 0.01;
	public static int STAMINA_DRAIN_ON_DAMAGED_TICKS = 1;
	public static int STAMINA_DRAIN_ON_DAMAGED_PERSIST_TICKS = 100;
	public static double STAMINA_DRAIN_ON_DAMAGE = 7.4;
	public static int STAMINA_DRAIN_ON_DAMAGE_TICKS = 1;
	public static double STAMINA_DRAIN_ON_DAMAGE_PERSIST = 0.01;
	public static double STAMINA_ON_DAMAGE_MULTIPLIER = 1.2;
	public static int STAMINA_DRAIN_ON_DAMAGE_PERSIST_TICKS = 100;
	public static double STAMINA_SPRINT_DRAIN = 0.95;
	public static int STAMINA_SPRINT_DRAIN_TICKS = 11;

	/**
	 * Stamina -> Regeneration
	 */
	public static double STAMINA_GAIN_FOOD_TRICKLE = 2.6;
	public static double STAMINA_GAIN_GAPPLE = 553.7;
	public static int STAMINA_GAIN_GAPPLE_TICKS = 200;
	public static int STAMINA_GAIN_FOOD_TRICKLE_TICKS_MULTIPLIER = 130;
	public static double STAMINA_FOLLOW_FOOD_MULTIPLIER = 1.0;
	public static double STAMINA_FOLLOW_FOOD_MULTIPLIER_BURST = 2.3;
	public static int STAMINA_FOLLOW_FOOD_TICKS = 50;
	public static int STAMINA_FOLLOW_FOOD_BURST_TICKS = 30;
	public static int STAMINA_GEN_OVER_TICKS = 30;
	public static double STAMINA_REGEN_WATER_MIN_STAMINA_PERCENT = 0.25;
	public static double STAMINA_REGEN_WATER_AMOUNT = 4.8;
	public static double STAMINA_REGEN_WATER_DRAIN = 1.4;
	public static double STAMINA_GEN_RATE_END_BASE = 0.15;
	public static double STAMINA_GEN_RATE_BASE = 0.21;
	public static double STAMINA_SATURATION_GEN = 10.62;
	public static double STAMINA_SATURATION_CONSUME = 0.7;
	public static double STAMINA_FOOD_GEN = 0.94;
	public static double STAMINA_MIN_FOOD_LEVEL = 17;
	public static double STAMINA_GEN_BASE = 1.71;
	public static double STAMINA_DRAIN_BREAK_PLACE = 10;
	public static int STAMINA_REGEN_FROM_HYDRATION_TICKS = 5;
	public static int STAMINA_GEN_TICK_INTERVAL = 3;

	/**
	 * Hydration Consumption
	 */
	public static double DEHYDRATION_BASE = 0.1;
	public static double DEHYDRATION_SPRINT_ADDITIVE = 0.7;
	public static int DEHYDRATION_SATURATION_MINIMUM = 1;
	public static double DEHYDRATE_FIRE_RATIO = 14;
	public static double HYDRATION_DRAIN_RATE_BASE = 0.5;
	public static double HYDRATION_DRAIN_HELL_BONUS = 1.21;
	public static double HYDRATION_DRAIN_RATE_TEMP_DIVISOR = 2;
	public static int HYDRATION_DRAIN_TICKS = 1;
	public static int HYDRATION_DRAIN_TICK_INTERVAL = 1;

	/**
	 * Hydration Regeneration
	 */
	public static int HYDRATE_IN_WATER_TICKS = 1;
	public static double HYDRATION_CONSUME_WATER = 450;
	public static int HYDRATION_CONSUME_WATER_TICKS = 5;
	public static double HYDRATION_GEN_RATE_BASE = 1;
	public static double HYDRATION_GEN_RATE_IN_WATER_BUNUS = 1.21;
	public static double HYDRATION_GEN_RATE_TEMP_DIVISOR = 4;
	public static double HYDRATE_IN_WATER_AMOUNT = 100;
	public static double DEHYDRATION_SATURATION_REDUCTION = 0.2;

	/**
	 * Bow
	 */
	public static double BOW_HEADSHOT_AABB_SIZE = 1;
	public static double BOW_HEADSHOT_CHECK_ITERATIONS = 24;
	public static double BOW_HEADSHOT_DAMAGE_MULTIPLIER = 2;

	/**
	 * Sword Stab
	 */
	public static double SWORD_STAB_MIN_STAMINA = 1250;
	public static double SWORD_STAB_SUCCESS_CONSUME_STAMINA = 50;
	public static int SWORD_STAB_SUCCESS_CONSUME_STAMINA_TICKS = 4;
	public static double SWORD_STAB_ATTEMPT_CONSUME_STAMINA = 10;
	public static int SWORD_STAB_ATTEMPT_CONSUME_STAMINA_TICKS = 3;
	public static int SWORD_STAB_ATTEMPT_COOLDOWN = 27;
	public static int SWORD_STAB_SUCCESS_COOLDOWN = 8;
	public static int SWORD_STAB_SUCCESS_DURABILITY = 44;
	public static double SWORD_STAB_DAMAGE_DIAMOND = 4;
	public static double SWORD_STAB_DAMAGE_IRON = 3;
	public static double SWORD_STAB_DAMAGE_GOLD = 3;
	public static double SWORD_STAB_DAMAGE_WOOD = 2;
	public static double SWORD_STAB_DAMAGE_STONE = 3;

	/**
	 * Axe Smash
	 */
	public static double AXE_SMASH_MIN_STAMINA = 1250;
	public static double AXE_SMASH_SUCCESS_CONSUME_STAMINA = 80;
	public static int AXE_SMASH_SUCCESS_CONSUME_STAMINA_TICKS = 4;
	public static double AXE_SMASH_ATTEMPT_CONSUME_STAMINA = 48;
	public static int AXE_SMASH_ATTEMPT_CONSUME_STAMINA_TICKS = 2;
	public static int AXE_SMASH_ATTEMPT_COOLDOWN = 16;
	public static int AXE_SMASH_SUCCESS_COOLDOWN = 8;
	public static int AXE_SMASH_SUCCESS_DURABILITY = 44;
	public static double AXE_SMASH_DAMAGE_DIAMOND = 13;
	public static double AXE_SMASH_DAMAGE_IRON = 9;
	public static double AXE_SMASH_DAMAGE_GOLD = 8;
	public static double AXE_SMASH_DAMAGE_WOOD = 6;
	public static double AXE_SMASH_DAMAGE_STONE = 7;

	/**
	 * Sword Sweep
	 */
	public static double SWORD_SWEEP_COOLDOWN_MULTIPLIER = 1;
	public static double SWORD_SWEEP_DURABILITY_MULTIPLIER = 0.35;
	public static double SWORD_SWEEP_MIN_STAMINA = 750;
	public static int SWORD_SWEEP_BASE_COOLDOWN = 7;

	/**
	 * Axe Sweep
	 */
	public static double AXE_SWEEP_COOLDOWN_MULTIPLIER = 1;
	public static double AXE_SWEEP_DURABILITY_MULTIPLIER = 0.35;
	public static double AXE_SWEEP_MIN_STAMINA = 750;
	public static int AXE_SWEEP_BASE_COOLDOWN = 7;
}
