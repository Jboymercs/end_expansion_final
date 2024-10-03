package com.example.structure.world.Biome.barrend;

import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import java.util.Random;

public class WorldGenBareArena extends WorldGenStructure {

    private int spacing = 0;

    public WorldGenBareArena(String structureName) {
        super("barrendbiome/arena/" + structureName);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        if(spacing > 100 && !world.isAirBlock(pos.add(18, 0, 18))) {
            System.out.println("Generated Arena At" + pos);
            spacing = 0;
            super.generateStructure(world, pos.add(-3, 0, -3), Rotation.NONE);
        }
        System.out.println("Spacing At" + spacing);
        spacing++;
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {


    }

}
