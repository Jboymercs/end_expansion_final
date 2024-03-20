package com.example.structure.util.handlers;

import com.example.structure.world.Biome.BiomeAshWasteland;
import com.example.structure.world.Biome.barrend.BiomeBarrendForest;
import com.example.structure.world.underground.BiomeBarrendCaves;
import com.example.structure.world.underground.BiomeUnderground;
import git.jbredwards.nether_api.api.event.NetherAPIRegistryEvent;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.common.BiomeDictionary.Type;

import javax.annotation.Nonnull;

public class BiomeRegister {

    public static final Biome END_ASH_WASTELANDS = new BiomeAshWasteland();
    public static final Biome END_BARREND_FOREST = new BiomeBarrendForest();


    public static void registerBiomes() {
        initBiome(END_ASH_WASTELANDS, "ash_wastelands", Type.END);
        initBiome(END_BARREND_FOREST, "barrend_forest", Type.END);
    }



    private static void initBiome(Biome biome, String name, Type... types)
    {
        biome.setRegistryName(name);
        ForgeRegistries.BIOMES.register(biome);
        BiomeDictionary.addTypes(biome, types);
    }
    //Registration with Nether-API


}
