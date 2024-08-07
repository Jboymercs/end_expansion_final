package com.example.structure.init;

import com.example.structure.config.ModConfig;
import com.example.structure.world.Biome.WorldProviderEndEE;
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
