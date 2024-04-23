package com.example.structure.world.Biome.generation;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.EntityEnderEyeFly;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenSmallCaves extends WorldGenStructure {
    /**
     * Generates small caves that will hold ruins and mini-structures within, this is a test fabric to hopefully add more onto this biome
     * @param name
     */


    //CHANGE ME LATER
    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "ash_caves");
    public WorldGenSmallCaves(String name) {
        super("ashbiome/" + name);
    }


    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        BlockPos modified = position.add(15, 0, 15);
        if(getGroundFromAbove(worldIn, position.getX(), position.getZ()) > 50 && getGroundFromAbove(worldIn, modified.getX(), modified.getZ()) > 50 &&
        !worldIn.isAirBlock(position) && !worldIn.isAirBlock(modified)) {
            return super.generate(worldIn, rand, position);

        }
        else {
            return false;
        }
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {

        if(function.startsWith("chest")) {
            BlockPos blockPos = pos.down();
            if(generateChestSpawn()) {

                TileEntity tileEntity = world.getTileEntity(blockPos);
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

        else if(function.startsWith("mob") && generateMobSpawn()) {
            world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER_ASH.getDefaultState(), 2);
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof tileEntityMobSpawner) {
                ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                        new MobSpawnerLogic.MobSpawnData[]{
                                //PlaceHolder
                                new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityChomper.class), 1)
                        },
                        new int[]{1},
                        1,
                        35);
            }
        } else {
            world.setBlockToAir(pos);
        }
    }


    public boolean generateMobSpawn() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if (randomNumberGenerator >= ModConfig.cave_spawn_rate) {
            return false;
        }
        return true;
    }

    //Generator for Chests
    public boolean generateChestSpawn() {
        int randomNumberChestGenerator = ModRand.range(0, 5);
        if(randomNumberChestGenerator >= ModConfig.cave_chest_chance) {
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
