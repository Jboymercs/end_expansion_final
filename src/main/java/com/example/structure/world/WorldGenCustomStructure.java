package com.example.structure.world;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModRand;
import com.example.structure.util.handlers.BiomeRegister;
import com.example.structure.world.Biome.BiomeAshWasteland;
import com.example.structure.world.Biome.generation.WorldGenAshRuins;
import com.example.structure.world.Biome.generation.WorldGenPurpleSpikes;
import com.example.structure.world.api.vaults.WorldGenEndVaults;
import com.example.structure.world.lamIslands.WorldGenBossArena;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.lwjgl.Sys;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldGenCustomStructure implements IWorldGenerator {

    public WorldGenStructure[] towers = {new WorldGenEndDungeon("ocean/tower_end_1", -1), new WorldGenEndDungeon("ocean/tower_end_2", -1),
    new WorldGenEndDungeon("ocean/tower_end_3", -1)};

    public static final WorldGenEndVaults endVaults = new WorldGenEndVaults();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos pos = new BlockPos(x + 8, 0, z + 8);
        //End Expansion Islands
        if(world.provider.getDimension() == 1) {
            if (ModConfig.does_structure_spawn) {

                // Lamented Islands , Can not spawn in the Ash Wastelands
                if (canStructureSpawn(chunkX, chunkZ, world, ModConfig.structureFrequency)) {
                    BlockPos posAdded = new BlockPos(x, 90, z);
                    if(world.provider.getBiomeForCoords(pos).getClass() != BiomeAshWasteland.class) {
                         new WorldGenBossArena().generateStructure(world, posAdded, Rotation.NONE);
                    }
                }

                //Where you left off at
                // Fix Loot tables
                // Add End Seeker and Mini-boss
                //End Vaults, Can not Spawn in Ash Wastelands
                if(world.getBiomeForCoordsBody(pos) != BiomeRegister.END_ASH_WASTELANDS) {
                    if(getGroundFromAbove(world, pos.getX(), pos.getZ()) > 57) {
                        endVaults.generate(world, random, pos);
                    }

                }
        }



        }
    }

    /**
     * Used for End Structure Generation Spawning
     * @param world
     * @param x
     * @param z
     * @return
     */
    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 255;
        boolean foundGround = false;
        while(!foundGround && y-- >= 31)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == Blocks.END_STONE;
        }

        return y;
    }

    private boolean generateBiomeSpecificStructure(WorldGenStructure generator, World world, Random rand, int x, int z, Class<?>... classes) {
        ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(Arrays.asList(classes));

        x += 8;
        z += 8;
        int y = generator.getYGenHeight(world, x, z);
        BlockPos pos = new BlockPos(x, y, z);

        Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();

        if (y > -1 && (world.getWorldType() != WorldType.FLAT || world.provider.getDimension() != 0)) {
            if (classesList.contains(biome)) {
                if (rand.nextFloat() > generator.getAttempts()) {
                    generator.generate(world, rand, pos);
                    return true;

                }
            }
        }
        return false;
    }


    private boolean generateLamentedIslands(WorldGenStructure generator, World world, Random rand, int x, int z, Class<?>... classes) {
        ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(Arrays.asList(classes));

        x += 8;
        z += 8;

        BlockPos pos = new BlockPos(x, 90, z);

        Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();

        if ((world.getWorldType() != WorldType.FLAT)) {
            if (classesList.contains(biome)) {
                if (rand.nextFloat() > generator.getAttempts()) {
                    generator.generateStructure(world, pos, Rotation.NONE);
                    return true;

                }
            }
        }
        return false;
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
}
