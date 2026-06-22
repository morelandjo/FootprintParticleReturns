package com.rimo.footprintparticle.particle;

import com.rimo.footprintparticle.FPPClient;
import com.rimo.footprintparticle.Util;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class FootprintParticle extends SingleQuadParticle {
	protected float startAlpha;

	protected FootprintParticle(ClientLevel clientLevel, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteProvider, FootprintParticleType parameters, String defName) {
		super(clientLevel, x, y, z, spriteProvider.get(0, 1));

		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
		this.setAlpha(FPPClient.CONFIG.getFootprintAlpha());
		this.roll = (float) Mth.atan2(vx, vz);
		this.lifetime = (int) (FPPClient.CONFIG.getPrintLifetime() * 20);
		this.quadSize = FPPClient.CONFIG.getFootprintSize() * 0.03125f;

		this.quadSize *= Util.getEntityScale((parameters.entity));

		List<TextureAtlasSprite> spriteList = Util.getCustomSprites(parameters.entity, spriteProvider, defName);
		try {
			this.setSprite(spriteList.get((int) (Math.random() * spriteList.size())));
		} catch (Exception e) {
			FPPClient.LOGGER.error("Wrong custom texture for " + EntityType.getKey(parameters.entity.getType()).toString() + ", please check.");
			this.setSprite(spriteProvider.get(0, 1));
		}
	}
	@Override
	public void setAlpha(float a) {
		super.setAlpha(a);
		this.startAlpha = a;
	}

	@Override
	public SingleQuadParticle.Layer getLayer() {
		return SingleQuadParticle.Layer.TRANSLUCENT;
	}

	@Override
	public void tick() {
		this.y -= 0.01f / this.lifetime;
		this.yo = this.y;

		if (this.age > this.lifetime / 2)
			this.alpha -= this.startAlpha / this.lifetime * 2;

		if (this.age++ >= this.lifetime || this.level.isEmptyBlock(net.minecraft.core.BlockPos.containing(Mth.floor(this.x), Mth.floor(this.y - 0.02f), Mth.floor(this.z))))
			this.remove();

		// Preserve the roll value (prevent SingleQuadParticle from changing it)
		this.oRoll = this.roll;
	}

	// Note: SingleQuadParticle now handles rendering internally with the roll field for rotation

	public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public DefaultFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, net.minecraft.util.RandomSource randomSource) {
			return new FootprintParticle(level, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider, (FootprintParticleType) parameters, "footprint");
		}
	}

}
