package com.example.structure.world.api.trader;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.entity.trader.EntityAvalon;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import com.example.structure.world.WorldGenStructure;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenTraderOutcamp extends WorldGenStructure {


    public WorldGenTraderOutcamp(String structureName) {
        super(structureName);
    }


    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
            super.generateStructure(world, pos, Rotation.NONE);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("trader")) {
            EntityAvalon avalon = new EntityAvalon(world);
            avalon.setPosition(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            world.spawnEntity(avalon);
            world.setBlockToAir(pos);
        }
    }
}
