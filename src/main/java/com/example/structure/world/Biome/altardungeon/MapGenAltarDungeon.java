package com.example.structure.world.Biome.altardungeon;

import com.example.structure.util.MapGenModStructure;
import com.example.structure.world.Biome.WorldChunkGeneratorEE;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraft.world.gen.structure.StructureEndCityPieces;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

public class MapGenAltarDungeon extends MapGenModStructure {

    WorldChunkGeneratorEE provider;
    protected World world;
    public MapGenAltarDungeon(int spacing, int offset, int odds, WorldChunkGeneratorEE provider) {
        super(spacing, offset, odds);
        this.provider = provider;
    }

    @Override
    public String getStructureName() {
        return "Altar Dungeon";
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenAltarDungeon.Start(this.world, this.rand, chunkX, chunkZ, provider);
    }

    private static int getYPosForStructure(int p_191070_0_, int p_191070_1_, ChunkGeneratorEnd p_191070_2_)
    {
        Random random = new Random((long)(p_191070_0_ + p_191070_1_ * 10387313));
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        ChunkPrimer chunkprimer = new ChunkPrimer();
        p_191070_2_.setBlocksInChunk(p_191070_0_, p_191070_1_, chunkprimer);
        int i = 5;
        int j = 5;

        if (rotation == Rotation.CLOCKWISE_90)
        {
            i = -5;
        }
        else if (rotation == Rotation.CLOCKWISE_180)
        {
            i = -5;
            j = -5;
        }
        else if (rotation == Rotation.COUNTERCLOCKWISE_90)
        {
            j = -5;
        }

        int k = chunkprimer.findGroundBlockIdx(7, 7);
        int l = chunkprimer.findGroundBlockIdx(7, 7 + j);
        int i1 = chunkprimer.findGroundBlockIdx(7 + i, 7);
        int j1 = chunkprimer.findGroundBlockIdx(7 + i, 7 + j);
        int k1 = Math.min(Math.min(k, l), Math.min(i1, j1));
        return k1;
    }

    @Override
    public boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= 19;
        }

        if (chunkZ < 0)
        {
            chunkZ -= 19;
        }

        int k = chunkX / 20;
        int l = chunkZ / 20;

        k = k * 20;
        l = l * 20;


        if (i == k && j == l && this.provider.isIslandChunk(i, j))
        {
            int i1 = getYPosForStructure(i, j, this.provider);
            return i1 >= 60;
        }
        else
        {
            return false;
        }
    }

    public static class Start extends StructureStart {

        WorldChunkGeneratorEE provider;

        public Start() {
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ, WorldChunkGeneratorEE provider) {
            super(chunkX, chunkZ);
            this.provider = provider;
            this.create(worldIn, provider, random, chunkX, chunkZ);
        }




        private void create(World worldIn, WorldChunkGeneratorEE provider, Random rnd, int chunkX, int chunkZ) {

            Random random = new Random((long)(chunkX + chunkZ * 10387313));
            Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            int i = MapGenAltarDungeon.getYPosForStructure(chunkX, chunkZ, provider);

            {
                BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
                AltarDungeon stronghold = new AltarDungeon(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), provider, components);
                stronghold.startStaircase(blockpos, rotation);
                this.updateBoundingBox();

            }
        }

        @Override
        public boolean isSizeableStructure() {
            return components.size() > 4;
        }
    }
}
