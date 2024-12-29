package com.example.structure.world.Biome.barrend.portal;

import com.example.structure.util.ModRand;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenBarePortal extends WorldGenStructure {

    int spacing = 0;

    public WorldGenBarePortal(String structureName) {

        super("barrendbiome/arena/" + structureName);
    }


    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        BlockPos posAdded = position.add(29, 0, 29);

        if(worldIn.getBlockState(position).isFullBlock() && worldIn.getBlockState(posAdded).isFullBlock() &&
        spacing > 75) {
            this.generateStructure(worldIn, position, Rotation.NONE);
            spacing = 0;
            System.out.println("Generated Portal for Barrend Bogs at" + position);
            return super.generate(worldIn, rand, position);
        }
        spacing++;
        return false;
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("portal")) {
            world.setBlockToAir(pos);
        }
        if(function.startsWith("mob")) {
            world.setBlockToAir(pos);
        }
    }


}
