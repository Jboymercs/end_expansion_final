package com.example.structure.world.Biome.generation;

import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenGeyser extends WorldGenStructure {


    public WorldGenGeyser() {
        super("ashbiome/geyser");
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if(worldIn.getBlockState(position.down()).isFullBlock() && worldIn.getBlockState(position).isFullBlock()) {
            super.generate(worldIn, rand, position);
            return true;
        }
  return false;
    }
}
