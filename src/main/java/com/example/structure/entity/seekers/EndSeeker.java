package com.example.structure.entity.seekers;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ProjectilePurple;
import com.example.structure.entity.ai.EntityAISeeker;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.*;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
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

/**
 * The Basic new dungeon creature for the End, mostly used for the End Vaults
 */
public class EndSeeker extends EntityModBase implements IAnimatable, IAttack, IAnimationTickable {

    private static final DataParameter<Boolean> SEEKER_MODE = EntityDataManager.createKey(EndSeeker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLINK_MODE = EntityDataManager.createKey(EndSeeker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_STRIKE_ONE = EntityDataManager.createKey(EndSeeker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_STRIKE_TWO = EntityDataManager.createKey(EndSeeker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_ATTACK = EntityDataManager.createKey(EndSeeker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_GUN = EntityDataManager.createKey(EndSeeker.class, DataSerializers.BOOLEAN);


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
     //   nbt.setBoolean("Seeker_Mode", this.dataManager.get(SEEKER_MODE));
     //   nbt.setBoolean("Blink_Mode", this.dataManager.get(BLINK_MODE));
     //   nbt.setBoolean("Melee_Strike_One", this.dataManager.get(MELEE_STRIKE_ONE));
     //   nbt.setBoolean("Melee_Strike_Two", this.dataManager.get(MELEE_STRIKE_TWO));
    //    nbt.setBoolean("Pierce_Attack", this.dataManager.get(PIERCE_ATTACK));
    //    nbt.setBoolean("Shoot_Gun", this.dataManager.get(SHOOT_GUN));

        nbt.setBoolean("Seeker_Mode", this.isFightMode());
        nbt.setBoolean("Blink_Mode", this.isBlinkMode());
        nbt.setBoolean("Melee_Strike_One", this.isMeleeStrikeOne());
        nbt.setBoolean("Melee_Strike_Two", this.isMeleeStrikeTwo());
        nbt.setBoolean("Pierce_Attack", this.isPierceAttack());
        nbt.setBoolean("Shoot_Gun", this.isShootGun());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
      //  this.dataManager.set(SEEKER_MODE, nbt.getBoolean("Seeker_Mode"));
     //   this.dataManager.set(BLINK_MODE, nbt.getBoolean("Blink_Mode"));
     //   this.dataManager.set(MELEE_STRIKE_ONE, nbt.getBoolean("Melee_Strike_One"));
      //  this.dataManager.set(MELEE_STRIKE_TWO, nbt.getBoolean("Melee_Strike_Two"));
      //  this.dataManager.set(PIERCE_ATTACK, nbt.getBoolean("Pierce_Attack"));
     //   this.dataManager.set(SHOOT_GUN, nbt.getBoolean("Shoot_Gun"));

        this.setFightMode(nbt.getBoolean("Seeker_Mode"));
        this.setBlinkMode(nbt.getBoolean("Blink_Mode"));
        this.setMeleeStrikeOne(nbt.getBoolean("Melee_Strike_One"));
        this.setMeleeStrikeTwo(nbt.getBoolean("Melee_Strike_Two"));
        this.setPierceAttack(nbt.getBoolean("Pierce_Attack"));
        this.setShootGun(nbt.getBoolean("Shoot_Gun"));
    }
    public void setFightMode(boolean value) {this.dataManager.set(SEEKER_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(SEEKER_MODE);}
    public void setBlinkMode(boolean value) {this.dataManager.set(BLINK_MODE, Boolean.valueOf(value));}
    public boolean isBlinkMode() {return this.dataManager.get(BLINK_MODE);}
    public void setMeleeStrikeOne(boolean value) {this.dataManager.set(MELEE_STRIKE_ONE, Boolean.valueOf(value));}
    public boolean isMeleeStrikeOne() {return this.dataManager.get(MELEE_STRIKE_ONE);}
    public void setMeleeStrikeTwo(boolean value) {this.dataManager.set(MELEE_STRIKE_TWO, Boolean.valueOf(value));}
    public boolean isMeleeStrikeTwo() {return this.dataManager.get(MELEE_STRIKE_TWO);}
    public void setPierceAttack(boolean value) {this.dataManager.set(PIERCE_ATTACK, Boolean.valueOf(value));}
    public boolean isPierceAttack() {return this.dataManager.get(PIERCE_ATTACK);}
    public void setShootGun(boolean value) {this.dataManager.set(SHOOT_GUN, Boolean.valueOf(value));}
    public boolean isShootGun() {return this.dataManager.get(SHOOT_GUN);}
    private Consumer<EntityLivingBase> prevAttack;

    //Idle Animations
    private final String ANIM_IDLE = "idle";
    private final String ANIM_IDLE_ARMS = "idle_arms";
    private final String ANIM_BLINK = "blink_1";

    //Ranged Attacks
    private final String ANIM_SHOOT_GUN = "shoot_gun";

    //Melee Attacks
    private final String ANIM_MELEE_ATTACK_ONE = "attack_melee";
    private final String ANIM_MELEE_ATTACK_TWO = "attack_melee_2";
    private final String ANIM_DASH_ATTACK = "pierce_dash";
    private AnimationFactory factory = new AnimationFactory(this);
    public int blinkCoolDown = 0;

    public EndSeeker(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.experienceValue = 20;
        if(rand.nextInt(2) == 0) {
            this.isRangedMode = true;
        } else {
            this.isMeleeMode = true;
        }
        this.setSize(0.6f, 2.2f);
    }

    public EndSeeker(World worldIn) {
        super(worldIn);
        this.experienceValue = 20;
        if(rand.nextInt(2) == 0) {
            this.isRangedMode = true;
        } else {
            this.isMeleeMode = true;
        }
        this.setSize(0.6f, 2.2f);
    }

    public boolean isMeleeMode = false;
    public boolean isRangedMode = false;

    public int switchTimer = 120 + ModRand.range(20, 40);
    @Override
    public void onUpdate() {
        super.onUpdate();


        if(rand.nextInt(5) == 0 && blinkCoolDown > 120) {

            this.setBlinkMode(true);
            addEvent(()-> this.setBlinkMode(false), 35);
            blinkCoolDown = 0;
        } else {
            blinkCoolDown++;
        }

        EntityLivingBase target = this.getAttackTarget();

        if(target != null) {
            //Selector for it's Movement
            if(this.isMeleeMode && !this.isRangedMode && switchTimer < 0) {
                this.isRangedMode = true;
                this.isMeleeMode = false;
                switchTimer = 120 + ModRand.range(20, 40);
            }
            else if(this.isRangedMode && !this.isMeleeMode && switchTimer < 0) {
                this.isMeleeMode = true;
                this.isRangedMode = false;
                switchTimer = 100 + ModRand.range(20, 30);
            } else {
                switchTimer--;
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
    public void onLivingUpdate() {
        super.onLivingUpdate();

        EntityLivingBase target = this.getAttackTarget();
        if(!this.isMeleeMode && target != null && !this.isBeingRidden()) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if(distance < 6) {
                double d0 = (this.posX - target.posX) * 0.025;
                double d1 = (this.posY - target.posY) * 0.01;
                double d2 = (this.posZ - target.posZ) * 0.025;
                this.addVelocity(d0, d1, d2);
            }
        }
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAISeeker<>(this, 1.5, 40, 14F, 0.4f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityEnderKnight>(this, EntityEnderKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityEnderMage>(this, EntityEnderMage.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EntityEnderShield>(this, EntityEnderShield.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SEEKER_MODE, Boolean.valueOf(false));
        this.dataManager.register(BLINK_MODE, Boolean.valueOf(false));
        this.dataManager.register(MELEE_STRIKE_ONE, Boolean.valueOf(false));
        this.dataManager.register(MELEE_STRIKE_TWO, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_GUN, Boolean.valueOf(false));
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)ModConfig.seeker_health * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.seeker_attack_damage * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(7.0D * ModConfig.lamented_multiplier);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(meleeAttackOne, meleeAttackTwo, dashAttack, shootGunAttack));
            double[] weights = {
                    (distance <= 2 && prevAttack != meleeAttackOne) ? 2/distance : 0, // Second Melee Attack
                    (distance <= 2 && prevAttack != meleeAttackTwo) ? 2/distance : 0, // Second Melee Attack
                    (this.isMeleeMode && distance <= 10 && distance > 2) ? 1.5/distance : (distance <= 8 && distance > 2) ? 1/distance : 0, //Dash Attack
                    (this.isRangedMode && distance > 1) ? distance * 0.9 : (distance >= 7) ? distance * 0.02 : 0 //Shoot Gun Attack
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 40;
    }

    private final Consumer<EntityLivingBase> meleeAttackOne = (target) -> {
        this.setFightMode(true);
        this.setMeleeStrikeOne(true);
        this.lockLook = true;

        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.4f, 0, false);
        }, 22);
        addEvent(()-> {
            this.lockLook = false;
        this.setFightMode(false);
        this.setMeleeStrikeOne(false);
        }, 35);
    };

    private final Consumer<EntityLivingBase> meleeAttackTwo = (target) -> {
    this.setFightMode(true);
    this.setMeleeStrikeTwo(true);
    this.lockLook = true;

    addEvent(()-> {
        this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,1.3,0)));
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
        float damage = this.getAttack();
        ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.4f, 0, false);
    },25);
    addEvent(()-> {
        this.lockLook = false;
    this.setFightMode(false);
    this.setMeleeStrikeTwo(false);
    }, 40);
    };

    private final Consumer<EntityLivingBase> dashAttack = (target)-> {
      this.setFightMode(true);
      this.setPierceAttack(true);


      addEvent(()-> {
          this.playSound(ModSoundHandler.SEEKER_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 18);
      addEvent(()-> {
        Vec3d posToGo = target.getPositionVector();
          float distance = getDistance(target);
        this.lockLook = true;
          this.setImmovable(true);
        addEvent(()-> {
            this.setImmovable(false);
            ModUtils.leapTowards(this, posToGo, (float) (0.45 * Math.sqrt(distance)), 0.1f);
            for(int i = 0; i < 15; i+=5) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,1.3,0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = this.getAttack() + 2;
                    ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.4f, 0, false);
                }, i);
            }
        },13 );
      }, 10);
      addEvent(()-> {
          this.lockLook = false;
        this.setPierceAttack(false);
        this.setFightMode(false);
      }, 60);
    };

    private final Consumer<EntityLivingBase> shootGunAttack = (target)-> {
      this.setFightMode(true);
      this.setShootGun(true);

      //This Basically adds a lag to the projectiles making it easier to dodge since these mobs aren't meant to be as difficult YET
        addEvent(()-> {

            addEvent(()-> {
                this.playSound(ModSoundHandler.SEEKER_SHOOT, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                ProjectilePurple projectilePurple = new ProjectilePurple(world, this, ModConfig.purp_projectile);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0.3)));
               Vec3d vel2 = target.getPositionVector().add(ModUtils.yVec(1.0D)).subtract(offset);
                projectilePurple.setPosition(offset.x, offset.y, offset.z);
                projectilePurple.shoot(vel2.x, vel2.y, vel2.z, 1.3f, 1.0f);
                projectilePurple.setTravelRange(20F);
                world.spawnEntity(projectilePurple);
            }, 10);
        }, 15);


        addEvent(()-> {

            addEvent(()-> {
                this.playSound(ModSoundHandler.SEEKER_SHOOT, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                ProjectilePurple projectilePurple = new ProjectilePurple(world, this, ModConfig.purp_projectile);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0.3)));
                Vec3d vel2 =  target.getPositionVector().add(ModUtils.yVec(1.0D)).subtract(offset);
                projectilePurple.setPosition(offset.x, offset.y, offset.z);
                projectilePurple.shoot(vel2.x, vel2.y, vel2.z, 1.3f, 1.0f);
                projectilePurple.setTravelRange(20F);
                world.spawnEntity(projectilePurple);
            }, 10);
        }, 25);


        addEvent(()-> {

            addEvent(()-> {
                this.playSound(ModSoundHandler.SEEKER_SHOOT, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                ProjectilePurple projectilePurple = new ProjectilePurple(world, this, ModConfig.purp_projectile);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0.3)));
                Vec3d vel2 =  target.getPositionVector().add(ModUtils.yVec(1.0D)).subtract(offset);
                projectilePurple.setPosition(offset.x, offset.y, offset.z);
                projectilePurple.shoot(vel2.x, vel2.y, vel2.z, 1.3f, 1.0f);
                projectilePurple.setTravelRange(20F);
                world.spawnEntity(projectilePurple);
            }, 10);
        }, 35);

      addEvent(()-> {
        this.setFightMode(false);
        this.setShootGun(false);
      }, 65);
    };

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "blink_controller", 0, this::predicateBlink));
        animationData.addAnimationController(new AnimationController(this, "arms_h_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attacks_h_controller", 0, this::predicateAttacks));
    }

    private<E extends IAnimatable> PlayState predicateBlink(AnimationEvent<E> event) {
        if(this.isBlinkMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLINK, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
       event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_ARMS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isMeleeStrikeOne()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_ATTACK_ONE, false));
            }
            if(this.isMeleeStrikeTwo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_ATTACK_TWO, false));
            }
            if(this.isPierceAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH_ATTACK, false));
            }
            if(this.isShootGun()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_GUN, false));
            }

            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {

    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.SEEKER_HURT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.SEEKER_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.SEEKER_HOVER;
    }


    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (rand.nextInt(5) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

    }

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "seeker");
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }

    //Particle Handler for the boss
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0))), ModColors.WHITE, new Vec3d(ModRand.getFloat(1) * -0.05, -0.3, ModRand.getFloat(1) * -0.05));
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
