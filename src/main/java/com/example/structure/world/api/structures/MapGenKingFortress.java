package com.example.structure.world.api.structures;

import com.example.structure.config.ModConfig;
import com.example.structure.util.MapGenModStructure;
import com.example.structure.util.handlers.BiomeRegister;
import git.jbredwards.nether_api.api.registry.INetherAPIRegistryListener;
import git.jbredwards.nether_api.api.structure.INetherAPIStructureEntry;
import git.jbredwards.nether_api.api.structure.ISpawningStructure;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
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

public class MapGenKingFortress extends MapGenModStructure implements INetherAPIStructureEntry, ISpawningStructure, INetherAPIRegistryListener {
    INetherAPIChunkGenerator provider;

    public MapGenKingFortress(int spacing, int offset, int odds) {
        super(spacing, offset, odds);

    }

    @Override
    public String getStructureName() {
        return "EndKingsFortress";
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenKingFortress.Start(this.world, provider, this.rand, chunkX, chunkZ, this);
    }


    @Nonnull
    @Override
    public String getCommandName() {
        return getStructureName();
    }

    @Nonnull
    @Override
    public Function<INetherAPIChunkGenerator, MapGenStructure> getStructureFactory() {
        return chunkGenerator -> new MapGenKingFortress(20, 0, 1);
    }

    @Nonnull
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType type, @Nonnull World world, @Nonnull BlockPos pos) {
        return Collections.emptyList();
    }


    public static class Start extends StructureStart {
        INetherAPIChunkGenerator provider;
        public Start() {

        }

        public Start(World worldIn, INetherAPIChunkGenerator provider, Random rand, int chunkX, int chunkZ, MapGenKingFortress structure) {
            super(chunkX, chunkZ);
            this.provider = provider;
            this.create(worldIn, provider, rand, chunkX, chunkZ, structure);

        }

        private void create(World worldIn, INetherAPIChunkGenerator provider, Random rnd, int chunkX, int chunkZ, @Nonnull MapGenKingFortress structure) {
            BlockPos posToSpawn = new BlockPos(chunkX * 16 + 8, 45, chunkZ * 16 + 8);
            //Check to make sure it doesn't start in the Air as well to make sure it's in the right Biome
            if( worldIn.getBiomeForCoordsBody(posToSpawn) == BiomeRegister.END_ASH_WASTELANDS) {
                Random random = new Random(chunkX + chunkZ * 10387313);
                int rand = random.nextInt(Rotation.values().length);
                final ChunkPrimer primer = new ChunkPrimer();
                int yHeight = 90;
                for (int i = 0; i < 4; i++) {
                    components.clear();
                    Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                    boolean isXEven = ((((chunkX - 0) / 20) % 2) & 1) == 0;
                    boolean isZEven = ((((chunkZ - 0) / 20) % 2) & 1) == 0;


                    BlockPos blockpos = new BlockPos(chunkX * 16 + 8, yHeight, chunkZ * 16 + 8);

                    KingFortress stronghold = new KingFortress(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), provider, components);
                    stronghold.startFortress(blockpos, rotation);
                    this.updateBoundingBox();
                    if (this.isSizeableStructure()) {
                        break;
                    }
                }
            }
        }

        @Override
        public boolean isSizeableStructure() {
            return components.size() > ModConfig.fortress_size;
        }

    }
}
