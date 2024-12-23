package com.example.structure.entity.shadowPlayer.ai;

import com.example.structure.entity.shadowPlayer.EntityShadowPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIShadowHurtByTarget extends EntityAITarget {

    EntityShadowPlayer tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAIShadowHurtByTarget(EntityShadowPlayer theDefendingTameableIn)
    {
        super(theDefendingTameableIn, false);
        this.tameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute()
    {
            EntityLivingBase entitylivingbase = this.tameable.getOwner();

            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.attacker = entitylivingbase.getRevengeTarget();
                int i = entitylivingbase.getRevengeTimer();
                return i != this.timestamp && this.isSuitableTarget(this.attacker, false);
            }

    }

    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if (entitylivingbase != null)
        {
            this.timestamp = entitylivingbase.getRevengeTimer();
        }

        super.startExecuting();
    }
}
