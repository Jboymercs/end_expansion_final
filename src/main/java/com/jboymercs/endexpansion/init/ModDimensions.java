package com.jboymercs.endexpansion.init;

import com.jboymercs.endexpansion.config.ModConfig;
import com.jboymercs.endexpansion.world.Biome.WorldProviderEndEE;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class ModDimensions {


    public static final DimensionType END = DimensionType.register("End", "_end", 1, WorldProviderEndEE.class, false);


    public static void registerDimensionChanges() {
        if(ModConfig.isSkyBoxEnalbed) {
            DimensionManager.unregisterDimension(1);
            DimensionManager.registerDimension(1, END);
        }
    }
}
