package com.rimo.footprintparticle;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Util {	private static Method pehkuiGetScaleMethod = null;
	private static Object pehkuiBaseScaleType = null;
	private static boolean pehkuiChecked = false;
	
	private static void initPehkui() {
		if (pehkuiChecked) return;
		pehkuiChecked = true;
		
		try {
			if (ModList.get().isLoaded("pehkui")) {
				Class<?> scaleDataClass = Class.forName("virtuoel.pehkui.api.ScaleData");
				Class<?> scaleTypesClass = Class.forName("virtuoel.pehkui.api.ScaleTypes");
				pehkuiBaseScaleType = scaleTypesClass.getField("BASE").get(null);
				
				// Get the getScale method: ScaleData.getScale(Entity entity, ScaleType scaleType)
				pehkuiGetScaleMethod = scaleDataClass.getMethod("getScale", net.minecraft.world.entity.Entity.class, pehkuiBaseScaleType.getClass());
			}
		} catch (Exception e) {
			// Pehkui not available or incompatible version
			pehkuiGetScaleMethod = null;
			pehkuiBaseScaleType = null;
		}
	}
	
	private static float getPehkuiScale(LivingEntity entity) {
		try {
			initPehkui();
			if (pehkuiGetScaleMethod != null && pehkuiBaseScaleType != null) {
				Object scaleData = pehkuiGetScaleMethod.invoke(null, entity, pehkuiBaseScaleType);
				if (scaleData != null) {
					Method getScaleMethod = scaleData.getClass().getMethod("getScale");
					return ((Number) getScaleMethod.invoke(scaleData)).floatValue();
				}
			}
		} catch (Exception e) {
			// Ignore errors and fall back to default scaling
		}
		return 1.0f;
	}
	
	private static float getRandomMobSizesScale(LivingEntity entity) {
		try {
			if (ModList.get().isLoaded("random_mob_sizes")) {
				// Random Mob Sizes uses the vanilla getScale() method
				return entity.getScale();
			}
		} catch (Exception e) {
			// Ignore errors and fall back to default scaling
		}
		return 1.0f;
	}	public static float getEntityScale(LivingEntity entity) {
		float scale = 1f;

		// Apply Random Mob Sizes scaling if available (check this first as it uses vanilla methods)
		if (ModList.get().isLoaded("random_mob_sizes")) {
			scale *= getRandomMobSizesScale(entity);
		}
		// Apply Pehkui scaling if available and Random Mob Sizes is not loaded
		else if (ModList.get().isLoaded("pehkui")) {
			scale *= getPehkuiScale(entity);
		}
		// Apply baby scaling if no scaling mods are loaded
		else if (entity.isBaby()) {
			scale *= 0.66f;
		}

		// Apply custom scaling from config
		for (String str : FPPClient.CONFIG.getSizePerMob()) {
			String[] str2 = str.split(",");
			try {
				if (str2[0].contentEquals(EntityType.getKey(entity.getType()).toString())) {
					scale *= Float.parseFloat(str2[1]);
				}
			} catch (Exception e) {
				// Ignore...
			}
		}

		return scale;
	}	public static List<TextureAtlasSprite> getCustomSprites(LivingEntity entity, SpriteSet spriteProvider, String def) {
		// TODO: Implement custom texture support based on entity type
		for (String str : FPPClient.CONFIG.getCustomPrint()) {
			String[] str2 = str.split(",");
			try {
				if (str2[0].contentEquals(EntityType.getKey(entity.getType()).toString())) {
					// Custom sprite found - would use str2[1] for texture name
					break;
				}
			} catch (Exception e) {
				// Use default sprite on error
			}
		}
		// For now, return default sprite
		return Arrays.asList(spriteProvider.get(0, 1));
	}

	public static boolean isPlayer(LivingEntity entity) {
		return entity.getType() == EntityTypes.PLAYER;
	}
}
