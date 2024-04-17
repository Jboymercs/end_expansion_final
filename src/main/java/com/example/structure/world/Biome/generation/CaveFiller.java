package com.example.structure.world.Biome.generation;

import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class CaveFiller extends WorldGenStructure {
    public CaveFiller(String structureName) {
        super("ashbiome/filler/" + structureName);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if(!worldIn.isAirBlock(position.down())) {
            return super.generate(worldIn, rand, position.add(0, -1, 0));
        } else {
            return false;
        }
    }
}
