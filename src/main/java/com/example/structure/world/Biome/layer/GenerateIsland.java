package com.example.structure.world.Biome.layer;

import com.example.structure.init.ModBlocks;
import com.example.structure.world.api.structures.MapGenKingFortress;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.structure.INetherAPIStructureEntry;
import git.jbredwards.nether_api.api.structure.ISpawningStructure;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.structure.MapGenStructure;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class GenerateIsland extends MapGenBase{
    protected List<BlockPos> islands = new ArrayList<>();
    private NoiseGeneratorPerlin islandNoiseGen;
    private double[] islandNoise = new double[256];

    private NoiseGeneratorPerlin cragNoiseGen;
    private double[] cragNoise = new double[256];


    public GenerateIsland() {

        long seed = world.getSeed();
        Random rng = new Random(seed);
        this.islandNoiseGen = new NoiseGeneratorPerlin(rng, 3);
        this.cragNoiseGen = new NoiseGeneratorPerlin(rng, 5);
    }


    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
        this.islands.clear();


        super.generate(worldIn, x, z, primer);

        for(BlockPos island : this.islands) {
            //early skip islands outside range
            if(island.getDistance(x * 16, island.getY(), z * 16) <= 52) {
                Random rand = new Random();
                rand.setSeed(island.getX() ^ island.getY() ^ island.getZ() ^ worldIn.getSeed());
                this.generateIsland(island, rand, x, z, primer);
            }
        }

    }


    protected void generateIsland(BlockPos center, Random rand, int chunkX, int chunkZ, ChunkPrimer primer) {
        long locationSeed = rand.nextLong(); //Need to always generate the seed otherwise world gen desyncs in other chunks


        float[] islandMask = new float[256];

        int i = rand.nextInt(4) + 6;

        float size = rand.nextFloat();

        float minSize = 20 + size * 8;
        float maxSize = 40 + size * 18;

        int relX = center.getX() - chunkX * 16;
        int relZ = center.getZ() - chunkZ * 16;

        for(int round = 0; round < i; ++round) {
            float wx = rand.nextFloat() * (maxSize - minSize) + minSize;
            float wz = rand.nextFloat() * (maxSize - minSize) + minSize;
            float mx = 32 + (rand.nextFloat() - 0.5F) * wx * 0.5F;
            float mz = 32 + (rand.nextFloat() - 0.5F) * wz * 0.5F;

            for(int bx = 0; bx < 16; ++bx) {
                for(int bz = 0; bz < 16; ++bz) {
                    int ix = bx - relX + 32;
                    int iz = bz - relZ + 32;

                    if(ix >= 0 && ix < 64 && iz >= 0 && iz < 64) {
                        float dx = (ix - mx) / wx;
                        float dz = (iz - mz) / wz;
                        float d = dx * dx + dz * dz;

                        islandMask[bx * 16 + bz] = Math.max(islandMask[bx * 16 + bz], 1 - d);
                    }
                }
            }
        }

        this.islandNoise = this.islandNoiseGen.getRegion(this.islandNoise, (double) (chunkX * 16), (double) (chunkZ * 16), 16, 16, 0.4D, 0.4D, 1.0D);
        this.cragNoise = this.cragNoiseGen.getRegion(this.cragNoise, (double) (chunkX * 16), (double) (chunkZ * 16), 16, 16, 0.55D, 0.55D, 1.0D);

        float minVal = 0.8f;

        for(int bx = 0; bx < 16; ++bx) {
            for(int bz = 0; bz < 16; ++bz) {
                if(islandMask[bx * 16 + bz] > minVal) {
                    double islandNoiseVal = this.islandNoise[bz * 16 + bx] / 0.9f + 2.1f;

                    double cragNoise = this.cragNoise[bz * 16 + bx] / 2.1f + 2.0f;

                    boolean isCrag = cragNoise <= 0;

                    double islandMaskVal = islandMask[bx * 16 + bz] - minVal;

                    int depth = MathHelper.ceil(islandMaskVal * islandMaskVal * 200 + islandNoiseVal * islandNoiseVal * islandMaskVal);
                    int height = MathHelper.floor(islandMaskVal * 12.0f - islandNoiseVal * islandMaskVal * 0.1f + (cragNoise < 0.2f ? 1 : 0));

                    int lowering = 0;


                    int water = -1;

                    int surface = center.getY();
                    for(; surface > depth + 1; surface--) {
                        IBlockState state = primer.getBlockState(bx, surface, bz);

                        boolean liquid = state.getMaterial().isLiquid();

                        if(liquid && water < 0) {
                            water = surface;
                        }

                        if(state.getBlock() != Blocks.AIR && !liquid) {
                            break;
                        }
                    }

                    for(int y = 0; y > -depth; y--) {
                        if(surface + y > 0 && !primer.getBlockState(bx, surface, bz).getMaterial().isLiquid()) {
                                primer.setBlockState(bx, surface + y, bz, Blocks.AIR.getDefaultState());
                        }
                    }

                    for(int y = height - lowering; y > -depth - lowering; y--) {
                        int by = center.getY() + y;

                        if(by > 0) {
                            if(isCrag) {
                                if(y == height - lowering) {
                                    primer.setBlockState(bx, by, bz, ModBlocks.END_ASH.getDefaultState());
                                } else if(y == height - lowering - 1) {
                                    primer.setBlockState(bx, by, bz, ModBlocks.END_ASH.getDefaultState());
                                } else if(y == height - lowering - 2) {
                                    primer.setBlockState(bx, by, bz, ModBlocks.END_ASH.getDefaultState());
                                } else {
                                    primer.setBlockState(bx, by, bz, ModBlocks.END_ASH.getDefaultState());
                                }
                            } else {
                                    if(y == height - lowering) {
                                        primer.setBlockState(bx, by, bz, ModBlocks.END_ASH.getDefaultState());
                                    } else if(y >= height - lowering - 2) {
                                        primer.setBlockState(bx, by, bz, ModBlocks.END_ASH.getDefaultState());
                                    } else {
                                        primer.setBlockState(bx, by, bz, ModBlocks.END_ASH.getDefaultState());
                                    }

                            }
                        }
                    }
                }
            }
        }
    }




}
