package com.example.structure.world.api.ashtower;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.barrend.EntityBarrendGolem;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.seekers.EndSeeker;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import com.example.structure.util.ModRand;
import com.example.structure.world.misc.ModStructureTemplate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class AshTowerTemplate extends ModStructureTemplate {

    public AshTowerTemplate() {

    }

    public AshTowerTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //Single Mob Spawn
       if(function.startsWith("mob")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderShield.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderKnight.class), 1)
                            },
                            new int[]{1, 4},
                            1,
                            16);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }


        if(function.startsWith("boss")) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBarrendGolem.class), 1)
                            },
                            new int[]{1},
                            1,
                            16);
                }
        }

    }


    //Generator for Mob Spawns
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= 5) {
            return false;
        }
        return true;
    }
    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= 3) {
            return false;
        }
        return true;
    }


    @Override
    public String templateLocation() {
        return "ash_towers";
    }
}
