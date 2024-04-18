package com.example.structure.world.Biome.generation;

import com.example.structure.blocks.BlockDepthsVines;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.example.structure.world.api.mines.WorldGenMines;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenVines extends WorldGenHelper {


    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        generateVineAt(worldIn, rand, position);
        return true;
    }
    private boolean interrupted = false;

    public void generateVineAt(World world, Random rand, BlockPos pos) {
        IBlockState vine = ModBlocks.SPROUT_VINE.getDefaultState().withProperty(BlockDepthsVines.CAN_GROW, false);
        if(world.getBlockState(pos.up()) == ModBlocks.BROWN_END_STONE.getDefaultState()) {
            world.setBlockState(pos, ModBlocks.SPROUT_VINE.getDefaultState().withProperty(BlockDepthsVines.CAN_GROW, false));
            int ySet = pos.getY() - ModRand.range(-5, -2);
            for(int y = pos.getY() - 1; y <= ySet; y-- ) {
                if(!world.isAirBlock(pos.add(0, y, 0))) {
                    this.interrupted = true;
                }
                if(!interrupted) {
                    int difference = pos.getY() - y;
                    world.setBlockState(pos.add(0, pos.getY() + difference, 0), vine);
                }

            }
        }

    }



}
