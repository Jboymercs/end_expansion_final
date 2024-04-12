package com.example.structure.entity.knighthouse;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityGroundCrystal;
import com.example.structure.entity.ai.ActionAerialTeleport;
import com.example.structure.entity.ai.EntityAerialTimedAttack;
import com.example.structure.entity.ai.EntityFlyMoveHelper;
import com.example.structure.entity.endking.EntityRedCrystal;
import com.example.structure.entity.util.IAttack;
import com.example.structure.entity.util.TimedAttackIniator;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import com.sun.jna.platform.win32.WinBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.Sys;
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

public class EntityKnightLord extends EntityKnightBase implements IAnimatable, IAttack, IAnimationTickable {

    private static final DataParameter<Boolean> FLYING_MODE = EntityDataManager.createKey(EntityKnightLord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE = EntityDataManager.createKey(EntityKnightLord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MULTI_ATTACK = EntityDataManager.createKey(EntityKnightLord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLOCKING = EntityDataManager.createKey(EntityKnightLord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MULTI_STRIKE = EntityDataManager.createKey(EntityKnightLord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_CRYSTALS = EntityDataManager.createKey(EntityKnightLord.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON = EntityDataManager.createKey(EntityKnightLord.class, DataSerializers.BOOLEAN);
    public void setFlyingMode(boolean value) {this.dataManager.set(FLYING_MODE, Boolean.valueOf(value));}
    public boolean isFlyingMode() {return this.dataManager.get(FLYING_MODE);}
    public void setPierce(boolean value) {this.dataManager.set(PIERCE, Boolean.valueOf(value));}
    public boolean isPierce() {return this.dataManager.get(PIERCE);}
    public void setBlocking(boolean value) {this.dataManager.set(BLOCKING, Boolean.valueOf(value));}
    public boolean isBlocking() {return this.dataManager.get(BLOCKING);}
    public void setMultiAttack(boolean value) {this.dataManager.set(MULTI_ATTACK, Boolean.valueOf(value));}
    public boolean isMultiAttack() {return this.dataManager.get(MULTI_ATTACK);}
    public void setMultiStrike(boolean value) {this.dataManager.set(MULTI_STRIKE, Boolean.valueOf(value));}
    public boolean isMultiStrike() {return this.dataManager.get(MULTI_STRIKE);}
    public void setSummonCrystals(boolean value) {this.dataManager.set(SUMMON_CRYSTALS, Boolean.valueOf(value));}
    public boolean isSummonCrystals() {return this.dataManager.get(SUMMON_CRYSTALS);}
    public void setSummonKnight(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}
    public boolean isSummonKnight() {return this.dataManager.get(SUMMON);}
    private float timeSinceNoTarget = 0;

    public boolean canAttemptBlock = false;

    public boolean isCurrentlyBlocking = false;


    private EntityAIBase attackAi = new EntityAerialTimedAttack(this, 10, 2, 30, new TimedAttackIniator<>(this, 20));
    private final String ANIM_WALKING_ARMS = "walk_upper";
    private final String ANIM_WALKING_LEGS = "walk_lower";
    private final String ANIM_IDLE = "idle";
    private final String ANIM_RING = "ring";
    private final String ANIM_FLYING = "flying";
    private final String ANIM_PIERCE = "fly_pierce";

    private final String ANIM_MULTI_ATTACK = "attack";
    private final String ANIM_STRIKE_ATTACK = "strike";
    private final String ANIM_CRYSTALS = "crystals";
    private final String ANIM_BLOCK = "block";

    private final String ANIM_ON_SUMMON = "summon";
    private Consumer<EntityLivingBase> prevAttack;
    private AnimationFactory factory = new AnimationFactory(this);
    public EntityKnightLord(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
            if(rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.RED, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
        }
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
        //Switches to Flying upon spotting the Enemy
        if(target != null && !this.isFlyingMode() && !this.isSummonKnight()) {
            this.tasks.addTask(4, attackAi);
            this.moveHelper = new EntityFlyMoveHelper(this);
            this.navigator = new PathNavigateFlying(this, world);
            this.setFlyingMode(true);
        }

        //If no enemy is present after awhile it will switch back to ground
        if(this.isFlyingMode() && this.timeSinceNoTarget > 200) {
            this.tasks.removeTask(attackAi);
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new PathNavigateGround(this, world);
            this.setFlyingMode(false);
        }
        if(target != null) {
            if(target instanceof EntityPlayer) {
                if(target.isSwingInProgress && this.canAttemptBlock) {
                this.blockCurrentTarget();
                }
            }

        }
        if(this.isBlocking()) {
            this.setMultiAttack(false);
            this.setPierce(false);
            this.setMultiStrike(false);
        }
        if(this.isSummonCrystals() && !this.isImmovable()) {
            this.motionY--;
        }

    }

    public void blockCurrentTarget() {
        this.setFightMode(true);
        this.setBlocking(true);
        this.isCurrentlyBlocking = true;
        addEvent(()-> {
            this.setFightMode(false);
            this.setBlocking(false);
            this.isCurrentlyBlocking = false;
        }, 30);
    }


    @Override
    public void entityInit() {
        super.entityInit();
       this.dataManager.register(FLYING_MODE, Boolean.valueOf(false));
       this.dataManager.register(PIERCE, Boolean.valueOf(false));
       this.dataManager.register(MULTI_ATTACK, Boolean.valueOf(false));
       this.dataManager.register(BLOCKING, Boolean.valueOf(false));
       this.dataManager.register(SUMMON_CRYSTALS, Boolean.valueOf(false));
       this.dataManager.register(MULTI_STRIKE, Boolean.valueOf(false));
       this.dataManager.register(SUMMON, Boolean.valueOf(true));
    }

    public EntityKnightLord(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setSize(0.8f, 2.0f);
        this.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.3F));
        addEvent(()-> {
            this.setImmovable(false);
            this.setSummonKnight(false);
        }, 10);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.unholy_knight_health * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.7D);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }
    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }


    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && !this.isBlocking() && !this.isSummonKnight()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(pierceAttack, multiAttack, multiStrike, summonLine, randomTeleport));
            double[] weights = {
                    (distance > 3 && prevAttack != pierceAttack) ? 1 : 0,
                    (distance < 4 && prevAttack != multiAttack) ? 1 : 0,
                    (distance < 4 && prevAttack != multiStrike) ? 1 : 0,
                    (distance > 3 && prevAttack != summonLine) ? 1 : 0,
                    (distance > 1 && prevAttack != randomTeleport) ? 1 : 0
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return (this.isBlocking()) ? 40 : 10;
    }

    private final Consumer<EntityLivingBase> randomTeleport = (target) -> {
        this.setFightMode(true);
        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.3F));
            new ActionLordTeleport(ModColors.RED).performAction(this, target);
        }, 10);

        addEvent(()-> this.setFightMode(false), 20);
    };

    private final Consumer<EntityLivingBase> summonLine = (target) -> {
      this.setFightMode(true);
      this.setSummonCrystals(true);
      addEvent(()-> {
        this.setImmovable(true);
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
                  EntityRedCrystal crystal = new EntityRedCrystal(this.world);
                  BlockPos blockPos = new BlockPos(initPos.x, initPos.y, initPos.z);
                  crystal.setPosition(blockPos);
                  crystal.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                  this.world.spawnEntity(crystal);

              }, t);
          }
      }, 18);
      addEvent(()-> {
          this.setImmovable(false);
          this.setFightMode(false);
          this.setSummonCrystals(false);
      }, 35);
    };
    private final Consumer<EntityLivingBase> multiStrike = (target) -> {
    this.setFightMode(true);
    this.setMultiStrike(true);
    this.canAttemptBlock = true;
    addEvent(()-> this.canAttemptBlock = false, 15);
        addEvent(()-> this.canAttemptBlock = true, 22);
        addEvent(()-> this.canAttemptBlock = false, 30);

        addEvent(()-> {
        if(!this.isBlocking()) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = 6.0f;
            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.5f, 0, false);
        }
        }, 17);

        addEvent(()-> {
            Vec3d targetPos = target.getPositionVector();
            addEvent(()-> {
            if(!this.isBlocking()) {
                ModUtils.leapTowards(this, targetPos, 0.4f, 0.1f);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = (float) (ModConfig.unholy_knight_damage * ModConfig.biome_multiplier);
                ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }
            }, 18);
        }, 15);

    addEvent(()-> {
        this.setFightMode(false);
        this.setMultiStrike(false);
    }, 40);
    };

    private final Consumer<EntityLivingBase> multiAttack = (target) -> {
      this.setFightMode(true);
      this.setMultiAttack(true);
      addEvent(()-> this.lockLook = true, 5);
      addEvent(()-> this.canAttemptBlock = true, 15);
        addEvent(()-> this.canAttemptBlock = false, 25);

        addEvent(()-> {
            if(!this.isBlocking()) {
                ModUtils.leapTowards(this, target.getPositionVector(), 0.3f, 0f);
            }
          }, 5);

        addEvent(()-> {
            if(!this.isBlocking()) {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = (float) (ModConfig.unholy_knight_damage * ModConfig.biome_multiplier);
                ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }
        }, 10);

        addEvent(()-> {
            if(!this.isBlocking()) {
                ModUtils.leapTowards(this, target.getPositionVector(), 0.3f, 0f);
            }
        }, 25);

        addEvent(()-> {
            if(!this.isBlocking()) {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5, 1.3, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = (float) (ModConfig.unholy_knight_damage * ModConfig.biome_multiplier);
                ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.5f, 0, false);
            }
        }, 30);

        addEvent(()-> this.lockLook = false, 35);
      addEvent(()-> {
          if(!this.isBlocking()) {
              this.setFightMode(false);
              this.setMultiAttack(false);
          }

      }, 40);
    };

    private final Consumer<EntityLivingBase> pierceAttack = (target) -> {
        this.setFightMode(true);
        this.setPierce(true);
        addEvent(()-> this.lockLook = true, 5);
        Vec3d targetPos = target.getPositionVector();
        addEvent(()-> this.canAttemptBlock = true, 25);
        addEvent(()-> this.canAttemptBlock = false, 35);
        addEvent(()-> ModUtils.leapTowards(this, targetPos, 0.9f, 0.1f), 18);
        addEvent(()-> {
            for(int i = 0; i < 10; i += 5) {
                addEvent(()-> {
                    if(!this.isBlocking()) {
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3, 1.3, 0)));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                        float damage = (float) ((ModConfig.unholy_knight_damage + 1) * ModConfig.biome_multiplier);
                        ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.5f, 0, false);
                    }
                }, i);
            }
        }, 18);

        addEvent(()-> this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 23);
        addEvent(()-> this.lockLook = false, 35);
        addEvent(()-> {
            if(!this.isBlocking()) {
                this.setFightMode(false);
                this.setPierce(false);
            }
        }, 35);
    };

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        animationData.addAnimationController(new AnimationController(this, "blink_controller", 0, this::predicateBlink));
        animationData.addAnimationController(new AnimationController(this, "wings_controller", 20, this::predicateWings));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttack));
        animationData.addAnimationController(new AnimationController(this, "block_controller", 0, this::predicateBlock));
    }

    private <E extends IAnimatable> PlayState predicateBlock(AnimationEvent<E> event) {
        if(!this.isSummonCrystals() && !this.isMultiStrike() && !this.isMultiAttack() && !this.isPierce() && !this.isSummonKnight()) {
            if (this.isBlocking()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLOCK, false));
                return PlayState.CONTINUE;
            }
        }
        if(this.isSummonKnight()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ON_SUMMON, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateWings(AnimationEvent<E> event) {
        if(this.isFlyingMode() && !this.isSummonKnight()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FLYING, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightMode() && !this.isBlocking()) {
            if(this.isPierce()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_PIERCE, false));
                return PlayState.CONTINUE;
            }
            if(this.isMultiAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MULTI_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isMultiStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonCrystals()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_CRYSTALS, false));
                return PlayState.CONTINUE;
            }
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    //Keep the Ring constantly turning
    private <E extends IAnimatable> PlayState predicateBlink(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RING, true));
        return PlayState.CONTINUE;
    }
    //Movement of Arms in Idle and walking
    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {

        if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F) && !this.isFightMode() && !this.isFlyingMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    //Movement of legs in Idle and walking
    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F) && !this.isFlyingMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_LEGS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    // Idle Handler
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {

        if(event.getLimbSwingAmount() > -0.09F && event.getLimbSwingAmount() < 0.09F && !this.isFlyingMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(this.isFlyingMode()) {
            ModUtils.aerialTravel(this, strafe, vertical, forward);
        } else {
            super.travel(strafe, vertical, forward);
        }

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
            this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 1.0f, 0.6f + ModRand.getFloat(0.2f));

            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn) {
        if (!damageSourceIn.isUnblockable() && this.isCurrentlyBlocking) {
            Vec3d vec3d = damageSourceIn.getDamageLocation();

            if (vec3d != null) {
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
