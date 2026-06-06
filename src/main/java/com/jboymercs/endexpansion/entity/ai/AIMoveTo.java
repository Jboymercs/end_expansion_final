package com.jboymercs.endexpansion.entity.ai;

import com.jboymercs.endexpansion.util.ModRand;
import com.jboymercs.endexpansion.util.ModUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class AIMoveTo<T extends EntityLiving> extends EntityAIBase {

    private final T entity;


    private @Nullable Vec3d planeVectorPath = getNewPlaneVector();

    private final float moveRadius;

    public AIMoveTo(T entity, float moveRadius) {
        this.entity = entity;
        this.moveRadius = moveRadius;
        setMutexBits(3);
    }
    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null;
    }


    @Override
    public void updateTask() {
        if(entity.getAttackTarget() != null) {
            Vec3d target = entity.getAttackTarget().getPositionVector();
            Vec3d nextPointToFollow = getNextPoint(target);
            Vec3d direction = nextPointToFollow.subtract(entity.getPositionVector()).normalize();
            double speed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            ModUtils.addEntityVelocity(entity, direction.scale(0.025f * speed));

            if (!hasClearPath(nextPointToFollow) || lineBlocked(nextPointToFollow, target)) {
                planeVectorPath = getNewPlaneVector();
            }

            ModUtils.facePosition(nextPointToFollow, entity, 10, 10);
            entity.getLookHelper().setLookPosition(nextPointToFollow.x, nextPointToFollow.y, nextPointToFollow.z, 3, 3);
        }

        super.updateTask();
    }

    private boolean hasClearPath(Vec3d nextPointToFollow) {
        AxisAlignedBB box = entity.getEntityBoundingBox();
        return !lineBlocked(box.maxX, box.maxY, box.maxZ, nextPointToFollow)
                && !lineBlocked(box.maxX, box.maxY, box.minZ, nextPointToFollow)
                && !lineBlocked(box.maxX, box.minY, box.maxZ, nextPointToFollow)
                && !lineBlocked(box.maxX, box.minY, box.minZ, nextPointToFollow)
                && !lineBlocked(box.minX, box.maxY, box.maxZ, nextPointToFollow)
                && !lineBlocked(box.minX, box.maxY, box.minZ, nextPointToFollow)
                && !lineBlocked(box.minX, box.minY, box.maxZ, nextPointToFollow)
                && !lineBlocked(box.minX, box.minY, box.minZ, nextPointToFollow);
    }

    private boolean lineBlocked(double x, double y, double z, Vec3d nextPointToFollow) {
        return lineBlocked(new Vec3d(x, y, z), nextPointToFollow);
    }

    private boolean lineBlocked(Vec3d start, Vec3d nextPointToFollow) {
        RayTraceResult rayTraceResult = entity.world.rayTraceBlocks(start, nextPointToFollow, false, true, false);
        return rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK;
    }

    private Vec3d getNewPlaneVector() {
        return ModUtils.Y_AXIS.add(ModRand.randVec().scale(2)).normalize();
    }

    private Vec3d getNextPoint(Vec3d center) {
        Vec3d circlePointInWorld = entity.getPositionVector().add(ModRand.randVec());
        Vec3d entityVelocity = ModUtils.getEntityVelocity(entity);
        Vec3d entityDirection = entity.getPositionVector().subtract(center);
        double rotationDegrees = 15 * entityVelocity.length();
        for(int i = 0; i < moveRadius; i++) {
            Vec3d point = getCirclePoint(center, entityDirection, rotationDegrees, i);
            if (hasClearPath(point)) {
                return point;
            }
            circlePointInWorld = point;
            planeVectorPath = getNewPlaneVector();

            point = getCirclePoint(center, entityDirection, rotationDegrees, -i);
            if (hasClearPath(point)) {
                return point;
            }
            circlePointInWorld = point;
            planeVectorPath = getNewPlaneVector();
        }
        return circlePointInWorld;
    }

    private Vec3d getCirclePoint(Vec3d center, Vec3d entityDirection, double rotationDegrees, int radiusOffset) {
        Vec3d projectedEntityDirection = ModUtils.planeProject(entityDirection, planeVectorPath).normalize().scale(moveRadius + radiusOffset);
        return ModUtils.rotateVector2(projectedEntityDirection, planeVectorPath, rotationDegrees).add(center);
    }
}
