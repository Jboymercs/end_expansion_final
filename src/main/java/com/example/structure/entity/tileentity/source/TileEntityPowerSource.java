package com.example.structure.entity.tileentity.source;

import com.example.structure.init.ModBlocks;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;


import javax.annotation.Nullable;

public class TileEntityPowerSource extends TileEntity implements ITickable {

   protected BlockPos savedPos;
    public boolean haveSource;

    protected int timer = 10;
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

            if (world.getBlockState(pos1) == ModBlocks.DOOR_REVERSAL_OFF.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.POWER_SOURCE_OFF.getDefaultState());
            }
            if (world.getBlockState(pos2) == ModBlocks.DOOR_REVERSAL_OFF.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.POWER_SOURCE_OFF.getDefaultState());
            }
            if (world.getBlockState(pos5) == ModBlocks.DOOR_REVERSAL_OFF.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.POWER_SOURCE_OFF.getDefaultState());
            }
            if (world.getBlockState(pos6) == ModBlocks.DOOR_REVERSAL_OFF.getDefaultState()) {
                world.setBlockState(posOriginal, ModBlocks.POWER_SOURCE_OFF.getDefaultState());
            }
        }
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.savedPos, 1, this.getUpdateTag());
    }





}
