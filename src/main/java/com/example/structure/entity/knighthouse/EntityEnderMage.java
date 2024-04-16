package com.example.structure.entity.knighthouse;


import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.ai.EntityAISupport;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.endking.ProjectileSpinSword;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

public class EntityEnderMage extends EntityKnightBase implements IAnimatable, IAttack, IAnimationTickable {

    private final String ANIM_IDLE = "idle";

    private Consumer<EntityLivingBase> prevAttack;
    public boolean isCloseTooAllies = false;
    public boolean selectEntity = false;

    public boolean nearbyMarked = false;

    private final String ANIM_WALKING_ARMS = "walk_upper";
    private final String ANIM_WALKING_LEGS = "walk_lower";
    private final String ANIM_CAST_HEAL = "heal";
    private final String ANIM_CAST_ATTACK = "attack";
    private final String ANIM_MARKED = "mark";

    public int randomMarkTimer = 800 + ModRand.range(50, (ModConfig.end_mage_ritual * 20));
    private static final DataParameter<Boolean> HEALING_MODE =EntityDataManager.createKey(EntityEnderMage.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACK_MODE = EntityDataManager.createKey(EntityEnderMage.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MARKED = EntityDataManager.createKey(EntityEnderMage.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);
    public EntityEnderMage(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityEnderMage(World worldIn) {
        super(worldIn);
        this.setSize(0.8f, 1.9f);
    }



    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(HEALING_MODE, Boolean.valueOf(false));
        this.dataManager.register(ATTACK_MODE, Boolean.valueOf(false));
        this.dataManager.register(MARKED, Boolean.valueOf(false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        List<EntityKnightBase> nearbyKnights = this.world.getEntitiesWithinAABB(EntityKnightBase.class, this.getEntityBoundingBox().grow(16D), e-> !e.getIsInvulnerable() && !EntityKnightBase.CAN_TARGET.apply(e));
        if(!nearbyKnights.isEmpty()) {
         this.isCloseTooAllies = true;
        }
        if(nearbyKnights.isEmpty()) {
            this.isCloseTooAllies = false;
        }

        List<EntityEnderKnight> nearbyChadKnight = this.world.getEntitiesWithinAABB(EntityEnderKnight.class, this.getEntityBoundingBox().grow(20D), e-> !e.getIsInvulnerable() && !EntityKnightBase.CAN_TARGET.apply(e));
        if(!nearbyChadKnight.isEmpty()) {
            for(EntityEnderKnight knight : nearbyChadKnight) {
                if(knight.isMarkedForUnholy()) {
                    nearbyMarked = true;
                }
            }

        }
        if(nearbyChadKnight.isEmpty()) {
            nearbyMarked =false;
        }

        EntityLivingBase target = this.getAttackTarget();
        List<EntityEnderKnight> nearbyCurses = this.world.getEntitiesWithinAABB(EntityEnderKnight.class, this.getEntityBoundingBox().grow(4D), e-> !e.getIsInvulnerable() && !EntityKnightBase.CAN_TARGET.apply(e));
        if(!nearbyCurses.isEmpty() && target == null && randomMarkTimer < 0 && !this.nearbyMarked) {
            for(EntityEnderKnight knight : nearbyCurses) {
                if(!knight.isMarkedForUnholy() && !this.selectEntity) {
                    castUnholyMarking(knight);
                }
            }
        } else if (!this.selectEntity){
            randomMarkTimer--;
        }
    }

    //Ender Mages can only perform this task once, making it limiting to how many Unholy Ender Knights can be in an area
    public void castUnholyMarking(EntityEnderKnight knight) {
        this.setFightMode(true);
        this.setMarked(true);
        this.setImmovable(true);
        this.faceEntity(knight, 30.0f, 30.0f);
        this.selectEntity = true;
        addEvent(()-> {
            if(!world.isRemote) {
                knight.setMarkedForUnholy(true);
            }

            for (int i = -5; i < 2; i++) {
                final float yOff = i * 0.5f;
                ModUtils.circleCallback(1, 20, (pos)-> {
                    pos = new Vec3d(pos.x, yOff, pos.y);
                    ParticleManager.spawnColoredSmoke(world, pos.add(knight.getPositionVector().add(ModUtils.yVec(0.3D))), ModColors.RED, ModUtils.yVec(0.1));
                });
            }
        }, 20);
        addEvent(()-> {
            this.setMarked(false);
            this.setFightMode(false);
            this.setImmovable(false);
        }, 35);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.knighthouse_health * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAISupport(this, 1.2, 0, 10f, ModConfig.end_mage_cooldown * 20));
        this.tasks.addTask(5, new EntityAITimedAttack<>(this, 1.5, 60, 16F, 0.4f));
    }


    public void setHealingMode(boolean value) {this.dataManager.set(HEALING_MODE, Boolean.valueOf(value));}
    public boolean isHealingMode() {return this.dataManager.get(HEALING_MODE);}

    public void setAttackMode(boolean value) {this.dataManager.set(ATTACK_MODE, Boolean.valueOf(value));}
    public boolean isAttackMode() {return this.dataManager.get(ATTACK_MODE);}
    public void setMarked(boolean value) {this.dataManager.set(MARKED, Boolean.valueOf(value));}
    public boolean isMarked() {return this.dataManager.get(MARKED);}
    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {

        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_LEGS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }


    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {

        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
    if(this.isHealingMode()) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_HEAL, false));
        return PlayState.CONTINUE;
    }
    if(this.isAttackMode()) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CAST_ATTACK, false));
        return PlayState.CONTINUE;
    }
    if(this.isMarked()) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MARKED, false));
        return PlayState.CONTINUE;
    }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }



    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(castSpell));
            double[] weights = {
                (distance < 17) ? 1 : 0
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 60;
    }

    private final Consumer<EntityLivingBase> castSpell = (target) -> {
      this.setFightMode(true);
      this.setAttackMode(true);
        this.playSound(ModSoundHandler.KNIGHT_CAST_ATTACK, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
      addEvent(() -> {
        ProjectileSpinSword sword = new ProjectileSpinSword(world, this, 6.0f);
        sword.setTravelRange(40f);
        Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.8,2.0,0)));
        sword.setPosition(pos.x, pos.y, pos.z);
          Vec3d vel = target.getPositionVector().add(ModUtils.yVec(1)).subtract(sword.getPositionVector());
          sword.shoot(vel.x, vel.y, vel.z, 1.5f, 0f);
          this.world.spawnEntity(sword);
      }, 23);
      addEvent(()-> {
        this.setFightMode(false);
        this.setAttackMode(false);
      }, 30);
    };



    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnSwirl2(world, this.getPositionVector(), ModColors.RED, Vec3d.ZERO);
        }


        super.handleStatusUpdate(id);
    }


    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        float maxhealth = target.getHealth() / target.getMaxHealth();
        if(maxhealth < 1) {
            this.setFightMode(true);
            this.setHealingMode(true);
            this.playSound(ModSoundHandler.KNIGHT_CAST_HEAL, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
            this.addEvent(() -> {
                if (!world.isRemote) {
                    EntityHealAura aura = new EntityHealAura(world, target);
                    Vec3d pos = target.getPositionVector();
                    aura.setPosition(pos.x, pos.y, pos.z);
                    world.spawnEntity(aura);
                    //Spawn the Healing Projectile Here
                    world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
                }
            }, 15);
            addEvent(() -> {
                this.setFightMode(false);
                this.setHealingMode(false);
            }, 30);
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }
}
