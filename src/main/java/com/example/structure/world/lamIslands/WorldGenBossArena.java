package com.example.structure.world.lamIslands;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenBossArena extends WorldGenLamentedIslands{

    public WorldGenBossArena() {
        super("islands/boss_arena", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {

        //Platform Chance
        if(function.startsWith("e_platform")) {
        if(doesPlatformGenerate()) {
            new WorldGenEastPlatform().generateStructure(world, pos.add(new BlockPos(0, 1, 0)), Rotation.NONE);
            world.setBlockToAir(pos);
        } else {
            world.setBlockToAir(pos);
        }
        }
        if(function.startsWith("s_platform")) {
            if(doesPlatformGenerate()) {
                new WorldGenSouthPlatform().generateStructure(world, pos.add(new BlockPos(0, 1, 0)), Rotation.NONE);
                world.setBlockToAir(pos);
            }
            else {
                world.setBlockToAir(pos);
            }
        }
        if(function.startsWith("n_platform")) {
            if(doesPlatformGenerate()) {
                new WorldGenNorthPlatform().generateStructure(world, pos.add(new BlockPos(0, 1, 0)), Rotation.NONE);
                world.setBlockToAir(pos);
            } else {
                world.setBlockToAir(pos);
            }

        }

        //Tiles
        if(function.startsWith("tile")) {
            new WorldGenTile().generateStructure(world, pos.add(new BlockPos(0, 1, 0)), Rotation.NONE);
            world.setBlockToAir(pos);
        }

        if(function.startsWith("special_tile")) {
            new WorldGenSpecialTile().generateStructure(world, pos.add(new BlockPos(0, 1, 0)), Rotation.NONE);
            world.setBlockToAir(pos);
        }

        //Mob Spawns
        if(function.startsWith("mob")) {
            if(generateMobSpawn() && ModConfig.constructor_center_spawn) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if(tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBuffker.class),  1)
                            },
                            new int[]{1},
                            1,
                            24);
                }
            }
            else {
                world.setBlockToAir(pos);
            }
        }

    }
}
