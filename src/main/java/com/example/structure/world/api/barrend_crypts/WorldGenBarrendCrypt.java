package com.example.structure.world.api.barrend_crypts;

import com.example.structure.config.WorldConfig;
import com.example.structure.init.ModBlocks;
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

public class WorldGenBarrendCrypt extends WorldGenerator {

    private int spacing = 0;


    public WorldGenBarrendCrypt() {

    }

    public String getStructureName() {
        return "barrend_crypt";
    }


    @Override
    public boolean generate(World world, Random random, BlockPos pos) {
        int yHieght = getGroundFromAbove(world, pos.getX() + 2, pos.getZ() + 2);
        int yHieghtAdjust = getGroundFromAbove(world, pos.getX() + 11, pos.getZ() + 11);
        if(yHieght > 58 && yHieghtAdjust > 58 && spacing > WorldConfig.bare_crypts_spacing && yHieght < 70 && yHieghtAdjust < 70) {
            getStructureStart(world, pos.getX() >> 4, pos.getZ() >> 4, random).generateStructure(world, random, new StructureBoundingBox(pos.getX() - 200, pos.getZ() - 200, pos.getX() + 200, pos.getZ() + 200));
            return true;
        }
        spacing++;
        return false;
    }


    public static int getGroundFromAbove(World world, int x, int z)
    {
        int y = 71;
        boolean foundGround = false;
        while(!foundGround && y-- >= 48)
        {
            Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            foundGround =  blockAt == Blocks.END_STONE || blockAt == ModBlocks.BARE_SANS;
        }

        return y;
    }


    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random rand) {
        spacing = 0;
        return new WorldGenBarrendCrypt.Start(world, rand , chunkX, chunkZ);
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
            if(yHeight > 58) {
                for (int i = 0; i < 4; i++) {
                    Rotation rotation = Rotation.values()[(rand + i) % Rotation.values().length];
                    components.clear();

                    BlockPos blockpos = posI.add(0, yHeight - 9, 0);
                    BarrendCrypts crypts = new BarrendCrypts(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    crypts.startCrypt(blockpos, Rotation.NONE);
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
            return components.size() > 0;
        }


    }


}
