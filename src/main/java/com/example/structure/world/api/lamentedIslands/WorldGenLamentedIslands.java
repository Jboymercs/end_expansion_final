package com.example.structure.world.api.lamentedIslands;

import com.example.structure.config.ModConfig;
import com.example.structure.init.ModBlocks;
import com.example.structure.world.api.mines.AshedMines;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

public class WorldGenLamentedIslands extends WorldGenerator {
    private int spacing = 0;

    public WorldGenLamentedIslands() {

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
        spacing = 0;
        return new WorldGenLamentedIslands.Start(world, rand, chunkX, chunkZ);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos) {

        if((spacing/2) > ModConfig.structureFrequency) {
            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 350, pos.getZ() - 350, pos.getX() + 350, pos.getZ() + 350));
        }
        spacing++;
        return false;
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
            return components.size() > ModConfig.islands_size;
        }
    }
}
