package com.example.structure.world.Biome.decorator;

import com.example.structure.world.Biome.WorldChunkGeneratorEE;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class ChunkGeneratorAshWasteland extends WorldChunkGeneratorEE {

    public ChunkGeneratorAshWasteland(World world, boolean featuresEnabled, long seed, BlockPos spawnPos) {
        super(world, featuresEnabled, seed, spawnPos);
    }

    @Override
    protected void generateFeatures(BlockPos pos, Biome biome) {

    }
}
