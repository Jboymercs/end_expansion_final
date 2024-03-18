package com.example.structure.world.Biome.generation;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenAshSpikes extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        this.generateASpike(worldIn, rand, position);
        return true;
    }

    public void generateASpike(World world, Random random, BlockPos pos) {
        int maxHeight = ModRand.range(7, 12);
        int maxHeightSide = ModRand.range(5, 8);
        int maxHeightSide2 = ModRand.range(5, 8);
        int maxHeightSide3 = ModRand.range(5, 8);
        int maxHeightSide4 = ModRand.range(5, 8);
        BlockPos pos1 = pos.add(1, 0, 0);
        BlockPos pos2 = pos.add(-1, 0, 0);
        BlockPos pos3 = pos.add(0, 0, 1);
        BlockPos pos4 = pos.add(0, 0, -1);
        if(world.getBlockState(pos).isFullBlock()) {
           for(int y = 0; y <= maxHeight; y++) {
               BlockPos buildPos = new BlockPos(pos.getX(), pos.getY() + y, pos.getZ());
               world.setBlockState(buildPos, ModBlocks.END_ASH.getDefaultState());
           }
           for(int y= 0; y<= maxHeightSide; y++) {
               BlockPos sidePos1 = new BlockPos(pos1.getX(), pos1.getY() + y, pos1.getZ());
               world.setBlockState(sidePos1, ModBlocks.END_ASH.getDefaultState());
           }
            for(int y= 0; y<= maxHeightSide2; y++) {
                BlockPos sidePos1 = new BlockPos(pos2.getX(), pos2.getY() + y, pos2.getZ());
                world.setBlockState(sidePos1, ModBlocks.END_ASH.getDefaultState());
            }
            for(int y= 0; y<= maxHeightSide3; y++) {
                BlockPos sidePos1 = new BlockPos(pos3.getX(), pos3.getY() + y, pos3.getZ());
                world.setBlockState(sidePos1, ModBlocks.END_ASH.getDefaultState());
            }
            for(int y= 0; y<= maxHeightSide4; y++) {
                BlockPos sidePos1 = new BlockPos(pos4.getX(), pos4.getY() + y, pos4.getZ());
                world.setBlockState(sidePos1, ModBlocks.END_ASH.getDefaultState());
            }

        }
    }
}
