package com.example.structure.entity.seekers;

import com.example.structure.config.ItemConfig;
import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.ProjectilePurple;
import com.example.structure.entity.ai.EntityAISeeker;
import com.example.structure.entity.ai.EntityAiSeekerPrime;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.knightlord.ActionQuickSlash;
import com.example.structure.entity.knighthouse.knightlord.EntityBloodSlash;
import com.example.structure.entity.util.IAttack;
import com.example.structure.init.ModBlocks;
import com.example.structure.util.*;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
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
import java.util.function.Supplier;

public class EndSeekerPrime extends EntityModBase implements IAnimatable, IAttack, IAnimationTickable {

    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.NOTCHED_6));
    private static final DataParameter<Boolean> PRIME_MODE = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLINK_MODE = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_STRIKE_ONE = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MELEE_STRIKE_TWO = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_ATTACK = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_GUN = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_ATTACK = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WIND_SWEEP_ATTACK = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> SUMMON_AOE = EntityDataManager.createKey(EndSeekerPrime.class, DataSerializers.BOOLEAN);

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);

        nbt.setBoolean("Prime_Mode", this.isFightMode());
        nbt.setBoolean("Blink_Mode", this.isBlinkMode());
        nbt.setBoolean("Melee_Strike_One", this.isMeleeStrikeOne());
        nbt.setBoolean("Melee_Strike_Two", this.isMeleeStrikeTwo());
        nbt.setBoolean("Pierce_Attack", this.isPierceAttack());
        nbt.setBoolean("Shoot_Gun", this.isShootGun());
        nbt.setBoolean("Combo_Attack", this.isComboAttack());
        nbt.setBoolean("Wind_Sweep_Attack", this.isWindSweepAttack());
        nbt.setBoolean("Summon_Aoe", this.isSummonAOE());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);

        this.setFightMode(nbt.getBoolean("Prime_Mode"));
        this.setBlinkMode(nbt.getBoolean("Blink_Mode"));
        this.setMeleeStrikeOne(nbt.getBoolean("Melee_Strike_One"));
        this.setMeleeStrikeTwo(nbt.getBoolean("Melee_Strike_Two"));
        this.setPierceAttack(nbt.getBoolean("Pierce_Attack"));
        this.setShootGun(nbt.getBoolean("Shoot_Gun"));
        this.setComboAttack(nbt.getBoolean("Combo_Attack"));
        this.setWindSweepAttack(nbt.getBoolean("Wind_Sweep_Attack"));
        this.setSummonAoe(nbt.getBoolean("Summon_Aoe"));
    }

    public void setFightMode(boolean value) {this.dataManager.set(PRIME_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(PRIME_MODE);}
    public void setBlinkMode(boolean value) {this.dataManager.set(BLINK_MODE, Boolean.valueOf(value));}
    public boolean isBlinkMode() {return this.dataManager.get(BLINK_MODE);}
    public void setMeleeStrikeOne(boolean value) {this.dataManager.set(MELEE_STRIKE_ONE, Boolean.valueOf(value));}
    public boolean isMeleeStrikeOne() {return this.dataManager.get(MELEE_STRIKE_ONE);}
    public void setMeleeStrikeTwo(boolean value) {this.dataManager.set(MELEE_STRIKE_TWO, Boolean.valueOf(value));}
    public boolean isMeleeStrikeTwo() {return this.dataManager.get(MELEE_STRIKE_TWO);}
    public void setPierceAttack(boolean value) {this.dataManager.set(PIERCE_ATTACK, Boolean.valueOf(value));}
    public boolean isPierceAttack() {return this.dataManager.get(PIERCE_ATTACK);}
    public void setShootGun(boolean value) {this.dataManager.set(SHOOT_GUN, Boolean.valueOf(value));}
    public boolean isShootGun() {return this.dataManager.get(SHOOT_GUN);}
    public void setComboAttack(boolean value) {this.dataManager.set(COMBO_ATTACK, Boolean.valueOf(value));}
    public boolean isComboAttack() {return this.dataManager.get(COMBO_ATTACK);}
    public void setWindSweepAttack(boolean value) {this.dataManager.set(WIND_SWEEP_ATTACK, Boolean.valueOf(value));}
    public boolean isWindSweepAttack() {return this.dataManager.get(WIND_SWEEP_ATTACK);}
    public void setSummonAoe(boolean value) {this.dataManager.set(SUMMON_AOE, Boolean.valueOf(value));}
    public boolean isSummonAOE() {return this.dataManager.get(SUMMON_AOE);}
    private Consumer<EntityLivingBase> prevAttack;

    //Idle Animations
    private final String ANIM_IDLE = "idle";
    private final String ANIM_IDLE_ARMS = "idle_arms";
    private final String ANIM_BLINK = "blink_1";

    //Ranged Attacks
    private final String ANIM_SHOOT_GUN = "shoot_gun";

    //Melee Attacks
    private final String ANIM_MELEE_ATTACK_ONE = "attack_melee";
    private final String ANIM_MELEE_ATTACK_TWO = "attack_melee_2";
    private final String ANIM_DASH_ATTACK = "pierce_dash";

    private final String ANIM_COMBO_ATTACK = "combo_1";
    private final String ANIM_WIND_SWEEP_ATTACK = "wind_sweep";
    private final String ANIM_SUMMON_AOE = "summon_aoe";
    private AnimationFactory factory = new AnimationFactory(this);
    public int blinkCoolDown = 0;
    public boolean isMeleeMode = false;
    public boolean isRangedMode = false;

    Supplier<Projectile> ground_projectiles = () -> new EntityBloodSlash(world, this, (float) this.getAttack(), null, ModColors.AZURE);


    public EndSeekerPrime(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.isMeleeMode = true;
        this.iAmBossMob = true;
        this.experienceValue = 150;
    }

    public EndSeekerPrime(World worldIn) {
        super(worldIn);
        this.isMeleeMode = true;
        this.iAmBossMob = true;
        this.experienceValue = 150;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAiSeekerPrime<>(this, 1.5, 20, 14F, 0.4f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityEnderKnight>(this, EntityEnderKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityEnderMage>(this, EntityEnderMage.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EntityEnderShield>(this, EntityEnderShield.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

        if(ticksExisted > 20 && ticksExisted < 100) {
            AxisAlignedBB box = getEntityBoundingBox().grow(15, 7, 15);
            BlockPos posToo = ModUtils.searchForBlocks(box, world, Blocks.IRON_BARS.getDefaultState());
            if(ModUtils.searchForBlocks(box, world, Blocks.IRON_BARS.getDefaultState()) != null) {
                if(posToo != null) {
                    world.setBlockToAir(posToo);
                }
            }
        }

        if(this.setFlameParticles) {
            AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.1, 1.25).offset(0, 0.1, 0);
            ModUtils.destroyBlocksInAABB(box, world, this);
        }


        if(rand.nextInt(5) == 0 && blinkCoolDown > 120) {

            this.setBlinkMode(true);
            addEvent(()-> this.setBlinkMode(false), 35);
            blinkCoolDown = 0;
        } else {
            blinkCoolDown++;
        }



        if(this.lockLook) {

            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;

        }
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(PRIME_MODE, Boolean.valueOf(false));
        this.dataManager.register(BLINK_MODE, Boolean.valueOf(false));
        this.dataManager.register(MELEE_STRIKE_ONE, Boolean.valueOf(false));
        this.dataManager.register(MELEE_STRIKE_TWO, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_GUN, Boolean.valueOf(false));
        this.dataManager.register(COMBO_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(WIND_SWEEP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_AOE, Boolean.valueOf(false));
    }

    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (rand.nextInt(3) == 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        if(setFlameParticles) {
            world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
        }

    }

    private boolean setFlameParticles = false;

    //Particle Handler for the boss
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.1, 0))), ModColors.WHITE, new Vec3d(ModRand.getFloat(1) * -0.05, -0.3, ModRand.getFloat(1) * -0.05));
        }
         if(id == ModUtils.SECOND_PARTICLE_BYTE) {
                 ModUtils.circleCallback(2, 30, (pos)-> {
                     pos = new Vec3d(pos.x, 0, pos.y);
                     ParticleManager.spawnColoredSmoke(world, this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 1.1, 0))), ModColors.RED, new Vec3d(pos.x * 0.3, -0.2, pos.z * 0.3));
                 });
        }
        super.handleStatusUpdate(id);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) MobConfig.seeker_prime_health * getHealthModifierLamented());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MobConfig.seeker_prime_attack_damage * getAttackModifierLamented());
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.seeker_prime_armor * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.seeker_prime_armor_toughness * ModConfig.lamented_multiplier);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(meleeAttackOne, meleeAttackTwo, dashAttack, shootGunAttack, comboAttack, wind_sweep, summon_aoe_attack));
            double[] weights = {
                    (distance < 2 && prevAttack != meleeAttackOne) ? 2/distance : 0, // First Melee Attack
                    (distance < 2 && prevAttack != meleeAttackTwo) ? 2/distance : 0, // Second Melee Attack
                    (distance <= 9) ? 1/distance : 0, //Dash Attack
                    (distance > 9) ? distance * 0.02 : 0, //Shoot Gun Attack
                    (distance < 6 && prevAttack != comboAttack) ? 1.5/distance : 0, //Combo Attack
                    (distance < 7 && prevAttack != wind_sweep && HealthChange <= 0.5) ? 1.5/distance : 0, // Wind Sweep
                    (distance > 3 && prevAttack != summon_aoe_attack && getScaleForNewAttacks()) ? 1.5/distance : 0 // Summon AOE, only activates from arena spawns
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return prevAttack == comboAttack || prevAttack == shootGunAttack || prevAttack == dashAttack ? 40 : 20;
    }

    private final Consumer<EntityLivingBase> summon_aoe_attack = (target) -> {
        this.setFightMode(true);
        this.setSummonAoe(true);

        addEvent(()-> this.setImmovable(true), 15);

        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(2.0f, (e)-> damage, this, offset, source, 0.9f, 0, false);
        }, 39);

        addEvent(()-> {
        //summon AOE
            this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            int distanceFrom = (int) this.getDistance(target);
            new ActionPrimeAOE(distanceFrom + 2).performAction(this, target);
        }, 50);

        addEvent(()-> this.setImmovable(false), 90);
        addEvent(()-> {
        this.setSummonAoe(false);
        this.setFightMode(false);
        }, 100);
    };

    private final Consumer<EntityLivingBase> wind_sweep = (target) -> {
      this.setFightMode(true);
      this.setWindSweepAttack(true);

      //first jump
      addEvent(()-> {
        this.lockLook = true;
        Vec3d playerPos = new Vec3d(target.posX, target.posY, target.posZ);
          float distance = getDistance(target);
          //Intial Frontal Attack
        addEvent(() -> {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.7f, 0, false);
        }, 10);
        //First leap
        addEvent(()-> ModUtils.leapTowards(this, playerPos,(float) (0.45 * Math.sqrt(distance + 5)), 0.2f ), 17);
        //Circle Attack 1
          addEvent(() -> {
              this.setFlameParticles = true;
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0.5,0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.8f, (e)-> damage, this, offset, source, 0.7f, 0, false);
              if(getScaleForNewAttacks()) {
                  new ActionQuickSlash(ground_projectiles, 0.55F).performAction(this, target);
              }
              this.lockLook = false;
              addEvent(()-> this.setFlameParticles = false, 2);
          }, 26);

      }, 10);


        //Second jump
        addEvent(()-> {
            this.lockLook = true;
            Vec3d playerPos = new Vec3d(target.posX, target.posY, target.posZ);
            float distance = getDistance(target);
            //Second leap
            addEvent(()-> ModUtils.leapTowards(this, playerPos,(float) (0.45 * Math.sqrt(distance + 5)), 0.2f ), 5);
            //Circle Attack 2
            addEvent(() -> {
                this.setFlameParticles = true;
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,0.5,0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.8f, (e)-> damage, this, offset, source, 0.7f, 0, false);
                if(getScaleForNewAttacks()) {
                    new ActionQuickSlash(ground_projectiles, 0.55F).performAction(this, target);
                }
                this.lockLook = false;
                addEvent(()-> this.setFlameParticles = false, 2);
            }, 12);

        }, 40);


        addEvent(()-> this.lockLook = false, 65);


      addEvent(()-> {
        this.setFightMode(false);
        this.setWindSweepAttack(false);
      }, 75);
    };
    private final Consumer<EntityLivingBase> comboAttack = (target) -> {
        this.setComboAttack(true);
        this.setFightMode(true);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetPosToo = target.getPositionVector();
            float distance = getDistance(target);
            addEvent(()-> ModUtils.leapTowards(this, targetPosToo,(float) (0.45 * Math.sqrt(distance)), 0.15f ), 6);
            addEvent(()-> {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4,1.3,0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.4f, 0, false);
                if(getScaleForNewAttacks()) {
                    new ActionQuickSlash(ground_projectiles, 0.55F).performAction(this, target);
                }
            }, 12);
        }, 5);

        addEvent(()-> this.lockLook = false, 25);

        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetPosToo = target.getPositionVector();
            float distance = getDistance(target);
            addEvent(()-> ModUtils.leapTowards(this, targetPosToo,(float) (0.45 * Math.sqrt(distance)), 0.15f ), 8);

            addEvent(()-> {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,1.3,0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.4f, 0, false);
            }, 12);

        }, 30);

        addEvent(()-> {
            this.lockLook =false;
        this.setFightMode(false);
        this.setComboAttack(false);
        }, 60);
    };

    private final Consumer<EntityLivingBase> meleeAttackOne = (target) -> {
        this.setFightMode(true);
        this.setMeleeStrikeOne(true);
        this.lockLook = true;

        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.4f, 0, false);
        }, 22);
        addEvent(()-> {
            this.lockLook = false;
            this.setFightMode(false);
            this.setMeleeStrikeOne(false);
        }, 35);
    };

    private final Consumer<EntityLivingBase> meleeAttackTwo = (target) -> {
        this.setFightMode(true);
        this.setMeleeStrikeTwo(true);
        this.lockLook = true;

        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.4,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = this.getAttack();
            ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.4f, 0, false);
        },25);
        addEvent(()-> {
            this.lockLook = false;
            this.setFightMode(false);
            this.setMeleeStrikeTwo(false);
        }, 40);
    };

    private final Consumer<EntityLivingBase> dashAttack = (target)-> {
        this.setFightMode(true);
        this.setPierceAttack(true);
        addEvent(()-> {
            this.playSound(ModSoundHandler.SEEKER_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
        }, 18);

        addEvent(()-> {
            Vec3d posToGo = target.getPositionVector();
            float distance = getDistance(target);
            this.lockLook = true;
            this.setImmovable(true);
            addEvent(()-> {
                this.setImmovable(false);
                ModUtils.leapTowards(this, posToGo, (float) (0.45 * Math.sqrt(distance + 5)), 0.1f);
                for(int i = 0; i < 15; i+=5) {
                    addEvent(()-> {
                        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,1.3,0)));
                        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                        float damage = this.getAttack() + 2;
                        ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.6f, 0, false);
                    }, i);
                }
            },13 );
        }, 10);
        addEvent(()-> {
            this.lockLook = false;
            this.setPierceAttack(false);
            this.setFightMode(false);
        }, 60);
    };

    private final Consumer<EntityLivingBase> shootGunAttack = (target)-> {
        this.setFightMode(true);
        this.setShootGun(true);

        //This Basically adds a lag to the projectiles making it easier to dodge since these mobs aren't meant to be as difficult YET
        addEvent(()-> {

            addEvent(()-> {
                this.playSound(ModSoundHandler.SEEKER_SHOOT, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                ProjectilePurple projectilePurple = new ProjectilePurple(world, this, ItemConfig.purp_projectile);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0.3)));
                Vec3d vel2 =  target.getPositionVector().add(ModUtils.yVec(1.0D)).subtract(offset);
                projectilePurple.setPosition(offset.x, offset.y, offset.z);
                projectilePurple.shoot(vel2.x, vel2.y, vel2.z, 1.3f, 1.0f);
                projectilePurple.setTravelRange(20F);
                world.spawnEntity(projectilePurple);
            }, 10);
        }, 15);


        addEvent(()-> {

            addEvent(()-> {
                this.playSound(ModSoundHandler.SEEKER_SHOOT, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                ProjectilePurple projectilePurple = new ProjectilePurple(world, this, ItemConfig.purp_projectile);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0.3)));
                Vec3d vel2 =  target.getPositionVector().add(ModUtils.yVec(1.0D)).subtract(offset);
                projectilePurple.setPosition(offset.x, offset.y, offset.z);
                projectilePurple.shoot(vel2.x, vel2.y, vel2.z, 1.3f, 1.0f);
                projectilePurple.setTravelRange(20F);
                world.spawnEntity(projectilePurple);
            }, 10);
        }, 25);


        addEvent(()-> {

            addEvent(()-> {
                this.playSound(ModSoundHandler.SEEKER_SHOOT, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
                ProjectilePurple projectilePurple = new ProjectilePurple(world, this, ItemConfig.purp_projectile);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.5, 0.3)));
                Vec3d vel2 =  target.getPositionVector().add(ModUtils.yVec(1.0D)).subtract(offset);
                projectilePurple.setPosition(offset.x, offset.y, offset.z);
                projectilePurple.shoot(vel2.x, vel2.y, vel2.z, 1.3f, 1.0f);
                projectilePurple.setTravelRange(20F);
                world.spawnEntity(projectilePurple);
            }, 10);
        }, 35);

        addEvent(()-> {
            this.setFightMode(false);
            this.setShootGun(false);
        }, 65);
    };

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

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "blink_controller", 0, this::predicateBlink));
        animationData.addAnimationController(new AnimationController(this, "arms_h_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attacks_h_controller", 0, this::predicateAttacks));
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {

    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.SEEKER_ELDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.SEEKER_ELDER_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.SEEKER_HOVER;
    }


    private<E extends IAnimatable> PlayState predicateBlink(AnimationEvent<E> event) {
        if(this.isBlinkMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLINK, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "seeker_prime");
    private static final ResourceLocation LOOT_ARENA = new ResourceLocation(ModReference.MOD_ID, "seeker_prime_arena");
    @Override
    protected ResourceLocation getLootTable() {
        if(getScaleForNewAttacks()) {
            return LOOT_ARENA;
        }
        return LOOT;
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_ARMS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isMeleeStrikeOne()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_ATTACK_ONE, false));
            }
            if(this.isMeleeStrikeTwo()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MELEE_ATTACK_TWO, false));
            }
            if(this.isPierceAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH_ATTACK, false));
            }
            if(this.isShootGun()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SHOOT_GUN, false));
            }
            if(this.isComboAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_ATTACK, false));
            }
            if(this.isWindSweepAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WIND_SWEEP_ATTACK, false));
            }
            if(this.isSummonAOE()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_AOE, false));
            }

            return PlayState.CONTINUE;
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
