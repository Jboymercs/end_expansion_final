package com.example.structure.world.Biome.barrend;

import com.example.structure.init.ModBlocks;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenBareSmallRuins extends WorldGenStructure {
    public WorldGenBareSmallRuins(String structureName) {
        super("barrendbiome/ruins/" + structureName);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        //ruin Gen
        if ((worldIn.getBlockState(position.down()) == ModBlocks.BARE_SANS.getDefaultState() || worldIn.getBlockState(position.down()) == Blocks.END_STONE.getDefaultState())) {

            return super.generate(worldIn, rand, position);
        }

        return false;
    }
}
