package com.example.structure.entity.endking.ghosts;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.endking.EntityAbstractEndKing;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.concurrent.atomic.AtomicReference;

public class EntityGhostPhase extends EntityModBase implements IAnimatable {

    protected Vec3d chargeDir;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_LEAP_ATTACK = "leap_attack";
    private final String ANIM_CLOSE_ATTACK = "close_attack";
    private AnimationFactory factory = new AnimationFactory(this);
    protected static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final  DataParameter<Boolean> LEAP_SWEEP_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> CLOSE_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    public boolean hasSelectedAttack = false;
    public int attackTimer = 10 + ModRand.range(40, 100);
    public int attackSelection;

    public boolean turnOnParticles = false;

    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setLeapSweepAttack(boolean value) {this.dataManager.set(LEAP_SWEEP_ATTACK, Boolean.valueOf(value));}
    public boolean isLeapSweepAttack() {return this.dataManager.get(LEAP_SWEEP_ATTACK);}
    public void setCloseAttack(boolean value) {this.dataManager.set(CLOSE_ATTACK, Boolean.valueOf(value));}
    public boolean isCloseAttack() {return this.dataManager.get(CLOSE_ATTACK);}
    public EntityGhostPhase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(2.0f, 3.7f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
    }

    @Override
    public void entityInit() {
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(LEAP_SWEEP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(CLOSE_ATTACK, Boolean.valueOf(false));
        super.entityInit();
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.lockLook) {
            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;
        }
        EntityLivingBase target = this.getAttackTarget();
        if(target != null) {
            if(!lockLook) {
                this.faceEntity(target, 10, 10);
                this.getLookHelper().setLookPositionWithEntity(target, 10, 10);
            }
            //Waiting the random timer to set off an attack
            if(!hasSelectedAttack && attackTimer < 0) {
                if(attackSelection == 1) {
                    initiateLeapAttack(target);
                    hasSelectedAttack = true;
                }
                if(attackSelection == 2) {
                    initiateCloseAttack(target);
                    hasSelectedAttack = true;
                }
            } else {
                attackTimer--;
            }
        }


        if(turnOnParticles) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        //Just a back up incase they are for some reason not in range or player leaves and joins game
        if(ticksExisted > 190) {
            this.setDead();
        }
    }

    public void initiateCloseAttack(EntityLivingBase target) {
        Vec3d lookPos = target.getPositionVector().add(ModUtils.yVec(1.0));
        ModUtils.facePosition(lookPos, this, 10, 10);
        this.setFightMode(true);
        this.setCloseAttack(true);
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.0, 1.4, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (ModConfig.end_king_damage * ModConfig.biome_multiplier);
            ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
            this.setImmovable(true);
        }, 18);

        addEvent(()-> turnOnParticles = true, 25);
        addEvent(this::setDead, 55);
    }
    public void initiateLeapAttack(EntityLivingBase target) {
        this.setLeapSweepAttack(true);
        this.setFightMode(true);
        addEvent(()->this.ActionDashForward(target), 3);
        addEvent(()-> {
            if(!this.world.isRemote) {

                for (int i = 0; i < 20; i += 5) {
                    addEvent(() -> {
                        Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.5f));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                        float damage = (float) ((ModConfig.end_king_damage * 1.5) * ModConfig.biome_multiplier);
                        ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
                    }, i);
                }
            }
        }, 18);
        addEvent(()-> {
        turnOnParticles = true;
        }, 35);


        addEvent(this::setDead, 80);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE) {
            for (int i = -5; i < 2; i++) {
                final float yOff = i * 0.5f;
                ModUtils.circleCallback(1, 40, (pos)-> {
                    pos = new Vec3d(pos.x, yOff, pos.y);
                    ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector()), ModColors.RANDOM_GREY, ModUtils.yVec(0.1));
                });
            }
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.attack_damage);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();

        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityCrystalKnight>(this, EntityCrystalKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected boolean canDropLoot() {
        return false;
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
            return false;
    }
    public EntityGhostPhase(World worldIn, int id) {
        super(worldIn);
        this.setSize(2.0f, 3.7f);
        this.attackSelection = id;
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.turnOnParticles = true;
        addEvent(()-> this.turnOnParticles = false, 20);
    }

    public EntityGhostPhase(World worldIn) {
        super(worldIn);
        this.setSize(2.0f, 3.7f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.turnOnParticles = true;
        addEvent(()-> this.turnOnParticles = false, 20);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private <E extends IAnimatable>PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isLeapSweepAttack() && !this.isCloseAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isLeapSweepAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LEAP_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isCloseAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CLOSE_ATTACK, false));
                return PlayState.CONTINUE;
            }
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    public void ActionDashForward(EntityLivingBase target) {
        Vec3d enemyPos = target.getPositionVector().add(ModUtils.yVec(1));
        addEvent(()-> {
            this.playSound(ModSoundHandler.KING_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            int randomDeterminedDistance = ModRand.range(4, 6);


            Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(getEyeHeight()));

            Vec3d dir = enemyPos.subtract(startPos).normalize();

            AtomicReference<Vec3d> teleportPos = new AtomicReference<>(enemyPos);

            ModUtils.lineCallback(enemyPos.add(dir),enemyPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {

                boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                boolean notOpen = ModUtils.cubePoints(0, 1, 0, 1, 3, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });
            this.chargeDir = teleportPos.get();
            this.setPositionAndUpdate(chargeDir.x, chargeDir.y, chargeDir.z);

        }, 15);
    }



    public static boolean isFriendlyKnight(Entity entity) {
        return !CAN_TARGET.apply(entity);
    }

    public static final Predicate<Entity> CAN_TARGET = entity -> {

        return !(entity instanceof EntityKnightBase || entity instanceof EntityAbstractEndKing || entity instanceof EntityGhostPhase);
    };


}
