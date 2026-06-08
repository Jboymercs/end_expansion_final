package com.jboymercs.endexpansion.world.Biome.decorator;

import com.jboymercs.endexpansion.world.Biome.WorldChunkGeneratorEE;
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
