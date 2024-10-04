package com.example.structure.world.Biome.barrend;

import com.example.structure.init.ModBlocks;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenBarrendArches extends WorldGenStructure {

    private final WorldGenBookCamp camp = new WorldGenBookCamp("camp");
    private int spacing = 0;

    public WorldGenBarrendArches(String structureName) {
        super("barrendbiome/ruins/" + structureName);
    }


    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        //fines opposite corner
        if(spacing > 60 && !world.isAirBlock(pos.add(11, 0, 2))) {
            spacing = 0;
            if(world.rand.nextInt(5) == 0) {
                camp.generateStructure(world, pos, rotation);
                return;
            }
            super.generateStructure(world, pos.add(0, 0, 0), Rotation.NONE);
        }
        spacing++;
    }
}
