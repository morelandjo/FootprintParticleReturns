package com.rimo.footprintparticle.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class WaterSplashParticle extends WaterDropParticle {

	// Vanilla's splash & rain particle can't apply vy (h), so we made custom one to override it.
	protected WaterSplashParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteProvider) {
		super(clientLevel, d, e, f, spriteProvider.get(0, 1));
		this.gravity = 0.04F;
		this.xd = g;
		this.yd = 0.1f + h;
		this.zd = i;
		this.lifetime *= 10;
	}

	public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public DefaultFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, net.minecraft.util.RandomSource randomSource) {
			return new WaterSplashParticle(clientLevel, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
