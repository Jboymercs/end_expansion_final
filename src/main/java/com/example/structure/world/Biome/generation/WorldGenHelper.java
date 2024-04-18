package com.example.structure.world.Biome.generation;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public abstract class WorldGenHelper extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        return false;
    }

    private final boolean doBlockNotify;

    public WorldGenHelper() {
        super(false);
        this.doBlockNotify = false;
    }

    public WorldGenHelper(boolean notify) {
        super(notify);
        this.doBlockNotify = notify;
    }

    @Override
    protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
        if (this.doBlockNotify) {
            worldIn.setBlockState(pos, state, 3 | 16);
        } else {
            worldIn.setBlockState(pos, state, 2 | 16);
        }
    }
}
