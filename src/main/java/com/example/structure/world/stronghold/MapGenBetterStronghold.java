package com.example.structure.world.stronghold;


import com.example.structure.config.ModConfig;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

public class MapGenBetterStronghold extends MapGenStronghold {




    public MapGenBetterStronghold() {


    }

    @Override
    public String getStructureName() {
        return "stronghold";
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenBetterStronghold.Start(this.world, rand, chunkX, chunkZ);
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

            BlockPos posI = new BlockPos(chunkX * 16 + 8, 6, chunkZ * 16 + 8);


                for (int i = 0; i < 4; i++) {
                    components.clear();

                    BetterStronghold stronghold = new BetterStronghold(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), components);
                    stronghold.startStronghold(posI, Rotation.NONE);
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
            return components.size() > ModConfig.stronghold_size;
        }
    }
}
