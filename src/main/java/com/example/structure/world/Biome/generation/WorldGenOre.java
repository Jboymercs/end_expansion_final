package com.example.structure.world.Biome.generation;

import com.example.structure.blocks.BlockCoriumOre;
import com.example.structure.init.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

public class WorldGenOre extends WorldGenerator {
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if(!worldIn.isAirBlock(position)) {
            generateOreNearby(worldIn, rand, position);
            return true;
        }
        return false;
    }

    public void generateOreNearby(World world, Random random, BlockPos pos) {
        world.setBlockState(pos, ModBlocks.AMBER_ORE.getDefaultState());

        if(world.getBlockState(pos.add(1, 0, 0)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(4) == 0) {
            world.setBlockState(pos.add(1, 0, 0), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(-1, 0, 0)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(2) == 0) {
            world.setBlockState(pos.add(-1, 0, 0), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(0, 0, -1)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(4) == 0) {
            world.setBlockState(pos.add(0, 0, -1), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(0, 0, 1)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(2) == 0) {
            world.setBlockState(pos.add(0, 0, 1), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(0, 1, 0)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(4) == 0) {
            world.setBlockState(pos.add(0, 1, 0), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(0, -1, 0)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(2) == 0) {
            world.setBlockState(pos.add(0, -1, 0), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(1, -1, 0)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(4) == 0) {
            world.setBlockState(pos.add(1, -1, 0), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(-1, -1, 0)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(2) == 0) {
            world.setBlockState(pos.add(-1, -1, 0), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(1, -1, -1)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(4) == 0) {
            world.setBlockState(pos.add(1, -1, -1), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(0, 1, -1)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(2) == 0) {
            world.setBlockState(pos.add(0, 1, -1), ModBlocks.AMBER_ORE.getDefaultState());
        }
        if(world.getBlockState(pos.add(1, 1, 0)).getBlock() == ModBlocks.BROWN_END_STONE && random.nextInt(4) == 0) {
            world.setBlockState(pos.add(1, 1, 0), ModBlocks.AMBER_ORE.getDefaultState());
        }

    }
}
