package com.jboymercs.endexpansion.entity.tileentity;

import com.jboymercs.endexpansion.blocks.IBlockUpdater;
import com.jboymercs.endexpansion.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityReverseOff extends TileEntity implements ITickable {

    private static final int SCAN_RANGE = 10;

    public boolean isOn;
    @Override
    public void update() {
        if (world.isRemote && this.getBlockType() instanceof IBlockUpdater) {
            ((IBlockUpdater) this.getBlockType()).update(world, pos);
        }
        if (hasActivationOrPowerOnNearby()) {
            world.setBlockState(pos, ModBlocks.DOOR_REVERSAL_ON.getDefaultState());
        }
    }

    private boolean hasActivationOrPowerOnNearby() {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= SCAN_RANGE; i++) {
            if (isActivationOrPowerOn(checkPos.setPos(pos.getX() + i, pos.getY(), pos.getZ()))
                    || isActivationOrPowerOn(checkPos.setPos(pos.getX() - i, pos.getY(), pos.getZ()))
                    || isActivationOrPowerOn(checkPos.setPos(pos.getX(), pos.getY() + i, pos.getZ()))
                    || isActivationOrPowerOn(checkPos.setPos(pos.getX(), pos.getY() - i, pos.getZ()))
                    || isActivationOrPowerOn(checkPos.setPos(pos.getX(), pos.getY(), pos.getZ() + i))
                    || isActivationOrPowerOn(checkPos.setPos(pos.getX(), pos.getY(), pos.getZ() - i))) {
                return true;
            }
        }
        return false;
    }

    private boolean isActivationOrPowerOn(BlockPos checkPos) {
        IBlockState state = world.getBlockState(checkPos);
        return state == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()
                || state == ModBlocks.POWER_SOURCE_ON.getDefaultState();
    }
}
