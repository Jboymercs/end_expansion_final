package com.example.structure.entity.tileentity.source;

import com.example.structure.blocks.BlockPowerSource;
import com.example.structure.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TileEntityNoSource extends TileEntity implements ITickable {
    @Override
    public void update() {
        BlockPos posOriginal = this.getPos();
        for (int i = 0; i <= 10; i++) {
            BlockPos pos1 = posOriginal.add(new BlockPos(i, 0, 0));
            BlockPos pos2 = posOriginal.add(new BlockPos(-i, 0, 0));
            BlockPos pos3 = posOriginal.add(new BlockPos(0, i, 0));
            BlockPos pos4 = posOriginal.add(new BlockPos(0, -i, 0));
            BlockPos pos5 = posOriginal.add(new BlockPos(0, 0, i));
            BlockPos pos6 = posOriginal.add(new BlockPos(0, 0, -i));

            if (world.getBlockState(pos1) == ModBlocks.DOOR_REVERSAL_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.POWER_SOURCE_ON.getDefaultState());
            }
            if (world.getBlockState(pos2) == ModBlocks.DOOR_REVERSAL_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.POWER_SOURCE_ON.getDefaultState());
            }
            if (world.getBlockState(pos5) == ModBlocks.DOOR_REVERSAL_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.POWER_SOURCE_ON.getDefaultState());
            }
            if (world.getBlockState(pos6) == ModBlocks.DOOR_REVERSAL_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.POWER_SOURCE_ON.getDefaultState());
            }
        }
    }

}
