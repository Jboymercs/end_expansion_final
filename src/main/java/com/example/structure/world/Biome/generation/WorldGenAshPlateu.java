package com.example.structure.world.Biome.generation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenAshPlateu extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        return false;
    }


    public List<BlockPos> genFirstLayer(World world, BlockPos pos, int radius) {
        List<BlockPos> firstLayer = new ArrayList<BlockPos>();
        double maxDistance = radius*radius;


        return firstLayer;
    }

}
