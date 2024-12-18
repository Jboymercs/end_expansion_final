package com.example.structure.entity;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.ai.snatcher.EntityStalkAI;
import com.example.structure.entity.knighthouse.EntityKnightBase;
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
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
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

public class EntityChomper extends EntityModBase implements IAnimationTickable, IAnimatable, IAttack {


    private final String ANIM_WALK = "walk";
    private final String ANIM_IDLE = "idle";
    private final String ANIM_ATTACK = "attack";
    private final String ANIM_BITE = "bite";
    private final String ANIM_WARN = "warn";

    private final String ANIM_DIG = "dig";
    private final String ANIM_DUG_LOOP = "dug_loop";
    private final String ANIM_POP_OUT = "pop_out";

    private final String ANIM_CHOMP_CHOMP = "eat";
    private Consumer<EntityLivingBase> prevAttack;

    private static final DataParameter<Boolean> ATTACK = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DIG_DOWN = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DIG_UP = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DUG_IN = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BITE = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WARN = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> CHOMP = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
     //   nbt.setBoolean("Attack", this.dataManager.get(ATTACK));
    //    nbt.setBoolean("Dig_Down", this.dataManager.get(DIG_DOWN));
     //   nbt.setBoolean("Dig_Up", this.dataManager.get(DIG_UP));
     //   nbt.setBoolean("Dug_In", this.dataManager.get(DUG_IN));
     //   nbt.setBoolean("Bite", this.dataManager.get(BITE));
     //   nbt.setBoolean("Warn", this.dataManager.get(WARN));
     //   nbt.setBoolean("Chomp", this.dataManager.get(CHOMP));

        nbt.setBoolean("Attack", this.isAttacking());
        nbt.setBoolean("Dig_Down", this.isDigDown());
        nbt.setBoolean("Dig_Up", this.isDigUp());
        nbt.setBoolean("Dug_In", this.isDugIn());
        nbt.setBoolean("Bite", this.isBite());
        nbt.setBoolean("Warn", this.isWarn());
        nbt.setBoolean("Chomp", this.isChomp());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
    //    this.dataManager.set(ATTACK, nbt.getBoolean("Attack"));
    //    this.dataManager.set(DIG_DOWN, nbt.getBoolean("Dig_Down"));
    //    this.dataManager.set(DIG_UP, nbt.getBoolean("Dig_Up"));
    //    this.dataManager.set(DUG_IN, nbt.getBoolean("Dug_In"));
    //    this.dataManager.set(BITE, nbt.getBoolean("Bite"));
    //    this.dataManager.set(WARN, nbt.getBoolean("Warn"));
    //    this.dataManager.set(CHOMP, nbt.getBoolean("Chomp"));

        this.setAttacking(nbt.getBoolean("Attack"));
        this.setDigDown(nbt.getBoolean("Dig_Down"));
        this.setDigUp(nbt.getBoolean("Dig_Up"));
        this.setDugIn(nbt.getBoolean("Dug_In"));
        this.setBite(nbt.getBoolean("Bite"));
        this.setWarn(nbt.getBoolean("Warn"));
        this.setChomp(nbt.getBoolean("Chomp"));
    }

    public void setDigDown(boolean value) {this.dataManager.set(DIG_DOWN, Boolean.valueOf(value));}
    public boolean isDigDown() {return this.dataManager.get(DIG_DOWN);}
    public void setDigUp(boolean value) {this.dataManager.set(DIG_UP, Boolean.valueOf(value));}
    public boolean isDigUp() {return this.dataManager.get(DIG_UP);}
    public void setAttacking(boolean value) {this.dataManager.set(ATTACK, Boolean.valueOf(value));}
    public boolean isAttacking() {return this.dataManager.get(ATTACK);}
    public void setDugIn(boolean value) {this.dataManager.set(DUG_IN, Boolean.valueOf(value));}
    public boolean isDugIn() {return this.dataManager.get(DUG_IN);}
    public void setBite(boolean value) {this.dataManager.set(BITE, Boolean.valueOf(value));}
    public boolean isBite() {return this.dataManager.get(BITE);}
    public void setWarn(boolean value) {this.dataManager.set(WARN, Boolean.valueOf(value));}
    public boolean isWarn() {return this.dataManager.get(WARN);}
    public void setChomp(boolean value) {this.dataManager.set(CHOMP, Boolean.valueOf(value));}
    public boolean isChomp() {return this.dataManager.get(CHOMP);}

    private AnimationFactory factory = new AnimationFactory(this);
    public EntityChomper(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0f, 1.3f);
        this.experienceValue = 30;
    }

    public EntityChomper(World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 1.3f);
        this.experienceValue = 30;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.0, 60, 8F, 0.35f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(DIG_DOWN, Boolean.valueOf(false));
        this.dataManager.register(DIG_UP, Boolean.valueOf(false));
        this.dataManager.register(ATTACK, Boolean.valueOf(false));
        this.dataManager.register(DUG_IN, Boolean.valueOf(false));
        this.dataManager.register(BITE, Boolean.valueOf(false));
        this.dataManager.register(WARN, Boolean.valueOf(false));
        this.dataManager.register(CHOMP, Boolean.valueOf(false));
    }

    public int timeTooDig = ModRand.range(120, 200);
    public int warnAmount = 60;

    public int warnCooldown = 4;
    public boolean beginDigging = false;
    public boolean isCurrentlyDugIn = false;

    public boolean isJumping = false;

    protected int chompCooldown = 10;
    protected boolean chompAttack = false;

    protected boolean hasStartedPopOut = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase target = this.getAttackTarget();

        if(target != null && this.isJumping) {
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.0D), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    if(base == target) {
                        if(world.rand.nextInt(3) == 0) {
                            stateChomp(target);
                        } else {
                            this.setTooBiteTarget(target);
                        }

                    }
                }
            }
        }

        if(target != null && this.isChomp()) {
            Vec3d targetPos = target.getPositionVector().add(ModUtils.yVec(target.getEyeHeight()));
            ModUtils.setEntityPosition(this, targetPos);
            if(chompCooldown <= 0 && !this.chompAttack) {
                this.stateChompAttack(target);
                this.chompAttack = true;
            } else {
                chompCooldown--;
            }
            if(target.getHealth() == 0) {

                timeTooDig = ModRand.range(400, 500);
                this.stateStopChomp();
            }
        }

        if(target != null) {
            if(target.getHealth() == 0) {
                timeTooDig = ModRand.range(1000, 2000);
            }
        }

        if(this.hurtTime > 0 && this.isDugIn()) {
            if(target != null) {
                statePopOut(this.getAttackingEntity());
            }

        }

        if(target == null && timeTooDig <= 0) {
            BlockPos setPos = this.getPosition().add(0, -1, 0);
            if(world.getBlockState(setPos).isFullBlock() && !beginDigging && !isCurrentlyDugIn) {
                this.warnAmount = 4;
                this.stateBeginDigging(setPos);

            }
        } else {
            timeTooDig--;
        }

        if(this.isCurrentlyDugIn && target == null) {
            this.motionX = 0;
            this.motionZ = 0;
            this.motionY = 0;
            this.rotationPitch = 0;
            this.rotationYaw = 0;
            this.rotationYawHead = 0;
            warnCooldown--;
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(8D), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    if(!(base instanceof EntityChomper)) {
                        if(base instanceof EntityPlayer) {
                            if(!base.isSneaking() && !((EntityPlayer) base).isCreative()) {
                                if (warnAmount <= 0 && !this.hasStartedPopOut) {
                                    this.statePopOut(base);
                                    this.hasStartedPopOut = true;
                                }

                                if (this.getEntitySenses().canSee(base) && this.warnCooldown <= 0 && !this.isWarn() && this.warnAmount > 0) {
                                    this.stateWarnNearby();
                                }
                            }
                        } else {
                            if (warnAmount <= 0 && !this.hasStartedPopOut) {
                                this.statePopOut(base);
                                this.hasStartedPopOut = true;
                            }
                            if (this.getEntitySenses().canSee(base) && this.warnCooldown <= 0 && !this.isWarn()  && this.warnAmount > 0) {
                                this.stateWarnNearby();
                            }
                        }

                    }
                }
            }
            if(nearbyEntities.isEmpty()) {
                this.warnAmount = 4;
            }

            List<EntityLivingBase> nearbyClose = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(4D), e -> !e.getIsInvulnerable());
            if(!nearbyClose.isEmpty()) {
                for(EntityLivingBase base : nearbyClose) {
                    if(!(base instanceof EntityChomper) && this.getEntitySenses().canSee(base) && !this.hasStartedPopOut) {
                            this.statePopOut(base);
                            this.hasStartedPopOut = true;


                    }
                }
            }
        }
    }
    public void stateStopChomp() {
        this.setAttackTarget(null);
        this.noClip = false;
        this.setNoGravity(false);
        this.setChomp(false);
    }

    @Override
    public boolean getCanSpawnHere()
    {
        if(this.posY <= 40) {
            return this.world.rand.nextInt(12) == 0;
        }
        return false;
    }


    @Override
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return this.ticksExisted > 20 * 60 * 20;

    }


    public void stateChompAttack(EntityLivingBase target) {
        this.playSound(ModSoundHandler.CHOMPER_BITE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
        Vec3d offset = target.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.0, 0.3, 0)));
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
        float damage = (float) ((MobConfig.chomper_attack_damange * 0.6) * ModConfig.biome_multiplier);
        ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
        addEvent(()-> {
            this.chompCooldown = 10;
            this.chompAttack = false;
        }, 10);
    }


    public void stateChomp(EntityLivingBase target) {
        this.setAttacking(false);
        this.setChomp(true);
        this.noClip = true;
        this.setNoGravity(true);
        this.isJumping = false;

    }
    public void statePopOut(EntityLivingBase target) {
        this.setDugIn(false);
        this.setDigUp(true);
        this.playSound(ModSoundHandler.CHOMPER_POP_OUT, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
      addEvent(()-> {
          this.setImmovable(false);
          this.noClip = false;
          this.setNoGravity(false);
          this.setDigUp(false);
          if(target != this) {
              this.setAttackTarget(target);
          }
          this.hasStartedPopOut = false;
          this.isCurrentlyDugIn = false;
      }, 20);
    }

    public void stateWarnNearby() {
        this.setWarn(true);
        this.warnCooldown = 80;
        this.playSound(ModSoundHandler.CHOMPER_WARN, 2.0f, 1.0f / rand.nextFloat() * 0.4f + 0.2f);
        this.warnAmount--;
        addEvent(()-> world.setEntityState(this, ModUtils.PARTICLE_BYTE), 5);
        addEvent(()-> {
            this.setWarn(false);
        }, 15);
    }

    public void stateBeginDigging(BlockPos pos) {
        this.setDigDown(true);
        this.beginDigging = true;
        addEvent(()-> {
            this.setDigDown(false);
            this.isCurrentlyDugIn = true;
            this.noClip = true;
            this.setNoGravity(true);
            this.setDugIn(true);
            this.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            this.setImmovable(true);
            this.beginDigging = false;
        }, 30);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) MobConfig.chomper_health * getHealthModifierAsh());
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16.0D * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.6D);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 30, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.0, 1.4, 0))), ModColors.AZURE, pos.normalize().scale(0.25).add(ModUtils.yVec(0)));
            });
        }
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isBite() && !this.isAttacking() && !this.isChomp()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(jump_attack));
            double[] weights = {
                    (distance <= 9) ? 1/distance : 0 //Jump ATTACK
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 60;
    }

    protected void setTooBiteTarget(EntityLivingBase target) {
        this.setAttacking(false);
        this.setBite(true);
        this.isJumping = false;
        addEvent(()-> {
            this.playSound(ModSoundHandler.CHOMPER_BITE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (MobConfig.chomper_attack_damange * getAttackModifierAsh());
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
        }, 3);

        addEvent(()-> this.setBite(false), 10);
    }

    private Consumer<EntityLivingBase> jump_attack = (target)-> {
        this.setAttacking(true);
        this.setImmovable(true);
        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            float distance = getDistance(target);
            addEvent(()-> {
                this.setImmovable(false);
                this.isJumping = true;
                this.playSound(ModSoundHandler.CHOMPER_LEAP, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
                ModUtils.leapTowards(this, targetedPos, (float) (1.3 * Math.sqrt(distance)), 0.7f);
            }, 10);
        }, 2);
        addEvent(()-> {
            if(this.isJumping) {
                this.playSound(ModSoundHandler.CHOMPER_BITE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (MobConfig.chomper_attack_damange * getAttackModifierAsh());
                ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
            }
        }, 29);


        addEvent(()-> this.setAttacking(false), 35);
    };



    @Override
    public void registerControllers(AnimationData data) {
    data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "other_controller", 0, this::predicateAttack));
        data.addAnimationController(new AnimationController(this, "dug_in_controller", 0, this::predicateLooped));
    }

    private <E extends IAnimatable> PlayState predicateLooped(AnimationEvent<E> event) {
        if(this.isDugIn() && !this.isDigDown() && !this.isDigUp()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DUG_LOOP, true));
            return PlayState.CONTINUE;
        }

        if(this.isChomp()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CHOMP_CHOMP, true));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isWarn()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WARN, false));
            return PlayState.CONTINUE;
        }
        if(this.isBite()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE, false));
            return PlayState.CONTINUE;
        }
        if(this.isDigUp()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_POP_OUT, false));
            return PlayState.CONTINUE;
        }
        if(this.isDigDown()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DIG, false));
            return PlayState.CONTINUE;
        }
        if(this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
            return PlayState.CONTINUE;
        }


        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isWarn() && !this.isBite() && !this.isAttacking() && !this.isDigDown() && !this.isDigUp() && !this.isDugIn() && !this.isChomp()) {
            if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
                return PlayState.CONTINUE;
            }

        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.CHOMPER_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.isDugIn()) {
            return ModSoundHandler.CHOMPER_IDLE;
        }
        return null;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(ModSoundHandler.STALKER_STEP, 0.7F, 1.0f / (rand.nextFloat() * 0.4F + 0.2f));
    }


    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "chomper");
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.CHOMPER_HURT;
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
