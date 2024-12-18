package com.example.structure.world.Biome;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.EntityEndBug;
import com.example.structure.entity.EntitySnatcher;
import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.entity.barrend.EntityMadSpirit;
import com.example.structure.entity.barrend.EntityVoidTripod;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.example.structure.world.Biome.barrend.*;
import com.example.structure.world.api.barrend_crypts.WorldGenBarrendCrypt;
import com.example.structure.world.islands.WorldGenOutpost;
import git.jbredwards.nether_api.api.audio.IDarkSoundAmbience;
import git.jbredwards.nether_api.api.audio.IMusicType;
import git.jbredwards.nether_api.api.audio.ISoundAmbience;
import git.jbredwards.nether_api.api.biome.IAmbienceBiome;
import git.jbredwards.nether_api.api.biome.IEndBiome;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import git.jbredwards.nether_api.mod.common.config.NetherAPIConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BiomeBarrendLands extends BiomeFogged implements IEndBiome, INetherAPIRegistryListener, IAmbienceBiome {

    private static final IBlockState BARE_FLOOR = ModBlocks.BARE_SANS.getDefaultState();

    public static final WorldGenBarrendPlant barrendPlant = new WorldGenBarrendPlant(ModBlocks.BARE_PLANT.getDefaultState());
    public static final WorldGenBarrendPlant barrendGrass = new WorldGenBarrendPlant(ModBlocks.BARE_GRASS.getDefaultState());
    private final WorldGenBarrendTree[] barrendTrees = {new WorldGenBarrendTree("bare_tree1"),new WorldGenBarrendTree("bare_tree2"),new WorldGenBarrendTree("bare_tree3"),
            new WorldGenBarrendTree("bare_tree4")};

    public WorldGenerator barrend_arena = new WorldGenBareArena("barrend_arena");

    public WorldGenBarrendCrypt barrend_crypt = new WorldGenBarrendCrypt();

    private final WorldGenBarrendArches[] arches = {new WorldGenBarrendArches("arch_1"),new WorldGenBarrendArches("arch_2"),new WorldGenBarrendArches("arch_3"),
            new WorldGenBarrendArches("arch_4"),};

    private WorldGenBarePatches bare_sand_patches = new WorldGenBarePatches();
    private WorldGenBareAcid bare_acid_patches = new WorldGenBareAcid();
    private final WorldGenBareSmallRuins[] barrendRuins = {new WorldGenBareSmallRuins("ruins_1"),new WorldGenBareSmallRuins("ruins_2"),new WorldGenBareSmallRuins("ruins_3"),
            new WorldGenBareSmallRuins("ruins_4"), new WorldGenBareSmallRuins("ruins_5"), new WorldGenBareSmallRuins("ruins_6"), new WorldGenBareSmallRuins("ruins_7")
            , new WorldGenBareSmallRuins("ruins_8")};
    private Random random;
    public static BiomeProperties properties = new BiomeProperties("Barrend Bogs");
    public BiomeBarrendLands() {
        super(properties.setRainDisabled());
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();

        this.spawnableCreatureList.add(new SpawnListEntry(EntityLidoped.class, 1, 1, 1));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityVoidTripod.class, 1, 1, 2));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityMadSpirit.class, 1, 1, 2));
        this.topBlock = BARE_FLOOR;
        random = new Random();

    }

    @Override
    public void decorate(World world, Random rand, BlockPos pos) {
        //Plants
        for (int k2 = 0; k2 < ModRand.range(8, 30); k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            for (int y = 110; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if (depthSignature == 1) {
                    if(rand.nextInt(3) == 0) {
                        barrendPlant.generate(world, rand, pos.add(l6, y + 1, k10));
                    } else {
                        barrendGrass.generate(world, rand, pos.add(l6, y + 1, k10));
                    }
                }

                if (currentBlock == ModBlocks.BARE_SANS.getDefaultState() || currentBlock == Blocks.END_STONE.getDefaultState() || currentBlock == ModBlocks.BARE_STONE.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature = 0;
                }
            }
        }

            //Barrend Trees
            for (int k2 = 0; k2 < ModRand.range(1, 3); k2++) {
                int l6 = random.nextInt(16) + 8;
                int k10 = random.nextInt(16) + 8;
                int depthSignature = 2;
                for (int y = 110; y > 50; y--) {
                    IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                    if (depthSignature == 1) {
                            WorldGenBarrendTree tree = ModRand.choice(barrendTrees);
                            tree.generate(world, rand, pos.add(l6 - 7, y + 1, k10 - 7));
                    }

                    if (currentBlock == ModBlocks.BARE_SANS.getDefaultState() || currentBlock == Blocks.END_STONE.getDefaultState()) {
                        depthSignature++;
                    } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                        depthSignature = 0;
                    }
                }
            }


        //ruins
        if (world.rand.nextInt(4) == 0) {
            for (int k2 = 0; k2 < ModRand.range(1, 4); k2++) {
                int l6 = random.nextInt(16) + 8;
                int k10 = random.nextInt(16) + 8;
                int depthSignature = 2;
                for (int y = 110; y > 32; y--) {
                    IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                    if (depthSignature == 1) {
                        WorldGenBareSmallRuins tree = ModRand.choice(barrendRuins);
                        tree.generate(world, rand, pos.add(l6, y + 1, k10));
                        return;
                    }

                    if (currentBlock == ModBlocks.BARE_SANS.getDefaultState() || currentBlock == Blocks.END_STONE.getDefaultState()) {
                        depthSignature++;
                    } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                        depthSignature = 0;
                    }
                }
            }
        }

        //Sand Patches
        for(int k2 = 0; k2 < ModRand.range(1, 3);k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            for(int y = 120; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(depthSignature == 1) {
                    if(!world.isAirBlock(pos.add(l6, y, k10))) {
                        bare_sand_patches.generate(world, rand, pos.add(l6, y, k10));
                    }
                }

                if(currentBlock == Blocks.END_STONE.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature = 0;
                }
            }
        }

        //Bare Acid Patches
        //Sand Patches
        for(int k2 = 0; k2 < ModRand.range(1, 3);k2++) {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int depthSignature = 2;
            for(int y = 120; y > 32; y--) {
                IBlockState currentBlock = world.getBlockState(pos.add(l6, y, k10));
                if(depthSignature == 1) {
                    if(!world.isAirBlock(pos.add(l6, y, k10))) {
                        bare_acid_patches.generate(world, rand, pos.add(l6, y, k10));
                    }
                }

                if(currentBlock == Blocks.END_STONE.getDefaultState()) {
                    depthSignature++;
                } else if (currentBlock == Blocks.AIR.getDefaultState()) {
                    depthSignature = 0;
                }
            }
        }
    }

    @Override
    public void buildSurface(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ, @Nonnull ChunkPrimer primer, int x, int z, double terrainNoise) {
        int currDepth = -1;
        for(int y = chunkGenerator.getWorld().getActualHeight() - 1; y >= 0; --y) {
            final IBlockState here = primer.getBlockState(x, y, z);
            if(here.getMaterial() == Material.AIR) currDepth = -1;
            else if(here.getBlock() == Blocks.END_STONE || here.getBlock() == ModBlocks.BARE_STONE) {
                if(currDepth == -1) {
                    currDepth = 20 + chunkGenerator.getRand().nextInt(5);
                    if(random.nextInt(6) == 0) {
                        primer.setBlockState(x,y,z, ModBlocks.BARE_SANS.getDefaultState());
                    } else if (random.nextInt(8) == 0) {
                        primer.setBlockState(x,y,z, ModBlocks.BARE_STONE.getDefaultState());
                    } else {
                        primer.setBlockState(x, y, z, Blocks.END_STONE.getDefaultState());
                    }
                }
                else if(currDepth > 0) {
                    --currDepth;
                            primer.setBlockState(x, y, z, Blocks.END_STONE.getDefaultState());

                }
            }
        }
    }

    @Override
    public void populate(@Nonnull INetherAPIChunkGenerator chunkGenerator, int chunkX, int chunkZ) {
        IEndBiome.super.populate(chunkGenerator, chunkX, chunkZ);

        BlockPos pos = new BlockPos(chunkX << 4, 0, chunkZ << 4);

        //Barrend Arena
        if(getGroundFromAbove(chunkGenerator.getWorld(), pos.getX(), pos.getZ()) > 58) {
            int y = getGroundFromAbove(chunkGenerator.getWorld(), pos.getX(), pos.getZ());
            barrend_arena.generate(chunkGenerator.getWorld(), random, pos.add(0, y -2, 0));
        }

        //Archways and Camps
        if(getGroundFromAbove(chunkGenerator.getWorld(), pos.getX(), pos.getZ()) > 55) {
            int y = getGroundFromAbove(chunkGenerator.getWorld(), pos.getX(), pos.getZ());
            WorldGenBarrendArches archTooGenerate = ModRand.choice(arches);
            archTooGenerate.generate(chunkGenerator.getWorld(), random, pos.add(0, y -4, 0));
        }

        //Barrend Crypts
        if(getGroundFromAbove(chunkGenerator.getWorld(), pos.getX(), pos.getZ()) > 58 && ModConfig.dev_stuff_enabled) {
            int y = getGroundFromAbove(chunkGenerator.getWorld(), pos.getX(), pos.getZ());
            barrend_crypt.generate(chunkGenerator.getWorld(), random, pos.add(0, y, 0));
        }


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
        return true;
    }

    @Nonnull
    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        return new Vec3d(0.6,0,0.384);
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

    @Nullable
    @Override
    public IParticleFactory[] getAmbientParticles() {
        return IAmbienceBiome.super.getAmbientParticles();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return IAmbienceBiome.super.getAmbientSound();
    }

    @Nullable
    @Override
    public ISoundAmbience getRandomAmbientSound() {
        return IAmbienceBiome.super.getRandomAmbientSound();
    }


    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 255;
        boolean foundGround = false;
        while(!foundGround && y-- >= 57)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == Blocks.END_STONE || blockAt == ModBlocks.BARE_SANS;
        }

        return y;
    }

    @Nullable
    @Override
    public IDarkSoundAmbience getDarkAmbienceSound() {
        return IAmbienceBiome.super.getDarkAmbienceSound();
    }
}
