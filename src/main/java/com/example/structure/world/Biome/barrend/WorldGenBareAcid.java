package com.example.structure.world.Biome.barrend;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.List;
import java.util.Random;

public class WorldGenBareAcid extends WorldGenerator {

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        intereperetCircle(position.getX(), position.getY(), position.getZ(), worldIn, ModRand.range(6, 8));
        return false;
    }

    public void intereperetCircle(double x, double y, double z, World world, int radius) {


        List<BlockPos> affectedConversionPositions = Lists.newArrayList();
        int radius_int_conversion = (int) Math.ceil(radius);
        for (int dx = -radius_int_conversion; dx < radius_int_conversion + 1; dx++) {
            int y_lim = (int) Math.sqrt(radius_int_conversion*radius_int_conversion-dx*dx);
            for (int dy = -y_lim; dy < y_lim + 1; dy++) {
                int z_lim = (int) Math.sqrt(radius_int_conversion * radius_int_conversion - dx * dx - dy * dy);
                for (int dz = -z_lim; dz < z_lim + 1; dz++) {
                    BlockPos blockPos = new BlockPos(x + dx, y + dy, z + dz);
                    double power = power(Math.sqrt(dx * dx + dy * dy + dz * dz), radius);
                    if ((power > 1) || (power > new Random().nextDouble())) {
                        affectedConversionPositions.add(blockPos);
                    }
                }
            }
        }
        int randomDeterminedSize = new Random().nextInt(16) + 220;
        for(BlockPos blockPos : affectedConversionPositions) {
            if(randomDeterminedSize < 0) {
                break;
            }
            for(int c = (int) y + 9; c >= y - 10; c--) {
                BlockPos findSmooth = new BlockPos(blockPos.getX(), c, blockPos.getZ());
                if(world.getBlockState(findSmooth) == Blocks.END_STONE.getDefaultState() && world.getBlockState(findSmooth.up()) == Blocks.AIR.getDefaultState()) {
                    if(world.getBlockState(findSmooth.add(1, 0, 0)) != Blocks.AIR.getDefaultState() && world.getBlockState(findSmooth.add(-1, 0, 0)) != Blocks.AIR.getDefaultState() &&
                            world.getBlockState(findSmooth.add(0, 0, 1)) != Blocks.AIR.getDefaultState() && world.getBlockState(findSmooth.add(0, 0, -1)) != Blocks.AIR.getDefaultState()) {
                        world.setBlockState(new BlockPos(blockPos.getX(), c, blockPos.getZ()), ModBlocks.BARE_ACID.getDefaultState());
                    }
                    randomDeterminedSize++;
                }
            }
        }
    }


    // }






    public static double power(double dist, double radius){
        double decay_rd = radius * 0.95;
        if(dist < decay_rd){
            return 1.1d;
        }
        else {
            return -(1/(radius-decay_rd))*(dist-decay_rd) + 1;
        }
    }
}