package com.jboymercs.endexpansion.init;

import com.jboymercs.endexpansion.config.ModConfig;
import com.jboymercs.endexpansion.util.integration.ModIntegration;
import com.jboymercs.endexpansion.world.Biome.WorldProviderEndEE;
import net.minecraft.world.DimensionType;

public class ModDimensions {

    public static void registerDimensionChanges() {
        if(ModConfig.isSkyBoxEnalbed && !ModIntegration.IS_BETTER_END_LOADED) {
            DimensionType.THE_END.clazz = WorldProviderEndEE.class;
        }
    }
}
