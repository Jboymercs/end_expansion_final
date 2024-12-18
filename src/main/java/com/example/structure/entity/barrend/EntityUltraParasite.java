package com.example.structure.entity.barrend;

import akka.japi.pf.FI;
import com.example.structure.Main;
import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityAbstractBuffker;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.ProjectileParasiteBomb;
import com.example.structure.entity.ai.EntityAIUltraParasite;
import com.example.structure.entity.barrend.ultraparasite.ActionAOE;
import com.example.structure.entity.barrend.ultraparasite.ActionLargeAOE;
import com.example.structure.entity.endking.EntityAbstractEndKing;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.entity.util.IAttack;
import com.example.structure.init.ModItems;
import com.example.structure.util.*;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityUltraParasite extends EntityBarrendMob implements IAnimatable, IAnimationTickable, IAttack {


    private final String ANIM_IDLE = "idle";
    private final String ANIM_IDLE_MOUTH = "mouth_idle";
    private final String ANIM_IDLE_TAIL = "tail_idle";
    private final String ANIM_WALK_LOWER = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";

    //actions
    private final String ANIM_PIERCE_ATTACK = "pierce_swing";
    private final String ANIM_COMBO_ATTACK = "combo_swing";
    private final String ANIM_JUMP_ATTACK = "jump_attack";
    private final String ANIM_SHOOT_PROJECTILE = "shoot_projectile";
    private final String ANIM_JUMP_BACK = "jump_back";
    private final String ANIM_LARGE_AOE = "large_aoe";
    private final String ANIM_TAIL_WHIP = "tail_whip";
    private final String ANIM_PHASE_TRANSITION = "phase_transition";

    //grab state
    private final String ANIM_BEGIN_GRAB = "begin_grab";
    private final String ANIM_CONTINUE_GRAB = "continue_grab";
    private final String ANIM_END_GRAB = "end_grab";

    private int wantedDistance;
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FULL_BODY_USAGE = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_SWING = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_SWING = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BEGIN_GRAB = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CONTINUE_GRAB = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_GRAB = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_ATTACK = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_PROJECTILE = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> JUMP_BACK = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> PHASE_TRANSITION = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> LARGE_AOE = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TAIL_WHIP = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_DONE_PHASE = EntityDataManager.createKey(EntityUltraParasite.class, DataSerializers.BOOLEAN);


    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setFullBodyUsage(boolean value) {this.dataManager.set(FULL_BODY_USAGE, Boolean.valueOf(value));}
    public boolean isFullBodyUsage() {return this.dataManager.get(FULL_BODY_USAGE);}
    public void setPierceSwing(boolean value) {this.dataManager.set(PIERCE_SWING, Boolean.valueOf(value));}
    public boolean isPierceSwing() {return this.dataManager.get(PIERCE_SWING);}
    public void setComboSwing(boolean value) {this.dataManager.set(COMBO_SWING, Boolean.valueOf(value));}
    public boolean isComboSwing() {return this.dataManager.get(COMBO_SWING);}
    public void setBeginGrab(boolean value) {this.dataManager.set(BEGIN_GRAB, Boolean.valueOf(value));}
    public boolean isBeginGrab() {return this.dataManager.get(BEGIN_GRAB);}
    public void setContinueGrab(boolean value) {this.dataManager.set(CONTINUE_GRAB, Boolean.valueOf(value));}
    public boolean isContinueGrab() {return this.dataManager.get(CONTINUE_GRAB);}
    public void setEndGrab(boolean value) {this.dataManager.set(END_GRAB, Boolean.valueOf(value));}
    public boolean isEndGrab() {return this.dataManager.get(END_GRAB);}
    public void setJumpAttack(boolean value) {this.dataManager.set(JUMP_ATTACK, Boolean.valueOf(value));}
    public boolean isJumpAttack() {return this.dataManager.get(JUMP_ATTACK);}
    public void setShootProjectile(boolean value) {this.dataManager.set(SHOOT_PROJECTILE, Boolean.valueOf(value));}
    public boolean isShootProjectile() {return this.dataManager.get(SHOOT_PROJECTILE);}
    public void setJumpBack(boolean value) {this.dataManager.set(JUMP_BACK, Boolean.valueOf(value));}
    public boolean isJumpBack() {return this.dataManager.get(JUMP_BACK);
    }
    public void setPhaseTransition(boolean value) {this.dataManager.set(PHASE_TRANSITION, Boolean.valueOf(value));}
    public boolean isPhaseTransition() {return this.dataManager.get(PHASE_TRANSITION);}
    public void setLargeAoe(boolean value) {this.dataManager.set(LARGE_AOE, Boolean.valueOf(value));}
    public boolean isLargeAoe() {return this.dataManager.get(LARGE_AOE);}
    public void setTailWhip(boolean value) {this.dataManager.set(TAIL_WHIP, Boolean.valueOf(value));}
    public boolean isTailWhip() {return this.dataManager.get(TAIL_WHIP);}
    public void setHasDonePhase(boolean value) {this.dataManager.set(HAS_DONE_PHASE, Boolean.valueOf(value));}
    public boolean isHasDonePhase() {return this.dataManager.get(HAS_DONE_PHASE);}

    private AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;

    private EntityLivingBase grabbedEntity = null;
    private boolean hasGrabbedSomething = false;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.NOTCHED_6));

    public EntityUltraParasite(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.2F, 3.1F);
        this.iAmBossMob = true;
        this.wantedDistance = 4;
    }

    public EntityUltraParasite(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 3.1F);
        this.iAmBossMob = true;
        this.wantedDistance = 4;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Fight_Mode", this.isFightMode());
        nbt.setBoolean("Full_Body", this.isFullBodyUsage());
        nbt.setBoolean("Pierce_Swing", this.isPierceSwing());
        nbt.setBoolean("Combo_Swing", this.isComboSwing());
        nbt.setBoolean("Begin_Grab", this.isBeginGrab());
        nbt.setBoolean("Continue_Grab", this.isContinueGrab());
        nbt.setBoolean("End_Grab", this.isEndGrab());
        nbt.setBoolean("Jump_Attack", this.isJumpAttack());
        nbt.setBoolean("Shoot_Proj", this.isShootProjectile());
        nbt.setBoolean("Jump_Back", this.isJumpBack());
        nbt.setBoolean("Phase", this.isPhaseTransition());
        nbt.setBoolean("Large_Aoe", this.isLargeAoe());
        nbt.setBoolean("Tail_Whip", this.isTailWhip());
        nbt.setBoolean("Has_Phase", this.isHasDonePhase());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setFightMode(nbt.getBoolean("Fight_Mode"));
        this.setFullBodyUsage(nbt.getBoolean("Full_Body"));
        this.setPierceSwing(nbt.getBoolean("Pierce_Swing"));
        this.setComboSwing(nbt.getBoolean("Combo_Swing"));
        this.setBeginGrab(nbt.getBoolean("Begin_Grab"));
        this.setContinueGrab(nbt.getBoolean("Continue_Grab"));
        this.setEndGrab(nbt.getBoolean("End_Grab"));
        this.setJumpAttack(nbt.getBoolean("Jump_Attack"));
        this.setShootProjectile(nbt.getBoolean("Shoot_Proj"));
        this.setJumpBack(nbt.getBoolean("Jump_Back"));
        this.setPhaseTransition(nbt.getBoolean("Phase"));
        this.setLargeAoe(nbt.getBoolean("Large_Aoe"));
        this.setTailWhip(nbt.getBoolean("Tail_Whip"));
        this.setHasDonePhase(nbt.getBoolean("Has_Phase"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(FULL_BODY_USAGE, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_SWING, Boolean.valueOf(false));
        this.dataManager.register(COMBO_SWING, Boolean.valueOf(false));
        this.dataManager.register(BEGIN_GRAB, Boolean.valueOf(false));
        this.dataManager.register(CONTINUE_GRAB, Boolean.valueOf(false));
        this.dataManager.register(END_GRAB, Boolean.valueOf(false));
        this.dataManager.register(JUMP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_PROJECTILE, Boolean.valueOf(false));
        this.dataManager.register(JUMP_BACK, Boolean.valueOf(false));
        this.dataManager.register(TAIL_WHIP, Boolean.valueOf(false));
        this.dataManager.register(PHASE_TRANSITION, Boolean.valueOf(false));
        this.dataManager.register(LARGE_AOE, Boolean.valueOf(false));
        this.dataManager.register(HAS_DONE_PHASE, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) MobConfig.evolved_parasite_health * getHealthModifierBarrend());
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.evolved_parasite_armor * ModConfig.barrend_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.evolved_parasite_toughness * ModConfig.barrend_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.evolved_parasite_attack_damage * getAttackModifiersBarrend());
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIUltraParasite<>(this, 1.1, 20, 16, 0.3F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityKnightBase>(this, EntityKnightBase.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityAbstractEndKing>(this, EntityAbstractEndKing.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, true));
    }

    private int damageTaken = 0;
    @Override
    public void onUpdate() {
        super.onUpdate();

        this.bossInfo.setPercent(getHealth() / getMaxHealth());

        EntityLivingBase target = this.getAttackTarget();

        //Lock Look system Implemented
        if(this.lockLook && !this.jumpFrom) {

            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;

        }

        if(target != null && !world.isRemote) {

            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            this.movementBoss(target, distSq, this.getEntitySenses().canSee(target), 30.0F);

            if(wantedDistance == 4) {
                if(this.hurtTime > 0 || world.rand.nextInt(40) == 0) {
                    this.damageTaken++;
                }
                if(damageTaken >= 100) {
                    this.wantedDistance = 14;
                }
            }

            if(wantedDistance == 14) {
                if(this.hurtTime > 0) {
                    damageTaken -=3;
                }

              //  if(this.isComboMode()) {
              //      this.wantedDistance = 4;
               //     this.damageTaken = 0;
              //  }
                if(damageTaken <= 0) {
                    this.wantedDistance = 4;
                }
            }

            double HealthChange = this.getHealth() / this.getMaxHealth();

            if(HealthChange <= 0.5 && !this.justDidTransition && !this.isFightMode()) {
                this.doPhaseTransition();
            }

            if(HealthChange >= 0.80 && this.isHasDonePhase()) {
                this.setHasDonePhase(false);
            }


            if(this.jumpFrom) {
                Vec3d targetedDir = this.getPositionVector().add(0,12,0).subtract(this.getPositionVector());
                ModUtils.addEntityVelocity(this, targetedDir.scale(0.025 * 1.1));
            }

            if(this.hasGrabbedSomething) {

                List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                        this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(0.8, -0.3, 0))).grow(2.5D, 3.0D, 1.5D),
                        e -> !e.getIsInvulnerable());
                if(!nearbyEntities.isEmpty()) {
                    for(EntityLivingBase base : nearbyEntities) {
                        if(base == target && !world.isRemote) {
                                //ModUtils.setEntityPosition(base, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.6, 0.7, 0))));
                                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4, 0.4, 0)));
                                //target.setPosition(offset.x, offset.y, offset.z);
                                base.setPosition(offset.x, offset.y, offset.z);
                                base.setPositionAndUpdate(offset.x, offset.y, offset.z);
                                double d0 = (offset.x - base.posX) * 0.08;
                                double d2 = (offset.y - base.posY) * 0.05;
                                double d1 = (offset.z - base.posZ) * 0.08;
                               // base.addVelocity(d0, d2, d1);
                                this.faceEntity(base, 30.0F, 30.0F);

                        }
                    }
                }
            }

        }

    }

    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private final float strafeAmount = 0.3F;

    private boolean justDidTransition = false;

    private static final float STRAFING_STOP_FACTOR = 0.75f;
    private static final float STRAFING_BACKWARDS_FACTOR = 0.25f;
    private static final float STRAFING_DIRECTION_TICK = 20;
    private static final float STRAFING_DIRECTION_CHANGE_CHANCE = 0.3f;

    private boolean startFirstParticles = false;
    private boolean startSecondParticles = false;

    private boolean setUpNextAttack = false;
    public void doPhaseTransition() {
        this.setFullBodyUsage(true);
        this.setFightMode(true);
        this.setPhaseTransition(true);
        this.setImmovable(true);
        this.lockLook = true;
        justDidTransition = true;
        addEvent(()-> {
            this.setImmovable(false);
            this.jumpFrom = true;
            addEvent(()-> {
                this.jumpFrom = false;
            }, 4);
        }, 15);

        addEvent(()-> {
            this.startFirstParticles = true;
            this.screenShakeValue = 1.2F;
        }, 20);
        addEvent(()-> this.setImmovable(true), 23);

        addEvent(()-> {
        //do the roar
        world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        //SCREENSHAKE BYTE
        }, 33);

        addEvent(()-> {
        this.startSecondParticles = true;
        }, 45);

        addEvent(()-> {
            this.startSecondParticles = false;
            this.startFirstParticles = false;
            this.setImmovable(false);
        }, 90);

        addEvent(()-> {
            this.setFightMode(false);
            this.setFullBodyUsage(false);
            this.setPhaseTransition(false);
            this.setHasDonePhase(true);
            this.lockLook = false;
            this.setUpNextAttack = true;
        }, 100);
    }

    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(this.startFirstParticles) {
            if(world.rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            }
        }

        if(this.startSecondParticles) {
            if(world.rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
            }
        }

    }


    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(new Vec3d(ModRand.range(-7, 7), ModRand.range(-3, 3), ModRand.range(-7, 7))), ModColors.GREEN, new Vec3d(0,0.05,0));
        }

        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(new Vec3d(ModRand.range(-7, 7), ModRand.range(-3, 3), ModRand.range(-7, 7))), ModColors.GREEN, new Vec3d(0,0.05,0));
        }
    }

    public void movementBoss(EntityLivingBase target, double distSq, boolean canSee, float lookSpeed) {
        int maxAttackDistance = wantedDistance * wantedDistance;
        if (!this.lockLook) {
            if(!this.isJumpBack()) {
                if (wantedDistance == 4) {
                    //Movement for short distance attacking
                    if (distSq <= maxAttackDistance && canSee) {
                        this.getNavigator().clearPath();
                        ++this.strafingTime;
                    } else {
                            this.getNavigator().tryMoveToEntityLiving(target, 1.1);
                            this.strafingTime = -1;
                    }
                } else if (wantedDistance == 14) {
                    if (distSq <= maxAttackDistance && canSee) {
                        this.getNavigator().clearPath();
                        //System.out.println("Within 14 block distance");
                        ++this.strafingTime;

                    } else {
                         System.out.println("Gathering Navigator to move too");
                        this.getNavigator().tryMoveToEntityLiving(target, 1.1);
                        this.strafingTime = -1;
                    }
                }

            } else {
                this.strafingTime = -1;
                this.getNavigator().clearPath();
                this.getLookHelper().setLookPositionWithEntity(target, lookSpeed, lookSpeed);
            }


            if (this.strafingTime >= STRAFING_DIRECTION_TICK) {
                if ((double) this.getRNG().nextFloat() < STRAFING_DIRECTION_CHANGE_CHANCE) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double) this.getRNG().nextFloat() < STRAFING_DIRECTION_CHANGE_CHANCE) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }


            if(this.strafingTime > -1 && wantedDistance == 14) {
                if(distSq < maxAttackDistance - 4) {
                    // System.out.println("Trying to strafe away from enemy");
                    double d0 = (this.posX - target.posX) * 0.012;
                    double d1 = (this.posY - target.posY) * 0.005;
                    double d2 = (this.posZ - target.posZ) * 0.012;
                    this.addVelocity(d0, d1, d2);
                    this.faceEntity(target, lookSpeed, lookSpeed);
                    this.getLookHelper().setLookPositionWithEntity(target, lookSpeed, lookSpeed);
                } else {
                    // System.out.println("Moving through strafe system");
                    if(distSq > maxAttackDistance * STRAFING_STOP_FACTOR) {
                        this.strafingBackwards = false;
                    } else if (distSq >= maxAttackDistance - 4) {
                        this.strafingBackwards = true;
                    }
                    this.getMoveHelper().strafe(1 * this.strafeAmount, (this.strafingClockwise ? 2 : -2) * this.strafeAmount);
                    this.faceEntity(target, lookSpeed, lookSpeed);
                }
            } else if (this.strafingTime > -1 && wantedDistance == 4) {
                if (distSq > maxAttackDistance * STRAFING_STOP_FACTOR) {
                    this.strafingBackwards = false;
                } else if (distSq < maxAttackDistance * STRAFING_BACKWARDS_FACTOR) {
                    this.strafingBackwards = true;
                }

                this.getMoveHelper().strafe((this.strafingBackwards ? -1 : 1) * this.strafeAmount, (this.strafingClockwise ? 1 : -1) * this.strafeAmount);
                this.faceEntity(target, lookSpeed, lookSpeed);
            } else {
                this.getLookHelper().setLookPositionWithEntity(target, lookSpeed, lookSpeed);
            }
        }
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();

        if(!this.isFightMode() && !this.isPhaseTransition()) {
            List<Consumer<EntityLivingBase>> close_attacks = new ArrayList<>(Arrays.asList(basic_attack, combo_attack, jump_back, begin_grab, continue_grab, jump_attack, spit_acid, tail_whip, large_AOE));
            double[] weights = {
                    (distance <= 4 && prevAttack != basic_attack) ? 1/distance : 0, //basic attack
                    (distance <= 6 && prevAttack != combo_attack) ? 1/distance : 0, // combo attack
                    (distance <= 7 && prevAttack != jump_back && wantedDistance == 14) ? 1.5/distance : (distance <= 7 && prevAttack != jump_back) ? 0.9/distance : 0, //jump back
                    (distance <= 8 && prevAttack != begin_grab) ? 1/distance : 0, // bgein grab
                    (distance <= 6 && prevAttack == begin_grab && hasGrabbedSomething && grabbedEntity != null) ? 100 : 0, //only activates if it has an entity // continue grab
                    (distance <= 16 && distance >= 5 && prevAttack != jump_attack) ? distance * 0.02 : 0, // jump attack
                    (distance <= 16 && distance >= 7 && prevAttack != spit_acid) ? distance * 0.02 : 0, // spit acid
                    (distance <= 10 && HealthChange <= 0.5 && prevAttack != tail_whip) ? 1/distance : 0, //Tail Whip 50% Health
                    (distance <= 16 && HealthChange <= 0.5 && prevAttack != large_AOE && setUpNextAttack) ? 100 : (distance <= 16 && HealthChange <= 0.5 && prevAttack != large_AOE) ? distance * 0.02 : 0//Mega Large AOE
            };

            prevAttack = ModRand.choice(close_attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return (prevAttack == begin_grab) ? 0 : HealthChange <= 0.5 ? 10 : 20;
    }


    private final Consumer<EntityLivingBase> large_AOE = (target) -> {
      this.setLargeAoe(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
      this.setFightMode(true);

      addEvent(()-> {
          this.setImmovable(false);
        this.jumpFrom = true;
        addEvent(()-> {
            this.jumpFrom = false;
        }, 3);
      }, 15);

      addEvent(()-> {
          this.setImmovable(true);
          this.lockLook = true;
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.1),-0.1F);
          }, 8);
      }, 22);


      addEvent(()-> {
        //do Action
          this.setImmovable(true);
          new ActionLargeAOE(false).performAction(this, target);
      }, 40);

      addEvent(()-> new ActionLargeAOE(true).performAction(this, target), 77);


      addEvent(()-> new ActionAOE(13).performAction(this, target), 115);


      addEvent(()-> this.lockLook = false, 130);


      addEvent(()-> {
        this.setLargeAoe(false);
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setImmovable(false);
        this.setUpNextAttack = false;
      }, 140);
    };
    private final Consumer<EntityLivingBase> tail_whip = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setTailWhip(true);
      this.setImmovable(true);

      addEvent(()-> {
        Vec3d targetedPos = target.getPositionVector();
        this.lockLook = true;
        //first whip
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.4),0.20F);
        }, 10);
      }, 15);

      //first damage
      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0.3,0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (this.getAttack() * 1.5);
          ModUtils.handleAreaImpact(3.5f, (e)-> damage, this, offset, source, 0.9f, 0, false);
      }, 35);

      addEvent(()-> {
          this.lockLook = false;
        this.setImmovable(true);
      }, 40);

      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          this.lockLook = true;
          //second whip
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.4),0.20F);
          }, 10);
      }, 50);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0.3,0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (this.getAttack() * 1.5);
          ModUtils.handleAreaImpact(3.5f, (e)-> damage, this, offset, source, 0.9f, 0, false);
      }, 67);

      addEvent(()-> this.setImmovable(true), 75);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setTailWhip(false);
        this.setImmovable(false);
        this.setFightMode(false);
        this.lockLook = false;
      }, 105);
    };

    private final Consumer<EntityLivingBase> spit_acid = (target) -> {
      this.setShootProjectile(true);
      this.setFightMode(true);

      addEvent(()-> {
          //SHoot Projectile Bomb
          ProjectileParasiteBomb projectile = new ProjectileParasiteBomb(this.world);
          Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 3.7, 0)));
          projectile.shoot(this, 0, this.rotationYaw, 0.0F, 0.65F, 0);
          projectile.setPosition(pos.x, pos.y, pos.z);
          projectile.setTravelRange(40F);
          world.spawnEntity(projectile);

          addEvent(()-> {
              ProjectileParasiteBomb projectile2 = new ProjectileParasiteBomb(this.world);
              Vec3d pos2 = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 3.7, 0)));
              projectile2.shoot(this, 0, this.rotationYaw - 65, 0.0F, 0.65F, 0);
              projectile2.setPosition(pos2.x, pos2.y, pos2.z);
              projectile2.setTravelRange(40F);
              world.spawnEntity(projectile2);
          }, 4);

          addEvent(()-> {
              ProjectileParasiteBomb projectile2 = new ProjectileParasiteBomb(this.world);
              Vec3d pos2 = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 3.7, 0)));
              projectile2.shoot(this, 0, this.rotationYaw + 65, 0.0F, 0.65F, 0);
              projectile2.setPosition(pos2.x, pos2.y, pos2.z);
              projectile2.setTravelRange(40F);
              world.spawnEntity(projectile2);
          }, 8);

      }, 18);

      addEvent(()-> {
          this.setFightMode(false);
          this.setShootProjectile(false);
      }, 35);
    };


    private final Consumer<EntityLivingBase> jump_attack = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setJumpAttack(true);
      this.setImmovable(true);

      addEvent(()-> {
        this.lockLook = true;
          Vec3d targetedPos = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              this.lockLook = false;
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3F),(float) (distance * 0.1F) );
          }, 10);
      }, 15);

      addEvent(()-> {
          //Do entity Move Tile Stuff
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5,0.5,0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (float) (this.getAttack() * 1.5);
          ModUtils.handleAreaImpact(2.0f, (e)-> damage, this, offset, source, 1.2f, 0, false);
          float distance = this.getDistance(target);
          new ActionAOE((int) (distance + 4)).performAction(this, target);
      }, 45);

      addEvent(()-> {
        this.setImmovable(true);
      }, 50);


      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setJumpAttack(false);
        this.setImmovable(false);
        this.lockLook = false;
      }, 70);
    };

    private boolean jumpFrom = false;

    private final Consumer<EntityLivingBase> continue_grab = (target) -> {
      this.setFightMode(true);
      this.setContinueGrab(true);
      this.setFullBodyUsage(true);

      //immovable and lock lock true

        addEvent(()-> this.screenShakeValue = 0.9F, 2);
        //disable shield
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,1.5,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack() * 0.5F;
            ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0F, 0, false);
        }, 15);

        //leap Up
        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetedPos);
          //  ModUtils.leapTowards(this, targetedPos.add(0, 10, 0), (float) (distance * 0.2),1.7F);
            this.jumpFrom = true;
            addEvent(()-> this.jumpFrom = false, 5);
        }, 35);

        addEvent(()-> {
        this.grabbedEntity = null;
        this.hasGrabbedSomething = false;
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.2f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,0.2,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (this.getAttack() * 1.3);
            ModUtils.handleAreaImpact(3.0f, (e)-> damage, this, offset, source, 1.4f, 0, false);
            this.setImmovable(true);
            this.jumpFrom = false;
        }, 53);

        addEvent(()-> this.setImmovable(false), 65);


        addEvent(()-> this.setImmovable(true), 90);


        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = false;
            this.setFullBodyUsage(false);
            this.setContinueGrab(false);
            this.setFightMode(false);
        }, 105);
    };


    private final Consumer<EntityLivingBase> begin_grab = (target) -> {
      this.setFightMode(true);
      this.setBeginGrab(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      addEvent(()-> {
        Vec3d targetedPos = target.getPositionVector();
        this.lockLook = true;

        //leap
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.4),0.20F);
        }, 10);

        //check for entity
        addEvent(()-> {
            this.setImmovable(true);
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                    this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(0.8, -0.3, 0))).grow(2.5D, 3.0D, 1.5D),
                    e -> !e.getIsInvulnerable());

            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    if(!this.hasGrabbedSomething) {
                        System.out.println("Detecting something here!");
                        if (base == target) {
                            grabbedEntity = base;
                            this.hasGrabbedSomething = true;
                        }
                    }
                }
            }


            if(!this.hasGrabbedSomething) {
                this.setBeginGrab(false);
                this.setEndGrab(true);


                addEvent(()-> {
                    this.setFightMode(false);
                    this.setFullBodyUsage(false);
                    this.setEndGrab(false);
                    this.setImmovable(false);
                    this.lockLook = false;
                }, 30);

            } else {
                this.setBeginGrab(false);
                this.setFightMode(false);
            }
        }, 17);
      }, 13);

    };
    private final Consumer<EntityLivingBase> jump_back = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setJumpBack(true);
      this.setImmovable(true);
      addEvent(()-> {
          Vec3d dirToo = this.getPositionVector().subtract(target.getPositionVector()).normalize();
          Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(22));
          this.setImmovable(false);
          ModUtils.leapTowards(this, jumpTooPos, 1.7F, 0.25F);
      }, 15);

      addEvent(()-> this.setImmovable(true), 35);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setJumpBack(false);
        this.setImmovable(false);
      }, 45);
    };

    private final Consumer<EntityLivingBase> basic_attack = (target) -> {
      this.setFightMode(true);
      this.setPierceSwing(true);

      addEvent(()-> this.lockLook = true, 10);

      addEvent(()-> {
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.2f));
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.8,1.5,0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.5f, 0, false);
      }, 19);

      addEvent(()-> this.lockLook = false, 25);

      addEvent(()-> {
        this.setFightMode(false);
        this.setPierceSwing(false);
      }, 30);
    };

    private final Consumer<EntityLivingBase> combo_attack = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setComboSwing(true);

      //first leap
      addEvent(()-> {
        this.setImmovable(true);
        Vec3d targetedPos = target.getPositionVector();
        this.lockLook = true;
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.22),0.22F);

        }, 11);
      }, 4);

      //first strike
      addEvent(()-> {
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.2f));
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,1.5,0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(2.0f, (e)-> damage, this, offset, source, 0.5f, 0, false);
      }, 21);
      //second leap
      addEvent(()-> {
        this.lockLook = false;
        this.setImmovable(true);
          Vec3d targetedPos = target.getPositionVector();
        addEvent(()-> {
            this.setImmovable(false);
            this.lockLook = true;
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.22),0.22F);
        }, 10);
      }, 30);
        //second strike
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.0,1.5,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * 1.2);
            ModUtils.handleAreaImpact(1.2f, (e)-> damage, this, offset, source, 0.8f, 0, false);

        }, 46);

        addEvent(()-> this.setImmovable(true), 55);
        addEvent(()-> this.lockLook = false, 60);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setComboSwing(false);
        this.setImmovable(false);
      }, 75);
    };


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        animationData.addAnimationController(new AnimationController(this, "mouth_controller", 0, this::predicateMouth));
        animationData.addAnimationController(new AnimationController(this, "tail_controller", 0, this::predicateTail));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
    }

    private <E extends IAnimatable>PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
           //do attack stuff
            if(this.isPierceSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PIERCE_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isComboSwing()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isBeginGrab()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BEGIN_GRAB, false));
                return PlayState.CONTINUE;
            }
            if(this.isContinueGrab()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CONTINUE_GRAB, false));
                return PlayState.CONTINUE;
            }
            if(this.isEndGrab()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_END_GRAB, false));
                return PlayState.CONTINUE;
            }
            if(this.isJumpAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_JUMP_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isShootProjectile()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_PROJECTILE, false));
                return PlayState.CONTINUE;
            }
            if(this.isJumpBack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_JUMP_BACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isTailWhip()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TAIL_WHIP, false));
                return PlayState.CONTINUE;
            }
            if(this.isLargeAoe()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LARGE_AOE, false));
                return PlayState.CONTINUE;
            }
            if(this.isPhaseTransition()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_TRANSITION, false));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateTail(AnimationEvent<E> event) {
        //states that this idle animation CANNOT play in
        if(!this.isBeginGrab() && !this.isEndGrab() && !this.isContinueGrab() && !this.isTailWhip()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_TAIL, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateMouth(AnimationEvent<E> event) {
        //states that this idle animation CANNOT play in
        if(!this.isComboSwing() && !this.isBeginGrab() && !this.isContinueGrab() && !this.isEndGrab() && !this.isShootProjectile() && !this.isPhaseTransition()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_MOUTH, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        //insert Full Body Usage
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFullBodyUsage()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            if (event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void tick() {

    }


    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isJumpBack() || this.isContinueGrab() || this.isPhaseTransition()) {
            return false;
        }


        return super.attackEntityFrom(source, amount);
    }


    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }


    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "big_rick");
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public void setCustomNameTag(@Nonnull String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void addTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(@Nonnull EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}
