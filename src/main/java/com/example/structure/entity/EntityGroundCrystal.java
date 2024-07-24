package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
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

public class EntityGroundCrystal extends EntityModBase implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_CRYSTAL = "summon";

    public EntityGroundCrystal(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.9f, 2.0f);

    }

    EntityLivingBase doNotTarget;
    public EntityGroundCrystal(World worldIn, EntityLivingBase doNotTarget) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.9f, 2.0f);
        this.doNotTarget = doNotTarget;
    }


    public EntityGroundCrystal(World worldIn, float x, float y, float z) {
        super(worldIn, x, y ,z);
        this.setPosition(x, y, z);
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(0.9f, 2.0f);

    }

    //this is a nerf to the shield summoning crystals
    int crystal_cooldown = 25;


    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;

        if(doNotTarget != null) {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!( e instanceof EntityGroundCrystal)));

            //Specifically used for the shield
            if(!targets.isEmpty() && crystal_cooldown >= 30) {
                for(EntityLivingBase target: targets) {
                    if(target != doNotTarget) {
                        Vec3d pos = this.getPositionVector().add(ModUtils.yVec(0.7));
                        DamageSource source = ModDamageSource.builder()
                                .type(ModDamageSource.MOB)
                                .directEntity(this)
                                .build();
                        float damage = this.getAttack();
                        ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, pos, source, 0.2F, 0, false );
                        crystal_cooldown = 0;
                    }
                }
            } else {
                crystal_cooldown++;
            }
        } else {
            List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityCrystalKnight || e instanceof EntityGroundCrystal)));

            if(!targets.isEmpty()) {
                Vec3d pos = this.getPositionVector().add(ModUtils.yVec(0.7));
                DamageSource source = ModDamageSource.builder()
                        .type(ModDamageSource.MOB)
                        .directEntity(this)
                        .build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, pos, source, 0.2F, 0, false );
            }
        }

        if (ticksExisted == 1) {
            playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f / (getRNG().nextFloat() * 0.04f + 0.8f));
        }
        if(ticksExisted > 70) {
            this.setDead();
        }

    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.ground_crystal_damage * ModConfig.lamented_multiplier);
    }

    @Override
    protected void initEntityAI() {

        ModUtils.removeTaskOfType(this.tasks, EntityAILookIdle.class);
        ModUtils.removeTaskOfType(this.tasks, EntityAISwimming.class);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
       animationData.addAnimationController(new AnimationController(this, "controller_spike", 0, this::predicateAttack));
    }

    private<E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CRYSTAL, false));
        return PlayState.CONTINUE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_GLASS_BREAK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {return SoundEvents.BLOCK_GLASS_HIT;}

    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
