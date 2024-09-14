package com.example.structure.entity;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.ActionDrawWalls;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.util.IAttack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class EntityEye extends EntityModBase implements IAnimatable, IAttack {
    private static final String ANIM_IDLE = "idle";
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityEye(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(3.0f, 3.0f);
        this.setImmovable(true);
        this.noClip = true;
        this.isImmuneToFire = true;

    }

    public EntityEye(World worldIn) {
        super(worldIn);
        this.setSize(3.0f, 3.0f);
        this.setImmovable(true);
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.attack_damage);
    }


    @Override
    protected boolean canDropLoot() {
        return false;
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
    public int fireBallTimer = 50;

    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase target = this.getAttackTarget();

        if(target != null) {
            //Fireball Counter
            if(fireBallTimer <= 0) {
                new ActionDrawWalls().performAction(this, target);
                fireBallTimer = 50;
            } else {
                fireBallTimer--;
            }


        }
        if(ticksExisted == 400) {
            this.setDead();
        }
    }


    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.0, 0, 1F, 0.3f));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityCrystalKnight>(this, EntityCrystalKnight.class, 1, true, false, null));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        return 0;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
