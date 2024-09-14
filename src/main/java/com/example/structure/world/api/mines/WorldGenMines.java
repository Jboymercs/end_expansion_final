package com.example.structure.world.api.mines;

import com.example.structure.config.ModConfig;
import com.example.structure.config.WorldConfig;
import com.example.structure.init.ModBlocks;
import com.example.structure.world.WorldGenStructure;
import com.example.structure.world.api.vaults.EndVaults;
import com.example.structure.world.api.vaults.WorldGenEndVaults;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

public class WorldGenMines extends WorldGenerator {

    private int spacing = 0;

    public WorldGenMines() {

    }

    public String getStructureName() {
        return "mines";
    }


    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 255;
        boolean foundGround = false;
        while(!foundGround && y-- >= 31)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == ModBlocks.END_ASH || blockAt == ModBlocks.BROWN_END_STONE;
        }

        return y;
    }


    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        int yHieght2 = getGroundFromAbove(world, pos.getX(), pos.getZ());
        int yHieght = getGroundFromAbove(world, pos.getX() + 15, pos.getZ() + 15);

        if(yHieght > 60 && yHieght2 > 60 && spacing >= WorldConfig.ashed_mines_distance) {
            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 200, pos.getZ() - 200, pos.getX() + 200, pos.getZ() + 200));
        }
        spacing++;
        return false;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        spacing = 0;
        return new WorldGenMines.Start(world, rand , chunkX, chunkZ);
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

            BlockPos posI = new BlockPos(chunkX * 16 + 8, 29, chunkZ * 16 + 8);
            int yHeight = getGroundFromAbove(worldIn,posI.getX(), posI.getZ());
            if(yHeight > 60) {
                for (int i = 0; i < 4; i++) {
                    components.clear();

                    AshedMines mines = new AshedMines(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    mines.startMines(posI, Rotation.NONE);
                    this.updateBoundingBox();


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
            return components.size() > WorldConfig.ashed_mines_size;
        }
    }
}
