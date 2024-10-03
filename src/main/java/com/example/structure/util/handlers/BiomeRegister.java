package com.example.structure.util.handlers;

import com.example.structure.world.Biome.BiomeAshWasteland;
import com.example.structure.world.Biome.BiomeBarrendLands;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.common.BiomeDictionary.Type;



public class BiomeRegister {

    public static final Biome END_ASH_WASTELANDS = new BiomeAshWasteland();
    public static final Biome BARREND_LOWLANDS = new BiomeBarrendLands();



    public static void registerBiomes() {
        initBiome(END_ASH_WASTELANDS, "ash_wastelands", Type.END);
        initBiome(BARREND_LOWLANDS, "barrend_low_lands", Type.END);
    }



    private static void initBiome(Biome biome, String name, Type... types)
    {
        biome.setRegistryName(name);
        ForgeRegistries.BIOMES.register(biome);
        BiomeDictionary.addTypes(biome, types);
    }
    //Registration with Nether-API


}
