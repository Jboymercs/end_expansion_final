package com.example.structure.world.lamIslands;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import com.example.structure.util.ModReference;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenSpecialTile extends WorldGenLamentedIslands{
    /**
     * Specific class to ensure that the Lamented Islands generates a key chest somewhere
     */
    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "end_dungeon");
    private static final ResourceLocation KEY_LOOT = new ResourceLocation(ModReference.MOD_ID, "end_key_dungeon");
    public WorldGenSpecialTile() {
        super("islands/tile_3", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        //Chests
        if(function.startsWith("chest")) {
            if(generateChestSpawn()) {
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
        }

        if(function.startsWith("key_chest")) {
                TileEntity tileEntity = world.getTileEntity(pos.down());
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(KEY_LOOT, random.nextLong());
                }
        }
        //Mobs
        if(function.startsWith("mob")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBuffker.class), 1)
                            },
                            new int[]{1},
                            1,
                            24);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }
    }
}
