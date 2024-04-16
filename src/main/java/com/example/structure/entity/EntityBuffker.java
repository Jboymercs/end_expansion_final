package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.ActionGolemQuake;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityBuffker extends EntityAbstractBuffker implements IAnimatable, IAttack, IAnimationTickable {

    private final String IDLE_ANIM = "idle";
    private final String MOVING_ARMS_ANIM = "movingArms";
    private final String MOVING_LEGS_ANIM = "movingLegs";
    private final String BLINK_ANIM = "peak";
    private final String SHOOT_ANIM = "shoot";
    private final String SHOCKWAVE_ANIM = "shockWave";

    private Consumer<EntityLivingBase> prevAttack;

    public EntityBuffker(World worldIn) {
        super(worldIn);
    }



    public int blinkCoolDown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase target = this.getAttackTarget();
        //Random Peak Animation that plays
        if(rand.nextInt(5) == 0 && blinkCoolDown > 120 && target == null) {

            this.setBlinkMode(true);
            addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 10);
            addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 25);
            addEvent(()-> this.setBlinkMode(false), 30);
            blinkCoolDown = 0;
        } else {
            blinkCoolDown++;
        }

        if(this.lockLook) {

            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;

        }

    }

    @Override
    public boolean getCanSpawnHere() {
        // Middle end island check
        if (this.world.provider.getDimension() == 1  && this.world.rand.nextInt(6) == 0) {
            return ModConfig.does_spawn_middle || this.posX > 500 || this.posX < -500 || this.posZ > 500 || this.posZ < -500;
        }

        return super.getCanSpawnHere();
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);

        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(mortarFire, groundSlam));
            double[] weights = {
                    (distance > 7) ? distance * 0.02 : 0, // Mortar Attack
                    (distance < 7) ? 1/distance : 0 // Ground Slam

            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return (prevAttack == mortarFire) ? constructor_cooldown_two : constructor_cooldown_one;
    }

    private final Consumer<EntityLivingBase> mortarFire = (target) -> {
        this.setFightMode(true);
        this.setShootAttack(true);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 5);
        addEvent(()-> {
            //Left Hand
            for(int i = 0; i < 20; i += 10) {
                addEvent(()-> {
                    this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0f, 0.8f);
                    EntityShulkerBullet projectile = new EntityShulkerBullet(this.world, this, target, EnumFacing.Axis.Y);
                    Vec3d startPosition = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.6, 0.8)));
                    Vec3d velocity = target.getPositionVector().subtract(startPosition).normalize().scale(0.5);
                    ModUtils.setEntityVelocity(projectile, velocity);
                    projectile.setPosition(startPosition.x, startPosition.y, startPosition.z);
                    this.world.spawnEntity(projectile);


                }, i);
            }
        }, 20);
        addEvent(()-> {
            //Right Hand
            for(int i = 0; i < 20; i += 10) {
                addEvent(()-> {
                    this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0f, 0.8f);
                    EntityShulkerBullet projectile = new EntityShulkerBullet(this.world, this, target, EnumFacing.Axis.Y);
                    Vec3d startPosition = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.6, -0.8)));
                    Vec3d velocity = target.getPositionVector().subtract(startPosition).normalize().scale(0.5);
                    ModUtils.setEntityVelocity(projectile, velocity);
                    projectile.setPosition(startPosition.x, startPosition.y, startPosition.z);
                    this.world.spawnEntity(projectile);
                }, i);
            }
        }, 25);

        addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 65);
        addEvent(()-> {
            this.setFightMode(false);
            this.setShootAttack(false);
        }, 70);
    };

    private final Consumer<EntityLivingBase> groundSlam = (target) -> {
        this.setFightMode(true);
        this.setShockwaveAttack(true);
        this.setImmovable(true);
        addEvent(()-> this.lockLook = true, 10);
        //Attack Status
        addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 3);
        addEvent(()-> {
            new ActionGolemQuake().performAction(this, target);
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.7f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
        }, 25);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 46);
        addEvent(()-> {
            this.setImmovable(false);
            this.setShockwaveAttack(false);
            this.setFightMode(false);
            this.lockLook = false;
        }, 50);
    };

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "animPeak", 0, this::predicatePeak));
        animationData.addAnimationController(new AnimationController(this, "moveIdle", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "armsMover", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attackHandler", 0, this::predicateAttack));
    }
    //The Peak animation shulkers do
    private<E extends IAnimatable> PlayState predicatePeak(AnimationEvent<E> event) {
        if(this.isBlinkMode() && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(BLINK_ANIM, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    //Handles movement of the Legs and Idle Animation
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isShockWaveAttack()) {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(MOVING_LEGS_ANIM, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE_ANIM, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    //Handles specifically movement of the Arms when moving and not attacking
    private <E extends IAnimatable>PlayState predicateArms(AnimationEvent<E> event) {
        if(event.isMoving() && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(MOVING_ARMS_ANIM, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if (this.isShootAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SHOOT_ANIM, false));
            return PlayState.CONTINUE;
        }
        if(this.isShockWaveAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SHOCKWAVE_ANIM, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.0, constructor_cooldown_one, 10F, 0.3f));
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
