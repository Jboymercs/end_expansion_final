package com.example.structure.entity.tileentity;

import com.example.structure.blocks.IBlockUpdater;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityDeactivate extends TileEntity implements ITickable {

    @Override
    public void update() {
        if (world.isRemote && this.getBlockType() instanceof IBlockUpdater) {
            ((IBlockUpdater) this.getBlockType()).update(world, pos);
        }
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
