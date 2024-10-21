package com.example.structure.entity.knighthouse;


import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.EntityTimedAttackShield;
import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
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

public class EntityEnderShield extends EntityKnightBase implements IAnimatable, IAttack, IAnimationTickable {
    private final String ANIM_WALKING_ARMS = "walk_upper";
    private final String ANIM_WALKING_LEGS = "walk_lower";
    private final String ANIM_WALKING_ARMS_SHIELD = "walk_upper_shield";
    private final String ANIM_IDLE_SHIELD = "idle_shield";
    private final String ANIM_IDLE = "idle";
    private final String ANIM_PIERCE = "pierce";
    private final String ANIM_ATTACK = "attack";
    private final String ANIM_SHIELD = "shield_bash";

    private final String ANIM_STUN = "stun";

    protected boolean openToParry = false;
    protected boolean shieldLowered = false;
    private Consumer<EntityLivingBase> prevAttack;
    private AnimationFactory factory = new AnimationFactory(this);

    private static final DataParameter<Boolean> SHIELDED = EntityDataManager.createKey(EntityEnderShield.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_ATTACK = EntityDataManager.createKey(EntityEnderShield.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> REGULAR_ATTACK = EntityDataManager.createKey(EntityEnderShield.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHIELD_ATTACK = EntityDataManager.createKey(EntityEnderShield.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STUNNED = EntityDataManager.createKey(EntityEnderShield.class, DataSerializers.BOOLEAN);


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
     //   nbt.setBoolean("Shielded", this.dataManager.get(SHIELDED));
     //   nbt.setBoolean("Pierce_Attack", this.dataManager.get(PIERCE_ATTACK));
      //  nbt.setBoolean("Regular_Attack", this.dataManager.get(REGULAR_ATTACK));
      //  nbt.setBoolean("Shield_Attack", this.dataManager.get(SHIELD_ATTACK));
     //   nbt.setBoolean("Stunned", this.dataManager.get(STUNNED));

        nbt.setBoolean("Shielded", this.isShielded());
        nbt.setBoolean("Pierce_Attack", this.isPierceAttack());
        nbt.setBoolean("Regular_Attack", this.isRegularAttack());
        nbt.setBoolean("Shield_Attack", this.isShieldAttack());
        nbt.setBoolean("Stunned", this.isStunned());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
     //   this.dataManager.set(SHIELDED, nbt.getBoolean("Shielded"));
     //   this.dataManager.set(PIERCE_ATTACK, nbt.getBoolean("Pierce_Attack"));
     //   this.dataManager.set(REGULAR_ATTACK, nbt.getBoolean("Regular_Attack"));
     //   this.dataManager.set(SHIELD_ATTACK, nbt.getBoolean("Shield_Attack"));
     //   this.dataManager.set(STUNNED, nbt.getBoolean("Stunned"));

        this.setShielded(nbt.getBoolean("Shielded"));
        this.setPierceAttack(nbt.getBoolean("Pierce_Attack"));
        this.setRegularAttack(nbt.getBoolean("Regular_Attack"));
        this.setShieldAttack(nbt.getBoolean("Shield_Attack"));
        this.setStunned(nbt.getBoolean("Stunned"));
    }
    public void setShielded(boolean value) {this.dataManager.set(SHIELDED, Boolean.valueOf(value));}
    public boolean isShielded() {return this.dataManager.get(SHIELDED);}
    public void setPierceAttack(boolean value) {this.dataManager.set(PIERCE_ATTACK, Boolean.valueOf(value));}
    public boolean isPierceAttack() {return this.dataManager.get(PIERCE_ATTACK);}
    public void setRegularAttack(boolean value) {this.dataManager.set(REGULAR_ATTACK, Boolean.valueOf(value));}
    public boolean isRegularAttack() {return this.dataManager.get(REGULAR_ATTACK);}
    public void setShieldAttack(boolean value) {this.dataManager.set(SHIELD_ATTACK, Boolean.valueOf(value));}
    public boolean isShieldAttack() {return this.dataManager.get(SHIELD_ATTACK);}
    public void setStunned(boolean value) {this.dataManager.set(STUNNED, Boolean.valueOf(value));}
    public boolean isStunned() {return this.dataManager.get(STUNNED);}

    public EntityEnderShield(World worldIn) {
        super(worldIn);
        this.setSize(0.8f, 1.9f);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(REGULAR_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHIELD_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHIELDED, Boolean.valueOf(false));
        this.dataManager.register(STUNNED, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.17D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) MobConfig.knighthouse_health * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14.0D * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    //To avoid weird movements of the shield
    public int standbyTimer = 40;
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
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if(distance < 8 && !this.isRandomGetAway) {
                this.setShielded(true);
                standbyTimer = 40;
            } else if(distance > 8 && standbyTimer < 0){
                this.setShielded(false);
            }

            if(this.isRandomGetAway && distance < 6) {
                double d0 = (this.posX - target.posX) * 0.030;
                double d1 = (this.posY - target.posY) * 0.01;
                double d2 = (this.posZ - target.posZ) * 0.030;
                this.addVelocity(d0, d1, d2);
            }

                standbyTimer--;
        }
        if(this.isStunned()) {
            this.setRegularAttack(false);
            this.setPierceAttack(false);

        }
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityTimedAttackShield<>(this, (this.isShielded()) ? 1.2 : 1.4, 40, 3F, 0.5f));
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {

        if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F) && !this.isFightMode() && !this.isStunned()) {
            if(this.isShielded()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS_SHIELD, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS, true));
            }
            return PlayState.CONTINUE;
        }


        return PlayState.STOP;
    }


    private <E extends  IAnimatable> PlayState predicateStunned(AnimationEvent<E> event) {
        if(this.isStunned()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STUN, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
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

        if(event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F && !this.isFightMode() && !this.isStunned()) {
            if(this.isShielded()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_SHIELD, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            }

            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isStunned()) {
            if(this.isPierceAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PIERCE, false));
                return PlayState.CONTINUE;
            }
            if(this.isRegularAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isShieldAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHIELD, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && !this.isStunned()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(simpleSwing, pierceAttack, shieldBash, randomGetBack));
            double[] weights = {
                    (distance < 4 && prevAttack != simpleSwing) ? 1/distance : 0, //Regular Attack
                    (distance < 4 && prevAttack != pierceAttack) ? 1/distance : 0, // Pierce Attack
                    (distance < 3 && prevAttack != shieldBash) ? 1/distance : 0, //Shield Attack
                    (distance < 4 && prevAttack != randomGetBack) ? 1/distance : 0 //Random Get away
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 40;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {

        if (id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(1, 30, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.1, 0))), ModColors.RED, pos.normalize().scale(0.5).add(ModUtils.yVec(0)));
            });
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE && impactLocation != null ) {
            Vec3d vec3d1 = impactLocation.add(ModUtils.yVec(1.4));
            ParticleManager.spawnColoredSmoke(this.world, vec3d1, ModColors.YELLOW, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
            ParticleManager.spawnColoredSmoke(this.world, vec3d1, ModColors.YELLOW, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
            ParticleManager.spawnColoredSmoke(this.world, vec3d1, ModColors.YELLOW, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
            ParticleManager.spawnColoredSmoke(this.world, vec3d1, ModColors.YELLOW, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
            ParticleManager.spawnColoredSmoke(this.world, vec3d1, ModColors.YELLOW, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
            ParticleManager.spawnColoredSmoke(this.world, vec3d1, ModColors.YELLOW, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
        }
        super.handleStatusUpdate(id);
    }

    public boolean isRandomGetAway = false;

    public Vec3d impactLocation;
    private final Consumer<EntityLivingBase> randomGetBack = (target) -> {
      this.setFightMode(true);
      this.isRandomGetAway = true;
      this.shieldLowered = true;
      addEvent(()-> {
          this.isRandomGetAway = false;
          this.setFightMode(false);
          this.shieldLowered = false;
      }, 25);
    };
    private final Consumer<EntityLivingBase> shieldBash = (target) -> {
      this.setFightMode(true);
      this.setImmovable(true);
      this.setShieldAttack(true);
      addEvent(()-> {
        for(int i = 0; i < 27; i += 9) {
            addEvent(()-> {
                List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2.0D), e -> !e.getIsInvulnerable() && (!(e instanceof EntityEndKing || e instanceof EntityEnderMage || e instanceof EntityEnderShield || e instanceof EntityEnderKnight)));
                if(!targets.isEmpty()) {
                    for(EntityLivingBase entity : targets) {
                        if(!(entity instanceof EntityEnderKnight || entity instanceof  EntityEnderShield || entity instanceof EntityEnderMage)) {
                            Vec3d pos = entity.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.2, 0.4, 0)));
                            DamageSource source = ModDamageSource.builder()
                                    .type(ModDamageSource.MOB)
                                    .directEntity(this)
                                    .build();
                            float damage = (float) ((MobConfig.end_shielder_damage - 1) * ModConfig.biome_multiplier);
                            ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, pos, source, 0.6F, 0, false );
                            this.playSound(SoundEvents.ITEM_SHIELD_BLOCK,1.0f, 0.6f - ModRand.getFloat(0.3f) );
                        }
                    }
                }
               world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }, i);
        }
      }, 8);
      addEvent(()-> {
        this.setFightMode(false);
        this.setImmovable(false);
        this.setShieldAttack(false);
      }, 35);
    };
    private final Consumer<EntityLivingBase> pierceAttack = (target) -> {
      this.setFightMode(true);
      this.setPierceAttack(true);
      this.openToParry = true;
      addEvent(()-> this.openToParry = false, 14);
    addEvent(()-> {
        this.shieldLowered = true;
        this.lockLook = true;
    }, 10);
    addEvent(()-> {
        if(!this.isStunned()) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.7, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage =(float) (MobConfig.end_shielder_damage * ModConfig.biome_multiplier);
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
        }
    }, 15);

    addEvent(()-> this.lockLook = false, 21);
    addEvent(()->{
        if(!this.isStunned()) {
        this.shieldLowered = false;}},24);
      addEvent(()-> {
          if(!this.isStunned()) {
              this.setPierceAttack(false);
              this.setFightMode(false);
          }
      }, 25);
    };
    private final Consumer<EntityLivingBase> simpleSwing = (target) -> {
      this.setFightMode(true);
      this.setRegularAttack(true);
      this.openToParry = true;
      addEvent(()->this.lockLook = true, 10);
      addEvent(()-> this.openToParry = false, 18);
      addEvent(()-> this.shieldLowered = true, 15);
      addEvent(()-> {
          if(!this.isStunned()) {
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.3, 0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = (float) ((MobConfig.end_shielder_damage + 1) * ModConfig.biome_multiplier);
              ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
          }
      },20 );

      addEvent(()-> this.lockLook = false, 23);
        addEvent(()-> {
            if(!this.isStunned()) {
                this.shieldLowered = false;
            }
            }, 30);
      addEvent(()-> {
          if(!this.isStunned()) {
              this.setRegularAttack(false);
              this.setFightMode(false);
          }
      }, 30);
    };

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
        animationData.addAnimationController(new AnimationController(this, "stun_controller", 0, this::predicateStunned));

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (amount > 0.0F && this.canBlockDamageSource(source)) {
            this.damageShield(amount);

            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }
            }
            this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 0.6f + ModRand.getFloat(0.2f));

            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    protected void knightIsStunned() {
        this.setStunned(true);
        this.setImmovable(true);
        this.setFightMode(true);
        addEvent(()-> {
            this.setFightMode(false);
            this.setImmovable(false);
        this.setStunned(false);
        this.shieldLowered = false;
        }, 60);
    }


    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && !this.shieldLowered && this.isShielded() && !this.isStunned()) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();
            //Handler for Parrying specifically
            if(this.openToParry && vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);
                this.knightIsStunned();
                impactLocation = vec3d;
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
                return vec3d2.dotProduct(vec3d1) < 0.0D;
            }
            //Handler for other
           else if (vec3d != null) {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                return vec3d2.dotProduct(vec3d1) < 0.0D;
            }
        }

        return false;
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
