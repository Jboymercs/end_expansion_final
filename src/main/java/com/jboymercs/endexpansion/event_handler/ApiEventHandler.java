package com.jboymercs.endexpansion.event_handler;


import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.config.ModConfig;
import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.util.handlers.BiomeRegister;
import git.jbredwards.nether_api.api.event.NetherAPIRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class ApiEventHandler {


    //Nether-API
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void onNetherAPIRegistry(@Nonnull final NetherAPIRegistryEvent.End event)
    {
        event.registry.registerBiome(BiomeRegister.END_ASH_WASTELANDS, ModConfig.biome_weight);

        event.registry.registerBiome(BiomeRegister.BARREND_LOWLANDS, 80);

        //King Fortress
        event.registry.registerStructure("EndKingsFortress", chunkGenerator -> Main.instance.fortress);


    }
}
