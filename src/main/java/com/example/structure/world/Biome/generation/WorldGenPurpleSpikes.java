package com.example.structure.world.Biome.generation;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenPurpleSpikes extends WorldGenStructure {
    public WorldGenPurpleSpikes() {
        super("");
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if(worldIn.getBlockState(position.down()).isFullBlock()) {
            this.generateCrystalSpikes(worldIn, rand, position);
        }
        return true;
    }


    protected void generateCrystalSpikes(World world, Random random, BlockPos pos) {

        int randomSideGen = ModRand.range(1, 4);
        int maxHeight = ModRand.range(2, 6);
        int maxHeightSide = ModRand.range(1, 5);
        int maxHeightSide2 = ModRand.range(1, 5);
        int maxHeightSide3 = ModRand.range(1, 5);
        int maxHeightSide4 = ModRand.range(1, 5);

        BlockPos pos1 = pos.add(1, 0, 0);
        BlockPos pos2 = pos.add(-1, 0, 0);
        BlockPos pos3 = pos.add(0, 0, 1);
        BlockPos pos4 = pos.add(0, 0, -1);

        for (int y = 0; y <= maxHeight; y++) {
            BlockPos buildPos = new BlockPos(pos.getX(), pos.getY() + y, pos.getZ());
            if(y != maxHeight) {
                world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL.getDefaultState());
            } else {
                if (random.nextInt(2) == 0) {
                    world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL_TOP.getDefaultState());
                }
            }
        }
        //Set Side 1
        if (randomSideGen <= 2) {
            for (int y = 0; y <= maxHeightSide; y++) {
                BlockPos buildPos = new BlockPos(pos1.getX(), pos1.getY() + y, pos1.getZ());
                if(y != maxHeightSide) {
                    world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL.getDefaultState());
                } else {
                    if (random.nextInt(2) == 0) {
                        world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL_TOP.getDefaultState());
                    }
                }
            }
        }
        //Set Side 2
        if (randomSideGen >= 3) {
            for (int y = 0; y <= maxHeightSide2; y++) {
                BlockPos buildPos = new BlockPos(pos2.getX(), pos2.getY() + y, pos2.getZ());
                if( y != maxHeightSide2) {
                    world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL.getDefaultState());
                } else {
                    if (random.nextInt(2) == 0) {
                        world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL_TOP.getDefaultState());
                    }
                }
            }
        }
        //Set Side 3
        if (randomSideGen <= 2) {
            for (int y = 0; y <= maxHeightSide3; y++) {
                BlockPos buildPos = new BlockPos(pos3.getX(), pos3.getY() + y, pos3.getZ());
                if(y != maxHeightSide3) {
                    world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL.getDefaultState());
                }else {
                    if (random.nextInt(2) == 0) {
                        world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL_TOP.getDefaultState());
                    }
                }
            }
        }
        //Set Side 4
        if (randomSideGen >= 3) {
            for (int y = 0; y <= maxHeightSide4; y++) {
                BlockPos buildPos = new BlockPos(pos4.getX(), pos4.getY() + y, pos4.getZ());
                if(y != maxHeightSide4) {
                    world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL.getDefaultState());
                } else {
                    if (random.nextInt(2) == 0) {
                        world.setBlockState(buildPos, ModBlocks.PURPLE_CRYSTAL_TOP.getDefaultState());
                    }
                }
            }

        }
    }
}
