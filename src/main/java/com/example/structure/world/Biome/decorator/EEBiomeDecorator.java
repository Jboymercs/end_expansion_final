package com.example.structure.world.Biome.decorator;

import com.example.structure.world.Biome.generation.WorldGenAshSpikes;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EEBiomeDecorator extends BiomeEndDecorator {



    public EEBiomeDecorator()
    {

    }



    @Override
    protected void genDecorations(Biome biomeIn, World worldIn, Random random)
    {
        super.genDecorations(biomeIn, worldIn, random);
    }



}
