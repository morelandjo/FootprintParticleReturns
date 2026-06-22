package com.rimo.footprintparticle;

import com.rimo.footprintparticle.config.FPPConfig;
import com.rimo.footprintparticle.particle.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class FPPClient {
	public static final String MOD_ID = "footprintparticle";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	// Configuration system for NeoForge
	public static final FPPConfig CONFIG = new FPPConfig();
	
	// Particle type references for compatibility
	public static final Supplier<WatermarkParticleType> WATERMARK = FootprintParticleReturns.WATERMARK;
	public static final Supplier<WaterSplashParticleType> WATERSPLASH = FootprintParticleReturns.WATERSPLASH;
}
