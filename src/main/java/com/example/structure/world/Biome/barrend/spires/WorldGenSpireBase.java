package com.example.structure.world.Biome.barrend.spires;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModRand;
import com.example.structure.util.data.FastNoise;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenSpireBase extends WorldGenerator {

    protected static final IBlockState rockTerrain = ModBlocks.BARE_SANS.getDefaultState();
    protected static final IBlockState rock_terrain_2 = ModBlocks.BARE_SANS.getDefaultState();
    protected static final IBlockState rock_terrain_3 = ModBlocks.BARE_STONE.getDefaultState();
    private final static IBlockState AIR = Blocks.AIR.getDefaultState();
    private final FastNoise perlin;

    public WorldGenSpireBase() {
        perlin = new FastNoise();
        perlin.SetNoiseType(FastNoise.NoiseType.Perlin);
        perlin.SetFrequency(0.2F);
    }


    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        int radius = 5 + rand.nextInt(20);
        int maxHeight = radius*3 - 1 - rand.nextInt(2);
        int randomStopHeight = ModRand.range(7, 9);
        IBlockState buildTerrain = rockTerrain;
        List<BlockPos> firstLayer = getFirstLayer(world, pos, radius);

        if(firstLayer == null)
            return false;
        else
        {
            for(BlockPos baseBlock : firstLayer)
            {
              //  world.setBlockState(baseBlock, buildTerrain);
                if(rand.nextInt(6) == 0) {
                    world.setBlockState(baseBlock, ModBlocks.BARE_SANS.getDefaultState());
                } else if (rand.nextInt(8) == 0){
                    world.setBlockState(baseBlock, ModBlocks.BARE_STONE.getDefaultState());
                } else {
                    world.setBlockState(baseBlock, Blocks.END_STONE.getDefaultState());
                }
            }
        }

        for(int y=1; y < maxHeight; y++)
        {
            int layerRadius = radius - y/2;
            double maxDist = layerRadius*layerRadius;
            int currentLayerX;
            int currentLayerZ;
            if(y < randomStopHeight) {
                for (int x = -layerRadius - 1; x <= layerRadius + 1; x++) {
                    double xDist = x * x;

                    for (int z = -layerRadius - 1; z <= layerRadius + 1; z++) {

                        double zDist = z * z;
                        double noiseMod = (perlin.GetNoise(pos.getX() + x, y * 3, pos.getZ() + z) + 1) / 2.0 + 0.5;
                        double noisyDist = xDist * noiseMod + zDist * noiseMod;

                        if (noisyDist > maxDist)
                            continue;


                    //    if(x == 0 && z == 0)
                      //      buildTerrain = rockTerrain;

                        currentLayerX = x;
                        currentLayerZ = z;

                        IBlockState downState = world.getBlockState(pos.add(x, y, z).down());
                        if (y == 0 || downState == buildTerrain || (y == 20 && (xDist <= 1 && zDist <= 1)) || downState == ModBlocks.BARE_STONE.getDefaultState() ||
                        downState == Blocks.END_STONE.getDefaultState())
                                if(rand.nextInt(6) == 0) {
                                    world.setBlockState(pos.add(x,y,z), ModBlocks.BARE_SANS.getDefaultState());
                                } else if (rand.nextInt(8) == 0){
                                    world.setBlockState(pos.add(x,y,z), ModBlocks.BARE_STONE.getDefaultState());
                                } else if (rand.nextInt(200) == 0){
                                    world.setBlockState(pos.add(x,y,z), ModBlocks.STAR_SHARD_ORE.getDefaultState());
                                } else {
                                    world.setBlockState(pos.add(x, y, z), Blocks.END_STONE.getDefaultState());
                                }


                    }
                }
            }
        }

        //negative


        return true;
    }


    public List<BlockPos> getFirstLayer(World world, BlockPos pos, int radius)
    {
        List<BlockPos> firstLayer = new ArrayList<BlockPos>();
        double maxDist = radius*radius;

        for(int x=-radius-5; x<=radius+5; x++)
        {
            double xDist = x*x;

            for(int z=-radius-5; z<=radius+5; z++)
            {
                double zDist = z*z;
                double noiseMod = (perlin.GetNoise(pos.getX()+x, 0, pos.getZ()+z)+1)/2.0 + 0.5;
                double noisyDist = xDist*noiseMod + zDist*noiseMod;

                if(noisyDist > maxDist)
                    continue;

                BlockPos checkPos = pos.add(x, 0, z).down();
                IBlockState downState = world.getBlockState(checkPos);
                if(downState == AIR)
                {
                    firstLayer.add(pos.add(x, 0, z));
                }
                else if(world.getBlockState(checkPos.down()) == AIR)
                {
                    firstLayer.add(pos.add(x, 0, z));
                    firstLayer.add(pos.add(x, -1, z));
                }
                else if(world.getBlockState(checkPos.down().down()) == AIR)
                {
                    firstLayer.add(pos.add(x, 0, z));
                    firstLayer.add(pos.add(x, -1, z));
                    firstLayer.add(pos.add(x, -2, z));
                }
                else
                {
                    return null;
                }

            }
        }

        return firstLayer;
    }


}
