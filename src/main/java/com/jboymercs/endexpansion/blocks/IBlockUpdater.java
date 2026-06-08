package com.jboymercs.endexpansion.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockUpdater {
    public void update(World world, BlockPos pos);
}
