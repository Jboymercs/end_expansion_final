package com.jboymercs.endexpansion.entity.tileentity;

import com.jboymercs.endexpansion.blocks.BlockDungeonDoor;
import com.jboymercs.endexpansion.blocks.IBlockUpdater;
import com.jboymercs.endexpansion.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TileEntityDoorStart extends TileEntity implements ITickable {

    private static final int SCAN_RANGE = 10;
    private static final int BARRIER_LENGTH = 8;

    @Override
    public void update() {

        if (world.isRemote && this.getBlockType() instanceof IBlockUpdater) {
            ((IBlockUpdater) this.getBlockType()).update(world, pos);
        }

        EnumFacing facing = BlockDungeonDoor.getFacing(this.getBlockMetadata());
        boolean triggered = (this.getBlockMetadata() & 8) > 0;
        boolean nearbyActivatedBlocks = hasNearbyActivatedBlocks();

        updateBarriers(facing, triggered && !nearbyActivatedBlocks);


    }

    private boolean hasNearbyActivatedBlocks() {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= SCAN_RANGE; i++) {
            if (isActivationOrPower(checkPos.setPos(pos.getX() + i, pos.getY(), pos.getZ()))
                    || isActivationOrPower(checkPos.setPos(pos.getX() - i, pos.getY(), pos.getZ()))
                    || isActivationOrPower(checkPos.setPos(pos.getX(), pos.getY() + i, pos.getZ()))
                    || isActivationOrPower(checkPos.setPos(pos.getX(), pos.getY() - i, pos.getZ()))
                    || isActivationOrPower(checkPos.setPos(pos.getX(), pos.getY(), pos.getZ() + i))
                    || isActivationOrPowerWithoutReversalOff(checkPos.setPos(pos.getX(), pos.getY(), pos.getZ() - i))) {
                return true;
            }
        }
        return false;
    }

    private boolean isActivationOrPower(BlockPos checkPos) {
        IBlockState state = world.getBlockState(checkPos);
        return state == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()
                || state == ModBlocks.DOOR_REVERSAL_OFF.getDefaultState()
                || state == ModBlocks.POWER_SOURCE_ON.getDefaultState();
    }

    private boolean isActivationOrPowerWithoutReversalOff(BlockPos checkPos) {
        IBlockState state = world.getBlockState(checkPos);
        return state == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()
                || state == ModBlocks.POWER_SOURCE_ON.getDefaultState();
    }

    private void updateBarriers(EnumFacing facing, boolean shouldPlace) {
        BlockPos.MutableBlockPos barrierPos = new BlockPos.MutableBlockPos();
        IBlockState barrier = ModBlocks.END_BARRIER.getDefaultState();
        int xOffset = facing.getXOffset();
        int yOffset = facing.getYOffset();
        int zOffset = facing.getZOffset();

        for (int i = 1; i <= BARRIER_LENGTH; i++) {
            barrierPos.setPos(pos.getX() + xOffset * i, pos.getY() + yOffset * i, pos.getZ() + zOffset * i);
            IBlockState state = world.getBlockState(barrierPos);

            if (shouldPlace) {
                if (canPlaceBarrier(state, barrierPos, facing)) {
                    if (state != barrier) {
                        world.setBlockState(barrierPos, barrier);
                    }
                } else if (facing.equals(EnumFacing.SOUTH)) {
                    return;
                }
            } else if (state == barrier) {
                world.setBlockToAir(barrierPos);
            }
        }
    }

    private boolean canPlaceBarrier(IBlockState state, BlockPos barrierPos, EnumFacing facing) {
        return !state.isFullBlock()
                && !state.isBlockNormalCube()
                && !state.isFullCube()
                && !state.isSideSolid(world, barrierPos, facing.getOpposite());
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
        return nbttagcompound;
    }

}
