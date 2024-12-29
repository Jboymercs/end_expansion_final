package com.example.structure.world.Biome.ore;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenStarShard extends WorldGenEndOre{
    private final IBlockState state;

    public WorldGenStarShard(int num, IBlockState state) {
        super(num);
        this.state = state;
    }

    @Override
    protected IBlockState getState(World world, Random rand, BlockPos pos) {
        return state;
    }
}
