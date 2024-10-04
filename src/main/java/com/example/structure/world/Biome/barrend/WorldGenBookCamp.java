package com.example.structure.world.Biome.barrend;

import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenBookCamp extends WorldGenStructure {

    public WorldGenBookCamp(String structureName) {
        super("barrendbiome/ruins/" + structureName);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {

        super.generateStructure(world, pos, Rotation.NONE);
    }
}
