package com.example.structure.world.api.vaults;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityEnderEyeFly;
import com.example.structure.entity.seekers.EndSeeker;
import com.example.structure.entity.seekers.EndSeekerPrime;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.world.misc.ModStructureTemplate;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class VaultTemplate extends ModStructureTemplate {

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "vault");

    public VaultTemplate() {

    }

    public VaultTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox sbb) {
        //Regular Chests
        if(function.startsWith("chest")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawn() && sbb.isVecInside(blockPos)) {

                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(LOOT, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }

        //Single Mob Spawn
        else if(function.startsWith("mob")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBuffker.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EndSeeker.class), 1)
                            },
                            new int[]{1, 3},
                            1,
                            16);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }


        //Mini-boss Spawn
        else if(function.startsWith("boss")) {

                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EndSeekerPrime.class), 1)
                            },
                            new int[]{1},
                            1,
                            8);
                }
             else {
                world.setBlockToAir(pos);
            }
        }

        //Ender Eye Spawns
        else if(function.startsWith("ambient")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderEyeFly.class), 1)

                            },
                            new int[]{1},
                            1,
                            16);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }
    }



    //Generator for Mob Spawns
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= ModConfig.vault_mob_chance) {
            return false;
        }
        return true;
    }
    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= ModConfig.vault_loot_chance) {
            return false;
        }
        return true;
    }
    @Override
    public String templateLocation() {
        return "end_vault";
    }
}
