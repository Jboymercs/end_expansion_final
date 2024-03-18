package com.example.structure.world.api.structures;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.EntityKnightLord;
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
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class FortressTemplate extends ModStructureTemplate {

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "fortress");
    private static final ResourceLocation BOX_LOOT = new ResourceLocation(ModReference.MOD_ID, "dungeon_box");

    private static final ResourceLocation KEY_LOOT = new ResourceLocation(ModReference.MOD_ID, "fortress_key");

    private boolean hasGeneratedKeyChest = false;

    public FortressTemplate() {

    }

    public FortressTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
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

        //Key Chest
        if(function.startsWith("key_chest")) {
            BlockPos blockPos = pos.down();
            if(!hasGeneratedKeyChest && sbb.isVecInside(blockPos)) {

                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if(tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(KEY_LOOT, rand.nextLong());
                    hasGeneratedKeyChest = true;
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }

        //Shulker Boxes
        else if(function.startsWith("box")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawn() && sbb.isVecInside(blockPos)) {

                TileEntity tileEntity = world.getTileEntity(blockPos);
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityShulkerBox) {
                    TileEntityShulkerBox chest = (TileEntityShulkerBox) tileEntity;
                    chest.setLootTable(BOX_LOOT, rand.nextLong());
                }
            } else {
                world.setBlockToAir(pos);
                world.setBlockToAir(pos.down());
            }
        }

        //Single Mob Spawn
       else if(function.startsWith("mob")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderKnight.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderShield.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderMage.class), 1)
                            },
                            new int[]{1, 1, 1},
                            1,
                            30);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }


        else if(function.startsWith("healer")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderMage.class), 1)
                            },
                            new int[]{1},
                            1,
                            30);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }

        //Group Mob Spawn
        else if(function.startsWith("group")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderKnight.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderShield.class), 1)
                            },
                            new int[]{1, 1},
                            3,
                            30);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }

        //Mini-boss
        else if(function.startsWith("miniboss")) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityKnightLord.class), 1)

                            },
                            new int[]{1},
                            1,
                            12);
                }
        }
    }


    //Generator for Mob Spawns
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= ModConfig.dungeon_mob_chance) {
            return false;
        }
        return true;
    }
    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= ModConfig.dungeon_chest_chance) {
            return false;
        }
        return true;
    }

    @Override
    public String templateLocation() {
        return "king_fortress";
    }
}
