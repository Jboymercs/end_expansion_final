package com.example.structure.world.Biome.generation;

import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenAshRuins extends WorldGenStructure {
    int yOffset;

    public WorldGenAshRuins(String structureName, int yOffset) {
        super("ashbiome/" + structureName);
        this.yOffset = yOffset;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        return super.generate(worldIn, rand, position.add(new BlockPos(0,yOffset,0)));
    }
}
