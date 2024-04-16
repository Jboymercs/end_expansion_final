package com.example.structure.world.api.vaults;


import com.example.structure.util.MapGenModStructure;
import com.example.structure.util.handlers.BiomeRegister;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.structure.INetherAPIStructureEntry;
import git.jbredwards.nether_api.api.structure.ISpawningStructure;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import git.jbredwards.nether_api.mod.common.world.gen.ChunkGeneratorTheEnd;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MapGenEndVaults extends MapGenModStructure implements INetherAPIStructureEntry, ISpawningStructure, INetherAPIRegistryListener {

    ChunkGeneratorTheEnd provider;

    public MapGenEndVaults(int spacing, int offset, int odds) {
        super(spacing, offset, odds);
    }



    @Nonnull
    @Override
    public String getCommandName() {
        return getStructureName();
    }

    @Nonnull
    @Override
    public Function<INetherAPIChunkGenerator, MapGenStructure> getStructureFactory() {
        return chunkGenerator -> new MapGenEndVaults(10, 0, 2);
    }


    private static int getYPosForStructure(int chunkX, int chunkY, ChunkGeneratorTheEnd generatorIn)
    {
        Random random = new Random((long)(chunkX + chunkY * 10387313L));
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        ChunkPrimer chunkprimer = new ChunkPrimer();
        generatorIn.setBlocksInChunk(chunkX, chunkY, chunkprimer);
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

    @Nonnull
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType type, @Nonnull World world, @Nonnull BlockPos pos) {
        return Collections.emptyList();
    }

    @Override
    public String getStructureName() {
        return "EndVaults";
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
            return new MapGenEndVaults.Start(this.world, provider, this.rand, chunkX, chunkZ, this);
    }

    public static class Start extends StructureStart {
        ChunkGeneratorTheEnd provider;
        public Start(){

        }

        public Start(World worldIn, ChunkGeneratorTheEnd provider, Random rand, int chunkX, int chunkZ, MapGenEndVaults structure) {
            super(chunkX, chunkZ);
            this.provider = provider;
            this.create(worldIn, provider, rand, chunkX, chunkZ, structure);
        }

        private void create(World worldIn, ChunkGeneratorTheEnd provider, Random rnd, int chunkX, int chunkZ, @Nonnull MapGenEndVaults structure) {
            BlockPos posToSpawn = new BlockPos(chunkX * 16 + 8, 45, chunkZ * 16 + 8);
            //Check to make sure it doesn't start in the Air as well to make sure it's in the right Biome
            if( worldIn.getBiomeForCoordsBody(posToSpawn) != BiomeRegister.END_ASH_WASTELANDS) {
                Random random = new Random(chunkX + chunkZ * 10387313L);
                int rand = random.nextInt(Rotation.values().length);

                BlockPos posI = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);


                    for (int i = 0; i < 4; i++) {
                        Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                        int yHeight = getYPosForStructure(chunkX, chunkZ, provider);
                        if(yHeight >= 60) {
                            components.clear();
                            BlockPos blockpos = posI.add(0, yHeight - 14, 0);


                            this.updateBoundingBox();
                            if (this.isSizeableStructure()) {
                                break;
                            }
                        }
                }
            }
        }





        /**
         * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
         * levels in the BB's horizontal rectangle).
         */


        @Override
        public boolean isSizeableStructure() {
            return components.size() > 0;
        }
    }
}
