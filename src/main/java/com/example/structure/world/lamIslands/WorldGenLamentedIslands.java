package com.example.structure.world.lamIslands;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenLamentedIslands extends WorldGenStructure {
    /**
     * Small Refurbished Class for the New Lamented Islands
     */
    int yOffset;
    public WorldGenLamentedIslands(String structureName, int yOffset) {
        super(structureName);
        this.yOffset = yOffset;
    }

    @Override
    public boolean generate(World worldIn, Random random, BlockPos blockPos) {
        BlockPos offset = this.getCenter(worldIn).subtract(this.getCenter(worldIn));
        return super.generate(worldIn, random, offset.add(new BlockPos(0,yOffset,0)));
    }

    @Override
    public int getYGenHeight(World world, int x, int z) {
        return ModUtils.calculateGenerationHeight(world, x, z);
    }

    //Generator for Mobs
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= ModConfig.structure_spawns) {
            return false;
        }
        return true;
    }
    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= ModConfig.lamentedIslandsLootChance) {
            return false;
        }
        return true;
    }
    //Generator for Extra Platforms
    public boolean doesPlatformGenerate() {
        int randomNumberGenerator = ModRand.range(0, 5);

            if(randomNumberGenerator >= 3) {
                return true;
            }
            return false;

    }
}
