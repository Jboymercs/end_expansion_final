package com.example.structure.entity;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.animation.Animation;
import com.example.structure.entity.animation.IAnimatedEntity;
import com.example.structure.entity.barrend.EntityLidoped;
import com.example.structure.entity.util.IAttack;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.google.common.collect.Sets;
import com.ibm.icu.text.DateIntervalInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class EntityEndBug extends EntityModBaseTameable implements IAnimatable, IAnimatedEntity, IAttack, IMob, IAnimationTickable {

    private Animation currentAnimation;
    private int animationTick;

    private static final Set<Item> TAME_ITEMS = Sets.newHashSet(ModItems.RED_CRYSTAL_ITEM);
    protected static final DataParameter<Boolean> BUG_MODE = EntityDataManager.createKey(EntityModBaseTameable.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> HEAD_TWITCH = EntityDataManager.createKey(EntityModBaseTameable.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> BUTT_TWITCH = EntityDataManager.createKey(EntityModBaseTameable.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> DIGGING = EntityDataManager.createKey(EntityModBaseTameable.class, DataSerializers.BOOLEAN);

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
      //  nbt.setBoolean("Bug_Mode", this.dataManager.get(BUG_MODE));
     //   nbt.setBoolean("Head_Twitch", this.dataManager.get(HEAD_TWITCH));
      //  nbt.setBoolean("Butt_Twitch", this.dataManager.get(BUTT_TWITCH));
     //   nbt.setBoolean("Digging", this.dataManager.get(DIGGING));

        nbt.setBoolean("Bug_Mode", this.isFightMode());
        nbt.setBoolean("Head_Twitch", this.isHeadTwitch());
        nbt.setBoolean("Butt_Twitch", this.isButtTwitch());
        nbt.setBoolean("Digging", this.isDigging());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
     //   this.dataManager.set(BUG_MODE, nbt.getBoolean("Bug_Mode"));
    //    this.dataManager.set(HEAD_TWITCH, nbt.getBoolean("Head_Twitch"));
    //    this.dataManager.set(BUTT_TWITCH, nbt.getBoolean("Butt_Twitch"));
    //    this.dataManager.set(DIGGING, nbt.getBoolean("Digging"));

        this.setFightMode(nbt.getBoolean("Bug_Mode"));
        this.setHeadTwitch(nbt.getBoolean("Head_Twitch"));
        this.setButtTwitch(nbt.getBoolean("Butt_Twitch"));
        this.setDigging(nbt.getBoolean("Digging"));
    }


    public void setHeadTwitch(boolean value) {this.dataManager.set(HEAD_TWITCH, Boolean.valueOf(value));}
    public boolean isHeadTwitch() {return this.dataManager.get(HEAD_TWITCH);}
    public void setButtTwitch(boolean value) {this.dataManager.set(BUTT_TWITCH, Boolean.valueOf(value));}
    public boolean isButtTwitch() {return this.dataManager.get(BUTT_TWITCH);}
    public void setFightMode(boolean value) {this.dataManager.set(BUG_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(BUG_MODE);}
    public void setDigging(boolean value) {this.dataManager.set(DIGGING, Boolean.valueOf(value));}
    public boolean isDigging() {return this.dataManager.get(DIGGING);}
    private final String ANIM_LEGS_IDLE = "legs_idle";
    private final String ANIM_LEGS_MOVE = "legs_move";
    private final String TORSO_MOVE = "torso_move";

    private final String ANIM_HEAD_TWITCH = "head_twitch";
    private final String ANIM_BUTT_TWITCH = "butt_twitch";
    private final String ANIM_BITE = "bite";
    private final String ANIM_DIG = "dig";
    private final String ANIM_TAKE_ITEM = "take_item";
    protected int buttTwitchTimer = 200 + (int) ModRand.getFloat(200);

    protected int digTimer = 20000 + (int) ModRand.range(2000, 15000);

    protected int tamedDigTimer = 3000 + (int) ModRand.getFloat(1000);
    private EntityAISit aiSit;

    protected boolean currentlyHoldingItem = false;
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityEndBug(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.5f, 1.0f);
        this.experienceValue = 13;
    }

    public EntityEndBug(World worldIn) {
        super(worldIn);
        this.setSize(1.5f, 1.0f);
        this.experienceValue = 13;
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(HEAD_TWITCH, Boolean.valueOf(false));
        this.dataManager.register(BUTT_TWITCH, Boolean.valueOf(false));
        this.dataManager.register(BUG_MODE, Boolean.valueOf(false));
        this.dataManager.register(DIGGING, Boolean.valueOf(false));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MobConfig.parasite_health * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D * ModConfig.biome_multiplier);
    }


    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(5, this.aiSit);
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.2, 60, 3.0f, 0.2f));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityLidoped>(this, EntityLidoped.class, 1, true, false, null));
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(!this.isTamed() && TAME_ITEMS.contains(stack.getItem())) {
            if(!player.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
            if(!this.world.isRemote) {
                if(this.rand.nextInt(2) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.setTamedBy(player);
                    this.navigator.clearPath();
                    this.setAttackTarget(null);
                    this.playTameEffect(true);
                    this.aiSit.setSitting(true);
                    this.world.setEntityState(this, (byte) 7);
                } else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    protected void setupTamedAI() {

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(buttTwitchTimer == 0) {
            this.setButtTwitch(true);
            addEvent(()-> this.setButtTwitch(false), 10);
            buttTwitchTimer = 200 + (int) ModRand.getFloat(200);
        } else {
            buttTwitchTimer--;
        }
        List<EntityPlayer> nearbySwords = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(4D), e -> !e.getIsInvulnerable());
        if(!nearbySwords.isEmpty()) {
            for(EntityPlayer player : nearbySwords) {
                if(player.getHeldItemMainhand().getItem() == ModItems.RED_CRYSTAL_ITEM && !this.isOwner(player)) {
                    this.setAttackTarget(player);
                } else {
                    this.setAttackTarget(null);
                }
            }
        }
        if(!this.currentlyHoldingItem) {
            Vec3d blockToo = this.getPositionVector();
            BlockPos blockPos = new BlockPos(blockToo.x, blockToo.y, blockToo.z);
            //Not Tamed
            if(!this.isTamed()) {
                if (world.getBlockState(blockPos.down()).getBlock() == ModBlocks.END_ASH) {
                    if (digTimer == 0 && !world.isRemote) {
                        this.setDigging(true);
                        this.setImmovable(true);
                        addEvent(() -> this.setDigging(false), 23);
                        addEvent(() -> this.setImmovable(false), 23);
                        addEvent(() -> {
                            this.dropItem(ModItems.INFUSION_CORE, 1);
                            this.playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0f, 1.0f);
                        }, 15);
                        digTimer = 20000 + (int) ModRand.range(2000, 15000);

                    } else {
                        digTimer--;
                    }
                }
            } else {
                //Tamed Version
                if (world.getBlockState(blockPos.down()).getBlock() == ModBlocks.END_ASH) {
                    if (tamedDigTimer == 0 && !world.isRemote) {
                        this.setDigging(true);
                        this.setImmovable(true);
                        addEvent(() -> this.setDigging(false), 23);
                        addEvent(() -> this.setImmovable(false), 23);
                        addEvent(() -> {
                            this.dropItem(ModItems.INFUSION_CORE, 1);
                            this.playSound(SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, 1.0f, 1.0f);
                        }, 15);
                        tamedDigTimer = 3000 + (int) ModRand.getFloat(1000);

                    } else {
                        tamedDigTimer--;
                    }
                }
            }
        }

    }




    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "torso_controller", 0, this::predicateTorso));
        animationData.addAnimationController(new AnimationController(this, "anim_static_controller", 0, this::predicateIdleAnims));
    }

    private <E extends IAnimatable> PlayState predicateIdleAnims(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BITE, false));
            return PlayState.CONTINUE;
        }
        if(this.isDigging()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DIG, false));
            return PlayState.CONTINUE;
        }
        if(this.isButtTwitch()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BUTT_TWITCH, false));
            return PlayState.CONTINUE;
        }
        if(this.isHeadTwitch()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_HEAD_TWITCH, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateTorso(AnimationEvent<E> event) {
        if(!this.isDigging()) {


            event.getController().setAnimation(new AnimationBuilder().addAnimation(TORSO_MOVE, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }



    @Override
    public boolean getCanSpawnHere()
    {
        return this.world.rand.nextInt(12) == 0;
    }

    @Override
    public float getBlockPathWeight(BlockPos pos)
    {
        return 0.0F;
    }
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isDigging()) {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LEGS_MOVE, true));

            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LEGS_IDLE, true));

            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
            this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] {

        };
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.currentlyHoldingItem) {
            this.setHeadTwitch(true);
            addEvent(() -> this.setHeadTwitch(false), 20);
        }
        return ModSoundHandler.PARASITE_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.PARASITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.PARASITE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(ModSoundHandler.PARASITE_STEP, 0.4F, 1.0f + ModRand.getFloat(0.3F));
    }

    private Consumer<EntityLivingBase> prevAttack;
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
       if(!this.isFightMode()) {
           List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(bite));
           double[] weights = {
                   (distance < 3) ? 1/distance : 0 //Bite Attack
           };
           prevAttack = ModRand.choice(attacks, rand, weights).next();
           prevAttack.accept(target);
       }
        return 60;
    }

    private final Consumer<EntityLivingBase> bite = (target) -> {
        this.setFightMode(true);
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1, 0)));
            DamageSource damageSource = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = (float) (MobConfig.parasite_damage * ModConfig.biome_multiplier);
            ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, damageSource, 0.5f, 0, false);
        }, 8);
        addEvent(()-> this.setFightMode(false), 10);
    };

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "bug");
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
