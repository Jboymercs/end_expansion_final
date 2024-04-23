package com.example.structure.entity.endking;

import com.example.structure.entity.EntityModBase;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityLargeAOEEffect extends EntityModBase implements IAnimatable {

    private final String ANIM_SUMMON = "summon";

    public EntityLargeAOEEffect(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setImmovable(true);
        this.noClip = true;
        this.setNoGravity(true);
        this.setSize(2.0F, 1.5F);
        this.setNoAI(true);
    }

    public EntityLargeAOEEffect(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.noClip = true;
        this.setNoGravity(true);
        this.setSize(2.0F, 1.5F);
        this.setNoAI(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(this.ticksExisted > 35) {
            this.setDead();
        }
    }

    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
        return PlayState.CONTINUE;
    }


    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void initEntityAI() {

        ModUtils.removeTaskOfType(this.tasks, EntityAILookIdle.class);
        ModUtils.removeTaskOfType(this.tasks, EntityAISwimming.class);
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller_sword_attack", 0, this::predicateAttack));
    }

    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
