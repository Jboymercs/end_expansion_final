package com.example.structure.world.Biome.barrend;

import com.example.structure.init.ModBlocks;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenBarrendPlant extends WorldGenerator {


    private final IBlockState plantState;
    private final BlockBush plantBlock;

    public WorldGenBarrendPlant(IBlockState plant)
    {
        this.plantState = plant;
        this.plantBlock = (BlockBush) plant.getBlock();
    }

    public boolean generate(World world, Random rand, BlockPos pos)
    {
        if(plantBlock.canBlockStay(world, pos.up(), plantState) && world.getBlockState(pos) == ModBlocks.BARE_PLANT.getDefaultState() ||
                plantBlock.canBlockStay(world, pos.up(), plantState) && world.getBlockState(pos) == Blocks.END_STONE.getDefaultState() ) {
            world.setBlockState(pos.up(), plantState);
        }
        return true;
    }
}
