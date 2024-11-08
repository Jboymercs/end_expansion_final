package com.example.structure.world.Biome.barrend;

import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenBookCamp extends WorldGenStructure {

    public WorldGenBookCamp(String structureName) {
        super("barrendbiome/ruins/" + structureName);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {

        super.generateStructure(world, pos, Rotation.NONE);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("chest")) {
            world.setBlockToAir(pos);
            world.setBlockToAir(pos.add(0, -1 ,0));
        }
    }
}
