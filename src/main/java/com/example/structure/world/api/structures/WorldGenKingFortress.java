package com.example.structure.world.api.structures;

import com.example.structure.config.WorldConfig;
import com.example.structure.util.handlers.BiomeRegister;
import com.example.structure.util.misc.EELogger;
import com.example.structure.world.api.lamentedIslands.WorldGenLamentedIslands;
import com.google.common.collect.Lists;
import git.jbredwards.nether_api.api.world.INetherAPIChunkGenerator;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class WorldGenKingFortress  extends WorldGenerator {
    private int spacing;
    private int separation;

    public WorldGenKingFortress() {
        this.spacing = WorldConfig.fortress_spacing;
        this.separation = 16;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        return new WorldGenKingFortress.Start(world, rand, chunkX, chunkZ);
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

        if (chunkX < 0) {
            chunkX -= this.spacing - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= this.spacing - 1;
        }

        int k = chunkX / this.spacing;
        int l = chunkZ / this.spacing;
        Random random = world.setRandomSeed(k, l, 50345840);
        k = k * this.spacing;
        l = l * this.spacing;
        k = k + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;
        l = l + (random.nextInt(this.spacing - this.separation) + random.nextInt(this.spacing - this.separation)) / 2;

        if (i == k && j == l && Math.abs(chunkX) > 35 && Math.abs(chunkZ) > 35) {
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
                return true;
            }
        }
        return false;
    }

    private static List<BiomeDictionary.Type> kingsFortressBiomeTypes;

    public static List<BiomeDictionary.Type> getSpawnBiomeTypes() {
        if(kingsFortressBiomeTypes == null) {
            kingsFortressBiomeTypes = Lists.newArrayList();

            for(String str : WorldConfig.biome_types_king_fortress) {
                try {
                    BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);

                    if (type != null) kingsFortressBiomeTypes.add(type);
                    else EELogger.logError("Biome Type" + str + " is not correct", new NullPointerException());
                } catch (Exception e) {
                    EELogger.logError(str + " is not a valid type name", e);
                }
            }
        }

        return kingsFortressBiomeTypes;
    }




    public static class Start extends StructureStart {
        public Start() {

        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, rand, chunkX, chunkZ);

        }

        private void create(World worldIn, Random rnd, int chunkX, int chunkZ) {
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

                    KingFortress stronghold = new KingFortress(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
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
            return components.size() > WorldConfig.fortress_size;
        }

    }
}
