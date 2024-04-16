package com.example.structure.entity.tileentity;

import com.example.structure.init.ModBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityReverse extends TileEntity implements ITickable {


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
            TileEntity tileEntity = world.getTileEntity(pos1);
            TileEntity tileEntity2 = world.getTileEntity(pos2);
            TileEntity tileEntity3 = world.getTileEntity(pos3);
            TileEntity tileEntity4 = world.getTileEntity(pos4);
            TileEntity tileEntity5 = world.getTileEntity(pos5);
            TileEntity tileEntity6 = world.getTileEntity(pos6);

            //Change states if the power is off
            if (world.getBlockState(pos1) == ModBlocks.END_ASH_DOOR.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_OFF.getDefaultState());
            }
            if (world.getBlockState(pos2) == ModBlocks.END_ASH_DOOR.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_OFF.getDefaultState());
            }
            if (world.getBlockState(pos3) == ModBlocks.END_ASH_DOOR.getDefaultState() || world.getBlockState(pos3) == ModBlocks.POWER_SOURCE_OFF.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_OFF.getDefaultState());
            }
            if (world.getBlockState(pos4) == ModBlocks.END_ASH_DOOR.getDefaultState() || world.getBlockState(pos4) == ModBlocks.POWER_SOURCE_OFF.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_OFF.getDefaultState());
            }
            if (world.getBlockState(pos5) == ModBlocks.END_ASH_DOOR.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_OFF.getDefaultState());
            }
            if (world.getBlockState(pos6) == ModBlocks.END_ASH_DOOR.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.DOOR_REVERSAL_OFF.getDefaultState());
            }
            //Testing for who has source power
            //Testing for nearby DOOR Reversal OFF's




        }
    }


}
