package com.example.structure.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockUpdater {
    public void update(World world, BlockPos pos);
}
