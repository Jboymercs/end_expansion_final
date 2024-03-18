package com.example.structure.entity;

import com.example.structure.entity.endking.EntityEndKing;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityWall extends EntityModBase implements IAnimatable {

    private static final String ANIM_IDLE = "rise";
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityWall(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0f, 3.0f);
        this.setImmovable(true);
        this.noClip = true;
        this.setNoAI(true);
    }

    @Override
    protected boolean canDropLoot() {
        return false;
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }


    public EntityWall(World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 3.0f);
        this.setImmovable(true);
        this.noClip = true;
        this.setNoAI(true);
        this.isImmuneToFire = true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(ticksExisted < 20 || ticksExisted > 190) {
            this.motionX = 0;
            this.motionZ = 0;
        }

        this.rotationPitch = 0;
        this.rotationYaw = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;


        if(ticksExisted == 20) {
            this.noClip = false;
            this.setImmovable(false);
        }

        if(ticksExisted == 190) {
            this.noClip = true;
        }
        if(ticksExisted > 20 && ticksExisted < 190) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityEndKing)));
            if(!targets.isEmpty()) {
                for (EntityLivingBase entitiesWithin : targets) {
                    this.blockUsingShield(entitiesWithin);
                }
            }
        }
        if(ticksExisted == 200) {
            this.setDead();
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
