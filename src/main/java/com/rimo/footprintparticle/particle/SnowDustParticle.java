package com.rimo.footprintparticle.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SnowDustParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    // Copy from net.minecraft.client.particle.CloudParticle
    protected SnowDustParticle(ClientLevel clientLevel, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteProvider) {
        super(clientLevel, x, y, z, 0.0, 0.0, 0.0);
        this.friction = 0.96F;
        this.spriteProvider = spriteProvider;
        this.xd *= 0.10000000149011612;
        this.yd *= 0.10000000149011612;
        this.zd *= 0.10000000149011612;
        this.xd += vx;
        //this.yd += vy;
        this.zd += vz;
        float g = 1.0F - (float)(Math.random() * 0.30000001192092896);
        this.rCol = g;
        this.gCol = g;
        this.bCol = g;
        this.quadSize *= 1.875F;
        int i = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int)Math.max((float)i * 2.5F, 1.0F);
        //this.hasPhysics = false;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }    @Override
    public void tick() {
        super.tick();
        if (!this.removed)        this.setSpriteFromAge(this.spriteProvider);
    }

    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public DefaultFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            Particle particle = new SnowDustParticle(level, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
            if (parameters instanceof SnowDustParticleType snowdust)
                particle.scale(snowdust.size);
            return particle;
        }
    }
}
