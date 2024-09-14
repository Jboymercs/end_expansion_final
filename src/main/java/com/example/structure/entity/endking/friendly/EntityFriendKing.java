package com.example.structure.entity.endking.friendly;

import com.example.structure.config.ItemConfig;
import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.ai.EntityKingTimedAttack;
import com.example.structure.entity.endking.*;
import com.example.structure.entity.endking.EndKingAction.ActionAOESimple;
import com.example.structure.entity.endking.EndKingAction.ActionHoldSwordAttack;
import com.example.structure.entity.endking.EndKingAction.ActionSummonSwordAttacks;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
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
import java.util.function.Supplier;

public class EntityFriendKing extends EntityAbstractEndKing implements IAnimatable, IAttack {
    /**
     * This is a Friendly Version of the King, where it will not hurt the User
     *
     */


    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_SWEEP_LEAP = "sweepLeap";
    private final String ANIM_FIRE_BALL = "fireBall";
    private final String SUMMON_AOE_CRYSTALS = "crystals";

    private final String ANIM_CAST_SWORD = "cast_sword";
    //PHASE TWO
    private final String ANIM_SIDE_SWIPE = "side_attack";
    private final String ANIM_TOP_SWIPE = "upper_attack";
    private final String ANIM_COMBO_LINE = "combo_1";

    private final String ANIM_CAST_ARENA = "castArena";

    private final String ANIM_SUMMON = "summon_ghost";
    private Consumer<EntityLivingBase> prevAttack;

    protected EntityPlayer owner;


    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public EntityFriendKing(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.IisGhost = true;
        this.isMeleeMode =true;
    }

    public EntityFriendKing(World worldIn) {
        super(worldIn);
        this.IisGhost = true;
        this.isMeleeMode =true;

    }

    public EntityFriendKing(World worldIn, EntityPlayer player, BlockPos pos) {
        super(worldIn);
        this.IisGhost = true;
        this.isMeleeMode =true;
        owner = player;
        this.setPGhostSummon(true);
        addEvent(()-> this.setPGhostSummon(false), 50);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityKingTimedAttack<>(this, 1.0, 20, 24.0f, 0.4f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }





    protected boolean currentlyHasTarget = false;


    protected int killCountdown = ItemConfig.minion_lifeTime * 20;
    @Override
    public void onUpdate() {
        super.onUpdate();

        //A kill countdown, will kill the Entity after a certain amount of time
        if(killCountdown < 0) {
            this.setDead();
        } else {
            killCountdown--;
        }
        if(this.isPGhostSummon()) {
            this.motionZ = 0;
            this.motionY = 0;
            this.motionX = 0;
            this.rotationPitch = 0;
            this.rotationYaw = 0;
            this.rotationYawHead = 0;
        }

        EntityLivingBase targetCurrent = this.getAttackTarget();
        if(targetCurrent != null && this.getEntitySenses().canSee(targetCurrent)) {
            currentlyHasTarget = true;
        } else {
            this.setAttackTarget(null);
            currentlyHasTarget = false;
        }

        if(targetCurrent != null) {
            if(targetCurrent.isDead) {
                this.setAttackTarget(null);
                currentlyHasTarget = false;
            }
        }





        //A simple function to allow the Ghost King to attack anything except the Owner

            //Checks to make sure the Owner is not dead or not there
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(25D), e -> !e.getIsInvulnerable());

            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase target : nearbyEntities) {
                    //If PVP Item abilities are enabled
                    if(ModConfig.enable_pvp_abilities) {
                        //If the crown has the friendly to owner only
                        if(ItemConfig.crown_is_friendly) {
                            if(!(target instanceof EntityGroundSword) && !(target instanceof EntityRedCrystal) && !(target instanceof EntityFriendKing) && target != owner) {
                                if(!currentlyHasTarget && this.getEntitySenses().canSee(target)) {
                                    this.setAttackTarget(target);
                                }
                            }
                        } else {
                            //Target Everything
                            if(!(target instanceof EntityGroundSword) && !(target instanceof EntityRedCrystal) && !(target instanceof EntityFriendKing)) {
                                if(!currentlyHasTarget && this.getEntitySenses().canSee(target)) {
                                    this.setAttackTarget(target);
                                }
                            }
                        }
                    } else {
                        //Target Everything Except Players
                        if(!(target instanceof EntityGroundSword) && !(target instanceof EntityRedCrystal) && !(target instanceof EntityFriendKing) && target != owner && !(target instanceof EntityPlayer)) {
                            if(!currentlyHasTarget && this.getEntitySenses().canSee(target)) {
                                this.setAttackTarget(target);
                            }
                        }
                    }

                }
            }






        //Lock Look system Implemented
        if(this.lockLook) {

            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;

        }

    }

    public void teleportTarget(double x, double y, double z) {
        this.setPosition(x , y, z);

    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        //Change Fight Manager to Independant
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            if(!this.isPGhostSummon()) {
                List<Consumer<EntityLivingBase>> attackCurrent = new ArrayList<>(Arrays.asList(upperAttack, sideAttack, regularAttack, sweepLeap, summon_ground_swords, projectileSwords)); //Readable Attacks
                double[] weights = {
                        (distance < 13 && distance > 2 && prevAttack != upperAttack) ? distance * 0.02 : 0, //Upper Attack
                        (distance < 7 && prevAttack != sideAttack) ? distance * 0.02 : 0,   //Side Swipe
                        (distance < 3 && prevAttack != regularAttack) ? 1/distance : 0,  //Close Regular Attack
                        (distance < 25) ? distance * 0.02 : 0, //LeapAttack - Keep
                        (distance < 25 && prevAttack != summon_ground_swords) ? distance * 0.02 : 0, //Summon Ground Swords - Keep
                        (distance > 1 && !hasSwordsNearby) ? distance * 0.02 : 0 // Projectile Swords Attack
                };

                prevAttack = ModRand.choice(attackCurrent, rand, weights).next();
                prevAttack.accept(target);
            }

        }

        return 20;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ItemConfig.minion_attack_damage * ModConfig.biome_multiplier);
    }



    Supplier<Projectile> projectileSupplierSpinSword = () -> new ProjectileSpinSword(world, this, 6.0f);

    private final Consumer<EntityLivingBase> projectileSwords = (target) -> {
        this.setFightMode(true);
        new ActionHoldSwordAttack(projectileSupplierSpinSword, 2.0f).performAction(this, target);

        addEvent(()-> this.setFightMode(false), 80);
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
                ModUtils.leapTowards(this, targetPos, (float) (0.45 * Math.sqrt(distance)), 0.3f);
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

    private final Consumer<EntityLivingBase> summon_ground_swords = (target) -> {
        this.setGroundSword(true);
        this.setFightMode(true);
        this.setSwingingArms(true);
        this.lockLook =true;
        this.playSound(ModSoundHandler.KING_THROW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f));
        addEvent(()-> {
            //Summon Ground Sword Variant
            //Makes them faster
            new ActionSummonSwordAttacks(true).performAction(this, target);
        }, 35);
        addEvent(()-> {
            this.lockLook =false;
            this.setSwingingArms(false);
            this.setFightMode(false);
            this.setGroundSword(false);
        }, 40);
    };

    private final Consumer<EntityLivingBase> regularAttack = (target)-> {
        this.setComboAttack(true);
        this.setFightMode(true);
        this.setSwingingArms(true);

        addEvent(()-> this.playSound(ModSoundHandler.KING_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.6f)), 5);

        addEvent(()-> this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 21);

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


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        animationData.addAnimationController(new AnimationController(this, "predicate_summon", 0, this::predicateSummon));
    }

    private <E extends IAnimatable> PlayState predicateSummon(AnimationEvent<E> event) {
        if(this.isPGhostSummon()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFullBodyUsage() && !this.isPGhostSummon()) {

            if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isSwingingArms() && !this.isFullBodyUsage() && !this.isPGhostSummon()) {

            if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
                return PlayState.CONTINUE;
            }

        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isPGhostSummon()) {
            if(this.isLeapSweepAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWEEP_LEAP, false));
            }
            if(this.isSummonCrystalsAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_AOE_CRYSTALS, false));
            }
            if(this.isSummonFireBallsAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FIRE_BALL, false));
            }
            if(this.isSummonGhosts()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_AOE_CRYSTALS, false));
            }
            if(this.isUpperAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_TOP_SWIPE, false));
            }
            if(this.isSideAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SIDE_SWIPE, false));
            }
            if(this.isComboAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_LINE, false));
            }
            if(this.isCastArena()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_ARENA, false));
            }

            if(this.isGroundSwords()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_SWORD, false));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }


    public void ActionDashForward(EntityLivingBase target) {
        setPhaseMode(true);
        addEvent(()-> setPhaseMode(false), 5);
        addEvent(()-> setPhaseMode(true), 10);
        addEvent(()-> {
            Vec3d enemyPosToo = target.getPositionVector().add(ModUtils.yVec(1));
            addEvent(()-> {
                this.playSound(ModSoundHandler.KING_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                int randomDeterminedDistance = ModRand.range(4, 6);
                Vec3d enemyPos = enemyPosToo;

                Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(getEyeHeight()));

                Vec3d dir = enemyPos.subtract(startPos).normalize();

                AtomicReference<Vec3d> teleportPos = new AtomicReference<>(enemyPos);

                ModUtils.lineCallback(enemyPos.add(dir),enemyPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {

                    boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                            .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                    .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                    boolean notOpen = ModUtils.cubePoints(0, 1, 0, 2, 3, 2).stream()
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

    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}
