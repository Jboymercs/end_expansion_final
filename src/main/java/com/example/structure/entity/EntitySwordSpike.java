package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.arrow.EntityModArrow;
import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.endking.EntityGroundSword;
import com.example.structure.entity.endking.EntityRedCrystal;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntitySwordSpike extends EntityModBase implements IAnimatable {


    private final String ANIM_SUMMON = "summon";

    public EntitySwordSpike(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.7F, 1.5F);
        this.setNoAI(true);
        this.noClip = true;
        this.setNoGravity(true);
    }

    public EntitySwordSpike(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 1.5F);
        this.setNoAI(true);
        this.noClip = true;
        this.setNoGravity(true);
    }

    public EntityPlayer owner;

    public EntityLivingBase targetToo;
    public EntitySwordSpike(World worldIn, EntityPlayer owner, EntityLivingBase target) {
        super(worldIn);
        this.owner = owner;
        this.targetToo = target;
        this.setSize(0.7F, 1.5F);
        this.setNoAI(true);
        this.noClip = true;
        this.setNoGravity(true);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(ticksExisted <= 5 && targetToo != null) {
            Vec3d targetPos = targetToo.getPositionVector();
            ModUtils.setEntityPosition(this,targetPos);
        } else {
            this.setImmovable(true);
            this.motionX = 0;
            this.motionZ = 0;
            this.rotationYaw = 0;
            this.rotationPitch = 0;
            this.rotationYawHead = 0;
            this.renderYawOffset = 0;
        }

        List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e == owner)));
        if(this.ticksExisted > 5 && this.ticksExisted <= 20) {
            if(!targets.isEmpty()) {
                for(EntityLivingBase base : targets) {
                    if (base != owner) {
                        Vec3d pos = base.getPositionVector().add(ModUtils.yVec(0.5));
                        DamageSource source = ModDamageSource.builder()
                                .type(ModDamageSource.MOB)
                                .directEntity(this).disablesShields()
                                .build();
                        float damage = (float) (8.0F * ModConfig.cordium_armor_scale);
                        ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, pos, source, 0.7F, 0, false);
                    }
                }
            }

        }

        if(this.ticksExisted > 25) {
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
        data.addAnimationController(new AnimationController(this, "controller_prok_attack", 0, this::predicateAttack));
    }

    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
