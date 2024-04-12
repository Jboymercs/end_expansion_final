package com.example.structure.entity.ai;


import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class EntityAISupport extends EntityAIBase {

    private final EntityEnderMage supporter;
    private final double movementSpeed;
    private final double heightAboveGround;
    private final double supportDistance;
    private final double supportCooldown;

    private double cooldown;

    public EntityAISupport(EntityEnderMage creature, double movementSpeed, double heightAboveGround, double supportDistance, double supportCooldown) {
        this.supportCooldown = supportCooldown;
        this.supportDistance = supportDistance;
        this.supporter = creature;
        this.movementSpeed = movementSpeed;
        this.heightAboveGround = heightAboveGround;
        this.setMutexBits(3);
    }
    @Override
    public boolean shouldExecute() {
        return supporter.isCloseTooAllies;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }


    @Override
    public void resetTask() {
        super.resetTask();
        this.cooldown = this.supportCooldown;
    }


    @Override
    public void updateTask() {
        super.updateTask();

        Vec3d groupCenter = ModUtils.findEntityGroupCenter(this.supporter, supporter.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue());
        boolean hasGroup = groupCenter.squareDistanceTo(this.supporter.getPositionVector()) != 0;

        /**
         * Provide support to the nearest mobs
         */
        EntityLivingBase optimalMob = null;
        double health = 2;
        for (EntityLivingBase entity : ModUtils.getEntitiesInBox(supporter, new AxisAlignedBB(supporter.getPosition()).grow(supporter.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()))) {
            if (!EntityKnightBase.CAN_TARGET.apply(entity) || entity instanceof EntityEnderShield && entity.getHealth() / entity.getMaxHealth() < health && this.supporter.getDistanceSq(entity) < Math.pow(supportDistance, 2)) {
                optimalMob = entity;
                health = entity.getHealth() / entity.getMaxHealth();
            }
        }

        if (optimalMob != null && hasGroup) {
            cooldown--;
            /**
             * Face the closest mob
             */
            this.supporter.faceEntity(optimalMob, 25, 25);
            this.supporter.getLookHelper().setLookPositionWithEntity(optimalMob, 25, 25);

            /**
             * Provide support if close enough
             */
            if (this.cooldown <= 0 && !supporter.isFightMode()) {
                this.supporter.attackEntityWithRangedAttack(optimalMob, (float) this.supporter.getDistanceSq(optimalMob));
                this.cooldown = supportCooldown;
            }
            else if( this.cooldown > 60 && !supporter.isFightMode()) {
                EntityLivingBase target = this.supporter.getAttackTarget();
                if(target != null) {
                    this.supporter.startAttack(target, 16F, false);
                }
            }

            Vec3d pos = groupCenter.add(ModUtils.yVec((float) (this.heightAboveGround + ModRand.getFloat(0.5f) * this.heightAboveGround)));
            this.supporter.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, this.movementSpeed);
        } else {
            /**
             * Move towards the target, which is the center of the group
             */
            Vec3d pos;

            if (hasGroup) {
                pos = groupCenter.add(ModUtils.yVec((float) (this.heightAboveGround + ModRand.getFloat(0.5f) * this.heightAboveGround)));
            }
            /**
             * Run away from the attack target if there are no mobs to support nearby
             */
            else if (this.supporter.getAttackTarget() != null) {
                EntityLivingBase target = this.supporter.getAttackTarget();
                double healthValue = this.supporter.getHealth() / this.supporter.getMaxHealth();
                if(target != null && healthValue < 0.5 ) {
                    this.supporter.startAttack(target, 16F, true);
                }
                Vec3d away = this.supporter.getPositionVector().subtract(this.supporter.getAttackTarget().getPositionVector()).normalize();
                pos = this.supporter.getPositionVector().add(away.scale(4)).add(ModRand.randVec().scale(4));
            }
            /**
             * There is no target and no mobs to support, slowly float down
             */
            else {
                pos = this.supporter.getPositionVector().add(ModUtils.yVec(0.01));
            }

            this.supporter.getNavigator().tryMoveToXYZ(pos.x, pos.y, pos.z, this.movementSpeed);
            this.supporter.getLookHelper().setLookPosition(pos.x, pos.y, pos.z, 3, 3);
            ModUtils.facePosition(pos, this.supporter, 10, 10);
        }
    }
}
