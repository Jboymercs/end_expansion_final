package com.example.structure.event_handler;


import com.example.structure.Main;
import com.example.structure.util.ModReference;
import com.example.structure.util.handlers.BiomeRegister;
import com.example.structure.world.api.structures.MapGenKingFortress;
import git.jbredwards.nether_api.api.event.NetherAPIRegistryEvent;
import net.minecraft.world.gen.structure.MapGenStructure;
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
        event.registry.registerBiome(BiomeRegister.END_ASH_WASTELANDS, 80);
        //King Fortress
        event.registry.registerStructure("EndKingsFortress", chunkGenerator -> Main.instance.fortress);


    }
}
