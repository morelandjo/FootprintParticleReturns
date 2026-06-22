package com.rimo.footprintparticle.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Footprint generation settings
    public static final ModConfigSpec.IntValue ENABLE_MOD;
    public static final ModConfigSpec.DoubleValue SEC_PER_PRINT;
    public static final ModConfigSpec.DoubleValue PRINT_LIFETIME;
    public static final ModConfigSpec.DoubleValue PRINT_HEIGHT;
    public static final ModConfigSpec.BooleanValue CAN_GEN_WHEN_INVISIBLE;
    
    // Visual settings
    public static final ModConfigSpec.IntValue WET_DURATION;
    public static final ModConfigSpec.DoubleValue WATERMARK_ALPHA;
    public static final ModConfigSpec.DoubleValue FOOTPRINT_ALPHA;
    public static final ModConfigSpec.IntValue FOOTPRINT_SIZE;
    
    // Particle effect settings
    public static final ModConfigSpec.DoubleValue RAIL_FLAME_RANGE;
    public static final ModConfigSpec.BooleanValue ENABLE_BOAT_TRAIL;
    public static final ModConfigSpec.IntValue SWIM_POP_LEVEL;
    public static final ModConfigSpec.IntValue SNOW_DUST_LEVEL;
    public static final ModConfigSpec.IntValue WATER_SPLASH_LEVEL;

    static {
        BUILDER.push("General Settings");
        
        ENABLE_MOD = BUILDER
            .comment("Enable mod: 0 = disabled, 1 = players only, 2 = all entities")
            .defineInRange("enableMod", 2, 0, 2);
            
        SEC_PER_PRINT = BUILDER
            .comment("Time between footprints in seconds (lower = more footprints)")
            .defineInRange("secPerPrint", 0.2, 0.05, 2.0);
            
        PRINT_LIFETIME = BUILDER
            .comment("How long footprints stay visible in seconds")
            .defineInRange("printLifetime", 10.0, 1.0, 60.0);
            
        PRINT_HEIGHT = BUILDER
            .comment("Height offset for footprints above blocks")
            .defineInRange("printHeight", 0.0, -0.5, 0.5);
            
        CAN_GEN_WHEN_INVISIBLE = BUILDER
            .comment("Generate footprints for invisible entities")
            .define("canGenWhenInvisible", true);
            
        BUILDER.pop();
        
        BUILDER.push("Visual Settings");
        
        WET_DURATION = BUILDER
            .comment("How long entities stay 'wet' after being in water (in ticks, 20 = 1 second)")
            .defineInRange("wetDuration", 10, 1, 100);
            
        WATERMARK_ALPHA = BUILDER
            .comment("Transparency of watermark particles (0.0 = invisible, 1.0 = opaque)")
            .defineInRange("watermarkAlpha", 0.4, 0.0, 1.0);
            
        FOOTPRINT_ALPHA = BUILDER
            .comment("Transparency of footprint particles (0.0 = invisible, 1.0 = opaque)")
            .defineInRange("footprintAlpha", 0.7, 0.0, 1.0);
            
        FOOTPRINT_SIZE = BUILDER
            .comment("Size multiplier for footprint particles")
            .defineInRange("footprintSize", 5, 1, 20);
            
        BUILDER.pop();
        
        BUILDER.push("Particle Effects");
        
        RAIL_FLAME_RANGE = BUILDER
            .comment("Probability of rail sparks for minecarts (0.0 = never, 1.0 = always)")
            .defineInRange("railFlameRange", 0.2, 0.0, 1.0);
            
        ENABLE_BOAT_TRAIL = BUILDER
            .comment("Enable particle trails for boats")
            .define("enableBoatTrail", true);
            
        SWIM_POP_LEVEL = BUILDER
            .comment("Swimming bubble particles: 0 = disabled, 1 = players only, 2 = all entities")
            .defineInRange("swimPopLevel", 2, 0, 2);
            
        SNOW_DUST_LEVEL = BUILDER
            .comment("Snow dust particles: 0 = disabled, 1 = players only, 2 = all entities")
            .defineInRange("snowDustLevel", 2, 0, 2);
            
        WATER_SPLASH_LEVEL = BUILDER
            .comment("Water splash particles: 0 = disabled, 1 = players only, 2 = all entities")
            .defineInRange("waterSplashLevel", 1, 0, 2);
            
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }
}