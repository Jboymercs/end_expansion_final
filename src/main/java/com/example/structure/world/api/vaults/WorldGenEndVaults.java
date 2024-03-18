package com.example.structure.world.api.vaults;

import com.example.structure.config.ModConfig;
import com.example.structure.util.handlers.BiomeRegister;
import com.example.structure.world.lamIslands.WorldGenLamentedIslands;
import git.jbredwards.nether_api.mod.common.world.gen.ChunkGeneratorTheEnd;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import org.lwjgl.Sys;
import scala.reflect.api.Internals;

import javax.annotation.Nonnull;
import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldGenEndVaults extends WorldGenerator {
    public static List<Biome> VALID_BIOMES = Arrays.asList(Biomes.SKY);
    private int separation;
    private int spacing = 0;

    public WorldGenEndVaults() {

    }



    public String getStructureName() {
        return "end_vault";
    }

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


    @Override
    public boolean generate(World world, Random random, BlockPos pos) {

        int yHieght = getGroundFromAbove(world, pos.getX() + 2, pos.getZ() + 2);
        int yHieghtAdjust = getGroundFromAbove(world, pos.getX() + 16, pos.getZ() + 16);
        if(yHieght > 57 && spacing > ModConfig.vault_distance && yHieghtAdjust > 57) {



            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 100, pos.getZ() - 100, pos.getX() + 100, pos.getZ() + 100));
            return true;

        }
        spacing++;
        return false;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        spacing = 0;
        return new WorldGenEndVaults.Start(world, rand , chunkX, chunkZ);
    }

    public static class Start extends StructureStart {

        private boolean valid;

        public Start(){

        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, rand, chunkX, chunkZ);
        }

        private void create(World worldIn, Random rnd, int chunkX, int chunkZ) {
                Random random = new Random(chunkX + chunkZ * 10387313L);
                int rand = random.nextInt(Rotation.values().length);

                BlockPos posI = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
                int yHeight = getGroundFromAbove(worldIn,posI.getX(), posI.getZ());
                if(yHeight > 57) {
                    for (int i = 0; i < 4; i++) {
                        Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                        components.clear();

                        BlockPos blockpos = posI.add(0, yHeight - 14, 0);
                        EndVaults stronghold = new EndVaults(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                        stronghold.startVault(blockpos, Rotation.NONE);
                        this.updateBoundingBox();

                        this.valid = true;
                        if (this.isSizeableStructure()) {

                            break;
                        }

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
            return components.size() > ModConfig.vault_size;
        }


    }

}
