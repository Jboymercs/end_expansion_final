package com.example.structure.world.Biome.decorator;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenLongVein extends WorldGenerator {

    private final IBlockState block;

    public WorldGenLongVein() {
        IBlockState stone = Blocks.STONE.getDefaultState();
        this.block = ModRand.choice(new IBlockState[]{
                stone,
                ModBlocks.END_ASH.getDefaultState()});
    }
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos pos) {
        int size = 15;
        for (int y = 0; y < size; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos newPos = new BlockPos(x, -y, z).add(pos);
                    if (worldIn.rand.nextFloat() > 0.25 && worldIn.getBlockState(newPos).getBlock() == ModBlocks.END_ASH) {
                        worldIn.setBlockState(newPos, block);
                    }
                }
            }
            if (worldIn.rand.nextFloat() > 0.975) {
                pos = pos.add(new BlockPos(ModRand.randSign(), 0, 0));
            }
            if (worldIn.rand.nextFloat() > 0.975) {
                pos = pos.add(new BlockPos(0, 0, ModRand.randSign()));
            }
        }
        return true;
    }

    private int getEndSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int maxY = max;
        int minY = min;
        int currentY = maxY;

        while(currentY >= minY)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)))
                return currentY;
            currentY--;
        }
        return 0;
    }
}
