package com.example.structure.world.Biome;


import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.EntitySnatcher;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.world.Biome.decorator.EEBiomeDecorator;
import com.example.structure.world.Biome.generation.*;
import com.example.structure.world.WorldGenStructure;
import com.example.structure.world.api.ashtower.WorldGenAshTower;
import com.example.structure.world.api.mines.WorldGenMines;
import com.example.structure.world.api.structures.MapGenKingFortress;
import com.example.structure.world.islands.WorldGenOutpost;
import git.jbredwards.nether_api.api.audio.IMusicType;
import git.jbredwards.nether_api.api.audio.ISoundAmbience;
import git.jbredwards.nether_api.api.audio.impl.VanillaMusicType;
import git.jbredwards.nether_api.api.biome.IAmbienceBiome;
import git.jbredwards.nether_api.api.biome.IEndBiome;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BiomeAshWasteland extends BiomeFogged implements IEndBiome, INetherAPIRegistryListener, IAmbienceBiome {



    public static BiomeProperties properties = new BiomeProperties("Ash Wastelands");
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();

    public int spikesPerChunk = 3;

    public int geyserPerChunk = ModRand.range(3, 9);
    public int ruinsPerChunk = ModRand.range(1, 2);
    public int crystalSelect = ModRand.range(1, 3);

    //Small usage of all the structures seen in the biome

    public WorldGenStructure[] large_caves = {new WorldGenLargeCave("large_cave_1"), new WorldGenLargeCave("large_cave_2"), new WorldGenLargeCave("large_cave_3"),
    new WorldGenLargeCave("large_cave_4"), new WorldGenLargeCave("large_cave_4")};
    public WorldGenStructure[] small_caves = {new WorldGenSmallCaves("small_cave_1"), new WorldGenSmallCaves("small_cave_2"), new WorldGenSmallCaves("small_cave_3")};
    public WorldGenStructure[] ruins = {new WorldGenAshRuins("ash_ruins_1", -1), new WorldGenAshRuins("ash_ruins_2", -1),
    new WorldGenAshRuins("ash_ruins_3", -1), new WorldGenAshRuins("ash_ruins_4", -1), new WorldGenAshRuins("ash_ruins_5", -1),
    new WorldGenAshRuins("ash_ruins_6", -1), new WorldGenAshRuins("ash_ruins_7", -1), new WorldGenAshRuins("ash_ruins_8", -1)};
    public WorldGenAshSpikes spikes = new WorldGenAshSpikes();
    public WorldGenOre ore_gen = new WorldGenOre();
    public WorldGenerator ashHeights = new WorldGenAshHeights();

    public WorldGenerator vines = new WorldGenVines();
    public WorldGenerator crystalOre = new WorldGenRedCrystals();
    public WorldGenerator outpost = new WorldGenOutpost();

    public WorldGenerator geyser = new WorldGenGeyser();

    public MapGenStructure[] structures = {new MapGenKingFortress(20, 0,1)};
    private static final IBlockState END_FLOOR = ModBlocks.END_ASH.getDefaultState();
    private static final IBlockState END_WASTES = ModBlocks.BROWN_END_STONE.getDefaultState();
    public static final WorldGenEndPlant depths_plants = new WorldGenEndPlant(ModBlocks.SPROUT_PLANT.getDefaultState());
    public static final WorldGenAshTower ash_tower = new WorldGenAshTower();
    public static final WorldGenMines ashed_mines = new WorldGenMines();

    private Random random;
    public BiomeAshWasteland() {
        super(properties.setBaseHeight(0.9f).setHeightVariation(1.2f).setRainDisabled().setTemperature(0.8F));
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();

        //Let's Try this again
        this.spawnableCreatureList.add(new SpawnListEntry(EntitySnatcher.class, 1, 1, 1));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityChomper.class, 1, 1, 3));

        this.topBlock = END_FLOOR;
        this.fillerBlock = END_WASTES;
        random = new Random();

        this.setFogColor(10, 30, 22);
    }



    @SideOnly(Side.CLIENT)
    @Override
    public int getSkyColorByTemp(float currentTemperature)
    {
        currentTemperature = MathHelper.clamp(Math.abs(1.25F - currentTemperature), 0.0F, 1.0F);
        int r = 140;
        int g = (int)(185 + currentTemperature * 15);
        int b = (int)(215 - currentTemperature * 5);
        return r << 16 | g << 8 | b;
    }


    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return new EEBiomeDecorator();
    }

    public void decorate(World world, Random rand, BlockPos pos)
    {
        //Ash Heights
        if(rand.nextInt(2) == 0) {
            int yHieght = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 70);
            if(yHieght > 0) {
                ashHeights.generate(world, rand, pos.add(ModRand.range(1, 16), yHieght + 1, ModRand.range(1, 16)));
            }
        }

        //Ash Spikes
        for (int k2 = 0; k2 < this.spikesPerChunk; ++k2)
        {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int yHieght = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 70);
            if(yHieght > 0) {
                this.spikes.generate(world, random, pos.add(l6, yHieght, k10));
            }

        }
        //Geyser's
        if(rand.nextInt(7) == 0) {
            for (int k2 = 0; k2 < this.geyserPerChunk; ++k2) {
                int l6 = random.nextInt(16) + 8;
                int k10 = random.nextInt(16) + 8;
                int yHieght = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 70);
                if (yHieght > 0) {
                    this.geyser.generate(world, random, pos.add(l6, yHieght, k10));
                }
            }
        }
        //Red Crystal Ore
        if(rand.nextInt(30) == 1) {
            for (int k2 = 0; k2 < this.crystalSelect; ++k2) {
                int l6 = random.nextInt(16) + 8;
                int k10 = random.nextInt(16) + 8;
                int yHieght = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 70);
                if (yHieght > 0) {
                this.crystalOre.generate(world, random, pos.add(l6, yHieght, k10));
                }
            }
        }
        //Ash Ruins
        if(rand.nextInt(4) == 0) {
            for (int k2 = 0; k2 < this.ruinsPerChunk; ++k2) {
                int l6 = random.nextInt(16) + 8;
                int k10 = random.nextInt(16) + 8;
                int yHieght2 = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 90);
                if (yHieght2 > 0 && world.getBlockState(pos.add(pos.getX() >> 4, yHieght2, pos.getZ() >> 4)).isFullBlock()) {
                    WorldGenStructure ruin = ModRand.choice(ruins);
                    ruin.generate(world, rand, pos.add(l6, yHieght2, k10));
                }
            }
        }

        //Vines
        for(int k2 = 0; k2< ModRand.range(30, 50); k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            int vineLength = ModRand.range(1, 4);
            for(int y = 60; y > 5; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(depthSignature == 1) {
                    world.setBlockState(pos.add(l6, y + 1, k10), ModBlocks.SPROUT_VINE.getDefaultState());
                }
                if(currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == ModBlocks.BROWN_END_STONE.getDefaultState()) {
                    depthSignature = 0;
                }
            }
        }

        //Plants
        for(int k2 = 0; k2 < ModRand.range(30, 50);k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            for(int y = 45; y > 14; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                BlockPos posModified = new BlockPos(l6, y + 1, k10);
                if(depthSignature == 1) {
                    depths_plants.generate(world, rand, pos.add(l6, y + 1, k10));
                }

                if(currentBlock == ModBlocks.BROWN_END_STONE.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature = 0;
                }
            }
        }

        //Corruption Blocks
        for(int k2 = 0; k2 < ModRand.range(30, 50);k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            for(int y = 40; y > 15; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(currentBlock == ModBlocks.BROWN_END_STONE.getDefaultState() && world.rand.nextInt(2) == 0) {
                    world.setBlockState(pos.add(l6, y, k10), ModBlocks.SPROUT_STONE.getDefaultState());
                }
            }
        }

        //Cordium Ore
        for(int k2 = 0; k2 < ModRand.range(2, 5);k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            for(int y = 40; y > 15; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(currentBlock == ModBlocks.BROWN_END_STONE.getDefaultState() && world.rand.nextInt(7) == 0) {
                    ore_gen.generateOreNearby(world, random, pos.add(l6, y, k10));
                }
            }
        }


        //Vines


        if(!ModConfig.disable_large_caves) {
            //Small Caves
            if (rand.nextInt(10) == 0 && getGroundFromAbove(world, pos.getX(), pos.getZ()) > 50) {
                WorldGenStructure cave = ModRand.choice(small_caves);
                cave.generate(world, rand, pos.add(0, ModRand.range(35, 45), 0));
            }
            //Large Custom Caves
            if (rand.nextInt(2) == 0 && getGroundFromAbove(world, pos.getX(), pos.getZ()) > 57) {
                WorldGenStructure largeCave = ModRand.choice(large_caves);
                largeCave.generate(world, rand, pos.add(0, ModRand.range(25, 30), 0));
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
        // copied from stygian end mod, to ensure terrain generates the same
        //We will use a similar way but later extend on from it
        int currDepth = -1;
        for(int y = chunkGenerator.getWorld().getActualHeight() - 1; y >= 0; --y) {
            final IBlockState here = primer.getBlockState(x, y, z);
            if(here.getMaterial() == Material.AIR) currDepth = -1;
            else if(here.getBlock() == Blocks.END_STONE) {
                if(currDepth == -1) {
                    currDepth = 40 + chunkGenerator.getRand().nextInt(2);
                        primer.setBlockState(x, y, z, topBlock);
                }
                else if(currDepth > 0) {
                    --currDepth;
                        fillerBlock = ModBlocks.BROWN_END_STONE.getDefaultState();
                        primer.setBlockState(x, y, z, fillerBlock);
                }
            }
        }
    }


    @Override
    public void populate(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ) {
        IEndBiome.super.populate(chunkGenerator, chunkX, chunkZ);
        BlockPos pos = new BlockPos(chunkX << 4, 0, chunkZ << 4);
        final Biome biome = chunkGenerator.getWorld().getBiome(pos.add(16, 0, 16));
        final ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);

        //Big Structures

        //Outpost
        if(canStructureSpawn(pos.getX(), pos.getZ(), chunkGenerator.getWorld(), 30)) {
            this.canGenerateKnightFortress(chunkGenerator.getWorld(), pos);
        }

        //Ashed Towers
        if(getGroundFromAbove(chunkGenerator.getWorld(), pos.getX(), pos.getZ()) > 58) {
            ash_tower.generate(chunkGenerator.getWorld(), random, pos);
        }

        //Ashed Mines
        if(getGroundFromAbove(chunkGenerator.getWorld(), pos.getX(), pos.getZ()) > 60) {
            ashed_mines.generate(chunkGenerator.getWorld(), random, pos);
        }

    }

    public boolean canGenerateKnightFortress(World worldIn, BlockPos pos) {
        int yHieghtGeneration = getEndSurfaceHeight(worldIn, pos.add(16, 0,16), 30, 80 );

        //First Check for generation
        if(yHieghtGeneration == 0 && !worldIn.getBlockState(pos.add(pos.getX() >> 6, 55, pos.getZ() >> 6)).isFullBlock()) {
            BlockPos statPos = new BlockPos(pos.getX(), 55, pos.getZ());
           return this.outpost.generate(worldIn, random, statPos);
        }
        return false;
    }

    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 255;
        boolean foundGround = false;
        while(!foundGround && y-- >= 31)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == ModBlocks.END_ASH || blockAt == ModBlocks.BROWN_END_STONE;
        }

        return y;
    }



    @Override
    public boolean generateIslands(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, float islandHeight) {
        return false;
    }

    @Override
    public boolean generateChorusPlants(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, float islandHeight) {
        return false;
    }

    @Override
    public boolean generateEndCity(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, int islandHeight) {
        return false;
    }

    @Override
    public boolean hasExtraXZFog(@Nonnull World world, int x, int z) {
        return true;
    }

    @Nonnull
    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return new Vec3d(0,0,0);
    }

    public static boolean canStructureSpawn(int chunkX, int chunkZ, World world, int frequency){
        if (frequency <= 0) return false;
        int realFreq= 48 - frequency;
        int maxDistanceBetween = realFreq + 36;

        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= maxDistanceBetween - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= maxDistanceBetween - 1;
        }

        int k = chunkX / maxDistanceBetween;
        int l = chunkZ / maxDistanceBetween;
        Random random = world.setRandomSeed(k, l, 14357617);
        k = k * maxDistanceBetween;
        l = l * maxDistanceBetween;
        k = k + random.nextInt(maxDistanceBetween - 8);
        l = l + random.nextInt(maxDistanceBetween - 8);


        return i == k && j == l;
    }



    @Override
    public ISoundAmbience getRandomAmbientSound() {
        return new ISoundAmbience() {
            @Nonnull
            @Override
            public SoundEvent getSoundEvent() {
                return ModSoundHandler.BIOME_AMBIENCE;
            }


            @Override
            public double getChancePerTick() {
                return 0.0005;
            }
        };
    }

}
