package com.example.structure.world.api.ashtower;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModRand;
import com.example.structure.util.handlers.BiomeRegister;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import org.lwjgl.Sys;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldGenAshTower extends WorldGenerator {

    public static List<Biome> VALID_BIOMES = Arrays.asList(Biomes.SKY);
    private int separation;

    /**
     * This is determined via how many times it attempts to spawn a tower and if the number is not met it will add on
     */
    private int spacing = 0;

    public WorldGenAshTower() {

    }

    public String getStructureName() {
        return "ash_towers";
    }

    public static int getGroundFromAbove(World world, int x, int z) {
        int y = 255;
        boolean foundGround = false;
        while (!foundGround && y-- >= 31) {
            Block blockAt = world.getBlockState(new BlockPos(x, y, z)).getBlock();
            foundGround = blockAt == Blocks.END_STONE;
        }

        return y;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {

        int yHieght = getGroundFromAbove(world, pos.getX() + 2, pos.getZ() + 2);
        int yHieghtAdjust = getGroundFromAbove(world, pos.getX() + 16, pos.getZ() + 16);
        if (yHieght > 57 && spacing > 150 && yHieghtAdjust > 57 ) { //&& world.getBiomeForCoordsBody(pos) == BiomeRegister.END_ASH_WASTELANDS

            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 150, pos.getZ() - 150, pos.getX() + 150, pos.getZ() + 150));
            return true;

        }
        System.out.println("Ashed Tower Spawn spacing at " + spacing);
        spacing++;
        return false;
    }


    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        spacing = 0;
        return new WorldGenAshTower.Start(world, rand, chunkX, chunkZ);
    }


    public static class Start extends StructureStart{
        public Start() {

        }

        public Start(World worldIn, Random rand, int chunkX, int chunkZ) {
        super(chunkX, chunkZ);
        this.create(worldIn, rand, chunkX, chunkZ);
        }

        protected void create(World worldIn, Random rnd, int chunkX, int chunkZ) {
            Random random = new Random(chunkX + chunkZ * 10387313L);
            int rand = random.nextInt(Rotation.values().length);

            BlockPos posI = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
            int yheight = getGroundFromAbove(worldIn, posI.getX(), posI.getZ());

            if(yheight > 57) {
                for (int i = 0; i < 4; i++) {
                    Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                    components.clear();
                    BlockPos blockpos = posI.add(0, yheight - 5, 0);
                    AshTower tower = new AshTower(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    tower.startBaseTower(blockpos, Rotation.NONE);
                    this.updateBoundingBox();

                    if (this.isSizeableStructure()) {
                        System.out.println("Popped is Sizeable");
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
            return components.size() > 4;
        }
    }

}
