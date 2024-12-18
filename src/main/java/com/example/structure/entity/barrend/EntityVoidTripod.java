package com.example.structure.entity.barrend;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityController;
import com.example.structure.entity.ai.EntityAILidopedHome;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.util.IAttack;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.nbt.NBTTagCompound;
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

import java.util.List;

public class EntityVoidTripod extends EntityBarrendMob implements IAnimatable, IAttack, IAnimationTickable {

    protected static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityVoidTripod.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> DUG_IN = EntityDataManager.createKey(EntityVoidTripod.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> STOMP_ATTACK = EntityDataManager.createKey(EntityVoidTripod.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> START_DUG = EntityDataManager.createKey(EntityVoidTripod.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> END_DUG = EntityDataManager.createKey(EntityVoidTripod.class, DataSerializers.BOOLEAN);

    private final String ANIM_WALK = "walk";
    private final String ANIM_IDLE = "idle";

    private final String ANIM_DUG_IN = "dug_in";
    private final String ANIM_START_DUG = "start_dug";
    private final String ANIM_END_DUG = "end_dug";
    private final String ANIM_STOMP_ATTACK = "stomp_attack";
    private AnimationFactory factory = new AnimationFactory(this);

    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setDugIn(boolean value) {this.dataManager.set(DUG_IN, Boolean.valueOf(value));}
    public boolean isDugIn() {return this.dataManager.get(DUG_IN);}
    public void setStompAttack(boolean value) {this.dataManager.set(STOMP_ATTACK, Boolean.valueOf(value));}
    public boolean isStompAttack() {return this.dataManager.get(STOMP_ATTACK);}
    public void setStartDug(boolean value) {
        this.dataManager.set(START_DUG, Boolean.valueOf(value));
    }
    public boolean isSTartDug() {return this.dataManager.get(START_DUG);}
    public void setEndDug(boolean value) {this.dataManager.set(END_DUG, Boolean.valueOf(value));}
    public boolean isEndDug() {return this.dataManager.get(END_DUG);}

    public EntityVoidTripod(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(2.0F, 5.2F);
        if(worldIn.rand.nextBoolean()) {
            this.setDugIn(true);
        }
    }

    public EntityVoidTripod(World worldIn) {
        super(worldIn);
        this.setSize(2.0F, 5.2F);
        if(worldIn.rand.nextBoolean()) {
            this.setDugIn(true);
        }
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(DUG_IN, Boolean.valueOf(false));
        this.dataManager.register(STOMP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(END_DUG, Boolean.valueOf(false));
        this.dataManager.register(START_DUG, Boolean.valueOf(false));

    }

    private int dugInTimer = 2400 + ModRand.range(500, 1500);

    private int startHibernation = 2400 + ModRand.range(500, 1500);

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase target = this.getAttackTarget();
        if(this.isDugIn() && !world.isRemote) {
            this.setImmovable(true);
            this.noClip = true;
            this.lockLook = true;
            //we will try this and see if something happens
            this.setSize(0.1F, 0.1f);
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.rotationYawHead = 0;
            this.rotationYaw = 0;

            //a timer for it too occasionally get up and move around after completed
            if(dugInTimer < 0 || target != null || this.hurtTime > 0) {
                this.endHibernation();
            } else {
                dugInTimer--;
            }
        } else {
            if(target == null && !world.isRemote) {

                if(world.getBlockState(this.getPosition()).getBlock() == ModBlocks.BARE_ACID) {
                    this.motionY = 0.1;
                }
                if(startHibernation < 0) {
                    if(world.isBlockFullCube(this.getPosition().add(1,0,1)) && world.isBlockFullCube(this.getPosition().add(-1, 0, -1)) &&
                    world.isBlockFullCube(this.getPosition().add(1,0,-1)) && world.isBlockFullCube(this.getPosition().add(-1,0,1))) {
                        this.beginHibernation();
                    }
                } else {
                    startHibernation--;
                }
            }
        }

    }

    private void beginHibernation() {
        this.setStartDug(true);
        this.setFightMode(true);
        this.setImmovable(true);
        this.lockLook = true;


        addEvent(()-> {
        this.setStartDug(false);
        this.setFightMode(false);
        this.setDugIn(true);
        startHibernation = 2400 + ModRand.range(500, 1500);

        }, 175);

    }


    private void endHibernation() {
        this.setDugIn(false);
        this.setEndDug(true);
        this.setFightMode(true);
        this.noClip = false;

        addEvent(()-> {
            //Launches nearby entities
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5, 0.5, 1.5), e -> !e.getIsInvulnerable());
            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base : nearbyEntities) {
                    Vec3d offset = base.getPositionVector().add(0,0.25D,0);
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                    float damage = (float) (2 * ModConfig.biome_multiplier);
                    ModUtils.handleAreaImpact(0.25f, (e) -> damage, this, offset, source, 4.0f, 0, false);
                }
            }
        }, 8);

        addEvent(()-> {
        this.setEndDug(false);
        this.setImmovable(false);
        this.setFightMode(false);
        this.lockLook = false;
        dugInTimer = 2400 + ModRand.range(500, 1500);
            this.setSize(2.0F, 5.2F);
        }, 80);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Fight_Mode", this.isFightMode());
        nbt.setBoolean("Dug_In", this.isDugIn());
        nbt.setBoolean("Stomp_Attack", this.isStompAttack());
        nbt.setBoolean("Start_Dug", this.isSTartDug());
        nbt.setBoolean("End_Dug", this.isEndDug());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        this.setFightMode(nbt.getBoolean("Fight_Mode"));
        this.setDugIn(nbt.getBoolean("Dug_In"));
        this.setStompAttack(nbt.getBoolean("Stomp_Attack"));
        this.setStartDug(nbt.getBoolean("Start_Dug"));
        this.setEndDug(nbt.getBoolean("End_Dug"));
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.2D, 60, 4F, 0.35f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.26D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) MobConfig.void_walker_health * getHealthModifierBarrend());
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(16 * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return this.world.rand.nextInt(40) == 0;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isDugIn()) {

            if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK, true));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isStompAttack() && !this.isDugIn()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STOMP_ATTACK, false));
            }
            if(this.isSTartDug()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_START_DUG, false));
            }
            if(this.isEndDug()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_END_DUG, false));
            }

            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateDugState(AnimationEvent<E> event) {

        if(this.isDugIn()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DUG_IN, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }




    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "dug_controller", 0, this::predicateDugState));
        data.addAnimationController(new AnimationController(this, "attack_state_controller", 0, this::predicateAttack));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        if(!this.isDugIn() && !this.isFightMode()) {
            this.setFightMode(true);
            this.setStompAttack(true);
            this.lockLook = true;
            this.setImmovable(true);

            addEvent(()-> {
            //do Attack effects and damage
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.5, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = (float) (MobConfig.void_walker_attack_damage * getAttackModifiersBarrend());
                ModUtils.handleAreaImpact(3.0f, (e) -> damage, this, offset, source, 1.4f, 0, false);
            },37);

            addEvent(()-> {
            this.setFightMode(false);
            this.setStompAttack(false);
            this.lockLook = false;
            this.setImmovable(false);
            }, 75);
        }
        return 80;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
