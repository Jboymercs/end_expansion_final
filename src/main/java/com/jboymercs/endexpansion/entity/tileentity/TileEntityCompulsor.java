package com.jboymercs.endexpansion.entity.tileentity;

import com.jboymercs.endexpansion.blocks.BlockCompulsorOn;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityCompulsor extends TileEntity implements ITickable {


    @Override
    public void update() {
        boolean triggered = (this.getBlockMetadata() & 8) > 0;
        if (!triggered) {
            return;
        }

        EnumFacing facing = BlockCompulsorOn.getFacing(this.getBlockMetadata());
        int maxDistance = 16;
        int distance;
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

        // Take into consideration any blocks in front of the fan
        for (distance = 1; distance <= maxDistance; distance++) {
            checkPos.setPos(pos.getX() + facing.getXOffset() * distance, pos.getY() + facing.getYOffset() * distance, pos.getZ() + facing.getZOffset() * distance);
            IBlockState block = world.getBlockState(checkPos);
            if (block.isFullBlock() || block.isFullCube() || block.isBlockNormalCube() || block.isSideSolid(world, checkPos, facing.getOpposite())
                    || block.isSideSolid(world, checkPos, facing)) {
                break;
            }

        }

        double strength = facing.getYOffset() != 0 ? 0.5 : 0.3;
        AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(facing.getXOffset() * distance, facing.getYOffset() * distance,
                facing.getZOffset() * distance);
        List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, box);

        if (list != null) {
            double centerX = this.pos.getX() + 0.5;
            double centerY = this.pos.getY() + 0.5;
            double centerZ = this.pos.getZ() + 0.5;
            for (Entity entity : list) {
                //Vec3d vel = new Vec3d(facing.getDirectionVec()).scale(strength / Math.sqrt(entity.getDistanceSq(this.pos.add(0.5, 0.5, 0.5))));

                double scale = -strength / Math.sqrt(entity.getDistanceSq(centerX, centerY, centerZ));

                entity.addVelocity(facing.getXOffset() * scale, facing.getYOffset() * scale, facing.getZOffset() * scale);
                entity.fallDistance = 0;
            }
        }
    }
}
