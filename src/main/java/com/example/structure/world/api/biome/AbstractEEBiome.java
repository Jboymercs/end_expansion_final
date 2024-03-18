package com.example.structure.world.api.biome;

import git.jbredwards.nether_api.api.biome.IEndBiome;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;

import javax.annotation.Nonnull;

public abstract class AbstractEEBiome extends Biome implements IEndBiome {

    public boolean arePlantsEnabled;

    private int[] fogColorRGB = new int[]{(int) 255, (int) 255, (int) 255};
    /**
     * Base Biome class that we will use for basic properties of End Expansion
     * @param properties
     */
    public AbstractEEBiome(BiomeProperties properties) {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();

    }

    private int getEndSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int maxY = max;
        int minY = min;
        int currentY = maxY;

        while(currentY >= minY)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)))
                return currentY;
            currentY--;
        }
        return 0;
    }
}
