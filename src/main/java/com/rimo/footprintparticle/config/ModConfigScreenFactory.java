package com.rimo.footprintparticle.config;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/**
 * Configuration screen factory for NeoForge integration.
 * This provides the config screen when accessing mod options from the mod list.
 */
public class ModConfigScreenFactory implements IConfigScreenFactory {
    
    @Override
    public Screen createScreen(ModContainer modContainer, Screen parent) {
        return ClothConfigIntegration.createConfigScreen(parent);
    }
}
