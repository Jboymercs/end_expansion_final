package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.snatcher.EntityStalkAI;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntitySnatcher extends EntityModBase implements IAttack, IAnimatable, IAnimationTickable {
    private final String ANIM_WALKING_ARMS = "walk_upper";
    private final String ANIM_WALKING_LEGS = "walk_lower";
    private final String ANIM_IDLE = "idle";
    private final String ANIM_SPOTTED = "spotted";
    private final String ANIM_DIG_DOWN = "dig";
    private final String ANIM_DIG_UP = "dig_up";

    private final String ANIM_ATTACK = "attack";

    private final String ANIM_ATTACK_FAST = "attack_fast";

    public boolean isCurrentlyinHibernation = false;

    public boolean iAmPissedOff = false;

    private static final DataParameter<Boolean> SPOTTED = EntityDataManager.createKey(EntitySnatcher.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DIG_DOWN = EntityDataManager.createKey(EntitySnatcher.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DIG_UP = EntityDataManager.createKey(EntitySnatcher.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACK = EntityDataManager.createKey(EntitySnatcher.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACK_QUICK = EntityDataManager.createKey(EntitySnatcher.class, DataSerializers.BOOLEAN);
    public void setSpotted(boolean value) {this.dataManager.set(SPOTTED, Boolean.valueOf(value));}
    public boolean isSpotted() {return this.dataManager.get(SPOTTED);}
    public void setDigDown(boolean value) {this.dataManager.set(DIG_DOWN, Boolean.valueOf(value));}
    public boolean isDigDown() {return this.dataManager.get(DIG_DOWN);}
    public void setDigUp(boolean value) {this.dataManager.set(DIG_UP, Boolean.valueOf(value));}
    public boolean isDigUp() {return this.dataManager.get(DIG_UP);}
    public void setAttacking(boolean value) {this.dataManager.set(ATTACK, Boolean.valueOf(value));}
    public boolean isAttacking() {return this.dataManager.get(ATTACK);}
    public boolean isAttackQuick() {return this.dataManager.get(ATTACK_QUICK);}
    public void setAttackQuick(boolean  value) {this.dataManager.set(ATTACK_QUICK, Boolean.valueOf(value));}
    private Consumer<EntityLivingBase> prevAttack;
    private AnimationFactory factory = new AnimationFactory(this);
    public EntitySnatcher(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.stalker_health * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(3, new EntityStalkAI<>(this, 1.0, 20, 10, 2, 0.6f, 10, true));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityKnightBase>(this, EntityKnightBase.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }
    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SPOTTED, Boolean.valueOf(false));
        this.dataManager.register(DIG_DOWN, Boolean.valueOf(false));
        this.dataManager.register(DIG_UP, Boolean.valueOf(false));
        this.dataManager.register(ATTACK, Boolean.valueOf(false));
        this.dataManager.register(ATTACK_QUICK, Boolean.valueOf(false));
    }


    public void playSpottedAnim() {
        this.setSpotted(true);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_ENDERMEN_SCREAM,1.0f, 0.6f - ModRand.getFloat(0.3f) ), 5);
        addEvent(()-> this.setSpotted(false), 25);
    }

    //Starts Hibernation
    public void startHibernation() {
        this.setDigDown(true);
        this.isCurrentlyinHibernation = true;
        this.setOnaggro = false;
   addEvent(()-> {
       this.noClip = true;
       Vec3d pos = this.getPositionVector().add(ModUtils.yVec(-3));
       this.setPosition(pos.x, pos.y, pos.z);
       this.setImmovable(true);
   }, 25);

        addEvent(()-> {
            this.setDigDown(false);
        }, 30);
    }

    //Ends Hibernation
    public void endHiberntaion() {
        this.setDigUp(true);
        Vec3d pos = this.getPositionVector();
        this.setPosition(pos.x, pos.y + 3, pos.z);
        this.setImmovable(false);
        this.noClip = false;
        addEvent(()-> {
        this.isCurrentlyinHibernation =false;
        this.setDigUp(false);
        }, 35);
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return this.world.rand.nextInt(18) == 0;
    }

    protected int hibernationTimer = 500 + ModRand.range(50, ModConfig.stalker_hibernation * 20);
    protected int aggroTimer = 400;

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.isDigUp()) {
            this.motionX = 0;
            this.motionZ = 0;
        }

        if(this.isCurrentlyinHibernation) {
            //Incase Players want to try to mine him up you'll regret it
            List<EntityPlayer> closeplayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(ModConfig.stalker_distance), e -> !e.getIsInvulnerable());

            //This is to keep it in hibernation if theres no players around, and they'll pop out with time

                List<EntityPlayer> farTarget = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(30D), e -> !e.getIsInvulnerable());
                if(!farTarget.isEmpty()) {
                    hibernationTimer--;
                }

            if(!closeplayers.isEmpty() && !this.setOnaggro) {
                this.endHiberntaion();
                hibernationTimer = 500 + ModRand.range(50, ModConfig.stalker_hibernation * 20);
                this.iAmPissedOff = true;
                setOnaggro = true;
            }
        }
        if(hibernationTimer < 0) {
            this.endHiberntaion();
            hibernationTimer = 500 + ModRand.range(50, ModConfig.stalker_hibernation * 20);
        }
        if(this.iAmPissedOff) {
            aggroTimer--;
        }
        if(aggroTimer < 0) {
            this.iAmPissedOff = false;
            aggroTimer = 400;
        }
    }

    public boolean setOnaggro = false;
    private <E extends IAnimatable> PlayState predicateDig(AnimationEvent<E> event) {
        if(this.isDigDown()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DIG_DOWN, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isSpotted()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SPOTTED, false));
            return PlayState.CONTINUE;
        }
        if(this.isDigUp()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DIG_UP, false));
            return PlayState.CONTINUE;
        }
        if(this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
            return PlayState.CONTINUE;
        }
        if(this.isAttackQuick()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK_FAST, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {

        if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F) && !this.isSpotted() && !this.isDigDown() && !this.isAttacking() && !this.isAttackQuick()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F) && !this.isDigDown()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_LEGS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {

        if(event.getLimbSwingAmount() > -0.09F && event.getLimbSwingAmount() < 0.09F && !this.isDigDown()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    public EntitySnatcher(World worldIn) {
        super(worldIn);
        this.setSize(1.0f, 2.0f);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isAttacking() && !this.isAttackQuick()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(basic_attack, fast_attack));
            double[] weights = {
                    (distance < 3 && prevAttack != basic_attack) ? 1/distance : 0, //Regular Attack
                    (distance < 3 && prevAttack != fast_attack) ? 1/distance : 0 //Fast Attack
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 10;
    }

    private Consumer<EntityLivingBase> basic_attack = (target)-> {
      this.setAttacking(true);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.3, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (ModConfig.stalker_damage * ModConfig.biome_multiplier);
          ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
      }, 9);

      addEvent(()-> this.setAttacking(false), 20);
    };

    private Consumer<EntityLivingBase> fast_attack = (target) -> {
      this.setAttackQuick(true);

      //L
      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.3, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) ((ModConfig.stalker_damage -2) * ModConfig.biome_multiplier);
          ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
      }, 10);
      //R
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = (float) ((ModConfig.stalker_damage -2) * ModConfig.biome_multiplier);
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
        }, 20);

        //End
        addEvent(()-> this.setAttackQuick(false), 25);
    };

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
        animationData.addAnimationController(new AnimationController(this, "dig_controller", 0, this::predicateDig));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "snatcher");
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
