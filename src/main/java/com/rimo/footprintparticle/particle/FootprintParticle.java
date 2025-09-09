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

public class FootprintParticle extends TextureSheetParticle {
	protected float startAlpha;

	protected FootprintParticle(ClientLevel clientLevel, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteProvider, FootprintParticleType parameters, String defName) {
		super(clientLevel, x, y, z, vx, vy, vz);

		this.xd = 0;
		this.yd = 0;
		this.zd = 0;
		this.setAlpha(FPPClient.CONFIG.getFootprintAlpha());
		this.oRoll = this.roll = (float) Mth.atan2(vx, vz);
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
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		this.y -= 0.01f / this.lifetime;
		this.yo = this.y;

		if (this.age > this.lifetime / 2)
			this.alpha -= this.startAlpha / this.lifetime * 2;

		if (this.age++ >= this.lifetime || this.level.isEmptyBlock(net.minecraft.core.BlockPos.containing(Mth.floor(this.x), Mth.floor(this.y - 0.02f), Mth.floor(this.z))))
			this.remove();
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
		// Use ground-oriented rendering instead of billboard
		Vector3f[] corners = new Vector3f[]{
			new Vector3f(-1.0F, 0.0F, -1.0F),
			new Vector3f(-1.0F, 0.0F, 1.0F),
			new Vector3f(1.0F, 0.0F, 1.0F),
			new Vector3f(1.0F, 0.0F, -1.0F)
		};

		float size = this.getQuadSize(partialTicks);
		
		// Create rotation around Y-axis for ground orientation
		Quaternionf rotation = new Quaternionf().rotationY(this.roll);

		// Transform corners and render
		for (int i = 0; i < 4; ++i) {
			Vector3f corner = corners[i];
			corner.mul(size);
			corner.rotate(rotation);
			corner.add((float)(this.x - camera.getPosition().x), (float)(this.y - camera.getPosition().y), (float)(this.z - camera.getPosition().z));
		}

		float u0 = this.getU0();
		float u1 = this.getU1();
		float v0 = this.getV0();
		float v1 = this.getV1();
		int light = this.getLightColor(partialTicks);

		// Add vertices (ground-oriented quad)
		vertexConsumer.addVertex(corners[0].x(), corners[0].y(), corners[0].z()).setUv(u1, v1).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
		vertexConsumer.addVertex(corners[1].x(), corners[1].y(), corners[1].z()).setUv(u1, v0).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
		vertexConsumer.addVertex(corners[2].x(), corners[2].y(), corners[2].z()).setUv(u0, v0).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
		vertexConsumer.addVertex(corners[3].x(), corners[3].y(), corners[3].z()).setUv(u0, v1).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(light);
	}

	public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteProvider;

		public DefaultFactory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new FootprintParticle(level, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider, (FootprintParticleType) parameters, "footprint");
		}
	}

}
