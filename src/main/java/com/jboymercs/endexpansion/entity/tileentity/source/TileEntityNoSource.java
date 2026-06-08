package com.jboymercs.endexpansion.entity.tileentity.source;

import com.jboymercs.endexpansion.blocks.IBlockUpdater;
import com.jboymercs.endexpansion.init.ModBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TileEntityNoSource extends TileEntity implements ITickable {

    private static final int SCAN_RANGE = 10;

    @Override
    public void update() {
        if (world.isRemote && this.getBlockType() instanceof IBlockUpdater) {
            ((IBlockUpdater) this.getBlockType()).update(world, pos);
        }
        if (hasDoorReversalOnNearby()) {
            world.setBlockState(pos, ModBlocks.POWER_SOURCE_ON.getDefaultState());
        }
    }

    private boolean hasDoorReversalOnNearby() {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= SCAN_RANGE; i++) {
            if (isDoorReversalOn(checkPos.setPos(pos.getX() + i, pos.getY(), pos.getZ()))
                    || isDoorReversalOn(checkPos.setPos(pos.getX() - i, pos.getY(), pos.getZ()))
                    || isDoorReversalOn(checkPos.setPos(pos.getX(), pos.getY(), pos.getZ() + i))
                    || isDoorReversalOn(checkPos.setPos(pos.getX(), pos.getY(), pos.getZ() - i))) {
                return true;
            }
        }
        return false;
    }

    private boolean isDoorReversalOn(BlockPos checkPos) {
        return world.getBlockState(checkPos) == ModBlocks.DOOR_REVERSAL_ON.getDefaultState();
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
