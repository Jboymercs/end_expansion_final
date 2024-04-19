package com.example.structure.entity.ai.snatcher;

import com.example.structure.entity.EntitySnatcher;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModRand;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class EntityStalkAI<T extends EntitySnatcher & IAttack> extends EntityAIBase{
    private final T entity;
    private final double moveSpeedAmp;
    private final int attackCooldown;
    private final int stalkCooldown;
    private final float maxAttackDistSq;
    private int attackTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private boolean callforHelp;
    private int strafingTime = -1;
    private final float strafeAmount;

    private static final float STRAFING_STOP_FACTOR = 0.75f;
    private static final float STRAFING_BACKWARDS_FACTOR = 0.25f;
    private static final float STRAFING_DIRECTION_TICK = 20;
    private static final float STRAFING_DIRECTION_CHANGE_CHANCE = 0.3f;


    private float lookSpeed;
    public EntityStalkAI(T entity, double moveSpeedAltercation, int stalkCooldown, int attackCooldown, float maxAttackDistance, float strafeAmount, float maxStalkDistance, boolean callforReinforcements) {
        this.entity = entity;
        this.moveSpeedAmp = moveSpeedAltercation;
        this.attackCooldown = attackCooldown;
        this.maxAttackDistSq = maxAttackDistance;
        this.strafeAmount = strafeAmount;
        this.stalkCooldown = stalkCooldown;
        this.callforHelp = callforReinforcements;


    }
    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath());
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.attackTime = Math.max(attackTime, attackCooldown);
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.entity.getAttackTarget();

        if(currentlyHiding) {
            distanceAwayCounter--;
        }
        //Check for if it's currently in Hibernation
        if (target == null || this.entity.isCurrentlyinHibernation) {
            return;
        }
        double distSq = this.entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        boolean canSee = this.entity.getEntitySenses().canSee(target);
        Vec3d pos;
        if(shouldRunaway(target) && !currentlyHiding && !this.entity.iAmPissedOff) {
            this.entity.playSpottedAnim();
            this.entity.getNavigator().clearPath();
            currentlyHiding = true;
        }
        if(currentlyHiding && !this.entity.spottedATorch) {
            if(stalkCooldown < distanceAwayCounter) {
                Vec3d away = this.entity.getPositionVector().subtract(target.getPositionVector()).normalize();
                pos = this.entity.getPositionVector().add(away.scale(4)).add(ModRand.randVec().scale(4));
                this.entity.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, this.moveSpeedAmp * 2);
                this.entity.getLookHelper().setLookPositionWithEntity(target, this.lookSpeed, this.lookSpeed);
            }

            if(!canSee) {
                this.entity.getNavigator().clearPath();
                this.entity.startHibernation();
                this.currentlyHiding =false;
                distanceAwayCounter = 600;
            }
            if(stalkCooldown > distanceAwayCounter) {
                currentlyHiding = false;
                distanceAwayCounter = 600;
            }
        }
        else if(!this.entity.spottedATorch) {
            move(target, distSq, canSee);
        }


        if (distSq <= this.maxAttackDistSq && canSee) {
            this.attackTime--;
            if (this.attackTime <= 0) {
                this.attackTime = this.entity.startAttack(target, (float) distSq, this.strafingBackwards);
            }
        }
    }

    public boolean shouldRunaway(EntityLivingBase player) {
        Vec3d vec3d = player.getLook(1.0F).normalize();
        Vec3d vec3d1 = new Vec3d(this.entity.posX - player.posX, this.entity.getEntityBoundingBox().minY + (double)this.entity.getEyeHeight() - (player.posY + (double)player.getEyeHeight()), this.entity.posZ - player.posZ);
        double d0 = vec3d1.length();
        vec3d1 = vec3d1.normalize();
        double d1 = vec3d.dotProduct(vec3d1);
        return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this.entity) : false;
    }

    protected int distanceAwayCounter = 600;
    protected boolean currentlyHiding = false;
    //We want there to be a way to keep these guys away
    public void move(EntityLivingBase target, double distSq, boolean canSee) {
        if (distSq <= this.maxAttackDistSq && canSee) {
            this.entity.iAmPissedOff = true;
            this.entity.getNavigator().clearPath();
            ++this.strafingTime;
        } else {
            if(this.entity.iAmPissedOff) {
                this.entity.getNavigator().tryMoveToEntityLiving(target, this.moveSpeedAmp * 2.4);
            }
             else {
                this.entity.getNavigator().tryMoveToEntityLiving(target, this.moveSpeedAmp);
            }
            this.strafingTime = -1;
        }

        if (this.strafingTime >= STRAFING_DIRECTION_TICK) {
            if ((double) this.entity.getRNG().nextFloat() < STRAFING_DIRECTION_CHANGE_CHANCE) {
                this.strafingClockwise = !this.strafingClockwise;
            }

            if ((double) this.entity.getRNG().nextFloat() < STRAFING_DIRECTION_CHANGE_CHANCE) {
                this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
        }

        if (this.strafingTime > -1) {
            if (distSq > this.maxAttackDistSq * STRAFING_STOP_FACTOR) {
                this.strafingBackwards = false;
            } else if (distSq < this.maxAttackDistSq * STRAFING_BACKWARDS_FACTOR) {
                this.strafingBackwards = true;
            }

            this.entity.getMoveHelper().strafe((this.strafingBackwards ? -1 : 1) * this.strafeAmount, (this.strafingClockwise ? 1 : -1) * this.strafeAmount);
            this.entity.faceEntity(target, this.lookSpeed, this.lookSpeed);
        } else {
            this.entity.getLookHelper().setLookPositionWithEntity(target, this.lookSpeed, this.lookSpeed);
        }
    }
}
