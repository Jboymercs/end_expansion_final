package com.jboymercs.endexpansion.world.Biome.barrend;

import com.jboymercs.endexpansion.init.ModBlocks;
import com.jboymercs.endexpansion.world.WorldGenStructure;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenBarrendTree extends WorldGenStructure {


    public WorldGenBarrendTree(String structureName) {
        super("barrendbiome/trees/" + structureName);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
            //tree GEn
            if (worldIn.isAirBlock(position.add(0, 12, 0)) && (worldIn.getBlockState(position.down()) == ModBlocks.BARE_SANS.getDefaultState() || worldIn.getBlockState(position.down()) == Blocks.END_STONE.getDefaultState())) {

                return super.generate(worldIn, rand, position);
            }

        return false;
    }

}
