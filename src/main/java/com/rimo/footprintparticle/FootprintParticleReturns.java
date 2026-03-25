package com.rimo.footprintparticle;

import com.rimo.footprintparticle.config.FPPConfig;
import com.rimo.footprintparticle.config.ModConfig;
// import com.rimo.footprintparticle.config.ModConfigScreenFactory; // Disabled until Cloth Config warnings resolved
import com.rimo.footprintparticle.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

@Mod(FootprintParticleReturns.MOD_ID)
public class FootprintParticleReturns {
    public static final String MOD_ID = "footprintparticle";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Global configuration instance
    public static final FPPConfig CONFIG = new FPPConfig();

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, MOD_ID);
    public static final Supplier<FootprintParticleType> FOOTPRINT = PARTICLES.register("footprint", () -> new FootprintParticleType(false));
    public static final Supplier<WatermarkParticleType> WATERMARK = PARTICLES.register("watermark", () -> new WatermarkParticleType(false));
    public static final Supplier<SnowDustParticleType> SNOWDUST = PARTICLES.register("snowdust", () -> new SnowDustParticleType(false));
    public static final Supplier<WaterSplashParticleType> WATERSPLASH = PARTICLES.register("watersplash", () -> new WaterSplashParticleType(false));
    
    public FootprintParticleReturns(IEventBus modEventBus, ModContainer modContainer) {
        PARTICLES.register(modEventBus);
        
        // Register configuration
        modContainer.registerConfig(Type.CLIENT, ModConfig.SPEC);
        
        // Register mod bus events manually to avoid annotation issues
        if (FMLEnvironment.getDist().isClient()) {
            modEventBus.addListener(this::onClientSetup);
            modEventBus.addListener(this::registerParticleProviders);
            
            // TODO: Re-enable configuration screen when Cloth Config @OnlyIn warnings are resolved
            // modContainer.registerExtensionPoint(net.neoforged.neoforge.client.gui.IConfigScreenFactory.class, 
            //     new ModConfigScreenFactory());
        }
        
        LOGGER.info("Footprint Particle Returns mod initialized");
    }
    
    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("Footprint Particle Returns client setup");
    }
    
    private void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(FOOTPRINT.get(), FootprintParticle.DefaultFactory::new);
        event.registerSpriteSet(WATERMARK.get(), WatermarkParticle.DefaultFactory::new);
        event.registerSpriteSet(SNOWDUST.get(), SnowDustParticle.DefaultFactory::new);
        event.registerSpriteSet(WATERSPLASH.get(), WaterSplashParticle.DefaultFactory::new);
    }
}
