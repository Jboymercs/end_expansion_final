package com.example.structure.world.underground;

import com.example.structure.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraftforge.fml.common.Mod;

public class BiomeBarrendCaves extends BasicUndergroundBiome{

    private final WorldGenShrub shrubGen = new WorldGenShrub(ModBlocks.BARE_LEAVES.getDefaultState(), ModBlocks.BARE_LEAVES.getDefaultState());

    public static double grassChance, shrubChance, vineChance;

    public BiomeBarrendCaves() {
        super(ModBlocks.BARE_SANS.getDefaultState(), ModBlocks.BARE_SANS.getDefaultState(), ModBlocks.BARE_SANS.getDefaultState());
    }

    @Override
    public void finalFloorPass(World world, BlockPos pos) {


        if(world.rand.nextDouble() < shrubChance)
            shrubGen.generate(world, world.rand, pos.up());
    }
}
