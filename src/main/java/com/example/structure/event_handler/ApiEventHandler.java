package com.example.structure.event_handler;


import com.example.structure.config.ModConfig;
import com.example.structure.sky.EndSkyHandler;
import com.example.structure.util.ModReference;
import com.example.structure.util.handlers.BiomeRegister;
import git.jbredwards.nether_api.api.event.NetherAPIRegistryEvent;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class ApiEventHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    static void renderSkybox(@Nonnull final WorldEvent.Load event) {
        if(ModConfig.isSkyBoxEnalbed && event.getWorld().provider.getDimensionType() == DimensionType.THE_END) {
            event.getWorld().provider.setSkyRenderer(new EndSkyHandler());
        }
    }

    @SubscribeEvent
    static void onNetherAPIRegistry(@Nonnull final NetherAPIRegistryEvent.End event)
    {
        event.registry.registerBiome(BiomeRegister.END_ASH_WASTELANDS, ModConfig.biome_weight);

        event.registry.registerBiome(BiomeRegister.BARREND_LOWLANDS, 80);

        //King Fortress
       // event.registry.registerStructure("EndKingsFortress", chunkGenerator ->
             //   new MapGenKingFortress(chunkGenerator, WorldConfig.fortress_spacing, 0, WorldConfig.fortress_odds));

    }
}
