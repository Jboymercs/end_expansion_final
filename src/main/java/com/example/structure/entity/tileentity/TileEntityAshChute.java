package com.example.structure.entity.tileentity;

import com.example.structure.blocks.BlockAshChute;
import com.example.structure.util.ModRand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TileEntityAshChute extends TileEntity implements ITickable {


    public int randomShutoff = ModRand.range(100, 200);
    public boolean isActive = true;

    @Override
    public void update() {


        if(isActive) {
            EnumFacing facing = BlockAshChute.getFacing(this.getBlockMetadata());
            float distance = 0;
            float maxDistance = 16;

            for (distance = 1; distance <= maxDistance; distance++) {
                BlockPos pos = this.getPos().add(new BlockPos(facing.getXOffset() * distance, facing.getYOffset() * distance, facing.getZOffset() * distance));
                IBlockState block = world.getBlockState(pos);
                if (block.isFullBlock() || block.isFullCube() || block.isBlockNormalCube() || block.isSideSolid(world, pos, facing.getOpposite())
                        || block.isSideSolid(world, pos, facing)) {
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
                for (Entity entity : list) {
                    Vec3d vel = new Vec3d(facing.getDirectionVec()).scale(strength / Math.sqrt(entity.getDistanceSq(this.pos.add(0.5, 0.5, 0.5))));
                    vel.scale(1 / entity.getEntityBoundingBox().getAverageEdgeLength()); // Take into consideration the entity's size
                    entity.addVelocity(vel.x, vel.y, vel.z);
                    entity.fallDistance = 0;
                }
            }
        } else if(world.rand.nextInt(100) == 0) {

            randomShutoff = ModRand.range(100, 200);
            isActive = true;
        }

    }
}
