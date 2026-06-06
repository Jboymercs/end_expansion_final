package com.jboymercs.endexpansion.world.api.mines;

import com.jboymercs.endexpansion.config.WorldConfig;
import com.jboymercs.endexpansion.entity.EntityEnderKnight;
import com.jboymercs.endexpansion.entity.knighthouse.EntityEnderShield;
import com.jboymercs.endexpansion.entity.tileentity.MobSpawnerLogic;
import com.jboymercs.endexpansion.entity.tileentity.tileEntityMobSpawner;
import com.jboymercs.endexpansion.init.ModBlocks;
import com.jboymercs.endexpansion.init.ModEntities;
import com.jboymercs.endexpansion.util.ModRand;
import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.world.misc.ModStructureTemplate;
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

public class MinesTemplate extends ModStructureTemplate {

    public MinesTemplate() {

    }

    //CHANGE ME LATER
    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "ash_mines");


    public MinesTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
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
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderKnight.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityEnderShield.class), 1)
                            },
                            new int[]{5, 1},
                            1,
                            24);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }

        else if(function.startsWith("chest")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawn()  && sbb.isVecInside(blockPos)) {

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
    }


    //Generator for Mob Spawns
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= WorldConfig.ashed_mines_mob_spawns) {
            return false;
        }
        return true;
    }

    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= WorldConfig.ashed_mines_chest_spawns) {
            return false;
        }
        return true;
    }

    @Override
    public String templateLocation() {
        return "mines";
    }
}
