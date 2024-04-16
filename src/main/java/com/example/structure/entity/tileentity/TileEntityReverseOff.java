package com.example.structure.entity.tileentity;

import com.example.structure.init.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityReverseOff extends TileEntity implements ITickable {

    public boolean isOn;
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
            if (world.getBlockState(pos1) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState() || world.getBlockState(pos1) == ModBlocks.POWER_SOURCE_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_ON.getDefaultState());
            }
            if (world.getBlockState(pos2) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState() || world.getBlockState(pos2) == ModBlocks.POWER_SOURCE_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_ON.getDefaultState());
            }
            if (world.getBlockState(pos3) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState() || world.getBlockState(pos3) == ModBlocks.POWER_SOURCE_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_ON.getDefaultState());
            }
            if (world.getBlockState(pos4) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState() || world.getBlockState(pos4) == ModBlocks.POWER_SOURCE_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_ON.getDefaultState());
            }
            if (world.getBlockState(pos5) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState() || world.getBlockState(pos5) == ModBlocks.POWER_SOURCE_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_ON.getDefaultState());
            }
            if (world.getBlockState(pos6) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState() || world.getBlockState(pos6) == ModBlocks.POWER_SOURCE_ON.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_ON.getDefaultState());
            }




        }
    }
}
