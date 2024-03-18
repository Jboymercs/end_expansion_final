package com.example.structure.world;

import com.example.structure.util.ModUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndDungeon extends WorldGenStructure{
    /**
     * Small Base class for the Dungeon
     */

    int yOffset;
    public WorldGenEndDungeon(String structureName, int yOffset) {
        super(structureName);
        this.yOffset = yOffset;
    }


    @Override
    public boolean generate(World worldIn, Random random, BlockPos blockPos) {
        BlockPos offset = this.getCenter(worldIn).subtract(this.getCenter(worldIn));
        return super.generate(worldIn, random, offset.add(new BlockPos(0,yOffset,0)));
    }

    @Override
    public int getYGenHeight(World world, int x, int z) {
        return ModUtils.calculateGenerationHeight(world, x, z);
    }

}
