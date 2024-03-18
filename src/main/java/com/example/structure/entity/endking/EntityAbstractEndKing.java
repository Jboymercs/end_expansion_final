package com.example.structure.entity.endking;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityEye;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.ai.EntityKingTimedAttack;
import com.example.structure.entity.ai.IMultiAction;
import com.example.structure.entity.endking.EndKingAction.ActionShootLazer;
import com.example.structure.entity.endking.ghosts.EntityGhostPhase;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.entity.util.IPitch;
import com.example.structure.renderer.ITarget;
import com.example.structure.util.*;
import com.example.structure.util.handlers.ParticleManager;
import com.google.common.base.Predicate;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class EntityAbstractEndKing extends EntityModBase implements IEntityMultiPart, IPitch, DirectionalRender, ITarget {

    /**
     * this is the base class for the End King, used by the EntityEndKing and as well EntityPermanantGhost
     */
    protected Vec3d chargeDir;

    protected boolean performLazerAttack = false;

    public IMultiAction lazerAttack =  new ActionShootLazer(this, stopLazerByte, (vec3d) -> {});
    protected static final byte stopLazerByte = 39;

    protected final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_12));

    //Boolean to check for any nearby swords
    protected boolean hasSwordsNearby = false;

    public boolean IPhaseTwo = false;


    public Vec3d renderLazerPos;
    public Vec3d prevRenderLazerPos;
    public boolean hasEyesNearby = false;
    public boolean IPhaseThree = false;
    //A call for if damage will be done in the selected area by the attack sorter
    protected boolean damageViable = false;
    protected static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    //Used for Full Bones usage of the body
    protected static final DataParameter<Boolean> FULL_BODY_USAGE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    //Used for Upper Body only attacks
    protected static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> PHASE_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final  DataParameter<Boolean> LEAP_SWEEP_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SUMMON_CRYSTALS_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SUMMON_FIREBALLS_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SUMMON_GHOSTS_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> UPPER_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SIDE_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> COMBO_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> CAST_ARENA = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> PHASE_INTRO = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> PHASE_HANDLER = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> GROUND_SWORD = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> MULTIPLE_STRIKES = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> LAZER_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> BOSS_START = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> BOSS_STALL = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> GHOST_SUMMON = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> DEATH_BOSS = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> FLY_DASH_MOVE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    //
    protected static final DataParameter<Boolean> TOP_HP = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.FLOAT);
    public void setTopHp(boolean value) {this.dataManager.set(TOP_HP, Boolean.valueOf(value));}
    public boolean isTopHP() {return this.dataManager.get(TOP_HP);}
    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setSwingingArms(boolean value) {this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(value));}
    public boolean isSwingingArms() {return this.dataManager.get(SWINGING_ARMS);}
    public void setFullBodyUsage(boolean value) {this.dataManager.set(FULL_BODY_USAGE, Boolean.valueOf(value));}
    public boolean isFullBodyUsage() {return this.dataManager.get(FULL_BODY_USAGE);}
    public void setPhaseMode(boolean value) {this.dataManager.set(PHASE_MODE, Boolean.valueOf(value));}
    public boolean isPhaseMode() {return this.dataManager.get(PHASE_MODE);}
    public void setLeapSweepAttack(boolean value) {this.dataManager.set(LEAP_SWEEP_ATTACK, Boolean.valueOf(value));}
    public boolean isLeapSweepAttack() {return this.dataManager.get(LEAP_SWEEP_ATTACK);}
    public void setSummonCrystalsAttack(boolean value) {this.dataManager.set(SUMMON_CRYSTALS_ATTACK, Boolean.valueOf(value));}
    public boolean isSummonCrystalsAttack() {return this.dataManager.get(SUMMON_CRYSTALS_ATTACK);}
    public void setSummonFireballsAttack(boolean value) {this.dataManager.set(SUMMON_FIREBALLS_ATTACK, Boolean.valueOf(value));}
    public boolean isSummonFireBallsAttack() {return this.dataManager.get(SUMMON_FIREBALLS_ATTACK);}
    public boolean isSummonGhosts() {return this.dataManager.get(SUMMON_GHOSTS_ATTACK);}
    public void setSummonGhostsAttack(boolean value) {this.dataManager.set(SUMMON_GHOSTS_ATTACK, Boolean.valueOf(value));}
    public boolean isSideAttack() {return this.dataManager.get(SIDE_ATTACK);}
    public void setSideAttack(boolean value) {this.dataManager.set(SIDE_ATTACK, Boolean.valueOf(value));}
    public boolean isUpperAttack() {return this.dataManager.get(UPPER_ATTACK);}
    public void setUpperAttack(boolean value) {this.dataManager.set(UPPER_ATTACK, Boolean.valueOf(value));}
    public boolean isComboAttack() {return this.dataManager.get(COMBO_ATTACK);}
    public void setComboAttack(boolean value) {this.dataManager.set(COMBO_ATTACK, Boolean.valueOf(value));}
    public boolean isCastArena() {return this.dataManager.get(CAST_ARENA);}
    public void setCastArena(boolean value) {this.dataManager.set(CAST_ARENA, Boolean.valueOf(value));}
    public boolean isPhaseIntro(){return this.dataManager.get(PHASE_INTRO);}
    public void setPhaseIntro(boolean value) {this.dataManager.set(PHASE_INTRO, Boolean.valueOf(value));}
    public boolean isPhaseHandler() {return this.dataManager.get(PHASE_HANDLER);}
    public void setPhaseHandler(boolean value) {this.dataManager.set(PHASE_HANDLER, Boolean.valueOf(value));}
    public boolean isGroundSwords() {return this.dataManager.get(GROUND_SWORD);}
    public void setGroundSword(boolean value) {this.dataManager.set(GROUND_SWORD, Boolean.valueOf(value));}
    public boolean isMultipleStrikes() {return this.dataManager.get(MULTIPLE_STRIKES);}
    public void setMultipleStrikes(boolean value) {this.dataManager.set(MULTIPLE_STRIKES, Boolean.valueOf(value));}
    public boolean isLazerAttack() {return this.dataManager.get(LAZER_ATTACK);}
    public void setLazerAttack(boolean value) {this.dataManager.set(LAZER_ATTACK, Boolean.valueOf(value));}
    public boolean isBossStart() {return this.dataManager.get(BOSS_START);}
    public void setBossStart(boolean value) {this.dataManager.set(BOSS_START, Boolean.valueOf(value));}
    public boolean isBossStall() {return this.dataManager.get(BOSS_STALL);}
    public void setBossStall(boolean value) {this.dataManager.set(BOSS_STALL, Boolean.valueOf(value));}
    public boolean isPGhostSummon() {return this.dataManager.get(GHOST_SUMMON);}
    public void setPGhostSummon(boolean value) {this.dataManager.set(GHOST_SUMMON, Boolean.valueOf(value));}
    public boolean isDeathBoss() {return this.dataManager.get(DEATH_BOSS);}
    public void setDeathBoss(boolean value) {this.dataManager.set(DEATH_BOSS, Boolean.valueOf(value));}
    public boolean isFlyDashMove() {return this.dataManager.get(FLY_DASH_MOVE);}
    public void setFlyDashMove(boolean value) {this.dataManager.set(FLY_DASH_MOVE, Boolean.valueOf(value));}
    private final MultiPartEntityPart[] hitboxParts;
    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0f, 0f);
    private final MultiPartEntityPart legsWhole = new MultiPartEntityPart(this, "legsWhole", 1.0f, 1.1f);
    private final MultiPartEntityPart torso = new MultiPartEntityPart(this, "torso", 1.2f, 1.7f);
    private final MultiPartEntityPart head = new MultiPartEntityPart(this, "head", 0.7f, 0.7f);

    protected boolean hasPlayedPhaseAnimation = false;

    public boolean isMeleeMode = false;

    public boolean isRangedMode = false;
    public float variable_distance = 10f;
    public EntityAbstractEndKing(World world) {
        super(world);
        this.hitboxParts = new MultiPartEntityPart[]{model, legsWhole, torso, head};
        this.setSize(2.0f, 3.7f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();

    }

    public EntityAbstractEndKing(World world, float x, float y, float z) {
        super(world, x, y, z);
        this.hitboxParts = new MultiPartEntityPart[]{model, legsWhole, torso, head};
        this.setSize(2.0f, 3.7f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
    }

    @Override
    public void entityInit() {

        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(FULL_BODY_USAGE, Boolean.valueOf(false));
        this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
        this.dataManager.register(PHASE_MODE, Boolean.valueOf(false));
        this.dataManager.register(LEAP_SWEEP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_CRYSTALS_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_FIREBALLS_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_GHOSTS_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(UPPER_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SIDE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(COMBO_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(CAST_ARENA, Boolean.valueOf(false));
        this.dataManager.register(PHASE_INTRO, Boolean.valueOf(false));
        this.dataManager.register(TOP_HP, Boolean.valueOf(true));
        this.dataManager.register(PHASE_HANDLER, Boolean.valueOf(false));
        this.dataManager.register(GROUND_SWORD, Boolean.valueOf(false));
        this.dataManager.register(MULTIPLE_STRIKES, Boolean.valueOf(false));
        this.dataManager.register(LAZER_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(GHOST_SUMMON, Boolean.valueOf(false));
        this.dataManager.register(BOSS_STALL, Boolean.valueOf(false));
        this.dataManager.register(BOSS_START, Boolean.valueOf(false));
        this.dataManager.register(FLY_DASH_MOVE, Boolean.valueOf(false));
        this.dataManager.register(DEATH_BOSS, Boolean.valueOf(false));
        super.entityInit();

    }
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }


    @Override
    protected boolean canDespawn() {
        return false;
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(!this.IisGhost) {
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }

        double HealthChange = this.getHealth() / this.getMaxHealth();


        if(this.lockLook) {

            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;

        }
        boolean hasGround = false;
        for (int i = 0; i > -10; i--) {
            if (!world.isAirBlock(getPosition().add(new BlockPos(0, i, 0))) && !this.IPhaseThree) {
                hasGround = true;
            }
        }

        if (!hasGround && this.motionY < -1) {
            this.setImmovable(true);
        } else if (this.isImmovable()) {
            this.setImmovable(false);
        }


        List<EntityEye> nearbyEyes = this.world.getEntitiesWithinAABB(EntityEye.class, this.getEntityBoundingBox().grow(40D), e -> !e.getIsInvulnerable());
        if(!nearbyEyes.isEmpty()) {
            this.hasEyesNearby = true;
        } else {
            this.hasEyesNearby =false;
        }
        if(HealthChange > 0.67) {
            variable_distance = 2.0f;
            this.setTopHp(false);

        }
        if(HealthChange >= 0.67) {
            this.IPhaseTwo = false;

        } else if(HealthChange < 0.67 && HealthChange >= 0.33) {
            this.IPhaseTwo= true;
            this.IPhaseThree = false;
            this.setPhaseHandler(false);
        } else if(HealthChange < 0.33) {
            this.IPhaseTwo = false;
            this.IPhaseThree = true;
            this.setPhaseHandler(true);
        }
        List<ProjectileSpinSword> nearbySwords = this.world.getEntitiesWithinAABB(ProjectileSpinSword.class, this.getEntityBoundingBox().grow(4D), e -> !e.getIsInvulnerable());
        if(!nearbySwords.isEmpty()) {
            hasSwordsNearby = true;
        } else {
            hasSwordsNearby = false;
        }

        if(this.IPhaseTwo || this.IPhaseThree) {
            if(rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            }
        }
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }
        //Location of Hitboxes
        this.setHitBoxPos(legsWhole, new Vec3d(0, -0.1, 0));
        this.setHitBoxPos(torso, new Vec3d(0, 1.0, 0));
        this.setHitBoxPos(head, new Vec3d(0, 2.7, 0));

        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
        if(!world.isRemote && performLazerAttack) {
            lazerAttack.update();
        }

        if(this.isBossStall()) {
            this.setImmovable(true);
            this.motionZ = 0;
            this.motionY = 0;
            this.motionX = 0;
            this.rotationPitch = 0;
            this.rotationYaw = 0;
            this.rotationYawHead = 0;
            List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(8D), e -> !e.getIsInvulnerable());
            if(!nearbyPlayers.isEmpty() && !hasStartedBossFight) {
                startBossFight();
            }
        } else if(this.isBossStart()) {
            this.motionZ = 0;
            this.motionY = 0;
            this.motionX = 0;
            this.rotationPitch = 0;
            this.rotationYaw = 0;
            this.rotationYawHead = 0;
        }
    }

    protected boolean hasStartedBossFight = false;
    public void startBossFight() {
        hasStartedBossFight = true;
        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "End King: " + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "king_talk_0")));
            }
            //Dialog 1
        }, 20);


        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "End King: " + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "king_talk_1")));
            }
            //Dialog 2
        }, 100);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "End King: " + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "king_talk_2")));
            }
            //Dialog 3
        }, 240);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "End King: " + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "king_talk_3")));
            }
            //Dialog 4
        }, 380);


        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "End King: " + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "king_talk_4")));
            }
            //Dialog 5
        }, 480);

        addEvent(()-> {
            this.setBossStall(false);
            this.setBossStart(true);
        }, 485);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "End King: " + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "king_talk_5")));
            }
            //Dialog 6
        }, 520);

        addEvent(()-> {
            this.setBossStart(false);
            this.setImmovable(false);

        }, 530);

    }




    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.end_king_health * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.end_king_damage * ModConfig.biome_multiplier);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityCrystalKnight>(this, EntityCrystalKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == stopLazerByte) {
            this.renderLazerPos = null;
        }
        else if(id == ModUtils.PARTICLE_BYTE) {
            for (int i = 0; i < 5; i++) {
                Vec3d lookVec = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
                Vec3d randOffset = ModUtils.rotateVector2(lookVec, lookVec.crossProduct(ModUtils.Y_AXIS), ModRand.range(-70, 70));
                randOffset = ModUtils.rotateVector2(randOffset, lookVec, ModRand.range(0, 360)).scale(1.5f);
                Vec3d velocity = Vec3d.ZERO.subtract(randOffset).normalize().scale(0.15f).add(new Vec3d(this.motionX, this.motionY, this.motionZ));
                Vec3d particlePos = this.getPositionEyes(1).add(ModUtils.getAxisOffset(lookVec, new Vec3d(1.4, 0, 0))).add(randOffset);
                ParticleManager.spawnColoredSmoke(world, particlePos, ModColors.RED, velocity);
            }
        }
        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.RED, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
        }

    }


    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }

    public boolean damageKing;

    @Override
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float damage) {
        if(!this.IPhaseTwo && !this.IPhaseThree) {
            if (!this.isPhaseMode()) {
                    damageKing = true;
                    return this.attackEntityFrom(source, damage);
            }

            if (damage > 0.0F && !source.isUnblockable()) {
                if (!source.isProjectile()) {
                    Entity entity = source.getImmediateSource();

                    if (entity instanceof EntityLivingBase) {
                        this.blockUsingShield((EntityLivingBase) entity);
                    }

                }
                return false;
            }

            return false;
        }
        return this.attackEntityFrom(source, damage);
    }

    public boolean IisGhost = false;

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        if(!damageKing && !source.isUnblockable() && !this.IPhaseTwo && !this.IPhaseThree || !CAN_TARGET.apply(source.getTrueSource()) || this.IisGhost || this.isBossStall()) {
            return false;

        }

        damageKing = false;
        return super.attackEntityFrom(source, amount);
    }


    public static boolean isFriendlyKnight(Entity entity) {
        return !CAN_TARGET.apply(entity);
    }

    public static final Predicate<Entity> CAN_TARGET = entity -> {

        return !(entity instanceof EntityKnightBase || entity instanceof EntityAbstractEndKing || entity instanceof EntityGhostPhase);
    };

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }





    @Override
    public final void setRenderDirection(Vec3d dir) {
        if (this.renderLazerPos != null) {
            this.prevRenderLazerPos = this.renderLazerPos;
        } else {
            this.prevRenderLazerPos = dir;
        }
        this.renderLazerPos = dir;
    }


    @Override
    public Optional<Vec3d> getTarget() {
        return Optional.ofNullable(renderLazerPos);
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }
}
