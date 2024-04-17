package com.example.structure.entity.barrend;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAIBarrendGolem;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class EntityBarrendGolem extends EntityAbstractBarrendGolem implements IAnimatable, IAttack {


    private Consumer<EntityLivingBase> prevAttack;
    private final AnimationFactory factory = new AnimationFactory(this);

    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";

    private final String ANIM_WORM_IDLE = "idle_worms";

    //Worm Animations
    private final String ANIM_HIDE_BACK_WORM = "no_worm_back";
    private final String ANIM_SHOW_BACK_WORM = "worm_back";

    private final String ANIM_HIDE_LEFT_WORM = "no_worm_left";
    private final String ANIM_SHOW_LEFT_WORM = "worm_left";

    private final String ANIM_HIDE_RIGHT_WORM = "no_worm_right";
    private final String ANIM_SHOW_RIGHT_WORM = "worm_right";

    //Attack Animations
    private final String ANIM_SWING_LEFT = "swing_left";
    private final String ANIM_SWING_RIGHT = "swing_right";
    private final String ANIM_PARASITE_ATTACK = "parasite_strike";
    private final String ANIM_SLAM = "slam";
    private final String ANIM_LEAP_SLAM = "leap_slam";

    //Charge Attack Animations
    private final String ANIM_PREPARE_CHARGE = "prepare_charge";
    private final String ANIM_CHARGE = "charge";
    private final String ANIM_COLLIDE = "collide";

    //Summoning and other things
    private final String ANIM_IDLE_STILL = "still";
    private final String ANIM_AWAKEN = "awaken";

    public EntityBarrendGolem(World worldIn) {
        super(worldIn);
        this.setStill(true);
        addEvent(()-> {
            this.setNoAI(true);
            this.setImmovable(true);
            }, 20);
    }

    protected int chargeTick = 140;

    protected int verticalMovementTick = 0;
    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.isParasiteAttack() || this.isCollide() || this.isPrepareCharge()) {
            this.motionY = 0;
            this.motionX = 0;
            this.motionZ = 0;
        }

        //Minor Helper with jumping
        if(this.isLeapSlam()) {
            verticalMovementTick++;

            //Bring the entity quicker to the ground in case it is still in the air
            if(verticalMovementTick >= 38) {
                this.motionY--;
            }


            if(verticalMovementTick > 45) {
                this.setImmovable(true);
            }

        }

        if(this.isCharge()) {
            //Damages Entities while this is Charging
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    Vec3d offset = base.getPositionVector().add(ModUtils.getRelativeOffset(base, new Vec3d(0.3, 0.3, 0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = (float) ((ModConfig.barrend_golem_attack_damage * ModConfig.barrend_golem_attack_multiplier) * ModConfig.biome_multiplier);
                    ModUtils.handleAreaImpact(0.7f, (e)-> damage, this, offset, source, 1.2f, 0, false);
                }
            }

            AxisAlignedBB boxForBlocks = this.getEntityBoundingBox().grow(1.1D, 0.2D, 1.1D).offset(0, 0.2, 0);
            ModUtils.destroySpecificBlocks(boxForBlocks, world, this);
            //This Forces the Entity into that Direction at a Constant Speed
            if(chargePos != null) {
                BlockPos asPos = new BlockPos(chargePos.x, chargePos.y, chargePos.z);
                this.getNavigator().tryMoveToXYZ(asPos.getX(), asPos.getY(), asPos.getZ(), 2.3D);
                this.getLookHelper().setLookPosition(asPos.getX(), this.getPositionVector().y + 2, asPos.getZ(), 3, 3);
                ModUtils.facePosition(chargePos, this, 10, 10);
            }

            if(chargeTick < 100) {
                //States to Change the Charge to Collide
                AxisAlignedBB box = this.getEntityBoundingBox().grow(1.2D, 0.5D, 1.2D).offset(0, 1.5, 0);
                if(ModUtils.collisionNearby(box, world, this)) {
                    this.setCharge(false);
                    this.navigator.clearPath();
                    this.lockLook = true;
                    playFinishChargedMove();
                }
            }

            if(chargeTick < 0) {
                this.setCharge(false);
                this.navigator.clearPath();
                this.lockLook = true;
                playFinishChargedMove();
            } else {
                chargeTick--;
            }
        }

        if(this.lockLook) {

            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;

        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(2, 50, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                Vec3d vel = pos.normalize().scale(0.5).add(ModUtils.yVec(0.0));
                Vec3d currentPos = this.getPositionVector().add(ModUtils.yVec(0.1));
                world.spawnParticle(EnumParticleTypes.CLOUD, currentPos.x, currentPos.y, currentPos.z, vel.x, vel.y, vel.z);
                //ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 0.1, 0))), ModColors.RED, pos.normalize().scale(0.5).add(ModUtils.yVec(0)));
            });
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {

        }
    }

    public void playFinishChargedMove() {
        this.setCollide(true);
        this.chargeTick = 140;
        this.setImmovable(true);


        addEvent(()-> {
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.lockLook = false;
            this.setCollide(false);
            this.setFightMode(false);
        }, 40);
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(simpleSwing, parasiteLaunch, charge, slam_regular, slam_jump));
            double[] weights = {
                    (distance < 4 && prevAttack != simpleSwing) ? 1/distance : 0, //Simple Swing
                    (distance < 4 && prevAttack != simpleSwing) ? 1/distance : 0, //Parasite Chest Attack
                    (distance < 17 && distance >= 4) ? distance * 0.02 : 0, //Charge Attack
                    (distance < 5) ? 1/distance : 0, //Slam Regular
                    (distance < 16 && distance >=5 && prevAttack != slam_jump) ? distance * 0.02 : 0 //Slam Jump

            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return prevAttack == charge ? 140 : 40;
    }

    Vec3d chargePos;
    protected boolean isLeft = false;

    private final Consumer<EntityLivingBase> slam_jump = (target) -> {
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setLeapSlam(true);
        this.setImmovable(true);

        addEvent(()-> {
        Vec3d targetedPos = target.getPositionVector();
            float distance = getDistance(target);
        addEvent(()-> {
            this.setImmovable(false);
        ModUtils.leapTowards(this, targetedPos, (float) (0.6 * Math.sqrt(distance)), 0.5f);
        }, 10);
        }, 15);

        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.7f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
            this.lockLook = true;
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0.1,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) ((ModConfig.barrend_golem_attack_damage * ModConfig.barrend_golem_attack_multiplier) * ModConfig.biome_multiplier);
            ModUtils.handleAreaImpact(2.5f, (e)-> damage, this, offset, source, 0.8f, 0, false);
        }, 40);


        addEvent(()-> {
        this.lockLook = false;
        this.setImmovable(false);
        this.setFullBodyUsage(false);
        verticalMovementTick = 0;
        this.setFightMode(false);
        this.setLeapSlam(false);
        }, 80);
    };
    private final Consumer<EntityLivingBase> slam_regular = (target) -> {
        this.setFightMode(true);
        this.setFullBodyUsage(true);
        this.setSlam(true);

        addEvent(()-> this.lockLook = true, 10);
        addEvent(()-> {
            this.setImmovable(true);
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.7f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0.1,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) ((ModConfig.barrend_golem_attack_damage * ModConfig.barrend_golem_attack_multiplier) * ModConfig.biome_multiplier);
            ModUtils.handleAreaImpact(2.5f, (e)-> damage, this, offset, source, 0.8f, 0, false);
        }, 36);

        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = false;
            this.setFightMode(false);
            this.setSlam(false);
            this.setFullBodyUsage(false);
        }, 60);
    };


    //here we'll start the charge animation, that will be followed by a looping, and ending with a crash
    private final Consumer<EntityLivingBase> charge = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setPrepareCharge(true);
      addEvent(()-> {
          //Sends a one time notification to change the Charge Dir

          Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(1.0D));
          Vec3d targetedPos = target.getPositionVector().add(ModUtils.yVec(1.0D));
          Vec3d Dir = targetedPos.subtract(startPos).normalize();
          AtomicReference<Vec3d> teleportPos = new AtomicReference<>(targetedPos);
          int maxDistance = 16;
          ModUtils.lineCallback(targetedPos.add(Dir), targetedPos.add(Dir.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
              boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                      .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                              .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
              boolean notOpen = ModUtils.cubePoints(0, 1, 0, 1, 3, 1).stream()
                      .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                              .causesSuffocation());
              if(safeLanding && !notOpen) {
                  teleportPos.set(pos);
              }
              this.chargePos = teleportPos.get();
          });

          addEvent(()-> {
              this.setPrepareCharge(false);
              this.setCharge(true);
          }, 5);
      }, 30);

    };



    private final Consumer<EntityLivingBase> simpleSwing = (target) -> {
        this.setFightMode(true);
        //Alternates Animations each calling of the attack
        if(this.isLeft) {
            this.setSwingLeft(true);
            this.isLeft = false;
        } else {
            this.setSwingRight(true);
            this.isLeft = true;
        }

        //Deals the Damage
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,1.5,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (ModConfig.barrend_golem_attack_damage * ModConfig.biome_multiplier);
            ModUtils.handleAreaImpact(1.2f, (e)-> damage, this, offset, source, 0.8f, 0, false);
        }, 29);



        addEvent(()-> {
            this.setFightMode(false);
            this.setSwingLeft(false);
            this.setSwingRight(false);
        }, 50);
    };

    private final Consumer<EntityLivingBase> parasiteLaunch = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setParasiteAttack(true);

      addEvent(()-> this.lockLook = true, 10);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2,1.5,0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (ModConfig.barrend_golem_attack_damage * ModConfig.biome_multiplier);
          ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.9f, 0, false);
      }, 31);

      addEvent(()-> this.lockLook =false, 50);
      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setParasiteAttack(false);
      }, 50);
    };

    @Override
    public void registerControllers(AnimationData data) {
    data.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
    data.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
    data.addAnimationController(new AnimationController(this, "worm_idle", 0, this::predicateWormIdle));
    data.addAnimationController(new AnimationController(this, "worm_b_controller", 0, this::predicateBackWorm));
    data.addAnimationController(new AnimationController(this, "worm_l_controller", 0, this::predicateLeftWorm));
    data.addAnimationController(new AnimationController(this, "worm_r_controller", 0, this::predicateRightWorm));
    data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
    data.addAnimationController(new AnimationController(this, "other_loops_controller", 0, this::predicateOtherLoops));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private <E extends IAnimatable> PlayState predicateOtherLoops(AnimationEvent<E> event) {
        if(this.isCharge()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CHARGE, true));
            return PlayState.CONTINUE;
        }
        if(this.isStill()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_STILL, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private<E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode()) {

            if(this.isSwingLeft()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_LEFT, false));
            }
            if(this.isSwingRight()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_RIGHT, false));
            }
            if(this.isParasiteAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PARASITE_ATTACK, false));
            }
            if(this.isPrepareCharge()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PREPARE_CHARGE, false));
            }
            if(this.isCollide()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COLLIDE, false));
            }
            if(this.isSlam()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SLAM, false));
            }
            if(this.isLeapSlam()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LEAP_SLAM, false));
            }
            return PlayState.CONTINUE;
        }

        if(this.isAwaken()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_AWAKEN, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable>PlayState predicateWormIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WORM_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable>PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isFullBodyUsage() && !this.isCharge()) {
            if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!this.isFullBodyUsage() && !this.isCharge()) {
            if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            }
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    //Worms Handlers

    private<E extends IAnimatable> PlayState predicateBackWorm(AnimationEvent<E> event) {
        if(this.isWormBack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOW_BACK_WORM, false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDE_BACK_WORM, true));
        }

        return PlayState.CONTINUE;
    }

    private<E extends IAnimatable> PlayState predicateLeftWorm(AnimationEvent<E> event) {
        if(this.isWormLeft()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOW_LEFT_WORM, false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDE_LEFT_WORM, true));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateRightWorm(AnimationEvent<E> event) {
        if(this.isWormRight()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOW_RIGHT_WORM, false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HIDE_RIGHT_WORM, true));
        }
        return PlayState.CONTINUE;
    }
    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIBarrendGolem<>(this, 1.0, 40, 16F, 0.3f));
    }

}
