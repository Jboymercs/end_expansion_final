package com.example.structure.entity.endking;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityEye;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.ai.*;
import com.example.structure.entity.endking.EndKingAction.*;
import com.example.structure.entity.endking.ghosts.EntityPermanantGhost;
import com.example.structure.entity.util.IAttack;
import com.example.structure.entity.util.TimedAttackIniator;
import com.example.structure.util.*;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EntityEndKing extends EntityAbstractEndKing implements IAnimatable, IAttack, DirectionalRender, IAnimationTickable {

    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_SWEEP_LEAP = "sweepLeap";
    private final String ANIM_FIRE_BALL = "fireBall";
    private final String SUMMON_AOE_CRYSTALS = "crystals";

    private final String ANIM_CAST_SWORD = "cast_sword";
    private final String ANIM_SLAM_ATTACK = "slam";

    //PHASE TWO
    private final String ANIM_SIDE_SWIPE = "side_attack";
    private final String ANIM_TOP_SWIPE = "upper_attack";
    private final String ANIM_COMBO_LINE = "combo_1";

    private final String ANIM_CAST_ARENA = "castArena";

    public static DataParameter<Boolean> SET_SPAWN_LOC_KING = EntityDataManager.createKey(EntityEndKing.class, DataSerializers.BOOLEAN);

    public static DataParameter<BlockPos> SPAWN_LOCATION = EntityDataManager.createKey(EntityEndKing.class, DataSerializers.BLOCK_POS);

    //PHASE THREE

    private final String ANIM_FLY = "fly";
    private final String ANIM_FLY_ARMS = "fly_arms";
    private final String ANIM_HALO = "halo";
    private final String ANIM_PHASE_INTRO = "phase_intro";

    private final String ANIM_PHASE_UPPER_ATTACK = "phase_upper_attack";
    private final String ANIM_PHASE_FIRE_BALL = "phase_fireBall";

    private final String ANIM_PHASE_SIDE_SWIPE = "phase_side_attack";

    private final String ANIM_PHASE_COMBO = "phase_combo";

    private final String ANIM_PHASE_CAST_SWORD = "phase_cast_sword";

    private final String ANIM_PHASE_SUPER_SWING = "superSwing";

    private final String ANIM_LAZER_BEAM = "lazerBeam";

    private final String ANIM_FLY_DASH = "fly_dash";



    //For Starting

    private final String ANIM_BOSS_STALL = "stall";
    private final String ANIM_START_BOSS = "awaken";

    private final String ANIM_DEATH_BOSS = "death";

    public boolean isSetSpawnLoc() {
        return this.dataManager.get(SET_SPAWN_LOC_KING);
    }
    public void setSetSpawnLoc(boolean value) {
        this.dataManager.set(SET_SPAWN_LOC_KING, Boolean.valueOf(value));
    }

    public void setSpawnLocation(BlockPos pos) {
        this.dataManager.set(SPAWN_LOCATION, pos);
    }

    public BlockPos getSpawnLocation() {
        return this.dataManager.get(SPAWN_LOCATION);
    }

    private final EntityAIBase flyattackAi = new EntityAerialKingAttack(this, 24, 1, 30, new TimedAttackIniator<>(this, 20));
    private final EntityAIBase basAttackAi = new EntityKingTimedAttack<>(this, 1.0, 60, 24.0f, 0.4f);
    private Consumer<EntityLivingBase> prevAttack;

    public EntityEndKing(World world) {
        super(world);
        this.healthScaledAttackFactor = MobConfig.king_scaled_factor;
        this.experienceValue = 4500;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Set_Spawn_Loc_King", this.dataManager.get(SET_SPAWN_LOC_KING));
        nbt.setInteger("Spawn_Loc_X", this.getSpawnLocation().getX());
        nbt.setInteger("Spawn_Loc_Y", this.getSpawnLocation().getY());
        nbt.setInteger("Spawn_Loc_Z", this.getSpawnLocation().getZ());
    }


    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.dataManager.set(SET_SPAWN_LOC_KING, nbt.getBoolean("Set_Spawn_Loc_King"));
        this.setSpawnLocation(new BlockPos(nbt.getInteger("Spawn_Loc_X"), nbt.getInteger("Spawn_Loc_Y"), nbt.getInteger("Spawn_Loc_Z")));
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SET_SPAWN_LOC_KING, Boolean.valueOf(false));
        //
        this.dataManager.register(SPAWN_LOCATION, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        animationData.addAnimationController(new AnimationController(this, "phase_intro_controller", 0, this::predicatePhaseIntro));
        animationData.addAnimationController(new AnimationController(this, "fly_phase_3_controller", 0, this::predicateFly));
        animationData.addAnimationController(new AnimationController(this, "predicate_hale_controller", 0, this::predicateHaloe));
        animationData.addAnimationController(new AnimationController(this, "predicate_starting", 0, this::predicateBossStart));
        animationData.addAnimationController(new AnimationController(this, "predicate_fly_arm_h", 0, this::predicateFlyArms));
    }

    public int switchTimer = 400;


    public int waitTimer = 60;


    @Override
    public void onUpdate() {
        super.onUpdate();

        if(this.getSpawnLocation() != null && this.isSetSpawnLoc()) {
            Vec3d SpawnLoc = new Vec3d(this.getSpawnLocation().getX(), this.getSpawnLocation().getY(), this.getSpawnLocation().getZ());

            double distSq = this.getDistanceSq(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
            double distance = Math.sqrt(distSq);
            //This basically makes it so if the Ashed King is too far away from the Arena, he will teleport back
            if(!world.isRemote) {
                if (distance > 30) {
                    this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                    //Also teleport the King if his position gets too Low from the Spawn Point
                } else if (SpawnLoc.y - 20 > this.posY) {
                    this.teleportTarget(SpawnLoc.x, SpawnLoc.y, SpawnLoc.z);
                }
            }
        }

        if(this.IPhaseThree && !this.hasPlayedPhaseAnimation && !world.isRemote) {
            playPhase3Animation(this.world);
        }

        //This is a check for if the boss has enter Phase 3 and then healed back into phase two
        if(this.IPhaseTwo && !this.IPhaseThree && this.sendPing && !this.hasSwitchedStates) {
            if(waitTimer < 0) {
                this.switchBackTooOtherStates();
            } else {
                waitTimer--;
            }
        }

        if(this.IPhaseThree && !this.IPhaseTwo) {

                //When Phase 3 and currently not in Phase Animation, this timer will alter the boss
                //Between a melee mode and a ranged mode while the ghost mimics opposite attacks
                //This timer has been taken from the Lamentor
            if(!this.isDeathBoss() && !this.isPhaseIntro()) {
                if (this.isRangedMode && switchTimer < 0 && renderLazerPos == null) {
                    //Switch to Melee Mode
                    this.isMeleeMode = true;
                    switchTimer = 400;
                    this.isRangedMode = false;
                }
                if (this.isMeleeMode && switchTimer < 0) {
                    //Switch to Ranged Mode
                    this.isRangedMode = true;
                    switchTimer = 400;
                    this.isMeleeMode = false;
                } else {

                    switchTimer--;
                }
            }
        }

        //A small handler for lowering the King while in Melee Mode
        if(this.isMeleeMode && !this.isRangedMode) {
            if(!world.getBlockState(this.getPosition().add(new BlockPos(0, -2, 0))).isFullBlock()) {
                this.motionY -= 0.05;
            }
        } else if(this.isRangedMode && !this.isMeleeMode) {
            for(int i = 0; i < 3; i++) {
                if(world.getBlockState(this.getPosition().add(0, -i, 0)).isFullBlock()) {
                    this.motionY += 0.05;
                }
            }
        }

    }


    public void teleportTarget(double x, double y, double z) {
        this.setPosition(x , y, z);

    }


    protected boolean beginAI = false;
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        EntityLivingBase target = this.getAttackTarget();

        //Destroys Blocks while this is Dashing if the config option enables it
        if(this.isFlyDashMove() && MobConfig.bosses_of_mass_destruction) {
            AxisAlignedBB box = getEntityBoundingBox().grow(1.1, 0.1, 1.1).offset(0, -0.1, 0);
            ModUtils.destroyBlocksInAABB(box, world, this);
        }

        //Suffocation check
        if(this.hurtTime > 0 && world.getBlockState(this.getPosition()).isFullBlock() && world.getBlockState(this.getPosition().up()).isFullBlock() &&
        world.getBlockState(this.getPosition().add(0, 2,0)).isFullBlock()) {
            AxisAlignedBB box = getEntityBoundingBox().grow(1.1, 0.1, 1.1).offset(0, -0.1, 0);
            ModUtils.destroyBlocksInAABB(box, world, this);
            this.setPosition(this.getPosition());
        }

        //This is used in Phase 3 for when the boss is flying
        if(this.IPhaseThree && target != null && !this.isBeingRidden() && !this.isMeleeMode && !this.isDeathBoss() && !this.isPhaseIntro()) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if(distance < 12) {
                double d0 = (this.posX - target.posX) * 0.015;
                double d1 = (this.posY - target.posY) * 0.01;
                double d2 = (this.posZ - target.posZ) * 0.015;
                this.addVelocity(d0, d1, d2);
            }
        }

        //This is used to keep the boss away in it's first phase
        if(!this.IPhaseTwo && !this.IPhaseThree && !this.isBossStall() && !this.isBossStart() && !this.isImmovable() && target != null && !this.isBeingRidden()) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if(distance < 4) {
                double d0 = (this.posX - target.posX) * 0.015;
                double d1 = (this.posY - target.posY) * 0.01;
                double d2 = (this.posZ - target.posZ) * 0.015;
                this.addVelocity(d0, d1, d2);
            }
        }
        if(!this.isBossStart() && !this.isBossStall() && !beginAI) {
            this.initBossAi();
        }
    }

    protected boolean hasSwitchedStates = false;
    protected void switchBackTooOtherStates() {
        //This is too switch the boss back to other default instances incase the player dies during phase 3 when the boss is healing back it's health
        this.navigator = new MobGroundNavigate(this, world);
        this.hasSwitchedStates = true;
        this.sendPing = false;
        waitTimer = 60;
    }

    protected void playPhase3Animation(World world) {
        this.hasPlayedPhaseAnimation = true;
        this.setPhaseIntro(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.playSound(ModSoundHandler.KING_TRANSFORM, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
        //Rid of any previous ghosts in the area
        List<EntityPermanantGhost> nearbyGhosts = this.world.getEntitiesWithinAABB(EntityPermanantGhost.class, this.getEntityBoundingBox().grow(50D), e -> !e.getIsInvulnerable());
        if(!nearbyGhosts.isEmpty()) {
            for(EntityPermanantGhost ghost : nearbyGhosts) {
                ghost.setDead();
            }
        }

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + "Ashed King: " + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "king_transform_1")));
            }
            //Transform Dialog
        }, 30);


        //After Playing the Animation, It will basically set itself to flight attack methods and as well remove previous tasks
        addEvent(()-> {
            this.setFullBodyUsage(true);
            this.setPhaseIntro(false);
            this.setImmovable(false);
            this.sendPing = true;
            this.hasSwitchedStates = false;
            //Set Initial Change to Ranged
            this.isRangedMode = true;
            this.tasks.addTask(4, flyattackAi);
            this.moveHelper = new EntityFlyMoveHelper(this);
            this.navigator = new PathNavigateFlying(this, world);

            //Spawn Ghost Entity
            if(!world.isRemote) {
                new EntityPermanantGhost(this.world).onSummon(this.getPosition(), this);
            }
        }, 90);
    }

    public void onSummon(BlockPos Pos, Projectile actor) {
        BlockPos offset = Pos.add(new BlockPos(0,0,0));
        this.setPosition(offset);
        this.setSpawnLocation(offset);
        this.setSetSpawnLoc(true);
        world.spawnEntity(this);
        double healthChange = this.getHealth() / this.getMaxHealth();
        if(healthChange == 1) {
            this.setBossStall(true);
        }
        actor.setDead();
    }


    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(this.isPhaseHandler()) {
            ModUtils.aerialTravel(this, strafe, vertical, forward);
        }else {
            super.travel(strafe, vertical, forward);
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    //ANIMATION HANDLERS BEGIN

    private<E extends IAnimatable> PlayState predicateHaloe(AnimationEvent<E> event) {
        //Halo Handler for Phase 3
        if(this.isPhaseHandler() && !this.isPhaseIntro()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HALO, true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateBossStart(AnimationEvent<E> event) {
        if(this.isBossStall()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BOSS_STALL, true));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable>PlayState predicateFly(AnimationEvent<E> event) {
        //Phase 3 - Change to Flight

        if(this.isPhaseHandler() && !this.isPhaseIntro() && !this.isDeathBoss()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY, true));

            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    //Small handler for the arms of the King
    private  <E extends IAnimatable>PlayState predicateFlyArms(AnimationEvent<E> event) {
        if(!this.isDeathBoss()) {
            if (this.isPhaseHandler() && !this.isSwingingArms() && !this.isFullBodyUsage()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY_ARMS, true));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable>PlayState predicatePhaseIntro(AnimationEvent<E> event) {
        //Phase 3 - Intro To Boss Upgrade
        if(this.isPhaseIntro()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_INTRO, false));
            return PlayState.CONTINUE;
        }

        //Boss start
        if(this.isBossStart()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_START_BOSS, false));
            return PlayState.CONTINUE;
        }

        //Boss Death
        if(this.isDeathBoss()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH_BOSS, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        //The default usage of the End King PHASE ONE & PHASE TWO
        if(!this.isFullBodyUsage() && !this.isPhaseHandler() && !this.isBossStall() && !this.isBossStart()) {
            //Fix 23 - End King deciding he doesn't want to stop walking, as there is no existing lower_idle Animation
            if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
                return PlayState.CONTINUE;
            }

        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        //The default usage of the End King PHASE ONE & PHASE TWO
        if(!this.isSwingingArms() && !this.isFullBodyUsage() && !this.isPhaseHandler() && !this.isBossStall() && !this.isBossStart() && !this.isDeathBoss()) {

            if(event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        //Handles All Attacks
        if(this.isFightMode() && !this.isPhaseIntro() && !this.isBossStall() && !this.isBossStart() && !this.isDeathBoss()) {
            if(this.isLeapSweepAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWEEP_LEAP, false));
            }
            if(this.isSummonCrystalsAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_AOE_CRYSTALS, false));
            }
            if(this.isSlamAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SLAM_ATTACK, false));
            }
            if(this.isSummonFireBallsAttack()) {
                if(this.isPhaseHandler()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_FIRE_BALL, false));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FIRE_BALL, false));
                }
            }
            if(this.isSummonGhosts()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_AOE_CRYSTALS, false));
            }
            if(this.isUpperAttack()) {
                if(this.isPhaseHandler()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_UPPER_ATTACK, false));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TOP_SWIPE, false));
                }

            }
            if(this.isSideAttack()) {
                if(this.isPhaseHandler()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_SIDE_SWIPE, false));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SIDE_SWIPE, false));
                }
            }
            if(this.isComboAttack()) {
                if(this.isPhaseHandler()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_COMBO, false));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_LINE, false));
                }

            }
            if(this.isCastArena()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_ARENA, false));
            }

            if(this.isGroundSwords()) {
                if(this.isPhaseHandler()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_CAST_SWORD, false));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_SWORD, false));
                }
            }
            if(this.isMultipleStrikes()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PHASE_SUPER_SWING, false));
            }
            if(this.isLazerAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LAZER_BEAM, false));
            }
            if(this.isFlyDashMove()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLY_DASH, false));
            }
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    //ANIMATION HANDLERS END



    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
    }



    public void initBossAi() {
        this.tasks.addTask(4,  new EntityKingTimedAttack<>(this, 1.0, 60, 24.0f, 0.4f));
        beginAI = true;
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

    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    Supplier<Projectile> projectileSupplierSpinSword = () -> new ProjectileSpinSword(world, this, 6.0f);
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        //Phase One
        if(!this.isFightMode() && !this.IPhaseTwo && !this.IPhaseThree && !this.isPhaseIntro() && !this.isBossStall() && !this.isBossStart()) {
            //attacks
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(sweepLeap, crystalSelfAOE, projectileSwords,throwFireball, castArenaAttack, summon_ground_swords, slam_Attack));
            //weights
            double[] weights = {
                    (distance < 24 && prevAttack != sweepLeap) ? distance * 0.02 : 0, //LeapAttack
                    (distance < 7 && prevAttack != crystalSelfAOE) ? 1/distance : 0,  //Crystal Self AOE
                    (distance > 5 && !hasSwordsNearby) ? distance * 0.02 : 0, // Projectile Swords Attack
                   (distance > 8 && prevAttack != throwFireball) ? distance * 0.02 : 0, //Throw Fireball Attack
                    (distance < 25 && HealthChange < 0.9 && !hasEyesNearby && prevAttack != castArenaAttack) ? distance * 0.02 : 0, //Cast Arena Eye Attack
                    (distance < 25 && prevAttack != summon_ground_swords) ? distance * 0.03 : 0, //Summon Ground Swords
                    (distance < 9 && prevAttack != slam_Attack) ? 1/distance : 0 //Slam Ground Attack
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        //Phase Two
        if(!this.isFightMode() && this.IPhaseTwo && !this.IPhaseThree && !this.isPhaseIntro()) {
            //attacks
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(sweepLeap, summonGhosts, upperAttack, sideAttack, regularAttack, slam_Attack));
            //weights
            double[] weights = {
                    (distance < 25 && prevAttack != sweepLeap) ? distance * 0.02 : 0, //LeapAttack
                    (distance < 25 && prevAttack != summonGhosts) ? distance * 0.02 : 0, //Summon Ghosts
                    (distance < 13 && distance > 5 && prevAttack != upperAttack) ? distance * 0.02 : 0,  //Upper Attack
                    (distance < 7 && prevAttack != sideAttack) ? distance * 0.02 : 0,   //Side Swipe
                    (distance < 3 && prevAttack != regularAttack) ? 1/distance : 0,  //Close Regular Attack
                    (distance < 9 && prevAttack != slam_Attack) ? distance * 0.02 : 0 //Slam Ground Attack
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        //Phase Three
        if(!this.isFightMode() && !this.IPhaseTwo && this.IPhaseThree && !this.isPhaseIntro()) {
            //Attacks
            if(this.isRangedMode && !this.isMeleeMode && !this.isDeathBoss()) {
                //Ranged Attacks
                List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(throwFireball, summon_ground_swords, projectileSwords, lazer_beam_attack));
                //weights
                double[] weights = {
                        (distance > 2) ? distance * 0.02 : 0, //Throw Fireball Attack
                        (distance < 25 && prevAttack != summon_ground_swords) ? distance * 0.03 : 0, //Summon Ground Swords
                        (distance > 2 && !hasSwordsNearby) ? distance * 0.02 : 0, // Projectile Swords Attack
                        (distance > 2 && prevAttack != lazer_beam_attack) ? distance * 0.02 : 0 //Lazer Beam Attack
                };
                prevAttack = ModRand.choice(attacks, rand, weights).next();
                prevAttack.accept(target);
            } else if(this.isMeleeMode && !this.isRangedMode && !this.isDeathBoss()) {
                //Melee Attacks
                List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(upperAttack, sideAttack, regularAttack, multiple_strikes, flyDash));
                //weights
                double[] weights = {
                        (distance < 13 && distance > 5 && prevAttack != upperAttack) ? distance * 0.02 : 0, //Upper Attack
                        (distance < 8 && prevAttack != sideAttack) ? distance * 0.02 : 0, //Side Attack PHASE
                        (distance < 3 && prevAttack != regularAttack) ? 1/distance : 0,  //Close Regular Attack PHASE
                        (distance < 8 && prevAttack != multiple_strikes) ? distance * 0.02 : 0, //New Phase Attack the circle Swing
                        (distance < 25 && distance >= 8) ? distance * 0.02 : 0 //Fly Dash Move to get the King to the target quicker
                };
                prevAttack = ModRand.choice(attacks, rand, weights).next();
                prevAttack.accept(target);
            }
        }
        return (this.IPhaseTwo) ? 20 : 60;
    }

    //Leap Attack Quick
    private final Consumer<EntityLivingBase> sweepLeap = (target) -> {
        this.setImmovable(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setLeapSweepAttack(true);
      this.ActionDashForward(target);

      addEvent(()-> {
          this.lockLook = true;
          this.setImmovable(false);
          this.playSound(ModSoundHandler.KING_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
      }, 10);
      addEvent(()-> {

          for(int i = 0; i < 20; i += 5) {
              addEvent(()-> {
                  Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.5f));
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                  float damage = (float) (this.getAttack() * MobConfig.end_king_leap_attack);
                  ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
              }, i);
          }

      }, 15);
      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setLeapSweepAttack(false);
        this.lockLook = false;
      }, 30);

      if(this.IPhaseTwo) {
          addEvent(()-> this.setFightMode(false), 60);
      } else {
          addEvent(()-> this.setFightMode(false), 30);
      }
    };

    //SLam Attack
    private final Consumer<EntityLivingBase> slam_Attack = (target) -> {
      this.setFightMode(true);
      this.setSlamAttack(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        this.playSound(ModSoundHandler.KING_PREPARE_SLAM, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));

      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          float distance = getDistance(target);
          this.setImmovable(false);
          ModUtils.leapTowards(this, targetedPos, (float) (0.6 * Math.sqrt(distance)), 0.5f);
      }, 22);
      addEvent(()-> {
          this.playSound(ModSoundHandler.KING_IMPACT, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
          Vec3d position = this.getPositionVector();
          EntityLargeAOEEffect effect = new EntityLargeAOEEffect(world);
          effect.setPosition(position.x, position.y, position.z);
          world.spawnEntity(effect);
          Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.0f));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (float) (this.getAttack());
          ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 1.8f, 0, false);
      }, 35);

      addEvent(()-> {
        this.setFullBodyUsage(false);
        this.setFightMode(false);
        this.setSlamAttack(false);
        this.setImmovable(false);
      }, 45);
    };


    //A simple Fly Move towards it's Target
    private final Consumer<EntityLivingBase> flyDash = (target)-> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setFlyDashMove(true);

      addEvent(()-> this.playSound(ModSoundHandler.KING_DASH, 1.4f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f)), 15);

      addEvent(()-> this.lockLook = true, 10);
      addEvent(()-> {
        //Leap Forward
          Vec3d targetPos = target.getPositionVector();
          float distance = getDistance(target);
          ModUtils.leapTowards(this, targetPos, (float) (0.45 * Math.sqrt(distance)), 0.3f);
            //hahahah this boss is gonna kill youuuuuuu
          for(int i = 0; i < 15; i += 5) {
              addEvent(()-> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0,1.4,0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = (float) (this.getAttack());
                ModUtils.handleAreaImpact(1.5F, (e)-> damage, this, offset, source, 0.8F, 0, false);
              }, i);
          }
      }, 15);

      addEvent(()-> {
          this.lockLook = false;
          this.setFlyDashMove(false);
        this.setFullBodyUsage(false);
        this.setFightMode(false);
      }, 40);
    };

    //Cast Arena Attack
    private final Consumer<EntityLivingBase> castArenaAttack = (target) -> {
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setCastArena(true);
      this.setImmovable(true);

      addEvent(()-> {
          this.playSound(ModSoundHandler.KING_SUMMON_EYE, 1.4f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
          EntityEye eye = new EntityEye(world);
          Vec3d position = this.getPositionVector().add(ModUtils.yVec(9));
          eye.setPosition(position.x, position.y, position.z);
          world.spawnEntity(eye);
      }, 35);
      addEvent(()-> {
          this.setFightMode(false);
          this.setFullBodyUsage(false);
          this.setCastArena(false);
          this.setImmovable(false);
      }, 60);
    };


    //SElf AOE Attack
    private final Consumer<EntityLivingBase> crystalSelfAOE = (target)-> {
      this.setFightMode(true);
      this.setImmovable(true);
      this.setSummonCrystalsAttack(true);
      this.setSwingingArms(true);

      addEvent(()-> this.playSound(ModSoundHandler.KING_CAST, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f)), 15);
      addEvent(()-> new ActionAOESimple().performAction(this, target), 20);

      addEvent(()-> this.setSummonCrystalsAttack(false), 23);
      addEvent(()-> this.setImmovable(false),23);
      addEvent(()-> this.setSwingingArms(false), 23);
      addEvent(()-> this.setFightMode(false), 25);


    };

    //Projectile Swords Attack
    private final Consumer<EntityLivingBase> projectileSwords = (target) -> {
      this.setFightMode(true);
      new ActionHoldSwordAttack(projectileSupplierSpinSword, 2.0f).performAction(this, target);

      addEvent(()-> this.setFightMode(false), 80);
    };

    Supplier<EntityFireBall> fireBallSupplier = () -> new EntityFireBall(world);


    //Throw Fireball
    private final Consumer<EntityLivingBase> throwFireball = (target)-> {
      this.setFightMode(true);
      this.setSummonFireballsAttack(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        this.playSound(ModSoundHandler.KING_FIREBALL, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
    new ActionThrowFireball(fireBallSupplier, 2.5f).performAction(this, target);

        addEvent(()-> this.setImmovable(false), 35);
        addEvent(()-> this.setFullBodyUsage(false), 35);
        addEvent(()-> this.setSummonFireballsAttack(false), 35);
      addEvent(()-> this.setFightMode(false), 80);
    };

    //Summon Ghosts
    private final Consumer<EntityLivingBase> summonGhosts = (target) -> {
      this.setFightMode(true);
      this.setSummonGhostsAttack(true);
      this.setSwingingArms(true);

      addEvent(()-> new ActionSummonGhosts().performAction(this, target), 13);
        addEvent(()-> this.playSound(ModSoundHandler.KING_CAST, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f)), 15);
      addEvent(()-> {
          this.setSwingingArms(false);
        this.setSummonGhostsAttack(false);
      }, 25);

      addEvent(()-> this.setFightMode(false), 25);
    };

    private final Consumer<EntityLivingBase> regularAttack = (target)-> {
      this.setComboAttack(true);
      this.setFightMode(true);
      this.setSwingingArms(true);
      addEvent(()-> this.playSound(ModSoundHandler.KING_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f)), 5);

      addEvent(()-> {
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 21);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.4f, 0, false);
      }, 23);

      addEvent(()-> {
        this.setFightMode(false);
        this.setComboAttack(false);
        this.setSwingingArms(false);
      }, 30);
    };

    private final Consumer<EntityLivingBase> upperAttack = (target) -> {
      this.setUpperAttack(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
        this.playSound(ModSoundHandler.KING_TOP_SWIPE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
      addEvent(()-> this.lockLook = true, 15);
      addEvent(()-> {
          Vec3d targetPos = target.getPositionVector();
          float distance = getDistance(target);
          addEvent(()-> {
              this.setImmovable(false);
              if(this.isPhaseHandler()) {
                  ModUtils.leapTowards(this, targetPos, (float) (0.20 * Math.sqrt(distance)), 0.1f);
              } else {
                  ModUtils.leapTowards(this, targetPos, (float) (0.45 * Math.sqrt(distance)), 0.3f);
              }

          }, 10);
      }, 7);

      addEvent(()-> {
          this.setImmovable(true);
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(3.5, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (float) (this.getAttack() * MobConfig.end_king_ghost_damage);
          ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.7f, 0, false);
      }, 30);

        addEvent(()-> this.lockLook = false, 45);
      addEvent(()-> {
        this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setUpperAttack(false);
      }, 45);


          addEvent(()-> this.setFightMode(false), 45);

    };

    private final Consumer<EntityLivingBase> sideAttack = (target) -> {
      this.setSwingingArms(true);
      this.setSideAttack(true);
      this.setFightMode(true);
        this.playSound(ModSoundHandler.KING_SIDE_SWIPE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
      addEvent(()-> this.lockLook = true, 10);
      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(3.5, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (float) (this.getAttack() * MobConfig.end_king_ghost_damage);
          ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);

      }, 21);

      addEvent(()-> this.lockLook = false, 25);
      addEvent(()-> {
        this.setSwingingArms(false);
        this.setSideAttack(false);
        this.setFightMode(false);
      }, 35);
    };

    private final Consumer<EntityLivingBase> summon_ground_swords = (target) -> {
      this.setGroundSword(true);
      this.setFightMode(true);
      this.setSwingingArms(true);
        this.lockLook =true;
        this.playSound(ModSoundHandler.KING_THROW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
        addEvent(()-> {
            //Summon Ground Sword Variant
            if(this.isPhaseHandler()) {
                //Makes them faster
                new ActionSummonSwordAttacks(true).performAction(this, target);
            } else {
                new ActionSummonSwordAttacks(false).performAction(this, target);
            }
        }, 35);
      addEvent(()-> {
          this.lockLook =false;
          this.setSwingingArms(false);
          this.setFightMode(false);
        this.setGroundSword(false);
      }, 40);
    };

    //New Melee Attack
    private final Consumer<EntityLivingBase> multiple_strikes = (target) -> {
      this.setSwingingArms(true);
      this.setFightMode(true);
      this.setMultipleStrikes(true);
        this.playSound(ModSoundHandler.KING_DOUBLE_SWIPE, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
      addEvent(()-> this.lockLook = true, 15);
      addEvent(()-> {
        Vec3d targetPos = target.getPositionVector();
          float distance = getDistance(target);
        addEvent(()-> {
            ModUtils.leapTowards(this, targetPos, (float) (0.20 * Math.sqrt(distance)), 0.1f);
        }, 15);
      }, 5);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(3.6, 1.5, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = (float) (this.getAttack() * MobConfig.end_king_ghost_damage);
          ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
      }, 26);


        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(-3.6, 1.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (this.getAttack() * MobConfig.end_king_ghost_damage);
            ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
        }, 35);

        addEvent(()-> {this.setMultipleStrikes(false);
                    this.lockLook = false;
                    this.setSwingingArms(false);
                    this.setFightMode(false);
            }, 57);


    };

    private final Consumer<EntityLivingBase> lazer_beam_attack = (target) -> {
      this.setFightMode(true);
      this.setSwingingArms(true);
      this.setLazerAttack(true);
        this.playSound(ModSoundHandler.KING_LAZER, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
      //Lazer Attack
      addEvent(()-> {
          this.performLazerAttack =true;
        lazerAttack.doAction();
      }, 15);

      addEvent(()-> {
          this.performLazerAttack =false;
      }, 115);
      addEvent(()-> {
        this.setFightMode(false);
        this.setSwingingArms(false);
        this.setLazerAttack(false);
      }, 120);
    };


    public void ActionDashForward(EntityLivingBase target) {
        setPhaseMode(true);
        addEvent(()-> setPhaseMode(false), 5);
        addEvent(()-> setPhaseMode(true), 10);
        addEvent(()-> {
            Vec3d enemyPosToo = target.getPositionVector().add(ModUtils.yVec(1));
            addEvent(()-> {
            this.playSound(ModSoundHandler.KING_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            int randomDeterminedDistance = 8;
            Vec3d enemyPos = enemyPosToo;

            Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(getEyeHeight()));

            Vec3d dir = enemyPos.subtract(startPos).normalize();

            AtomicReference<Vec3d> teleportPos = new AtomicReference<>(enemyPos);

            ModUtils.lineCallback(enemyPos.add(dir),enemyPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {

                boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                boolean notOpen = ModUtils.cubePoints(-2, 1, -2, 2, 4, 2).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });
            this.chargeDir = teleportPos.get();
            this.setPositionAndUpdate(chargeDir.x, chargeDir.y, chargeDir.z);
            }, 10);
        }, 5);
        addEvent(()-> setPhaseMode(false), 25);
        addEvent(()-> {setPhaseMode(true);
                    this.damageViable = false;
            }   , 30);
        addEvent(()-> setPhaseMode(false), 35);
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 0.6F, 0.7f + ModRand.getFloat(0.3F));
    }

    @Override
    protected float playFlySound(float p_191954_1_)
    {
        if(this.isPhaseHandler()) {
            this.playSound(ModSoundHandler.KING_FLY, 0.8F, 1.0F);
        }
        return 1F;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    //TIME TO DO SOUNDS

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.KING_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.KNIGHT_IDLE;
    }


    /**
     * Add a bit of brightness to the entity, because otherwise it looks pretty black
     */
    @Override
    public int getBrightnessForRender() {
        return Math.min(super.getBrightnessForRender() + 30, 200);
    }

    private static final ResourceLocation LOOT_BOSS = new ResourceLocation(ModReference.MOD_ID, "end_king");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_BOSS;
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }


    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }
    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

protected boolean setDeathTooActive = false;
    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.0001f);
        this.setDeathBoss(true);
        this.setImmovable(true);
        this.isMeleeMode = false;
        this.isRangedMode = false;

        if(this.isDeathBoss() && !setDeathTooActive) {
            this.playSound(ModSoundHandler.KING_DEATH, 1.0f, 1.0f);

            addEvent(()-> {
                for (EntityPlayer player : this.bossInfo.getPlayers()) {
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "Ashed King: " + TextFormatting.WHITE)
                            .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "king_death_1")));
                }
                //Death
            }, 20);
            addEvent(()-> this.setImmovable(false), 72);
            addEvent(()-> this.setDeathBoss(false), 72);
            addEvent(this::setDead, 72);
            addEvent(()-> this.setDropItemsWhenDead(true), 70);
            setDeathTooActive = true;
        }
        super.onDeath(cause);
    }


    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
