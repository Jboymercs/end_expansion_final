package com.example.structure.world.islands;

import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenOutpost extends WorldGenStructure {

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "outpost");

    public WorldGenOutpost() {
        super("ocean/outpost");
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("chest") && generateChestSpawn()) {
            TileEntity tileEntity = world.getTileEntity(pos.down());
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            if (tileEntity instanceof TileEntityChest) {
                TileEntityChest chest = (TileEntityChest) tileEntity;
                chest.setLootTable(LOOT, random.nextLong());
            }

        } else {
            world.setBlockToAir(pos);
            world.setBlockToAir(pos.down());
        }

        if(function.startsWith("mob") && generateMobSpawn()) {
            world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof tileEntityMobSpawner) {
                ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                        new MobSpawnerLogic.MobSpawnData[]{
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderKnight.class), 1),
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderShield.class), 1)
                        },
                        new int[]{1, 1},
                        1,
                        24);
            }
        } else {
            world.setBlockToAir(pos);
        }
    }


    //A Random configuration that will base if a creature will spawn with the given tag upon spawning the structure
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 6) {
            return false;
        }
        return true;
    }

    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= 3) {
            return false;
        }
        return true;
    }
}
