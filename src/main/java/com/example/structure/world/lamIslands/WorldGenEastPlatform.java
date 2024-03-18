package com.example.structure.world.lamIslands;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEastPlatform extends WorldGenLamentedIslands{

    public WorldGenEastPlatform() {
        super("islands/e_platform", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("tile")) {
            new WorldGenTile().generateStructure(world, pos.add(new BlockPos(0, 1, 0)), Rotation.NONE);
            world.setBlockToAir(pos);
        }
    }
}
