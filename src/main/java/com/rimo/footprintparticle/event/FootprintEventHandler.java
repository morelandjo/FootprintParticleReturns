package com.rimo.footprintparticle.event;

import com.rimo.footprintparticle.FPPClient;
import com.rimo.footprintparticle.FootprintParticleReturns;
import com.rimo.footprintparticle.Util;
import com.rimo.footprintparticle.particle.FootprintParticleType;
import com.rimo.footprintparticle.particle.SnowDustParticleType;
import com.rimo.footprintparticle.particle.WatermarkParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = FootprintParticleReturns.MOD_ID)
public class FootprintEventHandler {
    
    // Entity data storage for timers and states
    private static final Map<Integer, Integer> entityTimers = new HashMap<>();
    private static final Map<Integer, Boolean> entityWasOnGround = new HashMap<>();
    private static final Map<Integer, Integer> entityWetTimers = new HashMap<>();
    private static final Map<Integer, Integer> entityMinecartTimers = new HashMap<>();    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            generateFootprint(entity);
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();

        if (!entity.level().isClientSide()) {
            return; // Only process on client side
        }

        if (entity instanceof LivingEntity livingEntity) {
            handleLivingEntityTick(livingEntity);
        } else if (entity instanceof Boat boat) {
            handleBoatTick(boat);
        } else if (entity instanceof AbstractMinecart minecart) {
            handleMinecartTick(minecart);
        }
    }    private static void handleLivingEntityTick(LivingEntity entity) {
        int entityId = entity.getId();
        
        // Get or initialize timers
        int timer = entityTimers.getOrDefault(entityId, 0);
        boolean wasOnGround = entityWasOnGround.getOrDefault(entityId, true);
        int wetTimer = entityWetTimers.getOrDefault(entityId, FPPClient.CONFIG.getWetDuration() * 20);

        if (timer <= 0) {
            if (!entity.isCrouching() && !entity.isUnderWater()) {
                // Either on ground moving or landing
                if ((entity.getDeltaMovement().horizontalDistance() != 0 && entity.onGround()) || 
                    (!wasOnGround && entity.onGround())) {
                    generateFootprint(entity);
                }
                entityWasOnGround.put(entityId, entity.onGround());
            }
        } else {
            entityTimers.put(entityId, timer - 1);
        }        // Handle wet timer - when in water, reset to 0 (wet), when not in water, increment until dry
        if (entity.isInWaterOrRain()) {
            entityWetTimers.put(entityId, 0);
        } else if (wetTimer < FPPClient.CONFIG.getWetDuration() * 20) {
            entityWetTimers.put(entityId, wetTimer + 1);
        }

        // Swim Pop
        if (entity.isSwimming() &&
            (FPPClient.CONFIG.getSwimPopLevel() == 2 ||
             (FPPClient.CONFIG.getSwimPopLevel() == 1 && entity.getType() == EntityType.PLAYER))) {
            float range = Util.getEntityScale(entity);
            entity.level().addParticle(
                ParticleTypes.BUBBLE,
                entity.getX() + Math.random() - 0.5f * range,
                entity.getY() + Math.random() - 0.5f * range,
                entity.getZ() + Math.random() - 0.5f * range,
                0,
                Math.random() / 10f,
                0
            );
        }
    }

    private static void handleBoatTick(Boat boat) {
        if (!FPPClient.CONFIG.isEnableBoatTrail()) {
            return;
        }

        int k = (int)(boat.getDeltaMovement().horizontalDistance() * 10);
        while (Math.random() < k-- / 5f) {
            var i = Math.random() > 0.5f ? 1 : -1;
            if (boat.isInWater()) {
                boat.level().addParticle(
                    ParticleTypes.SPLASH,
                    boat.getX() + 1.2f * Mth.cos((float) Math.toRadians(boat.getYRot() + 90 + Math.random() * 30 * i)),
                    (int) boat.getY() + 1f,
                    boat.getZ() + 1.2f * Mth.sin((float) Math.toRadians(boat.getYRot() + 90 + Math.random() * 30 * i)),
                    0, 0, 0
                );
                
                for (int j = 0; j < 2; j++) {
                    boat.level().addParticle(
                        FootprintParticleReturns.WATERSPLASH.get(),
                        boat.getX() + i * Mth.cos((float) Math.toRadians(boat.getYRot() - 10 + Math.random() * 20)),
                        (int) boat.getY() + 1f,
                        boat.getZ() + i * Mth.sin((float) Math.toRadians(boat.getYRot() - 10 + Math.random() * 20)),
                        (Math.random() - 0.5f) / 4f,
                        Math.random() * boat.getDeltaMovement().horizontalDistance() / 2f,
                        (Math.random() - 0.5f) / 4f
                    );
                }
                
                boat.level().addParticle(
                    ParticleTypes.BUBBLE,
                    boat.getX() - 1.2f * Mth.cos((float) Math.toRadians(boat.getYRot() + 90 + Math.random() * 30 * i)),
                    (int) boat.getY() + 0.5f + Math.random() / 2f,
                    boat.getZ() - 1.2f * Mth.sin((float) Math.toRadians(boat.getYRot() + 90 + Math.random() * 30 * i)),
                    boat.getDeltaMovement().x / 5f,
                    Math.random() / 5f,
                    boat.getDeltaMovement().z / 5f
                );
            } else {
                if (Math.random() > 0.5f) {
                    boat.level().addParticle(
                        ParticleTypes.CLOUD,
                        boat.getX() + i * Mth.cos((float) Math.toRadians(boat.getYRot() - 10 + Math.random() * 20)),
                        boat.getY(),
                        boat.getZ() + i * Mth.sin((float) Math.toRadians(boat.getYRot() - 10 + Math.random() * 20)),
                        Math.random() / 5f,
                        Math.random() / 5f,
                        Math.random() / 5f
                    );
                } else {
                    boat.level().addParticle(
                        ParticleTypes.CLOUD,
                        boat.getX() - 1.2f * Mth.cos((float) Math.toRadians(boat.getYRot() + 90 + Math.random() * 30 * i)),
                        boat.getY(),
                        boat.getZ() - 1.2f * Mth.sin((float) Math.toRadians(boat.getYRot() + 90 + Math.random() * 30 * i)),
                        boat.getDeltaMovement().x / 5f,
                        Math.random() / 5f,
                        boat.getDeltaMovement().z / 5f
                    );
                }
            }
        }
    }

    private static void handleMinecartTick(AbstractMinecart minecart) {
        int entityId = minecart.getId();
        int timer = entityMinecartTimers.getOrDefault(entityId, 0);
        
        if (timer-- <= 0 && minecart.getDeltaMovement().horizontalDistance() != 0) {
            if (minecart.level().getBlockState(minecart.blockPosition()).is(net.minecraft.tags.BlockTags.RAILS) && 
                Math.random() <= FPPClient.CONFIG.getRailFlameRange()) {
                var i = Math.random() > 0.5f ? 1 : -1;
                minecart.level().addParticle(
                    ParticleTypes.ELECTRIC_SPARK,
                    minecart.getX() + i * 0.4f * Mth.cos((float) Math.toRadians(minecart.getYRot() + 90)),
                    minecart.getY() + 0.0625f,
                    minecart.getZ() + i * 0.4f * Mth.sin((float) Math.toRadians(minecart.getYRot() + 90)),
                    minecart.getDeltaMovement().x / 3f * Math.random(),
                    Math.random() / 3,
                    minecart.getDeltaMovement().z / 3f * Math.random()
                );
            }
            timer = minecart.level().getBlockState(minecart.blockPosition()).is(Blocks.POWERED_RAIL) ?
                (int) (FPPClient.CONFIG.getSecPerPrint() * 3.33f) :
                (int) (FPPClient.CONFIG.getSecPerPrint() * 6.66f);
        }
        
        entityMinecartTimers.put(entityId, timer);
    }    private static void generateFootprint(LivingEntity entity) {
        if (FPPClient.CONFIG.isEnable() == 0 ||
            (FPPClient.CONFIG.isEnable() == 1 && entity.getType() != EntityType.PLAYER)) {
            return;
        }
        
        if (FPPClient.CONFIG.getExcludedMobs().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())) {
            return;
        }
        
        if (!FPPClient.CONFIG.getCanGenWhenInvisible() && entity.isInvisible()) {
            return;
        }

        // Set interval timer
        int timer = entity.isSprinting() ? 
            (int) (FPPClient.CONFIG.getSecPerPrint() * 13.33f) : 
            (int) (FPPClient.CONFIG.getSecPerPrint() * 20);
            
        // Apply entity-specific intervals
        for (String stream : FPPClient.CONFIG.getMobInterval()) {
            String[] str = stream.split(",");
            if (str[0].equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())) {
                try {
                    timer *= Float.parseFloat(str[1]);
                } catch (Exception e) {
                    // Ignore invalid values
                }
                break;
            }
        }
        
        entityTimers.put(entity.getId(), timer);        // Calculate position
        double[] position = {entity.getX(), entity.getY() + 0.01f + FPPClient.CONFIG.getPrintHeight(), entity.getZ()};

        // Apply horizontal offsets based on entity type
        applyHorizontalOffsets(entity, position);
        
        double px = position[0];
        double py = position[1];
        double pz = position[2];        // Check if footprint can be generated at this position
        BlockPos pos = new BlockPos((int)px, (int)py, (int)pz);
        var stateAtPos = entity.level().getBlockState(pos);
        boolean canGen;

        if (stateAtPos.is(Blocks.SNOW) && isPrintCanGen(entity, pos)) {
            // Snow layers sit on top of the block below and are not a full collision block,
            // so they would otherwise fall through to the block underneath and the footprint
            // would be drawn sunk into the block below the snow. Place it on the snow surface.
            double snowTop = stateAtPos.getShape(entity.level(), pos).max(Direction.Axis.Y);
            py = pos.getY() + snowTop + 0.01f + FPPClient.CONFIG.getPrintHeight();
            py += getBlockHeightOffset(entity, pos);
            canGen = true;
        } else {
            canGen = isPrintCanGen(entity, pos) && stateAtPos.isCollisionShapeFullBlock(entity.level(), pos);
            if (!canGen) {
                pos = new BlockPos((int)px, (int)py - 1, (int)pz);
                canGen = isPrintCanGen(entity, pos) &&
                         entity.level().getBlockState(pos).isCollisionShapeFullBlock(entity.level(), pos) &&
                         entity.level().getBlockState(pos).getCollisionShape(entity.level(), pos).equals(Shapes.block());
            } else {
                // Apply block-specific height adjustments
                py += getBlockHeightOffset(entity, pos);
            }
        }

        // Generate particles based on block type and conditions
        generateParticlesForBlock(entity, pos, px, py, pz, canGen);
    }    private static void applyHorizontalOffsets(LivingEntity entity, double[] position) {
        double px = position[0];
        double py = position[1]; 
        double pz = position[2];
        
        // Front and back offset
        var side = Math.random() > 0.5f ? 1 : -1;
        var hOffset = 0.0625f;
        
        for (String stream : FPPClient.CONFIG.getHorseLikeMobs()) {
            String[] str = stream.split(",");
            if (str[0].equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())) {
                hOffset = 0.75f;
                try {
                    hOffset = Float.parseFloat(str[1]);
                } catch (Exception e) {
                    // Ignore invalid values
                }
                // Apply rider-based timer modification
                int currentTimer = entityTimers.getOrDefault(entity.getId(), 0);
                if (entity.getFirstPassenger() != null) {
                    currentTimer = (int) (entity.getFirstPassenger().getType() == EntityType.PLAYER ? 
                        currentTimer * 0.5f : currentTimer * 1.33f);
                } else {
                    currentTimer = (int) (currentTimer * 1.33f);
                }
                entityTimers.put(entity.getId(), currentTimer);
                break;
            }
        }
        
        px = px - hOffset * side * Mth.sin((float) Math.toRadians(entity.getYRot()));
        pz = pz + hOffset * side * Mth.cos((float) Math.toRadians(entity.getYRot()));
        
        // Left and right offset
        side = Math.random() > 0.5f ? 1 : -1;
        hOffset = 0.125f;
        
        for (String stream : FPPClient.CONFIG.getSpiderLikeMobs()) {
            String[] str = stream.split(",");
            if (str[0].equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())) {
                hOffset = 0.9f;
                try {
                    hOffset = Float.parseFloat(str[1]);
                } catch (Exception e) {
                    // Ignore invalid values
                }
                break;
            }
        }
        
        px = px - hOffset * side * Mth.sin((float) Math.toRadians(entity.getYRot() + 90));
        pz = pz + hOffset * side * Mth.cos((float) Math.toRadians(entity.getYRot() + 90));
        
        // Update the position array
        position[0] = px;
        position[1] = py;
        position[2] = pz;
    }

    private static float getBlockHeightOffset(LivingEntity entity, BlockPos pos) {
        float py = 0f;
        try {
            var block = entity.level().getBlockState(pos);
            for (String str : FPPClient.CONFIG.getBlockHeight()) {
                String[] str2 = str.split(",");
                if (str2[0].charAt(0) == '#') {
                    for (TagKey<Block> tag : block.getTags().toList()) {
                        if (str2[0].equals("#" + tag.location().toString())) {
                            py += Float.parseFloat(str2[1]);
                            break;
                        }
                    }
                } else if (str2[0].contentEquals(BuiltInRegistries.BLOCK.getKey(block.getBlock()).toString())) {
                    py += Float.parseFloat(str2[1]);
                    break;
                }
            }
        } catch (Exception e) {
            // Ignore invalid values
        }
        return py;
    }

    private static void generateParticlesForBlock(LivingEntity entity, BlockPos pos, double px, double py, double pz, boolean canGen) {
        var block = entity.level().getBlockState(pos);
        
        // Snow Dust
        if (block.is(Blocks.SNOW) &&
            (FPPClient.CONFIG.getSnowDustLevel() == 2 ||
             (FPPClient.CONFIG.getSnowDustLevel() == 1 && entity.getType() == EntityType.PLAYER))) {
            int i = entity.isSprinting() ? 4 : 2;
            int v = entity.isSprinting() ? 3 : 10;
            while (--i >= 0) {
                SnowDustParticleType snowdust = FootprintParticleReturns.SNOWDUST.get();
                entity.level().addParticle(snowdust.setData(Util.getEntityScale(entity)), px, py, pz,
                    (Math.random() - 0.5f) / v,
                    0,
                    (Math.random() - 0.5f) / v
                );
            }
        }

        // Calculate movement direction
        double dx, dz;
        if (entity.getDeltaMovement().horizontalDistance() == 0) {
            dx = -Mth.sin((float) Math.toRadians(entity.getYRot()));
            dz = Mth.cos((float) Math.toRadians(entity.getYRot()));
        } else {
            dx = entity.getDeltaMovement().x;
            dz = entity.getDeltaMovement().z;
        }        int wetTimer = entityWetTimers.getOrDefault(entity.getId(), FPPClient.CONFIG.getWetDuration() * 20);

        if (canGen) {
            // Generate footprint
            FootprintParticleType footprint = FootprintParticleReturns.FOOTPRINT.get();
            entity.level().addParticle(footprint.setData(entity), px, py, pz, dx, 0, dz);
        } else if (wetTimer < FPPClient.CONFIG.getWetDuration() * 20) {
            // Generate watermark when footprint can't be generated and entity is still wet
            WatermarkParticleType watermark = FootprintParticleReturns.WATERMARK.get();
            var i = Math.random() > 0.5f ? 1 : -1;
            entity.level().addParticle(watermark.setData(entity), px, py, pz, dx * i, wetTimer, dz * i);
        }

        // Water splash (generates when entity is wet)
        if (wetTimer < FPPClient.CONFIG.getWetDuration() * 20 &&
            (FPPClient.CONFIG.getWaterSplashLevel() == 2 ||
             (FPPClient.CONFIG.getWaterSplashLevel() == 1 && entity.getType() == EntityType.PLAYER))) {
            float range = Util.getEntityScale(entity);
            int splashCount = (int)((entity.isSprinting() ? 18 : 10) * Math.max((0.7f - (float) wetTimer / (FPPClient.CONFIG.getWetDuration() * 20)), 0));
            int v = entity.isSprinting() ? 3 : 6;
            while (--splashCount > 0) {
                entity.level().addParticle(
                    FootprintParticleReturns.WATERSPLASH.get(),
                    px - 0.25f * range + Math.random() / 4,
                    py,
                    pz - 0.25f * range + Math.random() / 4,
                    (Math.random() - 0.5f) / v,
                    0.02f + Math.random() * entity.getDeltaMovement().horizontalDistance(),
                    (Math.random() - 0.5f) / v
                );
            }
        }
    }

    private static boolean isPrintCanGen(LivingEntity entity, BlockPos pos) {
        var block = entity.level().getBlockState(pos);
        var canGen = FPPClient.CONFIG.getApplyBlocks().contains(BuiltInRegistries.BLOCK.getKey(block.getBlock()).toString());
        
        if (!canGen) {
            for (TagKey<Block> tag : block.getTags().toList()) {
                canGen = FPPClient.CONFIG.getApplyBlocks().contains("#" + tag.location().toString());
                if (canGen) break;
            }
            
            if (!canGen) {
                // Hardness Filter
                canGen = Math.abs(block.getDestroySpeed(entity.level(), pos)) < 0.7f;
                if (canGen) {
                    canGen = !FPPClient.CONFIG.getExcludedBlocks().contains(BuiltInRegistries.BLOCK.getKey(block.getBlock()).toString());
                    if (canGen) {
                        for (TagKey<Block> tag : block.getTags().toList()) {
                            canGen = !FPPClient.CONFIG.getExcludedBlocks().contains("#" + tag.location().toString());
                            if (!canGen) break;
                        }
                    }
                }
            }
        }
        return canGen;
    }

    // Clean up entity data when entities are removed
    public static void cleanupEntityData(int entityId) {
        entityTimers.remove(entityId);
        entityWasOnGround.remove(entityId);
        entityWetTimers.remove(entityId);
        entityMinecartTimers.remove(entityId);
    }
}
