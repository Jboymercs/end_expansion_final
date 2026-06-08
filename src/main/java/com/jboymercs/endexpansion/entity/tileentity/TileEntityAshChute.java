package com.jboymercs.endexpansion.entity.tileentity;

import com.jboymercs.endexpansion.blocks.BlockAshChute;
import com.jboymercs.endexpansion.util.ModRand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityAshChute extends TileEntity implements ITickable {


    public int randomShutoff = ModRand.range(100, 200);
    public boolean isActive = true;

    @Override
    public void update() {


        if(isActive) {
            EnumFacing facing = BlockAshChute.getFacing(this.getBlockMetadata());
            int distance;
            int maxDistance = 16;
            BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

            for (distance = 1; distance <= maxDistance; distance++) {
                checkPos.setPos(pos.getX() + facing.getXOffset() * distance, pos.getY() + facing.getYOffset() * distance, pos.getZ() + facing.getZOffset() * distance);
                IBlockState block = world.getBlockState(checkPos);
                if (block.isFullBlock() || block.isFullCube() || block.isBlockNormalCube() || block.isSideSolid(world, checkPos, facing.getOpposite())
                        || block.isSideSolid(world, checkPos, facing)) {
                    break;
                }

            }

            if(randomShutoff == 1) {
                isActive = false;

            } else {
                randomShutoff--;
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
                    double scale = strength / Math.sqrt(entity.getDistanceSq(centerX, centerY, centerZ));
                    entity.addVelocity(facing.getXOffset() * scale, facing.getYOffset() * scale, facing.getZOffset() * scale);
                    entity.fallDistance = 0;
                }
            }
        } else if(world.rand.nextInt(100) == 0) {

            randomShutoff = ModRand.range(100, 200);
            isActive = true;
        }

    }
}
