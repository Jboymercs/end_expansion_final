package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.*;
import com.example.structure.entity.lamentorUtil.ActionCircleAOE;
import com.example.structure.entity.lamentorUtil.ActionWaveAOE;
import com.example.structure.entity.util.IAttack;
import com.example.structure.entity.util.TimedAttackIniator;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.*;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The Boss of the mod, Here I will test myself in skills to make something immaculate
 */

public class EntityCrystalKnight extends EntityModBase implements IAnimatable, IAttack {

    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.NOTCHED_6));
    private Consumer<EntityLivingBase> prevAttack;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_BLINK = "blink";

    private final String STRIKE_ANIM = "strike";
    private final String CRYSTAL_ANIM = "crystal";
    private final String PIERCE_DASH_ANIM = "pierce";
    private final String SPIN_BEGIN_ANIM = "spinBegin";
    private final String SPIN_ANIM = "spin";
    private final String SPIN_ATTACK_ANIM = "spinAttack";
    private final String GROUND_CRYSTALS_ANIM = "groundCrystals";

    private final String HAMMER_BEGIN_ANIM = "hammerBegin";
    private final String HAMMER_TRAVEL_ANIM = "hammerTravel";
    private final String HAMMER_ATTACK_ANIM = "hammerAttack";
    private final String MULTI_ATTACK_ANIM = "multiple";
    private final String SUMMON_SHULKERS_ANIM = "shulker";
    private final String RANGED_HAMMER_ANIM = "hammerProjectile";
    private final String SUMMON_BOSS_ANIM = "summon";
    private final String DEATH_BOSS_ANIM = "death";
    private static final DataParameter<Boolean> LAMENTOR_MODE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRIKE_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CRYSTAL_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIN_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIN_START = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIN_CYCLE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAMMER_START = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAMMER_CYCLE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAMMER_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MULTI_PIERCE_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHULKER_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAMMER_PROJECTILE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_BOOLEAN = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_BOOLEAN = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);

    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BLOCK_POS);

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Lamentor_Mode", this.dataManager.get(LAMENTOR_MODE));
       nbt.setBoolean("Strike_Attack", this.dataManager.get(STRIKE_ATTACK));
       nbt.setBoolean("Crystal_Attack", this.dataManager.get(CRYSTAL_ATTACK));
        nbt.setBoolean("Spin_Attack", this.dataManager.get(SPIN_ATTACK));
        nbt.setBoolean("Spin_Start", this.dataManager.get(SPIN_START));
        nbt.setBoolean("Spin_Cycle", this.dataManager.get(SPIN_CYCLE));
        nbt.setBoolean("Pierce_Attack", this.dataManager.get(PIERCE_ATTACK));
        nbt.setBoolean("Summon_Attack", this.dataManager.get(SUMMON_ATTACK));
       nbt.setBoolean("Hammer_Start", this.dataManager.get(HAMMER_START));
        nbt.setBoolean("Hammer_Cycle", this.dataManager.get(HAMMER_CYCLE));
       nbt.setBoolean("Hammer_Attack", this.dataManager.get(HAMMER_ATTACK));
        nbt.setBoolean("Multi_Pierce_Attack", this.dataManager.get(MULTI_PIERCE_ATTACK));
        nbt.setBoolean("Shulker_Attack", this.dataManager.get(SHULKER_ATTACK));
        nbt.setBoolean("Hammer_Projectile", this.dataManager.get(HAMMER_PROJECTILE));
        nbt.setBoolean("Summon_Boolean", this.dataManager.get(SUMMON_BOOLEAN));
        nbt.setBoolean("Death_Boolean", this.dataManager.get(DEATH_BOOLEAN));
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        this.dataManager.set(LAMENTOR_MODE, nbt.getBoolean("Lamentor_Mode"));
        this.dataManager.set(STRIKE_ATTACK, nbt.getBoolean("Strike_Attack"));
        this.dataManager.set(CRYSTAL_ATTACK, nbt.getBoolean("Crystal_Attack"));
       this.dataManager.set(SPIN_ATTACK, nbt.getBoolean("Spin_Attack"));
        this.dataManager.set(SPIN_START, nbt.getBoolean("Spin_Start"));
        this.dataManager.set(SPIN_CYCLE, nbt.getBoolean("Spin_Cycle"));
       this.dataManager.set(PIERCE_ATTACK, nbt.getBoolean("Pierce_Attack"));
        this.dataManager.set(SUMMON_ATTACK, nbt.getBoolean("Summon_Attack"));
        this.dataManager.set(HAMMER_START, nbt.getBoolean("Hammer_Start"));
        this.dataManager.set(HAMMER_CYCLE, nbt.getBoolean("Hammer_Cycle"));
       this.dataManager.set(HAMMER_ATTACK, nbt.getBoolean("Hammer_Attack"));
        this.dataManager.set(MULTI_PIERCE_ATTACK, nbt.getBoolean("Multi_Pierce_Attack"));
        this.dataManager.set(SHULKER_ATTACK, nbt.getBoolean("Shulker_Attack"));
        this.dataManager.set(HAMMER_PROJECTILE, nbt.getBoolean("Hammer_Projectile"));
        this.dataManager.set(SUMMON_BOOLEAN, nbt.getBoolean("Summon_Boolean"));
        this.dataManager.set(DEATH_BOOLEAN, nbt.getBoolean("Death_Boolean"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
    }
    public boolean rangeSwitch;
    public boolean meleeSwitch;

    public boolean targetFloating = false;

    public boolean startFlameSpawns = false;

    public int SwitchCoolDown;

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityCrystalKnight(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.experienceValue = 1000;
        this.setSize(0.8f, 2.2f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.healthScaledAttackFactor = ModConfig.lamentor_scaled_attack;
        this.meleeSwitch = true;
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        this.iAmBossMob = true;
        if(!world.isRemote) {
            initBossAI();
        }

        this.setAlive(true);
        if(this.isSummoned()) {
            addEvent(()-> this.playSound(ModSoundHandler.BOSS_SUMMON, 1.5f, 1.0f), 50);
            addEvent(()-> this.setAlive(false), 70);
        }

    }


    public BlockPos centerPos;


    public void onSummon(BlockPos Pos, Projectile actor) {
        BlockPos offset = Pos.add(new BlockPos(0,3,0));
        this.setSpawnLocation(offset);
        //we're trying to set up the AOE attack and the reset key block if the player doesn't kill the Lamentor
        centerPos = Pos.add(0, -1, 0);
        this.setPosition(offset);
        world.spawnEntity(this);
        actor.setDead();
    }



    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(LAMENTOR_MODE, Boolean.valueOf(false));
        this.dataManager.register(STRIKE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(CRYSTAL_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SPIN_START, Boolean.valueOf(false));
        this.dataManager.register(SPIN_CYCLE, Boolean.valueOf(false));
        this.dataManager.register(SPIN_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(HAMMER_PROJECTILE, Boolean.valueOf(false));
        //Phase Two
        this.dataManager.register(HAMMER_START, Boolean.valueOf(false));
        this.dataManager.register(HAMMER_CYCLE, Boolean.valueOf(false));
        this.dataManager.register(HAMMER_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(MULTI_PIERCE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHULKER_ATTACK, Boolean.valueOf(false));
        //States
        this.dataManager.register(DEATH_BOOLEAN, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_BOOLEAN, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, null);

    }


    public void setFightMode(boolean value) {this.dataManager.set(LAMENTOR_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(LAMENTOR_MODE);}
    public void setStrikeAttack(boolean value) {this.dataManager.set(STRIKE_ATTACK, Boolean.valueOf(value));}
    public boolean isStrikeAttack() {return this.dataManager.get(STRIKE_ATTACK);}
    public void setCrystalAttack(boolean value){this.dataManager.set(CRYSTAL_ATTACK, Boolean.valueOf(value));}
    public boolean isCrystalAttack() {return this.dataManager.get(CRYSTAL_ATTACK);}
    public void setSpinAttack(boolean value){this.dataManager.set(SPIN_ATTACK,Boolean.valueOf(value));}
    public boolean isSpinAttack() {return this.dataManager.get(SPIN_ATTACK);}
    public void setSpinStart(boolean value) {
        this.dataManager.set(SPIN_START, Boolean.valueOf(value));
    }
    public boolean isSpinStart() {
        return this.dataManager.get(SPIN_START);
    }
    public void setSpinCycle(boolean value) {
        this.dataManager.set(SPIN_CYCLE, Boolean.valueOf(value));
    }
    public boolean isSpinCycle() {
        return this.dataManager.get(SPIN_CYCLE);
    }
    public void setPierceAttack(boolean value) {
        this.dataManager.set(PIERCE_ATTACK, Boolean.valueOf(value));
    }
    public boolean isPierceAttack() {
        return this.dataManager.get(PIERCE_ATTACK);
    }
    public void setSummonAttack(boolean value) {
        this.dataManager.set(SUMMON_ATTACK, Boolean.valueOf(value));
    }
    public boolean isSummonAttack() {
        return this.dataManager.get(SUMMON_ATTACK);
    }
    public void setHammerStart(Boolean value) {
        this.dataManager.set(HAMMER_START, Boolean.valueOf(value));
    }
    public boolean isHammerStart() {
        return this.dataManager.get(HAMMER_START);
    }
    public void setHammerCycle(Boolean value) {
        this.dataManager.set(HAMMER_CYCLE, Boolean.valueOf(value));
    }
    public boolean isHammerCycle() {
        return this.dataManager.get(HAMMER_CYCLE);
    }
    public void setHammerAttack(Boolean value) {
        this.dataManager.set(HAMMER_ATTACK, Boolean.valueOf(value));
    }
    public boolean isHammerAttack() {
        return this.dataManager.get(HAMMER_ATTACK);
    }
    public void setMultiPierceAttack(Boolean value) {
        this.dataManager.set(MULTI_PIERCE_ATTACK, Boolean.valueOf(value));
    }
    public boolean isMultiPierceAttack() {
        return this.dataManager.get(MULTI_PIERCE_ATTACK);
    }
    public void setShulkerAttack(Boolean value) {
        this.dataManager.set(SHULKER_ATTACK, Boolean.valueOf(value));
    }
    public boolean isShulkerAttack() {
        return this.dataManager.get(SHULKER_ATTACK);
    }

    public void setHammerProjectile(Boolean value) {
        this.dataManager.set(HAMMER_PROJECTILE, Boolean.valueOf(value));
    }
    public boolean isHammerProjectile() {
        return this.dataManager.get(HAMMER_PROJECTILE);
    }
    public void setAlive(Boolean value) {
        this.dataManager.set(SUMMON_BOOLEAN, Boolean.valueOf(value));
    }
    public boolean isSummoned() {
        return this.dataManager.get(SUMMON_BOOLEAN);
    }
    public void setDeadAnim(Boolean value) {
        this.dataManager.set(DEATH_BOOLEAN, Boolean.valueOf(value));
    }
    public boolean isDeathAnim() {
        return this.dataManager.get(DEATH_BOOLEAN);
    }

    public void setSpawnLocation(BlockPos pos) {
        this.dataManager.set(SPAWN_LOCATION, pos);
    }

    public BlockPos getSpawnLocation() {
        return this.dataManager.get(SPAWN_LOCATION);
    }


    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)ModConfig.health * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.39590D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(ModConfig.lamentor_toughness_armor * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(ModConfig.lamentor_armor * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.attack_damage * ModConfig.lamented_multiplier);
    }


    public boolean isDead()
    {
        return this.getHealth() <= 0.0F || this.isDead;
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        //Lock Look system Implemented
        if(this.lockLook) {

            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;

        }

        //Spawn Telporting Location
        //This is too keep the boss at it's starting location and keep it from getting too far away

        if(this.getSpawnLocation() != null) {
            Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

            double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
            double distance = Math.sqrt(distSq);
            //This basically makes it so the Lamentor will be teleported if they are too far away from the Arena
            if(!world.isRemote) {
                if (distance > 40) {
                    this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                }
            }
        }

        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if(meleeSwitch && SwitchCoolDown == 500) {
            rangeSwitch = true;
            SwitchCoolDown = 0;
            meleeSwitch = false;
        }
        if(rangeSwitch && SwitchCoolDown == 500) {
            meleeSwitch = true;
            SwitchCoolDown = 0;
            rangeSwitch = false;
        }
        else {
            SwitchCoolDown++;
        }

        if (this.isShulkerAttack()) {
            this.motionY = 0;
        }
        if(targetFloating) {
            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, 3));
            }
        }

        if(this.isSummoned() || this.isDeathAnim()) {
            this.setImmovable(true);
        } else {
            this.setImmovable(false);
        }
        //Allows boss to destory blocks while quick dashing
        if(ModConfig.bosses_of_mass_destruction) {
            if(this.isSpinCycle() || this.isPierceAttack() || this.isMultiPierceAttack()) {
                AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.1, 1.25).offset(0, 0.1, 0);
                ModUtils.destroyBlocksInAABB(box, world, this);
            }
        } else if (this.isMultiPierceAttack() || this.isPierceAttack()) {
            AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.1, 1.25).offset(0, 0.1, 0);
            ModUtils.destroyBlocksInAABB(box, world, this);
        }


    }
    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(rand.nextInt(5)== 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(startFlameSpawns) {
            if(rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
            }
        }
        if(this.isSpinCycle()) {
            if(rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
            }
        }

    }
    //Particle Handler for the boss
    @Override
    public void handleStatusUpdate(byte id) {
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.WHITE, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
        }
        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ParticleManager.spawnBrightFlames(world, rand, this.getPositionVector(), ModRand.randVec().scale(0.3f));
        }
        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.WHITE, new Vec3d(rand.nextFloat() * 0.2,1,rand.nextFloat() * 0.2));
        }

        super.handleStatusUpdate(id);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        EntityLivingBase target = this.getAttackTarget();
        if(target != null && !this.isBeingRidden() && !meleeSwitch) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if(distance < 12) {
                double d0 = (this.posX - target.posX) * 0.015;
                double d1 = (this.posY - target.posY) * 0.01;
                double d2 = (this.posZ - target.posZ) * 0.015;
                this.addVelocity(d0, d1, d2);
            }
        }
    //Spin Cycle
        if(this.isSpinCycle() && target != null) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if (distance > 2) {
            meleeSwitch = true;
            rangeSwitch = false;
            double d0 = (target.posX - this.posX) * 0.005;
            double d1 = (target.posY - this.posY) * 0.009;
            double d2 = (target.posZ - this.posZ) * 0.005;
            this.addVelocity(d0, d1, d2);
        }
            else {
                this.setSpinCycle(false);
                this.setFightMode(false);
            }
        }
        //Hammer Cycle
        if(this.isHammerCycle() && target != null) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            meleeSwitch = true;
            rangeSwitch = false;
            if(distance > 3) {
                double d0 = (target.posX - this.posX) * 0.015;
                double d1 = (target.posY - this.posY) * 0.009;
                double d2 = (target.posZ - this.posZ) * 0.015;
                this.addVelocity(d0, d1, d2);
            } else {
                this.setHammerCycle(false);
                this.setFightMode(false);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    animationData.addAnimationController(new AnimationController(this, "animBlink", 0, this::predicateBlink));
    animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    animationData.addAnimationController(new AnimationController(this, "phaseONE_controller", 0, this::predicateAttack));
    animationData.addAnimationController(new AnimationController(this, "SD_controller", 0, this::predicateStates));
    }

    private<E extends IAnimatable>PlayState predicateBlink(AnimationEvent<E> event) {
        //Handles the Eyes, Small movements of the remaining Crystals
        if(!this.isDeathAnim() && !this.isSummoned()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLINK, true));
        }
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isSummoned()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_BOSS_ANIM, false));
            return PlayState.CONTINUE;
        }
        if(this.isDeathAnim()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(DEATH_BOSS_ANIM, false));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable>PlayState predicateIdle(AnimationEvent<E> event) {
        //Idle Movements
        if(!this.isFightMode() && !this.isDeathAnim() && !this.isSummoned()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        }

        return PlayState.CONTINUE;
    }


    private <E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        //Attack Animations
        //Simple Strike
        if(this.isStrikeAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(STRIKE_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Summon Small Crystals
        if(this.isCrystalAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(CRYSTAL_ANIM, false));
            return PlayState.CONTINUE;
        }
        // Spin Start Anim
        if(this.isSpinStart()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SPIN_BEGIN_ANIM, false));
            return PlayState.CONTINUE;
        }
        // Spin Cycle End - Attack
        if(this.isSpinAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SPIN_ATTACK_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Pierce Attack
        if(this.isPierceAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(PIERCE_DASH_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Spin Loop Anim
        if(this.isSpinCycle()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SPIN_ANIM, true));
            return PlayState.CONTINUE;
        }
        //Summon Ground Crystals
        if(this.isSummonAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(GROUND_CRYSTALS_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Hammer Start Anim
        if(this.isHammerStart()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(HAMMER_BEGIN_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Hammer Travel Cycle Anim
        if(this.isHammerCycle()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(HAMMER_TRAVEL_ANIM, true));
            return PlayState.CONTINUE;
        }
        //Hammer Attack Anim
        if(this.isHammerAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(HAMMER_ATTACK_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Multiple Pierce Anims
        if(this.isMultiPierceAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(MULTI_ATTACK_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Summon Shulkers Anim
        if(this.isShulkerAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_SHULKERS_ANIM, false));
            return PlayState.CONTINUE;
        }
        //HammerProjectileAttack
        if(this.isHammerProjectile()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(RANGED_HAMMER_ANIM, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));

    }

    public void initBossAI() {
        float attackDistance = 14;
        this.tasks.addTask(4, new EntityAerialTimedAttack(this, attackDistance, 3, 30, new TimedAttackIniator<>(this, 20)));
    }

    public void teleportTarget(double x, double y, double z) {
        this.setPosition(x , y, z);

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    Supplier<Projectile> crystalBallProjectile = () -> new EntityCrystalSpikeSmall(world, this, ModConfig.crystal_damage, null);


    public static int Boss_Cooldown = ModConfig.boss_speed * 20;

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isDeathAnim() && !this.isSummoned()) {
            //Begin Attacks REPEATED
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(meleeStrike, summonCrystals, dashPierce, circleDash, circleAttack, summonGroundCrystals, hammerSLAM, hammerExplosion, animeStrike, hammerProjectile, summonAOEAttack, summonShulkers));
            double[] weights = {
                    //Phase One Abilities
                    (distance < 3 && prevAttack != meleeStrike) ? 1/distance : 0, //Melee Strike
                    (distance > 10 && prevAttack != summonCrystals) ? distance * 0.02 : 0, //Summon Crystal Projectiles
                    (distance < 8 && prevAttack != dashPierce) ? 1/distance : 0, // Dash Pierce
                    (distance > 10 && prevAttack == summonCrystals) ? distance * 0.02 : 0, //CircleDash
                    (prevAttack == circleDash) ?  100 : 0, //CircleAttack
                    (distance < 8 && prevAttack != summonGroundCrystals) ? 1/distance : 0, //SummonGroundCrystals
                    //Phase Two Abilities Added

                    (distance < 9 && prevAttack != hammerSLAM && HealthChange < 0.5) ? 1/distance : 0, //Hammer Slam Attack
                    (prevAttack == hammerSLAM) ? 100 : 0, // 2 Part Hammer Attack
                    (distance < 9 && prevAttack != animeStrike && HealthChange < 0.5) ? 1/distance : 0, //Multi Strike
                    (distance > 7) ? distance * 0.02 : 0, //Hammer Projectile Attack
                    (distance > 8 && prevAttack != summonAOEAttack && HealthChange < 0.75) ? distance * 0.02 : 0, //Summon an Arena AOE
                    (distance > 7 && prevAttack != summonShulkers && prevAttack != summonAOEAttack && HealthChange < 0.75) ? distance * 0.02 : 0 //Summon Shulker Attack
                    //Possibly one more Attack

            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);

        }


        return (prevAttack == circleDash || prevAttack == hammerSLAM) ? 0 : Boss_Cooldown;
    }

    //Basic Melee Attack
    private final Consumer<EntityLivingBase> meleeStrike = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setStrikeAttack(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 5);
            addEvent(()-> this.lockLook = true, 10);

            addEvent(() -> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.5, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(2.5f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            }, 25);

            addEvent(()-> this.lockLook = false, 35);
            addEvent(() -> {
                this.setFightMode(false);
                this.setStrikeAttack(false);
            }, 40);
        }
    };
    //Crystal Ranged Attack
    private final Consumer<EntityLivingBase>summonCrystals = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setCrystalAttack(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_CAST_AMBIENT, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 25);
            addEvent(() -> {



                for (int i = 0; i < 60; i += 4) {
                    addEvent(() -> {
                        this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.4f, 0.8f + ModRand.getFloat(0.2F));
                        float damage =ModConfig.crystal_damage;
                        EntityCrystalSpikeSmall projectile = new EntityCrystalSpikeSmall(this.world, this, damage, null);
                        Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(ModRand.getFloat(2), 3, ModRand.getFloat(2))));
                        Vec3d targetPos = new Vec3d(target.posX + ModRand.getFloat(2) - 1, target.posY, target.posZ + ModRand.getFloat(2) - 1);
                        Vec3d velocity = targetPos.subtract(pos).normalize().scale(0.55f);
                        projectile.setPosition(pos.x, pos.y, pos.z);
                        projectile.setTravelRange(30f);
                        ModUtils.setEntityVelocity(projectile, velocity);
                        world.spawnEntity(projectile);
                    }, i);
                }
            }, 40);

            addEvent(() -> this.setFightMode(false), 110);
            addEvent(() -> {
                this.setCrystalAttack(false);

            }, 110);
        }
    };
    // Dash Pierce
    private final Consumer<EntityLivingBase> dashPierce = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setPierceAttack(true);
            addEvent(() -> this.playSound(ModSoundHandler.BOSS_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f)), 21);


            addEvent(() -> {
                Vec3d targetPosition = target.getPositionVector();
                addEvent(() -> {
                    startFlameSpawns = true;
                    ModUtils.leapTowards(this, targetPosition, 0.9f, 0.0f);
                    this.playSound(ModSoundHandler.BOSS_DASH, 0.7f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                }, 17);


                addEvent(()-> this.lockLook = true, 30);
                addEvent(() -> {
                    for (int i = 0; i < 20; i += 5) {
                        addEvent(() -> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.75, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
                            float damage = this.getAttack() * ModConfig.pierce_multiplier;
                            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
                        }, i);
                    }
                }, 17);
            }, 30);

            addEvent(() -> startFlameSpawns = false, 67);
            addEvent(()-> this.lockLook = false, 70);
            addEvent(() -> {
                this.setPierceAttack(false);
                this.setFightMode(false);
            }, 80);
        }
    };
    //Circle Dash
    private final Consumer<EntityLivingBase> circleDash = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setSpinStart(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 12);
            addEvent(() -> {
                this.setSpinStart(false);
                this.setSpinCycle(true);
            }, 30);
            addEvent(() -> {

                    for (int i = 0; i < 110; i += 5) {
                        addEvent(() -> {
                            if(this.isSpinCycle()) {
                                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.6f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                            }
                        }, i);
                    }

            }, 30);
            addEvent(() -> {
                if (this.isSpinCycle()) {
                    this.setSpinCycle(false);
                    this.setFightMode(false);
                }
            }, 150);
        }
    };
    //Circle Attack - A Continuation of the Circle Dash
    private final Consumer<EntityLivingBase> circleAttack = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setSpinAttack(true);
            addEvent(() -> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack() * ModConfig.circle_multiplier;
                ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            }, 4);

            addEvent(() -> {
                this.setFightMode(false);
                this.setSpinAttack(false);
            }, 30);
        }
    };


    //Summon Ground Crystals
    private final Consumer<EntityLivingBase> summonGroundCrystals = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setSummonAttack(true);
            addEvent(() -> {

            }, 10);
            addEvent(() -> {
                Vec3d targetPosition = target.getPositionVector();
                Vec3d throwerPosition = this.getPositionVector();
                Vec3d dir = targetPosition.subtract(throwerPosition).normalize();
                AtomicReference<Vec3d> spawnPos = new AtomicReference<>(throwerPosition);

                for (int t = 0; t < 7; t += 1) {
                    int additive = t;
                    addEvent(() -> {

                        ModUtils.lineCallback(throwerPosition.add(dir), throwerPosition.add(dir.scale(additive)), additive * 2, (pos, r) -> {
                            spawnPos.set(pos);
                        });
                        Vec3d initPos = spawnPos.get();
                        EntityGroundCrystal crystal = new EntityGroundCrystal(this.world);
                        BlockPos blockPos = new BlockPos(initPos.x, initPos.y, initPos.z);
                        crystal.setPosition(blockPos);
                        this.world.spawnEntity(crystal);

                    }, t);
                }
            }, 20);


            addEvent(() -> this.setFightMode(false), 40);
            addEvent(() -> this.setSummonAttack(false), 40);
        }
    };
    //Hammer Slam Attack
    private final Consumer<EntityLivingBase> hammerSLAM = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setHammerStart(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_DRAW_HAMMER, 0.9f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 12);
            addEvent(() -> {
                this.setHammerStart(false);
                this.setHammerCycle(true);
                //Set's into a continuation where until the requirements are met, the cycle will continue
            }, 40);

            addEvent(() -> {
                if (isHammerCycle()) {
                    this.setHammerCycle(false);
                    this.setFightMode(false);
                }
            }, 200);
        }
    };
    //Hammer PT 2 Attack
    private final Consumer<EntityLivingBase> hammerExplosion = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setHammerAttack(true);
            addEvent(()-> this.lockLook = true, 10);
            addEvent(() -> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.0, 0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack() * ModConfig.hammer_multiplier;
                float explostionFactor = ModConfig.explosion_size;
                ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.9f, 1, false);
                this.world.newExplosion(this, offset.x, offset.y, offset.z, explostionFactor, false, true);
            }, 20);

            addEvent(() -> this.setFightMode(false), 40);
            addEvent(() -> this.setHammerAttack(false), 35);
            addEvent(()-> this.lockLook = false, 35);
        }
    };
    //Anime Pierce Strike
    private final Consumer<EntityLivingBase> animeStrike = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            targetFloating = true;
            //Repeat Attack Times 3
            addEvent(() -> {
                this.meleeSwitch = true;
                this.rangeSwitch = false;
                new ActionAerialTeleport(ModColors.AZURE).performAction(this, target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.3F));
                addEvent(() -> {
                    this.setMultiPierceAttack(true);
                }, 20);
                addEvent(() -> {
                    ModUtils.leapTowards(this, target.getPositionVector(), 0.8f, -0.4f);
                    this.playSound(ModSoundHandler.BOSS_DASH, 0.7f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                    for (int t = 0; t < 15; t += 5) {
                        addEvent(() -> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = this.getAttack() * ModConfig.pierce_multiplier;
                            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.3f, 0, false);
                        }, t);
                    }

                }, 60);
                addEvent(() -> {
                    this.setMultiPierceAttack(false);

                }, 100);
            }, 0);


            addEvent(() -> {
                this.meleeSwitch = true;
                this.rangeSwitch = false;
                new ActionAerialTeleport(ModColors.AZURE).performAction(this, target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.3F));
                addEvent(() -> {
                    this.setMultiPierceAttack(true);
                }, 20);
                addEvent(() -> {
                    ModUtils.leapTowards(this, target.getPositionVector(), 0.8f, -0.4f);
                    this.playSound(ModSoundHandler.BOSS_DASH, 0.7f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                    for (int t = 0; t < 15; t += 5) {
                        addEvent(() -> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage =  this.getAttack() * ModConfig.pierce_multiplier;
                            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.3f, 0, false);
                        }, t);
                    }

                }, 60);
                addEvent(() -> {
                    this.setMultiPierceAttack(false);

                }, 100);
            }, 120);


            addEvent(() -> {
                this.meleeSwitch = true;
                this.rangeSwitch = false;
                new ActionAerialTeleport(ModColors.AZURE).performAction(this, target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.3F));
                addEvent(() -> {
                    this.setMultiPierceAttack(true);
                }, 20);
                addEvent(() -> {
                    ModUtils.leapTowards(this, target.getPositionVector(), 0.8f, -0.4f);
                    this.playSound(ModSoundHandler.BOSS_DASH, 0.7f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                    for (int t = 0; t < 15; t += 5) {
                        addEvent(() -> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = this.getAttack() * ModConfig.pierce_multiplier;
                            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.3f, 0, false);
                        }, t);
                    }
                }, 60);

                addEvent(() -> {

                    this.setMultiPierceAttack(false);
                    targetFloating = false;
                    this.rangeSwitch = true;
                }, 100);
            }, 240);

            addEvent(() -> this.setFightMode(false), 360);
        }
    };

    //Summon Shulkers
    private final Consumer<EntityLivingBase> summonShulkers = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setShulkerAttack(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_CAST_AMBIENT, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 25);
            addEvent(() -> {
                //Changing this to be slower but longer
                for (int i = 0; i < 60; i += 10) {
                    addEvent(() -> {
                        EntityShulkerBullet projectile = new EntityShulkerBullet(this.world);
                        Vec3d targetPos = target.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 12, 0)));
                        BlockPos initPos = new BlockPos(targetPos.x, targetPos.y, targetPos.z);
                        projectile.setPosition(initPos.getX(), initPos.getY(), initPos.getZ());
                        this.world.spawnEntity(projectile);
                    }, i);
                }
            }, 30);
            addEvent(() -> {
                this.setFightMode(false);
                this.setShulkerAttack(false);
            }, 50);
        }
    };

    private final Consumer<EntityLivingBase> summonAOEAttack = (target) -> {
      this.setFightMode(true);
      this.setShulkerAttack(true);
      //Summons an AOE wave

        addEvent(() -> this.playSound(ModSoundHandler.BOSS_CAST_AMBIENT, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f)), 25);

      addEvent(()-> {
            //commented out other variant due to lag issues and overall being to OP
              new ActionCircleAOE().performAction(this, target);

      }, 30);

      addEvent(()-> {
          this.setFightMode(false);
          this.setShulkerAttack(false);
      }, 50);
    };
    //Summon Crystals with Hammer
    private Consumer<EntityLivingBase> hammerProjectile = (target)-> {
        this.setFightMode(true);
        this.setHammerProjectile(true);
            new ActionVollet(crystalBallProjectile, 0.55f).performAction(this, target);

        addEvent(()-> this.setFightMode(false), 80);
        addEvent(()-> this.setHammerProjectile(false), 40);
    };

    @Override
    public void travel(float strafe, float vertical, float forward) {
        ModUtils.aerialTravel(this, strafe, vertical, forward);
    }

    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(ModReference.MOD_ID, "lamentor");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_BOSS;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }
    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.BOSS_IDLE;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.BOSS_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.BOSS_DEATH;
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.0001f);
        this.setDeadAnim(true);
        this.setImmovable(true);
        if(this.isDeathAnim()) {
            addEvent(()-> this.setImmovable(false), 90);
            addEvent(()-> this.playSound(ModSoundHandler.BOSS_DEATH, 1.5f, 1.0f), 0);
            addEvent(()-> this.setDeadAnim(false), 90);
            addEvent(()-> this.setDead(), 90);
            addEvent(()-> this.setDropItemsWhenDead(true), 90);

        }
        super.onDeath(cause);
    }
}
