package com.example.structure.world.Biome.bridgestructure;

import com.example.structure.util.MapGenModStructure;
import com.example.structure.world.Biome.WorldChunkGeneratorEE;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;
import scala.reflect.internal.Trees;

import java.util.Random;

public class MapGenBridgeStructure extends MapGenModStructure {
    WorldChunkGeneratorEE provider;
    protected World world;

    public MapGenBridgeStructure(int spacing, int offset, int odds, WorldChunkGeneratorEE provider) {
        super(spacing, offset, odds);
        this.provider = provider;
    }

    @Override
    public String getStructureName() {
        return "Bridge Ruins";
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenBridgeStructure.Start(this.world, this.rand, chunkX, chunkZ);
    }

    public static class Start extends StructureStart {
        public Start() {

        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ) {
            super(chunkX, chunkZ);
            this.create(worldIn, random, chunkX, chunkZ);
        }

        private void create(World worldIn, Random rnd, int chunkX, int chunkZ) {
            Random random = new Random(chunkX + chunkZ * 10387313);
            Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            int y = 95;

            BlockPos blockpos = new BlockPos(chunkX * 16 + 8, y, chunkZ * 16 + 8);
           BridgeStructure.startFortress(worldIn, worldIn.getSaveHandler().getStructureTemplateManager(), blockpos, rotation, this.components, rnd);
            this.updateBoundingBox();
        }
    }
}
