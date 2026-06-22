package com.rimo.footprintparticle.config;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FPPConfig {	static final List<String> DEF_APPLYBLOCKS = Arrays.asList(
			"#minecraft:wool",
			"minecraft:dirt",
			"minecraft:grass_block", 
			"minecraft:sand",
			"minecraft:gravel",
			"minecraft:snow_block",
			"minecraft:snow",
			"minecraft:stone",
			"minecraft:cobblestone",
			"minecraft:oak_planks",
			"minecraft:spruce_planks",
			"minecraft:birch_planks"
	);
	static final List<String> DEF_BLOCKHEIGHT = Arrays.asList(
			"minecraft:snow,0.125",
			"minecraft:soul_sand,0.125",
			"minecraft:mud,0.125"
	);
	static final List<String> DEF_EXCLUDEDBLOCKS = Arrays.asList(
			"minecraft:beehive",
			"#minecraft:flower",
			"#minecraft:crop",
			"#minecraft:leaves",
			"#minecraft:sapling",
			"#minecraft:replaceable_plants"
	);
	static final List<String> DEF_MODS = Arrays.asList(
			"minecraft:parrot",
			"minecraft:bee",
			"minecraft:allay",
			"minecraft:bat",
			"minecraft:phantom",
			"minecraft:squid",
			"minecraft:glow_squid",
			"minecraft:axolotl",
			"minecraft:dolphin",
			"minecraft:wither",
			"minecraft:ender_dragon"
	);
	static final List<String> DEF_SIZE = Arrays.asList(
			"minecraft:chicken,0.6",
			"minecraft:pig,0.8",
			"minecraft:cat,0.5",
			"minecraft:ocelot,0.5",
			"minecraft:wolf,0.6",
			"minecraft:enderman,0.6",
			"minecraft:slime,2",
			"minecraft:magma_cube,2",
			"minecraft:creeper,0.8",
			"minecraft:iron_golem,1.2",
			"minecraft:ravager,2"
	);
	static final List<String> DEF_FOUR_LEGS = Arrays.asList(
			"minecraft:horse",
			"minecraft:donkey",
			"minecraft:mule",
			"minecraft:zombie_horse",
			"minecraft:skeleton_horse",
			"minecraft:ravager,1",
			"minecraft:creeper,0.3"
	);
	static final List<String> DEF_EIGHT_LEGS = Arrays.asList(
			"minecraft:spider",
			"minecraft:cave_spider",
			"minecraft:iron_golem,0.3",
			"minecraft:ravager,0.5"
	);
	static final List<String> DEF_MOB_INTERVAL = Arrays.asList(
			"minecraft:spider,0.5",
			"minecraft:cave_spider,0.5",
			"minecraft:iron_golem,2",
			"minecraft:creeper,0.8"
	);
	static final List<String> DEF_CUSTOM_PRINT = Arrays.asList(
			"mod_id:mob_id,fileName_NoExtend"
	);
	// Configuration values are now managed by ModConfig
	// These methods return values from the NeoForge config system
	private List<String> applyBlocks = DEF_APPLYBLOCKS;
	private List<String> blockHeight = DEF_BLOCKHEIGHT;
	private List<String> excludedBlocks = DEF_EXCLUDEDBLOCKS;
	private List<String> excludedMobs = DEF_MODS;
	private List<String> sizePerMob = DEF_SIZE;
	private List<String> spiderLikeMobs = DEF_EIGHT_LEGS;
	private List<String> horseLikeMobs = DEF_FOUR_LEGS;
	private List<String> mobInterval = DEF_MOB_INTERVAL;
	private List<String> customPrint = DEF_CUSTOM_PRINT;

	public int isEnable() {return ModConfig.ENABLE_MOD.get();}
	public float getSecPerPrint() {return ModConfig.SEC_PER_PRINT.get().floatValue();}
	public float getPrintLifetime() {return ModConfig.PRINT_LIFETIME.get().floatValue();}
	public float getPrintHeight() {return ModConfig.PRINT_HEIGHT.get().floatValue();}
	public List<String> getApplyBlocks() {return applyBlocks;}
	public List<String> getBlockHeight() {return blockHeight;}
	public List<String> getExcludedBlocks() {return excludedBlocks;}
	public boolean getCanGenWhenInvisible() {return ModConfig.CAN_GEN_WHEN_INVISIBLE.get();}
	public List<String> getExcludedMobs() {return excludedMobs;}
	public List<String> getSizePerMob() {return sizePerMob;}
	public List<String> getHorseLikeMobs() {return horseLikeMobs;}
	public List<String> getSpiderLikeMobs() {return spiderLikeMobs;}
	public int getWetDuration() {return ModConfig.WET_DURATION.get();}
	public float getWatermarkAlpha() {return ModConfig.WATERMARK_ALPHA.get().floatValue();}
	public float getFootprintAlpha() {return ModConfig.FOOTPRINT_ALPHA.get().floatValue();}
	public float getRailFlameRange() {return ModConfig.RAIL_FLAME_RANGE.get().floatValue();}
	public boolean isEnableBoatTrail() {return ModConfig.ENABLE_BOAT_TRAIL.get();}
	public int getSwimPopLevel() {return ModConfig.SWIM_POP_LEVEL.get();}
	public int getSnowDustLevel() {return ModConfig.SNOW_DUST_LEVEL.get();}
	public int getWaterSplashLevel() {return ModConfig.WATER_SPLASH_LEVEL.get();}
	public List<String> getMobInterval() {return mobInterval;}
	public int getFootprintSize() {return ModConfig.FOOTPRINT_SIZE.get();}
	public List<String> getCustomPrint() {return customPrint;}

	// These setters are deprecated - use the config file instead
	public void setEnableMod(int isEnable) { /* Config-managed value */ }
	public void setSecPerPrint(float sec) { /* Config-managed value */ }
	public void setPrintLifetime(float time) { /* Config-managed value */ }
	public void setPrintHeight(float height) { /* Config-managed value */ }
	public void setApplyBlocks(List<String> list) {applyBlocks = new ArrayList<>(list);}
	public void setBlockHeight(List<String> list) {blockHeight = new ArrayList<>(list);}
	public void setExcludedBlocks(List<String> list) {excludedBlocks = new ArrayList<>(list);}
	public void setCanGenWhenInvisible(boolean isEnable) { /* Config-managed value */ }
	public void setExcludedMobs(List<String> list) {excludedMobs = new ArrayList<>(list);}
	public void setSizePerMob(List<String> list) {sizePerMob = new ArrayList<>(list);}
	public void setHorseLikeMobs(List<String> list) {horseLikeMobs = new ArrayList<>(list);}
	public void setSpiderLikeMobs(List<String> list) {spiderLikeMobs = new ArrayList<>(list);}
	public void setWetDuration(int time) { /* Config-managed value */ }
	public void setWatermarkAlpha(float watermarkAlpha) { /* Config-managed value */ }
	public void setFootprintAlpha(float footprintAlpha) { /* Config-managed value */ }
	public void setRailFlameRange(float railFlameRange) { /* Config-managed value */ }
	public void setEnableBoatTrail(boolean enableBoatTrail) { /* Config-managed value */ }
	public void setSwimPopLevel(int swimPopLevel) { /* Config-managed value */ }
	public void setSnowDustLevel(int snowDustLevel) { /* Config-managed value */ }
	public void setWaterSplashLevel(int waterSplashLevel) { /* Config-managed value */ }
	public void setMobInterval(List<String> mobInterval) {this.mobInterval = mobInterval;}
	public void setFootprintSize(int footprintSize) { /* Config-managed value */ }
	public void setCustomPrint(List<String> customPrint) {this.customPrint = customPrint;}
}
