package com.example.structure.world.api.lamentedIslands;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.seekers.EndSeeker;
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

public class LamentedIslandsTemplate extends ModStructureTemplate {

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "end_dungeon");
    private static final ResourceLocation KEY_LOOT = new ResourceLocation(ModReference.MOD_ID, "end_key_dungeon");

    public LamentedIslandsTemplate() {

    }

    public LamentedIslandsTemplate(TemplateManager manager, String type, BlockPos pos, Rotation rot, int distance, boolean overWriteIn) {
        super(manager, type, pos,distance, rot, overWriteIn);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random, StructureBoundingBox sbb) {

        //Mobs
        //Constructor's and maybe the Shulker?
        if(function.startsWith("mob")) {
            if (generateMobSpawn()) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBuffker.class), 1),
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EndSeeker.class), 1)
                            },
                            new int[]{4, 1},
                            1,
                            24);
                }
            } else {
                world.setBlockToAir(pos);
            }
        }
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
    }


    //Generator for Mob Spawns
    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= ModConfig.structure_spawns) {
            return false;
        }
        return true;
    }

    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= ModConfig.lamentedIslandsLootChance) {
            return false;
        }
        return true;
    }

    @Override
    public String templateLocation() {
        return "islands";
    }
}
