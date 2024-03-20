package com.example.structure.world.Biome.barrend;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.example.structure.world.Biome.BiomeFogged;
import com.example.structure.world.Biome.decorator.EEBiomeDecorator;
import com.example.structure.world.Biome.generation.WorldGenEndPlant;
import git.jbredwards.nether_api.api.audio.IMusicType;
import git.jbredwards.nether_api.api.biome.IEndBiome;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeBarrendForest extends BiomeFogged implements IEndBiome, INetherAPIRegistryListener {

    /**
     * An underground Foresty Biome
     */
    public WorldGenerator bare_weeds;
    private Random random;
    public static BiomeProperties properties = new BiomeProperties("Barrend Forest");
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private static final IBlockState END_FLOOR = Blocks.END_STONE.getDefaultState();
    private static final IBlockState BARE_SANDS = ModBlocks.BARE_SANS.getDefaultState();
    public BiomeBarrendForest() {
        super(properties.setBaseHeight(5f).setHeightVariation(8f).setRainDisabled().setTemperature(0.8F));
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.topBlock = END_FLOOR;
        this.fillerBlock = END_FLOOR;
        random = new Random();
        bare_weeds = new WorldGenEndPlant(ModBlocks.BARE_PLANT.getDefaultState());
        this.setFogColor(10, 30, 22);
    }

    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return new EEBiomeDecorator();
    }

    public void decorate(World world, Random rand, BlockPos pos) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int plantRoll = random.nextInt(100);
                if (plantRoll <= 17) {
                    int terrainHeight = getEndSurfaceHeight(world, pos.add(x + 8, 0, z + 8), 8 - random.nextInt(5), 42);
                    if (terrainHeight > 0) {
                        BlockPos plantPos = pos.add(x + 8, terrainHeight, z + 8);
                        if (plantRoll == 17)
                            bare_weeds.generate(world, random, plantPos);

                    }
                }
            }
        }
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





    @Override
    public void buildSurface(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, @Nonnull ChunkPrimer primer, int x, int z, double terrainNoise) {


        int currDepth = -1;

        //Blotch Paint lower Lands of the End
        for(int y = chunkGenerator.getWorld().getActualHeight() - 1; y >= 0; y--) {
            final IBlockState here = primer.getBlockState(x, y, z);
            if(here.getMaterial() == Material.AIR) currDepth = -1;
            if(here.getBlock() == Blocks.END_STONE) {
                int randomStart = 42 + ModRand.range(3, 8);
                if(y < randomStart && random.nextInt(4) == 0) {
                    currDepth--;
                    primer.setBlockState(x, y, z, BARE_SANDS);
                }
            }
        }
    }

    @Override
    public void populate(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ) {
        IEndBiome.super.populate(chunkGenerator, chunkX, chunkZ);
    }

    @Override
    public boolean generateIslands(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, float islandHeight) {
        return IEndBiome.super.generateIslands(chunkGenerator, chunkX, chunkZ, islandHeight);
    }

    @Override
    public boolean generateChorusPlants(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, float islandHeight) {
        return IEndBiome.super.generateChorusPlants(chunkGenerator, chunkX, chunkZ, islandHeight);
    }

    @Override
    public boolean generateEndCity(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, int islandHeight) {
        return IEndBiome.super.generateEndCity(chunkGenerator, chunkX, chunkZ, islandHeight);
    }

    @Override
    public boolean hasExtraXZFog(@Nonnull World world, int x, int z) {
        return IEndBiome.super.hasExtraXZFog(world, x, z);
    }

    @Nonnull
    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return IEndBiome.super.getFogColor(celestialAngle, partialTicks);
    }

    @Nonnull
    @Override
    public IMusicType getMusicType() {
        return IEndBiome.super.getMusicType();
    }

    @Nonnull
    @Override
    public IMusicType getBossMusicType() {
        return IEndBiome.super.getBossMusicType();
    }
}
