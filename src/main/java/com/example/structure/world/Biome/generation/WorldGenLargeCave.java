package com.example.structure.world.Biome.generation;

import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.EntityEnderEyeFly;
import com.example.structure.entity.EntitySnatcher;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import com.example.structure.util.ModRand;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenLargeCave extends WorldGenStructure {
    /**
     * Generates the Large Caves in the Ash Wastelands
     * @param structureName
     */
    public WorldGenLargeCave(String structureName) {
        super("ashbiome/" + structureName);
    }


    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        BlockPos modified = position.add(32, 0, 32);
        if(getGroundFromAbove(worldIn, position.getX(), position.getZ()) > 57 && getGroundFromAbove(worldIn, modified.getX(), modified.getZ()) > 57 &&
                !worldIn.isAirBlock(position) && !worldIn.isAirBlock(modified)) {
            return super.generate(worldIn, rand, position);

        }
        else {

            return false;
        }
    }

    WorldGenStructure[] fillerParts = {new CaveFiller("filler_1"), new CaveFiller("filler_2"),
            new CaveFiller("filler_3"),new CaveFiller("filler_4"),new CaveFiller("filler_5"),new CaveFiller("filler_6")
            ,new CaveFiller("filler_7"),new CaveFiller("filler_8"),new CaveFiller("filler_9"),new CaveFiller("filler_10"),
            new CaveFiller("filler_11"),new CaveFiller("filler_12")
    };

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {

        if(function.startsWith("filler") && random.nextInt(3) == 0) {
            WorldGenStructure fillerType = ModRand.choice(fillerParts);
            fillerType.generate(world, random, pos);
        }
        if(function.startsWith("mob") && generateMobSpawn() && !world.isAirBlock(pos.down())) {
            world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof tileEntityMobSpawner) {
                ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                        new MobSpawnerLogic.MobSpawnData[]{

                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntitySnatcher.class), 1),
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityChomper.class), 1)
                        },
                        new int[]{1, 2},
                        1,
                        24);
            }
        } else {
            world.setBlockToAir(pos);
        }
    }



    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 4) {
            return false;
        }
        return true;
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
}
