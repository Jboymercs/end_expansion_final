package com.example.structure.entity.ai;

import com.example.structure.entity.EntityController;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class EntityAIRandomFly extends EntityAIBase {
    private final EntityController parentEntity;

    protected BlockPos posUpdate;
    public EntityAIRandomFly(EntityController e) {
        this.parentEntity = e;
        this.setMutexBits(3);
    }

    protected int stopMovingTimer = 120;

    @Override
    public boolean shouldExecute() {
        EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
        stopMovingTimer--;
        if(parentEntity.isInteract() || parentEntity.isHuntingBlock) {
            return false;
        }
        return parentEntity.getPosition() != posUpdate && stopMovingTimer < 180;

    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }

    @Override
    public void startExecuting() {
        Random random = this.parentEntity.getRNG();
        BlockPos Loc = parentEntity.getPosition();
        BlockPos Var = ModUtils.findGroundBelow(parentEntity.world, Loc);
        if(Var.getY() != 0) {
            if (Loc.getY() > (Var.getY() + 6)) {
                //This Declares whether the Entity is above it's fly limit to bring it's next point down
                double d0 = this.parentEntity.posX + ModRand.range(-10, 10);
                double d1 = (this.parentEntity.posY - ModRand.range(2, 5));
                double d2 = this.parentEntity.posZ + ModRand.range(-10, 10);
                this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
                posUpdate = new BlockPos(d0, d1, d2);
                stopMovingTimer = 120;
            } else {
                //Otherwise it will Continue in it's pseudo random Pathfinding
                double d0 = this.parentEntity.posX + ModRand.range(-10, 10);
                double d1 = this.parentEntity.posY + (random.nextFloat() * 2.0F - 1.0F) * 1.2F;
                double d2 = this.parentEntity.posZ + ModRand.range(-10, 10);
                this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
                posUpdate = new BlockPos(d0, d1, d2);
                stopMovingTimer = 120;
            }
        } else {
            double d1;
            double d0 = this.parentEntity.posX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            if(Loc.getY() > 45) {
                //Again Random Flying
                d1 =  this.parentEntity.posY + (random.nextFloat() * 2.0F - 1.0F) * 1.2F;
            }  else {
                //Bring it back up to Island Level
                d1 = this.parentEntity.posY + 5;
            }
            double d2 = this.parentEntity.posZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
            posUpdate = new BlockPos(d0, d1, d2);
            stopMovingTimer = 120;
        }
    }

    @Override
    public void updateTask() {
            stopMovingTimer++;
            Vec3d pos = ModUtils.getEntityVelocity(parentEntity).normalize().scale(0.1).add(parentEntity.getPositionVector());
            ModUtils.facePosition(pos, parentEntity, 45, 45);
            parentEntity.getLookHelper().setLookPosition(pos.x, pos.y, pos.z, 3, 3);
            super.updateTask();


    }

    public static BlockPos getHeight(Entity entityIn) {
        return entityIn.world.getHeight(entityIn.getPosition());
    }




}
