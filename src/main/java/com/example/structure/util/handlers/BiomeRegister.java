package com.example.structure.util.handlers;

import com.example.structure.world.Biome.BiomeAshWasteland;
import git.jbredwards.nether_api.api.event.NetherAPIRegistryEvent;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.common.BiomeDictionary.Type;

import javax.annotation.Nonnull;

public class BiomeRegister {

    public static final Biome END_ASH_WASTELANDS = new BiomeAshWasteland();

    public static void registerBiomes() {
        initBiome(END_ASH_WASTELANDS, "ash_wastelands", Type.END);
    }



    private static void initBiome(Biome biome, String name, Type... types)
    {
        biome.setRegistryName(name);
        ForgeRegistries.BIOMES.register(biome);
        BiomeDictionary.addTypes(biome, types);
    }
    //Registration with Nether-API


}
