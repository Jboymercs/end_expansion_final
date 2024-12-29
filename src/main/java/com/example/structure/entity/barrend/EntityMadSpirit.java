package com.example.structure.entity.barrend;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.*;
import com.example.structure.entity.util.IAttack;
import com.example.structure.entity.util.TimedAttackIniator;
import com.example.structure.init.ModItems;
import com.example.structure.init.ModPotions;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scala.tools.nsc.backend.icode.Primitives;
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

public class EntityMadSpirit extends EntityBarrendMob implements IAttack, IAnimatable, IAnimationTickable {

    private static final DataParameter<Boolean> ON_SUMMON = EntityDataManager.createKey(EntityMadSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BITE_ATTACK = EntityDataManager.createKey(EntityMadSpirit.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> UNSEEN_BY_ALL = EntityDataManager.createKey(EntityMadSpirit.class, DataSerializers.BOOLEAN);
    private AnimationFactory factory = new AnimationFactory(this);
    private Consumer<EntityLivingBase> prevAttack;

    private final String ANIM_IDLE_SEEN = "idle_seen";
    private final String ANIM_IDLE_UNSEEN = "idle";
    private final String ANIM_SUMMON = "summon";
    private final String ANIM_ATTACK = "attack";


    public void setUnseenByAll(boolean value) {
        this.dataManager.set(UNSEEN_BY_ALL, Boolean.valueOf(value));
    }

    public boolean isUnseenByAll() {return this.dataManager.get(UNSEEN_BY_ALL);}
    public void setOnSummon(boolean value) {this.dataManager.set(ON_SUMMON, Boolean.valueOf(value));}
    public boolean isOnSummon() {return this.dataManager.get(ON_SUMMON);}
    public void setBiteAttack(boolean value) {this.dataManager.set(BITE_ATTACK, Boolean.valueOf(value));}
    public boolean isBiteAttack() {return this.dataManager.get(BITE_ATTACK);}

    private boolean isCurrentlyVisible = false;

    public EntityMadSpirit(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0F, 1.6F);
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
    }

    public EntityMadSpirit(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.6F);
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Bite_Attack", this.isBiteAttack());
        nbt.setBoolean("Unseen_By", this.isUnseenByAll());
        nbt.setBoolean("On_Summon", this.isOnSummon());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setBiteAttack(nbt.getBoolean("Bite_Attack"));
        this.setUnseenByAll(nbt.getBoolean("Unseen_By"));
        this.setOnSummon(nbt.getBoolean("On_Summon"));

    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(BITE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(UNSEEN_BY_ALL, Boolean.valueOf(false));
        this.dataManager.register(ON_SUMMON, Boolean.valueOf(false));
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isBiteAttack() && !this.isOnSummon()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(jump_attack));
            double[] weights = {
                    (distance <= 6) ? 1/distance : 0 //Jump ATTACK
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }

        return 60;
    }

    private Consumer<EntityLivingBase> jump_attack = (target)-> {
        this.setBiteAttack(true);
        this.setImmovable(true);
        addEvent(()-> {
            Vec3d targetPos = target.getPositionVector().add(ModUtils.yVec(1.0D));
            float distance = getDistance(target);
            addEvent(()-> {
                this.setImmovable(false);
                ModUtils.leapTowards(this, targetPos, (float) (0.4 * Math.sqrt(distance)), 0.05f);
            }, 8);
        }, 7);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.5, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (MobConfig.mad_spirit_attack_damage * getAttackModifiersBarrend());
            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.6f, 0, false);
        }, 27);


        addEvent(()-> this.setBiteAttack(false), 40);
    };

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return this.world.rand.nextInt(24) == 0;
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase target = this.getAttackTarget();

        if(target != null && !world.isRemote) {

            if(!isCurrentlyVisible) {
                this.showSelf();
            }

          //  Vec3d targetPos = target.getPositionVector();

         //   if(this.getPositionVector().y > targetPos.y + 1) {
           //     double distSq = this.getDistanceSq(target.posX, targetPos.y + 1, target.posZ);
            //    double distance = Math.sqrt(distSq);
            //    if(distance <= 6 && !this.isBiteAttack()) {
            //        this.motionY -= 0.1;
           //     }
          //  } else if (this.getPositionVector().y < targetPos.y + 2) {
           //     this.motionY += 0.1;
          //  }

            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5D), e -> !e.getIsInvulnerable());

            if(!nearbyEntities.isEmpty()) {
                for(EntityLivingBase base: nearbyEntities) {
                    if(!(base instanceof EntityBarrendMob)) {
                        if(base.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != ModItems.LIDOPED_HELMET) {
                            base.addPotionEffect(new PotionEffect(ModPotions.MADNESS, 400, 0));
                        }
                    }
                }
            }

            if(!target.isPotionActive(ModPotions.MADNESS)) {
                //flee away
                double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
                double distance = Math.sqrt(distSq);
                if(distance < 16) {
                    double d0 = (this.posX - target.posX) * 0.015;
                    double d1 = (this.posY - target.posY) * 0.01;
                    double d2 = (this.posZ - target.posZ) * 0.015;
                    this.addVelocity(d0, d1, d2);
                } else if (this.isCurrentlyVisible){
                    hideSelf();
                }
            }

        } else {
            if (!world.isRemote) {
                for (int i = 0; i < 6; i++) {
                    if (!world.isAirBlock(this.getPosition().add(0, -i, 0))) {
                        this.motionY += 0.1;
                    } else {
                        this.motionY = 0;
                    }
                }
                this.motionX = 0;
                this.motionZ = 0;
            }
        }
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) MobConfig.mad_spirit_health * getHealthModifierBarrend());
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4 * ModConfig.barrend_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        //this.tasks.addTask(4, new EntityAiTimedAttackLocked<>(this, 1.3D, 60, 5F, 0.5f));
        this.tasks.addTask(4, new EntityAerialTimedAttack(this, 5F, 2F, 30, new TimedAttackIniator<>(this, 20)));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return this.ticksExisted > 20 * 60 * 20;

    }

    public void showSelf() {
        this.setImmovable(true);
        this.setOnSummon(true);
        this.isCurrentlyVisible = true;

        addEvent(()-> {
        this.setImmovable(false);
        this.setOnSummon(false);
        this.setUnseenByAll(true);
        }, 30);

    }

    public void hideSelf() {
        this.setUnseenByAll(false);
        this.setAttackTarget(null);
        this.isCurrentlyVisible = false;
        this.getNavigator().clearPath();
        this.setBiteAttack(false);
    }

    private<E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isBiteAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
            return PlayState.CONTINUE;
        }
        if(this.isOnSummon()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isBiteAttack() && !this.isOnSummon()) {
            if(this.isUnseenByAll()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_SEEN, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UNSEEN, true));
            }
            return PlayState.CONTINUE;
        }


        return PlayState.STOP;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        ModUtils.aerialTravel(this, strafe, vertical, forward);
    }

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "mad_spirit");
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(!this.isCurrentlyVisible) {
            return super.attackEntityFrom(source, 1F);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
