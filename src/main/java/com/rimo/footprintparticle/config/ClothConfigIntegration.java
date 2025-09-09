package com.rimo.footprintparticle.config;

import com.rimo.footprintparticle.FootprintParticleReturns;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigIntegration {
    
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.footprintparticle.title"))                .setSavingRunnable(() -> {
                    // Save configuration logic - For NeoForge, configurations are automatically saved
                    // when the setSaveConsumer callbacks are triggered
                    FootprintParticleReturns.LOGGER.info("Configuration saved");
                });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        // General Settings Category
        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.footprintparticle.category.general"));
          general.addEntry(entryBuilder.startEnumSelector(
                Component.translatable("config.footprintparticle.enable_mod"),
                EnableMode.class,
                EnableMode.fromInt(FootprintParticleReturns.CONFIG.isEnable()))
                .setDefaultValue(EnableMode.ALL_ENTITIES)
                .setTooltip(Component.translatable("config.footprintparticle.enable_mod.tooltip"))
                .setSaveConsumer(value -> FootprintParticleReturns.CONFIG.setEnableMod(value.getValue()))
                .build());
          general.addEntry(entryBuilder.startFloatField(
                Component.translatable("config.footprintparticle.sec_per_print"),
                FootprintParticleReturns.CONFIG.getSecPerPrint())
                .setDefaultValue(0.5f)
                .setMin(0.05f)
                .setMax(5.0f)
                .setTooltip(Component.translatable("config.footprintparticle.sec_per_print.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setSecPerPrint)
                .build());
        
        general.addEntry(entryBuilder.startFloatField(
                Component.translatable("config.footprintparticle.print_lifetime"),
                FootprintParticleReturns.CONFIG.getPrintLifetime())
                .setDefaultValue(60.0f)
                .setMin(1.0f)
                .setMax(300.0f)
                .setTooltip(Component.translatable("config.footprintparticle.print_lifetime.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setPrintLifetime)
                .build());
        
        general.addEntry(entryBuilder.startFloatField(
                Component.translatable("config.footprintparticle.print_height"),
                FootprintParticleReturns.CONFIG.getPrintHeight())
                .setDefaultValue(0.0f)
                .setMin(-0.5f)
                .setMax(0.5f)
                .setTooltip(Component.translatable("config.footprintparticle.print_height.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setPrintHeight)
                .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(
                Component.translatable("config.footprintparticle.can_gen_when_invisible"),
                FootprintParticleReturns.CONFIG.getCanGenWhenInvisible())
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.footprintparticle.can_gen_when_invisible.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setCanGenWhenInvisible)
                .build());
        
        // Particle Settings Category
        ConfigCategory particles = builder.getOrCreateCategory(Component.translatable("config.footprintparticle.category.particles"));
        
        particles.addEntry(entryBuilder.startFloatField(
                Component.translatable("config.footprintparticle.footprint_alpha"),
                FootprintParticleReturns.CONFIG.getFootprintAlpha())
                .setDefaultValue(0.7f)
                .setMin(0.1f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("config.footprintparticle.footprint_alpha.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setFootprintAlpha)
                .build());
        
        particles.addEntry(entryBuilder.startFloatField(
                Component.translatable("config.footprintparticle.watermark_alpha"),
                FootprintParticleReturns.CONFIG.getWatermarkAlpha())
                .setDefaultValue(0.4f)
                .setMin(0.1f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("config.footprintparticle.watermark_alpha.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setWatermarkAlpha)
                .build());
        
        particles.addEntry(entryBuilder.startIntField(
                Component.translatable("config.footprintparticle.wet_duration"),
                FootprintParticleReturns.CONFIG.getWetDuration())
                .setDefaultValue(10)
                .setMin(1)
                .setMax(60)
                .setTooltip(Component.translatable("config.footprintparticle.wet_duration.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setWetDuration)
                .build());
        
        particles.addEntry(entryBuilder.startEnumSelector(
                Component.translatable("config.footprintparticle.water_splash_level"),
                ParticleLevel.class,
                ParticleLevel.fromInt(FootprintParticleReturns.CONFIG.getWaterSplashLevel()))
                .setDefaultValue(ParticleLevel.PLAYER_ONLY)
                .setTooltip(Component.translatable("config.footprintparticle.water_splash_level.tooltip"))
                .setSaveConsumer(value -> FootprintParticleReturns.CONFIG.setWaterSplashLevel(value.getValue()))
                .build());
        
        particles.addEntry(entryBuilder.startEnumSelector(
                Component.translatable("config.footprintparticle.swim_pop_level"),
                ParticleLevel.class,
                ParticleLevel.fromInt(FootprintParticleReturns.CONFIG.getSwimPopLevel()))
                .setDefaultValue(ParticleLevel.ALL_ENTITIES)
                .setTooltip(Component.translatable("config.footprintparticle.swim_pop_level.tooltip"))
                .setSaveConsumer(value -> FootprintParticleReturns.CONFIG.setSwimPopLevel(value.getValue()))
                .build());
        
        particles.addEntry(entryBuilder.startEnumSelector(
                Component.translatable("config.footprintparticle.snow_dust_level"),
                ParticleLevel.class,
                ParticleLevel.fromInt(FootprintParticleReturns.CONFIG.getSnowDustLevel()))
                .setDefaultValue(ParticleLevel.ALL_ENTITIES)
                .setTooltip(Component.translatable("config.footprintparticle.snow_dust_level.tooltip"))
                .setSaveConsumer(value -> FootprintParticleReturns.CONFIG.setSnowDustLevel(value.getValue()))
                .build());
        
        // Vehicle Settings Category
        ConfigCategory vehicles = builder.getOrCreateCategory(Component.translatable("config.footprintparticle.category.vehicles"));
        
        vehicles.addEntry(entryBuilder.startBooleanToggle(
                Component.translatable("config.footprintparticle.enable_boat_trail"),
                FootprintParticleReturns.CONFIG.isEnableBoatTrail())
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.footprintparticle.enable_boat_trail.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setEnableBoatTrail)
                .build());
        
        vehicles.addEntry(entryBuilder.startFloatField(
                Component.translatable("config.footprintparticle.rail_flame_range"),
                FootprintParticleReturns.CONFIG.getRailFlameRange())
                .setDefaultValue(0.2f)
                .setMin(0.0f)
                .setMax(1.0f)
                .setTooltip(Component.translatable("config.footprintparticle.rail_flame_range.tooltip"))
                .setSaveConsumer(FootprintParticleReturns.CONFIG::setRailFlameRange)
                .build());
        
        return builder.build();
    }
    
    public enum EnableMode {
        DISABLED(0, "disabled"),
        PLAYER_ONLY(1, "player_only"), 
        ALL_ENTITIES(2, "all_entities");
        
        private final int value;
        private final String key;
        
        EnableMode(int value, String key) {
            this.value = value;
            this.key = key;
        }
        
        public int getValue() {
            return value;
        }
        
        public String getKey() {
            return key;
        }
        
        public static EnableMode fromInt(int value) {
            for (EnableMode mode : values()) {
                if (mode.value == value) {
                    return mode;
                }
            }
            return ALL_ENTITIES;
        }
        
        @Override
        public String toString() {
            return Component.translatable("config.footprintparticle.enable_mode." + key).getString();
        }
    }
    
    public enum ParticleLevel {
        DISABLED(0, "disabled"),
        PLAYER_ONLY(1, "player_only"),
        ALL_ENTITIES(2, "all_entities");
        
        private final int value;
        private final String key;
        
        ParticleLevel(int value, String key) {
            this.value = value;
            this.key = key;
        }
        
        public int getValue() {
            return value;
        }
        
        public String getKey() {
            return key;
        }
        
        public static ParticleLevel fromInt(int value) {
            for (ParticleLevel level : values()) {
                if (level.value == value) {
                    return level;
                }
            }
            return ALL_ENTITIES;
        }
        
        @Override
        public String toString() {
            return Component.translatable("config.footprintparticle.particle_level." + key).getString();
        }
    }
}
