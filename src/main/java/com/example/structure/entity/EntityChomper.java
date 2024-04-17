package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.ai.snatcher.EntityStalkAI;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
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
    private Consumer<EntityLivingBase> prevAttack;

    private static final DataParameter<Boolean> ATTACK = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DIG_DOWN = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DIG_UP = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DUG_IN = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BITE = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WARN = EntityDataManager.createKey(EntityChomper.class, DataSerializers.BOOLEAN);

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

    private AnimationFactory factory = new AnimationFactory(this);
    public EntityChomper(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0f, 1.3f);
    }

    public EntityChomper(World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 1.3f);
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
    }

    public int timeTooDig = ModRand.range(120, 200);
    public int warnAmount = 3;

    public int warnCooldown = 80;
    public boolean beginDigging = false;
    public boolean isCurrentlyDugIn = false;

    public boolean isJumping = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase target = this.getAttackTarget();

        if(target != null && this.isJumping) {
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.0D), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    if(base == target) {
                        this.setTooBiteTarget(target);
                    }
                }
            }
        }
        if(target == null && timeTooDig <= 0) {
            BlockPos setPos = this.getPosition().add(0, -1, 0);
            if(world.getBlockState(setPos).isFullBlock() && !beginDigging && !isCurrentlyDugIn) {
                this.warnAmount = 3;
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
            warnAmount--;
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10D), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    if(base != this) {
                        if (warnAmount <= 0) {
                            this.statePopOut(base);
                        }
                        if (this.getEntitySenses().canSee(base) && this.warnAmount <= 0 && !this.isWarn()) {
                            this.stateWarnNearby();
                        }
                    }
                }
            }

            List<EntityLivingBase> nearbyClose = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(5D), e -> !e.getIsInvulnerable());
            if(!nearbyClose.isEmpty()) {
                for(EntityLivingBase base : nearbyClose) {
                    if(base != this) {
                        this.statePopOut(base);
                    }
                }
            }
        }
    }

    public void statePopOut(EntityLivingBase target) {
        this.setDugIn(false);
        this.setDigUp(true);
      addEvent(()-> {
          this.setImmovable(false);
          this.noClip = false;
          this.setNoGravity(false);
          this.setDigUp(false);
          this.setAttackTarget(target);
          this.isCurrentlyDugIn = false;
      }, 20);
    }

    public void stateWarnNearby() {
        this.setWarn(true);
        this.warnCooldown = 80;
        this.warnAmount--;
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
            this.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
            this.setImmovable(true);
            this.beginDigging = false;
        }, 30);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.6D);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isBite() && !this.isAttacking()) {
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
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) (9 * ModConfig.biome_multiplier);
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
                ModUtils.leapTowards(this, targetedPos, (float) (1.3 * Math.sqrt(distance)), 0.7f);
            }, 10);
        }, 2);
        addEvent(()-> {
            if(this.isJumping) {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (9 * ModConfig.biome_multiplier);
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
        if(!this.isWarn() && !this.isBite() && !this.isAttacking() && !this.isDigDown() && !this.isDigUp() && !this.isDugIn()) {
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
