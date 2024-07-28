package com.example.structure.world.api.trader;

import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenTraderArena extends WorldGenStructure {
    public WorldGenTraderArena(String structureName) {
        super(structureName);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {

        super.generateStructure(world, pos.add(-14, -3, -14), Rotation.NONE);
    }
}
