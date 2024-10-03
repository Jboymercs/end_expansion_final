package com.example.structure.entity.barrend;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.endking.EntityAbstractEndKing;
import com.example.structure.entity.endking.ghosts.EntityGhostPhase;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.init.ModPotions;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntityBarrendMob extends EntityModBase {


    public EntityBarrendMob(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityBarrendMob(World worldIn) {
        super(worldIn);
    }


    private int checkForEnemies = 60;

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase taret = this.getAttackTarget();
        if(!world.isRemote && taret == null && checkForEnemies < 0) {
            double distance = this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue();
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(distance), e -> !e.getIsInvulnerable());

            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase entity: nearbyEntities) {
                    if(entity != this) {
                        if(entity instanceof EntityPlayer) {
                            if(entity.isPotionActive(ModPotions.MADNESS) && !((EntityPlayer) entity).isCreative() && !((EntityPlayer) entity).isSpectator()) {
                                if(entity.isEntityAlive()) {
                                    this.setAttackTarget(entity);
                                    this.checkForEnemies = 300;
                                }
                            }
                        } else {
                            if(entity.isEntityAlive() && entity.isPotionActive(ModPotions.MADNESS)) {
                                this.setAttackTarget(entity);
                                this.checkForEnemies = 300;
                            } else {
                                this.checkForEnemies = 60;
                            }
                        }
                    }
                }
                this.checkForEnemies = 60;
            }

        } else {
            checkForEnemies--;
        }
    }


    public static final Predicate<Entity> CAN_TARGET = entity -> {

        return !(entity instanceof EntityBarrendMob);
    };

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!CAN_TARGET.apply(source.getTrueSource())) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

}
