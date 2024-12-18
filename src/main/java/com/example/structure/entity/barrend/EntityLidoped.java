package com.example.structure.entity.barrend;

import com.example.structure.blocks.BlockGreenCrystal;
import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityChomper;
import com.example.structure.entity.ai.EntityAILidopedHome;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.entity.util.IAttack;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModPotions;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityLidoped extends EntityBarrendMob implements IAttack, IAnimatable, IAnimationTickable {

    private final String ANIM_WALK = "walk";
    private final String ANIM_IDLE = "idle";
    private final String ANIM_SLEEP = "sleep";

    private final String ANIM_ATTACK = "attack";
    private final String ANIM_ATTACK_LOOP = "attack_loop";

    private final String ANIM_BEGIN_HARVEST = "prepare_harvest";
    private final String ANIM_HARVEST_LOOP = "harvest";
    private final String ANIM_END_HARVEST = "end_harvest";

    private AnimationFactory factory = new AnimationFactory(this);

    private static final DataParameter<Boolean> ATTACK = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACK_LOOPED = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> START_HARVEST = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HARVEST = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> END_HARVEST = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean>HAS_HOME = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean>HAS_HARVESTED_ITEM = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean>ON_SLEEP = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ATTACHED_HEAD = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BOOLEAN);

    private static final DataParameter<ItemStack> ITEM_BACK = EntityDataManager.<ItemStack>createKey(EntityLidoped.class, DataSerializers.ITEM_STACK);

    private static final DataParameter<BlockPos> HOME_POS = EntityDataManager.createKey(EntityLidoped.class, DataSerializers.BLOCK_POS);

    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityLidoped.class, DataSerializers.VARINT);
    public void setAttacking(boolean value) {this.dataManager.set(ATTACK, Boolean.valueOf(value));}
    public boolean isAttacking() {return this.dataManager.get(ATTACK);}
    public void setAttackLooped(boolean value) {this.dataManager.set(ATTACK_LOOPED, Boolean.valueOf(value));}
    public boolean isAttackLooped() {return this.dataManager.get(ATTACK_LOOPED);}
    public void setStartHarvest(boolean value) {this.dataManager.set(START_HARVEST, Boolean.valueOf(value));}
    public boolean isStartHarvest() {return this.dataManager.get(START_HARVEST);}
    public void setHarvest(boolean value) {this.dataManager.set(HARVEST, Boolean.valueOf(value));}
    public boolean isHarvest() {return this.dataManager.get(HARVEST);}
    public void setEndHarvest(boolean value) {this.dataManager.set(END_HARVEST, Boolean.valueOf(value));}
    public boolean isEndHarvest() {return this.dataManager.get(END_HARVEST);}
    public void setAttachedHead(boolean value) {
        this.dataManager.set(ATTACHED_HEAD, Boolean.valueOf(value));
    }
    public boolean isAttachedHead() {
        return this.dataManager.get(ATTACHED_HEAD);
    }
    public void setOnSleep(boolean value) {
        this.dataManager.set(ON_SLEEP, Boolean.valueOf(value));
    }

    public boolean isOnSleep() {
        return this.dataManager.get(ON_SLEEP);
    }
    public void setHasHarvestedItem(boolean value) {
        this.dataManager.set(HAS_HARVESTED_ITEM, Boolean.valueOf(value));
    }
    public boolean isHasHarvestedItem() {
        return this.dataManager.get(HAS_HARVESTED_ITEM);
    }
    public BlockPos getHomePos() {
        return this.dataManager.get(HOME_POS);
    }
    public void setHomePos(BlockPos pos) {
        this.dataManager.set(HOME_POS, pos);
    }
    public void setHasHome(boolean value) {
        this.dataManager.set(HAS_HOME, Boolean.valueOf(value));
    }
    public boolean isHasHome() {
        return this.dataManager.get(HAS_HOME);
    }

    public EntityLidoped(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.5F, 0.4F );
    }

    public EntityLidoped(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.4F );

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Variant", getSkin());
        nbt.setBoolean("Attacking", this.isAttacking());
        nbt.setBoolean("AttackLoop", this.isAttackLooped());
        nbt.setBoolean("StartHarvest", this.isStartHarvest());
        nbt.setBoolean("Harvest", this.isHarvest());
        nbt.setBoolean("EndHarvest", this.isEndHarvest());
        nbt.setBoolean("HasHome", this.isHasHome());
        nbt.setBoolean("HasHarvestItem", this.isHasHarvestedItem());
        nbt.setBoolean("Attached_Head", this.isAttachedHead());
        nbt.setBoolean("OnSleep", this.isOnSleep());
        nbt.setInteger("Home_Loc_X", this.getHomePos().getX());
        nbt.setInteger("Home_Loc_Y", this.getHomePos().getY());
        nbt.setInteger("Home_Loc_Z", this.getHomePos().getZ());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setSkin(nbt.getInteger("Variant"));
        this.setAttacking(nbt.getBoolean("Attacking"));
        this.setAttackLooped(nbt.getBoolean("AttackLoop"));
        this.setStartHarvest(nbt.getBoolean("StartHarvest"));
        this.setHarvest(nbt.getBoolean("Harvest"));
        this.setEndHarvest(nbt.getBoolean("EndHarvest"));
        this.setHasHome(nbt.getBoolean("HasHome"));
        this.setOnSleep(nbt.getBoolean("OnSleep"));
        this.setHasHarvestedItem(nbt.getBoolean("HasHarvestItem"));
        this.setAttachedHead(nbt.getBoolean("Attached_Head"));
        this.setHomePos(new BlockPos(nbt.getInteger("Home_Loc_X"), nbt.getInteger("Home_Loc_Y"), nbt.getInteger("Home_Loc_Z")));
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
        this.dataManager.register(ITEM_BACK, ItemStack.EMPTY);
        this.dataManager.register(ATTACK, Boolean.valueOf(false));
        this.dataManager.register(ATTACK_LOOPED, Boolean.valueOf(false));
        this.dataManager.register(START_HARVEST, Boolean.valueOf(false));
        this.dataManager.register(HARVEST, Boolean.valueOf(false));
        this.dataManager.register(END_HARVEST, Boolean.valueOf(false));
        this.dataManager.register(HAS_HOME, Boolean.valueOf(false));
        this.dataManager.register(HAS_HARVESTED_ITEM, Boolean.valueOf(false));
        this.dataManager.register(ON_SLEEP, Boolean.valueOf(false));
        this.dataManager.register(ATTACHED_HEAD, Boolean.valueOf(false));

        this.dataManager.register(HOME_POS, new BlockPos(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z));
    }

    private boolean hasUndoneotherStuff = false;
    private int harvestSoundTimer = 20;

    private int attackCooldownTwo = 25;

    @Override
    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase target = this.getAttackTarget();
        if(target == null) {
            this.setAttachedHead(false);
            this.setAttackLooped(false);

            if(this.isOnSleep() || this.isHarvest() || this.isStartHarvest()) {
                this.setImmovable(true);
                this.lockLook = true;
                this.hasUndoneotherStuff = true;
            }

            if(this.isHarvest()) {
                if(harvestSoundTimer < 0) {
                    this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.3f, 1.4f);
                    harvestSoundTimer = 15;
                } else {
                    harvestSoundTimer--;
                }
            }

            if(world.getBlockState(this.getPosition()).getBlock() == ModBlocks.BARE_ACID) {
                Vec3d lookVec = this.getLookVec().scale(0.1F);
                this.motionY = 0.15;
                this.motionX = lookVec.x;
                this.motionZ = lookVec.z;

            }

            if(!this.isHasHome()) {
                AxisAlignedBB box = getEntityBoundingBox().grow(12, 8, 12);
                BlockPos setTooPos = ModUtils.searchForBlocks(box, world, ModBlocks.BARE_BARK_HOLE.getDefaultState());
                if(setTooPos != null) {
                    //Found Home
                    this.setHomePos(setTooPos);
                    this.setHasHome(true);
                }
            }
        } else {
            if(this.hasUndoneotherStuff) {
                this.setTooDefault();
            }

            if(this.isJumping) {
                List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1.5D), e -> !e.getIsInvulnerable());
                if(!nearbyEntities.isEmpty()) {
                        for(EntityLivingBase entity : nearbyEntities) {
                            if(entity == target) {
                            if(entity instanceof EntityLidoped) {
                                if(!((EntityLidoped) entity).isAttachedHead()) {
                                    //attach Too head
                                    this.setAttachedHead(true);
                                    this.setAttackLooped(true);
                                    this.isJumping = false;
                                }
                            } else {
                                this.setAttackLooped(true);
                                this.setAttachedHead(true);
                                this.isJumping = false;
                            }
                        }
                    }
                }
                this.isJumping = false;
            } else if (this.isAttachedHead()) {
                Vec3d playerLookVec = target.getLookVec();
                Vec3d playerPos = new Vec3d(target.posX + playerLookVec.x * 0.5D,target.posY + playerLookVec.y + target.getEyeHeight() - 0.4, target. posZ + playerLookVec.z * 0.5D);
                ModUtils.setEntityPosition(this, playerPos);
                if(attackCooldownTwo < 0) {
                    performAttachedAttack(target);
                } else {
                    attackCooldownTwo--;
                }
            }
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }




    private void performAttachedAttack(EntityLivingBase target) {
        Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0, 0)));
        DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
        float damage = (float) (MobConfig.lidoped_attack_damage * getAttackModifiersBarrend());
        ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.2f, 0, false);
        target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0));
        attackCooldownTwo = 25;
    }

    //Helps remove booleans or it being stuck in a position, for attacking back
    private void setTooDefault() {
        this.setImmovable(false);
        this.lockLook = false;
        this.setHarvest(false);
        this.setOnSleep(false);
        this.hasUndoneotherStuff = false;
        this.setEndHarvest(false);
        this.setStartHarvest(false);

    }

    @Override
    protected boolean canDespawn() {

        // Edit this to restricting them not despawning in Dungeons
        return this.ticksExisted > 20 * 60 * 20;

    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(3, new EntityAILidopedHome<>(this, 1.2D, 30F));
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.2D, 60, 5F, 0.35f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) MobConfig.lidoped_health * getHealthModifierBarrend());
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10 * ModConfig.barrend_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.3D);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        data.addAnimationController(new AnimationController(this, "attack_loops", 0, this::predicateAttackLooped));
        data.addAnimationController(new AnimationController(this, "action_attacks", 0, this::predicateActions));
    }

    private <E extends IAnimatable> PlayState predicateAttackLooped(AnimationEvent<E> event) {
        if(this.isOnSleep() && !this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SLEEP, true));
            return PlayState.CONTINUE;
        }
        if(this.isHarvest()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HARVEST_LOOP, true));
            return PlayState.CONTINUE;
        }
        if(this.isAttackLooped()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK_LOOP, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateActions(AnimationEvent<E> event) {
        if(this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_ATTACK, false));
            return PlayState.CONTINUE;
        } else if (this.isStartHarvest()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BEGIN_HARVEST, false));
            return PlayState.CONTINUE;
        } else if (this.isEndHarvest()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_END_HARVEST, false));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isAttackLooped() && !this.isAttacking() && !this.isStartHarvest() && !this.isHarvest() && !this.isEndHarvest() && !this.isOnSleep()) {

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

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public boolean getCanSpawnHere()
    {
            return this.world.rand.nextInt(35) == 0;
    }

    private Consumer<EntityLivingBase> prevAttack;

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isAttacking() && !this.isAttackLooped()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(jump_attack));
            double[] weights = {
                    (distance <= 9) ? 1/distance : 0 //Jump ATTACK
            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 60;
    }

    private boolean isJumping = false;
    private Consumer<EntityLivingBase> jump_attack = (target)-> {
      this.setAttacking(true);
      this.setImmovable(true);
      addEvent(()-> {
        Vec3d targetPos = target.getPositionVector().add(ModUtils.yVec(1.5D));
          float distance = getDistance(target);
          addEvent(()-> {
              this.setImmovable(false);
              this.isJumping = true;
              ModUtils.leapTowards(this, targetPos, (float) (0.5 * Math.sqrt(distance)), 1.7f);
          }, 7);
      }, 10);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.2, 0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
          float damage = (float) (MobConfig.lidoped_attack_damage * getAttackModifiersBarrend());
          ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.2f, 0, false);
      }, 27);

      addEvent(()-> this.isJumping = false, 33);
      addEvent(()-> this.setAttacking(false), 35);
    };

    @Override
    public void tick() {

    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
            this.setSkin(rand.nextInt(5));
        return super.onInitialSpawn(difficulty, entityLivingData);
    }

    //For the item render on it's back

    public enum LIDOPED_BACK {
        BACK("Torso");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        LIDOPED_BACK(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static LIDOPED_BACK getFromBoneName(String boneName) {
            if ("Torso".equals(boneName)) {
                return BACK;
            }
            return null;
        }

        public int getIndex() {
            if (this == LIDOPED_BACK.BACK) {
                return 1;
            }
            return 0;
        }
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }

    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "lidoped");
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }

    public void equipBugBackItem(LIDOPED_BACK head, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (head == LIDOPED_BACK.BACK) {
            this.dataManager.set(ITEM_BACK, state);
        }
    }


    public ItemStack getItemFromBugBack(LIDOPED_BACK head) {
        if (head == LIDOPED_BACK.BACK) {
            return this.dataManager.get(ITEM_BACK);
        }
        return null;
    }
}
