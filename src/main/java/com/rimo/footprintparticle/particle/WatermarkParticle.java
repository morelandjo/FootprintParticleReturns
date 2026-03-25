package com.rimo.footprintparticle.particle;

import com.rimo.footprintparticle.FPPClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class WatermarkParticle extends FootprintParticle {

	protected WatermarkParticle(ClientLevel clientLevel, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteProvider, FootprintParticleType parameters, String defName) {
		super(clientLevel, x, y, z, vx, vy, vz, spriteProvider, parameters, defName);
		this.setAlpha(FPPClient.CONFIG.getWatermarkAlpha() * (FPPClient.CONFIG.getWetDuration() * 20 - (float) vy) / FPPClient.CONFIG.getWetDuration() / 20);
	}

	public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public DefaultFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, net.minecraft.util.RandomSource randomSource) {
			return new WatermarkParticle(level, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider, (WatermarkParticleType) parameters, "watermark");
		}
	}
}
