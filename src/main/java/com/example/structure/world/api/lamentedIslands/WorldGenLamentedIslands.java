package com.example.structure.world.api.lamentedIslands;

import com.example.structure.config.ModConfig;
import com.example.structure.config.WorldConfig;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.misc.EELogger;
import com.example.structure.world.api.mines.AshedMines;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;

public class WorldGenLamentedIslands extends WorldGenerator {
    private int spacing;
    private int separation;

    public WorldGenLamentedIslands() {
        this.spacing = WorldConfig.structureFrequency;
        this.separation = 16;
    }

    public String getStructureName() {
        return "islands";
    }

    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 255;
        boolean foundGround = false;
        while(!foundGround && y-- >= 31)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == ModBlocks.END_ASH || blockAt == ModBlocks.BROWN_END_STONE || blockAt == Blocks.END_STONE;
        }

        return y;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenLamentedIslands.Start(world, rand, chunkX, chunkZ);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {

        if(canSpawnStructureAtPos(world, pos.getX() >> 4, pos.getZ() >> 4)) {
            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 350, pos.getZ() - 350, pos.getX() + 350, pos.getZ() + 350));
        }

        return false;
    }

    protected boolean canSpawnStructureAtPos(World world, int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.spacing - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.spacing - 1;
        }

        int k = chunkX / this.spacing;
        int l = chunkZ / this.spacing;
        Random random = world.setRandomSeed(k, l, 10388824);
        k = k * this.spacing;
        l = l * this.spacing;
        k = k + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;

        if (i == k && j == l && Math.abs(chunkX) > 35 && Math.abs(chunkZ) > 35)
        {
            BlockPos pos = new BlockPos(i << 4, 0, j << 4);
            return isAbleToSpawnHere(pos, world);
        } else {

            return false;
        }

    }

    public static boolean isAbleToSpawnHere(BlockPos pos, World world) {
        for(BiomeDictionary.Type types : getSpawnBiomeTypes()) {
            Biome biomeCurrently = world.provider.getBiomeForCoords(pos);
            if(BiomeDictionary.hasType(biomeCurrently, types)) {
                return false;
            }
        }
        return true;
    }

    private static List<BiomeDictionary.Type> lamentedIslandsBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypes() {
        if(lamentedIslandsBiomeTypes == null) {
            lamentedIslandsBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_lamented_islands) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) lamentedIslandsBiomeTypes.add(type);
                    else EELogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    EELogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return lamentedIslandsBiomeTypes;
    }


    public static class Start extends StructureStart {

        public Start(){

        }


        public Start(World worldIn, Random rand, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, rand, chunkX, chunkZ);
        }

        private void create(World worldIn, Random rnd, int chunkX, int chunkZ) {
            Random random = new Random(chunkX + chunkZ * 10387313L);
            int rand = random.nextInt(Rotation.values().length);

            BlockPos posI = new BlockPos(chunkX * 16 + 8, 90, chunkZ * 16 + 8);
                for (int i = 0; i < 4; i++) {
                    components.clear();
                    LamentedIslands mines = new LamentedIslands(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    mines.startIslands(posI, Rotation.NONE);
                    this.updateBoundingBox();

                    if (this.isSizeableStructure()) {

                        break;
                    }
                }

        }

        @Override
        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
        {
            super.generateStructure(worldIn, rand, structurebb);
        }

        @Override
        public boolean isSizeableStructure() {
            return components.size() > WorldConfig.islands_size;
        }
    }
}
