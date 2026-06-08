package com.jboymercs.endexpansion.entity.tileentity;

import com.jboymercs.endexpansion.blocks.IBlockUpdater;
import com.jboymercs.endexpansion.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TileEntityReverse extends TileEntity implements ITickable {

    private static final int SCAN_RANGE = 10;

    @Override
    public void update() {

        if (world.isRemote && this.getBlockType() instanceof IBlockUpdater) {
            ((IBlockUpdater) this.getBlockType()).update(world, pos);
        }
        if (hasDoorOrPowerOffNearby()) {
            world.setBlockState(pos, ModBlocks.DOOR_REVERSAL_OFF.getDefaultState());
        }
    }

    private boolean hasDoorOrPowerOffNearby() {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= SCAN_RANGE; i++) {
            if (isEndAshDoor(checkPos.setPos(pos.getX() + i, pos.getY(), pos.getZ()))
                    || isEndAshDoor(checkPos.setPos(pos.getX() - i, pos.getY(), pos.getZ()))
                    || isEndAshDoorOrPowerOff(checkPos.setPos(pos.getX(), pos.getY() + i, pos.getZ()))
                    || isEndAshDoorOrPowerOff(checkPos.setPos(pos.getX(), pos.getY() - i, pos.getZ()))
                    || isEndAshDoor(checkPos.setPos(pos.getX(), pos.getY(), pos.getZ() + i))
                    || isEndAshDoor(checkPos.setPos(pos.getX(), pos.getY(), pos.getZ() - i))) {
                return true;
            }
        }
        return false;
    }

    private boolean isEndAshDoor(BlockPos checkPos) {
        return world.getBlockState(checkPos) == ModBlocks.END_ASH_DOOR.getDefaultState();
    }

    private boolean isEndAshDoorOrPowerOff(BlockPos checkPos) {
        IBlockState state = world.getBlockState(checkPos);
        return state == ModBlocks.END_ASH_DOOR.getDefaultState()
                || state == ModBlocks.POWER_SOURCE_OFF.getDefaultState();
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
