package com.example.structure.entity.shadowPlayer;

import com.example.structure.config.MobConfig;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.ai.EntityAITimedKnight;
import com.example.structure.entity.ai.EntityAIUltraParasite;
import com.example.structure.entity.shadowPlayer.action.ActionArenaAOE;
import com.example.structure.entity.shadowPlayer.action.ActionForwardTeleport;
import com.example.structure.entity.shadowPlayer.action.ActionRevoltAOE;
import com.example.structure.entity.shadowPlayer.action.ActionShootRanged;
import com.example.structure.entity.trader.EntityControllerLift;
import com.example.structure.entity.util.IAttack;
import com.example.structure.init.ModItems;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EntityShadowPlayer extends EntityModBase implements IAnimatable, IAnimationTickable, IAttack {

    private Consumer<EntityLivingBase> prevAttack;

    public Vec3d chargeDir;
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.NOTCHED_6));
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FULL_BODY_USAGE = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> RING_OFF = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_DASH = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> COMBO_DASH_ALT = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> QUICK_ATTACK = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> QUICK_ATTACK_ALT = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWING_BASIC = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DASH_AWAY = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AXE_ATTACK = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> AXE_AOE = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BEGIN_BS_ATTACK = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FINISH_BS_ATTACK = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CONTINUE_BS_ATTACK = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> USE_HEALTH_POTION = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DO_RANGED_ATTACK = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);

    private static final DataParameter<Boolean> SUMMON_START = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MIDDLE_START = EntityDataManager.createKey(EntityShadowPlayer.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> ITEM_HAND = EntityDataManager.<ItemStack>createKey(EntityShadowPlayer.class, DataSerializers.ITEM_STACK);
    public void setFightModeZero(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightModeZero() {return this.dataManager.get(FIGHT_MODE);}
    public void setFullBodyUsage(boolean value) {this.dataManager.set(FULL_BODY_USAGE, Boolean.valueOf(value));}
    public boolean isFullBodyUsage() {return this.dataManager.get(FULL_BODY_USAGE);}
    public void setRingOff(boolean value) {this.dataManager.set(RING_OFF, Boolean.valueOf(value));}
    public boolean isRingOff() {return this.dataManager.get(RING_OFF);}
    public void setComboDash(boolean value) {this.dataManager.set(COMBO_DASH, Boolean.valueOf(value));}
    public void setComboDashAlt(boolean value) {this.dataManager.set(COMBO_DASH_ALT, Boolean.valueOf(value));}
    public void setQuickAttack(boolean value) {this.dataManager.set(QUICK_ATTACK, Boolean.valueOf(value));}
    public void setQuickAttackAlt(boolean value) {this.dataManager.set(QUICK_ATTACK_ALT, Boolean.valueOf(value));}
    public void setSwingBasic(boolean value) {this.dataManager.set(SWING_BASIC, Boolean.valueOf(value));}
    public void setDashAway(boolean value) {this.dataManager.set(DASH_AWAY, Boolean.valueOf(value));}
    public void setAxeAttack(boolean value) {this.dataManager.set(AXE_ATTACK, Boolean.valueOf(value));}
    public void setAxeAoe(boolean value) {this.dataManager.set(AXE_AOE, Boolean.valueOf(value));}
    public void setBeginBsAttack(boolean value) {this.dataManager.set(BEGIN_BS_ATTACK, Boolean.valueOf(value));}
    public void setFinishBsAttack(boolean value) {this.dataManager.set(FINISH_BS_ATTACK, Boolean.valueOf(value));}
    public void setContinueBsAttack(boolean value) {this.dataManager.set(CONTINUE_BS_ATTACK, Boolean.valueOf(value));}

    public void setUseHealthPotion(boolean value) {this.dataManager.set(USE_HEALTH_POTION, Boolean.valueOf(value));}
    public void setDoRangedAttack(boolean value) {this.dataManager.set(DO_RANGED_ATTACK, Boolean.valueOf(value));}
    public void setSummonStart(boolean value) {this.dataManager.set(SUMMON_START, Boolean.valueOf(value));}
    public void setMiddleStart(boolean value) {this.dataManager.set(MIDDLE_START, Boolean.valueOf(value));}
    public boolean isComboDash() {return this.dataManager.get(COMBO_DASH);}
    public boolean isComboDashAlt() {return this.dataManager.get(COMBO_DASH_ALT);}
    public boolean isQuickAttack() {return this.dataManager.get(QUICK_ATTACK);}
    public boolean isQuickAttackAlt() {return this.dataManager.get(QUICK_ATTACK_ALT);}
    public boolean isSwingBasic() {return this.dataManager.get(SWING_BASIC);}
    public boolean isDashAway() {return this.dataManager.get(DASH_AWAY);}
    public boolean isAxeAttack() {return this.dataManager.get(AXE_ATTACK);}
    public boolean isAxeAOE() {return this.dataManager.get(AXE_AOE);}
    public boolean isBeginBSAttack() {return this.dataManager.get(BEGIN_BS_ATTACK);}
    public boolean isFinishBSAttack() {return this.dataManager.get(FINISH_BS_ATTACK);}
    public boolean isContinueBSAttack() {return this.dataManager.get(CONTINUE_BS_ATTACK);}
    public boolean isUseHealthPotion() {return this.dataManager.get(USE_HEALTH_POTION);}
    public boolean isDoRangedAttack() {return this.dataManager.get(DO_RANGED_ATTACK);}
    public boolean isSummonStart() {return this.dataManager.get(SUMMON_START);}
    public boolean isMiddleStart() {return this.dataManager.get(MIDDLE_START);}

    //Idle and movement of small non-important parts
    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_SWORD_IDLE = "other_idle";
    private final String ANIM_RING_IDLE = "ring_idle";
    private final String ANIM_NO_RING = "ring_off";
    private final String ANIM_SUMMON_START = "summon";
    private final String ANIM_MIDDLE_START = "phase_transition";

    //Attack Animations
    private final String ANIM_COMBO_DASH = "combo";
    private final String ANIM_COMBO_DASH_ALT = "combo_alt";
    private final String ANIM_SWING_QUICK = "quick_swing";
    private final String ANIM_SWING_ALT = "quick_swing_base";
    private final String ANIM_SWING_BASIC = "swing_basic";
    private final String ANIM_DASH_AWAY = "dash_away";
    private final String ANIM_AXE_ATTACK = "axe_attack";
    private final String ANIM_AXE_AOE = "aoe_axe";
    private final String ANIM_BS_STRIKE = "bs_attack";
    private final String ANIM_BS_FINISH = "bs_finish";
    private final String ANIM_BS_CONTINUE = "bs_continue";
    private final String ANIM_USE_POTION = "use_potion";
    private final String ANIM_DO_RANGED_ATTACK = "ranged_attack";

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Fight_Mode", this.isFightModeZero());
        nbt.setBoolean("Full_Body", this.isFullBodyUsage());
        nbt.setBoolean("Ring_Off", this.isRingOff());
        nbt.setBoolean("Combo_Dash", this.isComboDash());
        nbt.setBoolean("Combo_Dash_Alt", this.isComboDashAlt());
        nbt.setBoolean("Quick_Attack", this.isQuickAttack());
        nbt.setBoolean("Quick_Attack_Alt", this.isQuickAttackAlt());
        nbt.setBoolean("Swing_Basic", this.isSwingBasic());
        nbt.setBoolean("Dash_Away", this.isDashAway());
        nbt.setBoolean("Axe_Attack", this.isAxeAttack());
        nbt.setBoolean("Axe_Aoe", this.isAxeAOE());
        nbt.setBoolean("Begin_BS_Attack", this.isBeginBSAttack());
        nbt.setBoolean("Finish_BS_Attack", this.isFinishBSAttack());
        nbt.setBoolean("Continue_BS_Attack", this.isContinueBSAttack());
        nbt.setBoolean("Use_Potion", this.isUseHealthPotion());
        nbt.setBoolean("Do_Ranged", this.isDoRangedAttack());
        nbt.setBoolean("Summon_Start", this.isSummonStart());
        nbt.setBoolean("Phase_Transition", this.isMiddleStart());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setFightModeZero(nbt.getBoolean("Fight_Mode"));
        this.setFullBodyUsage(nbt.getBoolean("Full_Body"));
        this.setRingOff(nbt.getBoolean("Ring_Off"));
        this.setComboDash(nbt.getBoolean("Combo_Dash"));
        this.setComboDashAlt(nbt.getBoolean("Combo_Dash_Alt"));
        this.setQuickAttack(nbt.getBoolean("Quick_Attack"));
        this.setQuickAttackAlt(nbt.getBoolean("Quick_Attack_Alt"));
        this.setSwingBasic(nbt.getBoolean("Swing_Basic"));
        this.setDashAway(nbt.getBoolean("Dash_Away"));
        this.setAxeAttack(nbt.getBoolean("Axe_Attack"));
        this.setAxeAoe(nbt.getBoolean("Axe_Aoe"));
        this.setBeginBsAttack(nbt.getBoolean("Begin_BS_Attack"));
        this.setFinishBsAttack(nbt.getBoolean("Finish_BS_Attack"));
        this.setContinueBsAttack(nbt.getBoolean("Continue_BS_Attack"));
        this.setUseHealthPotion(nbt.getBoolean("Use_Potion"));
        this.setDoRangedAttack(nbt.getBoolean("Do_Ranged"));
        this.setSummonStart(nbt.getBoolean("Summon_Start"));
        this.setMiddleStart(nbt.getBoolean("Phase_Transition"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityShadowPlayer(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(0.6F, 1.95F);
    }

    private int potionAmount = MobConfig.shadow_potion_amount;

    private double heal_amount_potion = MobConfig.shadow_heal_amount * this.getMaxHealth();

    private int heal_shadow_cooldown = MobConfig.shadow_heal_cooldown * 20;
    public EntityPlayer owner;
    private double setShadowHealth;
    private double setShadowAttackDamage;

    /**
     * Used for summoning the Shadow under the Player's Aid
     * @param worldIn
     * @param owner
     * @param potionAmount
     * @param health
     * @param attackDamage
     */
    public EntityShadowPlayer(World worldIn, EntityPlayer owner, int potionAmount, double health, double attackDamage) {
        super(worldIn);
        this.potionAmount = potionAmount;
        this.owner = owner;
        this.setShadowAttackDamage = attackDamage;
        this.setShadowHealth = health;

    }

    public EntityShadowPlayer(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(ITEM_HAND, ItemStack.EMPTY);
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(FULL_BODY_USAGE, Boolean.valueOf(false));
        this.dataManager.register(RING_OFF, Boolean.valueOf(true));
        this.dataManager.register(COMBO_DASH, Boolean.valueOf(false));
        this.dataManager.register(COMBO_DASH_ALT, Boolean.valueOf(false));
        this.dataManager.register(QUICK_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(QUICK_ATTACK_ALT, Boolean.valueOf(false));
        this.dataManager.register(SWING_BASIC, Boolean.valueOf(false));
        this.dataManager.register(DASH_AWAY, Boolean.valueOf(false));
        this.dataManager.register(AXE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(AXE_AOE, Boolean.valueOf(false));
        this.dataManager.register(BEGIN_BS_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(FINISH_BS_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(CONTINUE_BS_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(USE_HEALTH_POTION, Boolean.valueOf(false));
        this.dataManager.register(DO_RANGED_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_START, Boolean.valueOf(false));
        this.dataManager.register(MIDDLE_START, Boolean.valueOf(false));
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAIAttackShadow<>(this, 1.2, 10, 12, 0.35F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        if(owner == null) {
            this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((setShadowHealth != 0) ? setShadowHealth : (double) MobConfig.shadow_player_health * getHealthModifierBarrend());
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue((setShadowAttackDamage != 0) ? setShadowAttackDamage : MobConfig.shadow_player_damage * getAttackModifiersBarrend());
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(MobConfig.shadow_armor * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(MobConfig.shadow_armor_toughness * ModConfig.lamented_multiplier);
    }

    private int stopSpammingAttacks = 0;
    private int reCheckDelay = 110;
    private boolean setSpamCheck = false;

    private boolean searchingForJab = false;

    public boolean isBackOff = false;
    private int usedPotions = 0;
    private EntityLivingBase grabbedEntity;

    private int needsHeavyAttack = 0;
    private int needsLightAttack = 0;
    private int needsRangedAttack = 0;

    private int useMilkBuckettimer = 400;
    public boolean setStrafing = false;
    private int resetTimer = 60;

    private int rangedAttackCooldown = 200;

    private boolean hasDonePhase = false;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(owner == null) {
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }

        EntityLivingBase target = this.getAttackTarget();

        if(target != null && !world.isRemote) {
            //this is to keep the boss from taking too much damage, it will attempt to back off
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            //Special stuff related to player combat, this will help the shadow choose more preferrible attacks
            //to best open there weakness
            if(target instanceof EntityPlayer) {
                if(resetTimer > 0) {
                    EntityPlayer player = ((EntityPlayer) target);
                    ItemStack stack = player.getActiveItemStack();
                    //maybe we'll do something with this later
                    double playerH = player.getHealth() / player.getMaxHealth();

                    if (distSq <= 6) {
                        if (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemPotion) {
                            needsLightAttack++;
                        }

                        if (stack.getItem() instanceof ItemShield) {
                            needsHeavyAttack++;
                        }
                    } else {
                        if (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemPotion) {
                            needsRangedAttack++;
                        }

                        if(stack.getItem() instanceof ItemBow) {
                            needsHeavyAttack++;
                        }
                    }

                    resetTimer--;
                }
                if (resetTimer < 1){
                    needsRangedAttack = 0;
                    needsHeavyAttack = 0;
                    needsLightAttack = 0;
                    resetTimer = 60;
                }
            }
            //A check to allow the shadow to throw ranged attacks if not in range to do anything else
            if(distSq >= 13) {
                if(rangedAttackCooldown > 0 && !this.isDoRangedAttack() && !this.isFightModeZero()) {
                    rangedAttackCooldown--;
                }

                if (rangedAttackCooldown < 1) {
                    do_ranged_attack.accept(target);
                    rangedAttackCooldown = 200;
                }
            }

            double healthCurr = this.getHealth() /this.getMaxHealth();

            if(healthCurr <= 0.5 && !this.hasDonePhase && !this.isFightModeZero()) {
                this.beginPhaseDialog(target);
            }

            //checking spam attacks
            if(reCheckDelay > 0) {
                if(this.hurtTime > 0) {
                    stopSpammingAttacks++;
                }
                if(stopSpammingAttacks > 10) {
                    //do a quick dash backwards if true
                    this.setSpamCheck = true;

                }
                reCheckDelay--;
            }
            if(reCheckDelay < 1) {
                stopSpammingAttacks = 0;
                reCheckDelay = 110;
                this.setSpamCheck = false;
            }
            // This is for doing the grab attack of the boss
            if(this.searchingForJab && grabbedEntity != null) {
                if(grabbedEntity == target) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 0.2, 0)));
                    grabbedEntity.setPosition(offset.x, offset.y, offset.z);
                    grabbedEntity.setPositionAndUpdate(offset.x, offset.y, offset.z);
                    this.faceEntity(grabbedEntity, 30.0F, 30.0F);

                }
            }
            //Used For Healing the Boss
            if(healthCurr < (this.getMaxHealth() - this.heal_amount_potion - 0.05)) {
                if(usedPotions < potionAmount) {
                    if (heal_shadow_cooldown > 0) {
                        heal_shadow_cooldown--;
                    }
                    //when the cooldown is met, it may use a potion inbetween attacks
                    if (heal_shadow_cooldown < 1 && !this.isFightModeZero()) {
                            this.useHealthPotion();
                    }
                }

                //checks current potion effects and if it can clear them
                if(useMilkBuckettimer > 0 && !world.isRemote) {
                    useMilkBuckettimer--;
                } else if (useMilkBuckettimer < 1 && !this.isFightModeZero() && !world.isRemote) {
                    if(this.isPotionActive(MobEffects.POISON) || this.isPotionActive(MobEffects.SLOWNESS) || this.isPotionActive(MobEffects.WEAKNESS) || this.isPotionActive(MobEffects.LEVITATION) ||
                    this.isPotionActive(MobEffects.WITHER)) {
                        this.useMilkBucket();
                    }
                }
            }
        }

        if(this.lockLook) {
            this.rotationPitch = this.prevRotationPitch;
            this.rotationYaw = this.prevRotationYaw;
            this.rotationYawHead = this.prevRotationYawHead;
            this.renderYawOffset = this.rotationYaw;
        }

        if(this.isBackOff && target != null && !world.isRemote) {
            double d0 = (this.posX - target.posX) * 0.01;
            double d1 = (this.posY - target.posY) * 0.005;
            double d2 = (this.posZ - target.posZ) * 0.01;
            this.addVelocity(d0, d1, d2);
        }
    }

    private void useMilkBucket() {
        this.setFightModeZero(true);
        this.setUseHealthPotion(true);
        this.isBackOff = true;

        addEvent(()-> this.equipBlock(SHADOW_HAND.HAND, new ItemStack(Items.MILK_BUCKET)), 10);

        addEvent(()-> {
            for(int i = 0; i <= 38; i += 2) {
                addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.25f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f)), i);
            }
        }, 20);

        addEvent(()-> {
            this.clearActivePotions();
            this.equipBlock(SHADOW_HAND.HAND, ItemStack.EMPTY);
        }, 60);

        addEvent(()-> {
            this.isBackOff = false;
            this.heal_shadow_cooldown = (MobConfig.shadow_heal_cooldown * 20 )/ 2;
            this.useMilkBuckettimer = 400;
            this.setFightModeZero(false);
            this.setUseHealthPotion(false);
        }, 65);
    }
    private void useHealthPotion() {
        this.setFightModeZero(true);
        this.setUseHealthPotion(true);
        this.isBackOff = true;
        this.usedPotions++;

        addEvent(()-> this.equipBlock(SHADOW_HAND.HAND, new ItemStack(ModItems.FAKE_HEALING_POTION)), 10);

        addEvent(()-> {
        for(int i = 0; i <= 38; i += 2) {
            addEvent(()-> this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 1.25f, 1.0f / (rand.nextFloat() * 0.4F + 0.7f)), i);
        }
        }, 20);


        addEvent(()-> {
            this.heal((float) heal_amount_potion);
            this.equipBlock(SHADOW_HAND.HAND, ItemStack.EMPTY);
            }, 60);

        addEvent(()-> {
            this.isBackOff = false;
            this.heal_shadow_cooldown = MobConfig.shadow_heal_cooldown * 20;
            this.useMilkBuckettimer = 200;
            this.setFightModeZero(false);
            this.setUseHealthPotion(false);
        }, 65);
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightModeZero() && !this.isUseHealthPotion()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(combo_dash, quick_swing, swing_basic, dash_away, axe_attack, axe_aoe_attack, bs_attack, do_ranged_attack));
            double[] weights = {
                    (distance <= 12 && distance >= 5 && prevAttack != combo_dash) ? 1/distance + needsHeavyAttack : 0,
                    (distance <= 6 && prevAttack != quick_swing) ? 1/distance + needsLightAttack : 0,
                    (distance <= 6 && prevAttack != swing_basic) ? 1/distance + needsLightAttack: 0,
                    (distance <= 6 && prevAttack != dash_away && setSpamCheck) ? 2/distance : 0,
                    (distance <= 9 && prevAttack != axe_attack && distance >= 2) ? 1/distance + needsHeavyAttack : 0,
                    (distance <= 9 && prevAttack != axe_aoe_attack && distance >= 2) ? 1/distance + needsHeavyAttack : 0,
                    (distance <= 12 && prevAttack != bs_attack && distance >= 7) ? 1/distance + needsHeavyAttack : 0,
                    (distance <= 13 && distance >= 9 && prevAttack != do_ranged_attack) ? 1/distance + needsRangedAttack : 0
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 12 + ModRand.range(1, 10);
    }

    private final Consumer<EntityLivingBase> do_ranged_attack = (target) -> {
      this.setFightModeZero(true);
      this.setDoRangedAttack(true);
      this.setStrafing = true;

      addEvent(()-> {
        new ActionShootRanged().performAction(this, target);
      }, 15);

      addEvent(()-> {
        this.setDoRangedAttack(false);
        addEvent(()-> {
            this.setFightModeZero(false);
            this.setStrafing = false;
        }, 30);
      }, 50);

    };

    private final Consumer<EntityLivingBase> bs_attack = (target) -> {
        this.setBeginBsAttack(true);
        this.setFightModeZero(true);
        this.setFullBodyUsage(true);
        this.setImmovable(true);

        addEvent(()-> {
            Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
            Vec3d adjusted = target.getPositionVector().add(posSet.scale(-2));
            Vec3d targetedPos = adjusted.add(ModUtils.yVec(1.0D));
            this.lockLook = true;
            addEvent(()-> {
                this.setImmovable(false);
                new ActionForwardTeleport(targetedPos).performAction(this, target);
                for(int i = 0; i <= 8; i += 2) {
                    addEvent(()-> {
                        List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                                this.getEntityBoundingBox().offset(ModUtils.getRelativeOffset(this, new Vec3d(0.8, -0.3, 0))).grow(1D, 3.0D, 1D),
                                e -> !e.getIsInvulnerable());

                        if(!nearbyEntities.isEmpty()) {
                            for(EntityLivingBase base : nearbyEntities) {
                                if(!this.searchingForJab) {
                                    if (base == target) {
                                        grabbedEntity = base;
                                        this.searchingForJab = true;
                                    }
                                }
                            }
                        }
                    }, i);
                }
            }, 7);
        }, 13);

        addEvent(()-> {
            this.setImmovable(true);
            if(this.searchingForJab && grabbedEntity != null) {

                //continues the attack successfully
                this.lockLook = false;
                this.setBeginBsAttack(false);
                this.setContinueBsAttack(true);
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.3,1.1,0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                float damage = this.getAttack();
                ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.1f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.9f));
                //prepare axe and do a lot of damage
                addEvent(()-> {
                    Vec3d offset2 = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2,1.1,0)));
                    DamageSource source2 = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage2 = this.getAttack() * 2 + 4;
                    this.searchingForJab = false;
                    this.grabbedEntity = null;
                    ModUtils.handleAreaImpact(1.5f, (e)-> damage2, this, offset2, source2, 1.4f, 0, false);
                    this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.1f));
                }, 30);

                addEvent(()-> {
                this.setImmovable(false);
                this.setContinueBsAttack(false);
                this.setFullBodyUsage(false);
                this.setFightModeZero(false);
                }, 60);

            } else {
                //finishs the attack as a failure
                this.setBeginBsAttack(false);
                this.setFinishBsAttack(true);

                addEvent(()-> {
                    this.lockLook =false;
                    this.setFinishBsAttack(false);
                    this.setImmovable(false);
                    this.setFullBodyUsage(false);
                    this.setFightModeZero(false);
                }, 15);
            }
        }, 30);
    };

    private final Consumer<EntityLivingBase> axe_aoe_attack = (target) -> {
      this.setAxeAoe(true);
      this.setFightModeZero(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          this.lockLook = true;
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3),0.3F);
          }, 5);
      }, 15);

      addEvent(()-> {
        this.setImmovable(true);
        //Do AOE Action
          float distance = this.getDistance(target);
          new ActionRevoltAOE((int) distance + 3).performAction(this, target);
          this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 2.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 30);


      addEvent(()-> {
          this.lockLook = false;
        this.setImmovable(false);
        this.setFullBodyUsage(false);
        this.setAxeAoe(false);
        this.setFightModeZero(false);
      }, 50);
    };

    private final Consumer<EntityLivingBase> axe_attack = (target) -> {
        this.setFightModeZero(true);
        this.setAxeAttack(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        addEvent(()-> {
            this.lockLook = true;
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.37),0.1F);
            }, 8);
        }, 22);

        addEvent(()-> {
            for(int i = 0; i <= 8; i += 2) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,1,0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = this.getAttack() * 2;
                    ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.4f, 0, false);
                }, i);
            }
            addEvent(()-> {
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.1f));
            }, 5);
        }, 30);

        addEvent(()-> {
            this.setImmovable(true);
            this.lockLook = false;
        }, 40);

        addEvent(()-> {
            Vec3d targetedPos = target.getPositionVector();
            addEvent(()-> {
                this.setImmovable(false);
                double distance = this.getPositionVector().distanceTo(targetedPos);
                ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.37),0.1F);
                this.lockLook = true;
            }, 8);
        }, 42);

        addEvent(()-> {
            for(int i = 0; i <= 8; i += 2) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0,1,0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = this.getAttack() * 2;
                    ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.4f, 0, false);
                }, i);
                addEvent(()-> {
                    this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.1f));
                }, 5);
            }
        }, 50);

        addEvent(()-> this.setImmovable(true), 65);

        addEvent(()-> this.lockLook = false, 70);

        addEvent(()-> {
            this.setFullBodyUsage(false);
            this.setAxeAttack(false);
            this.setImmovable(false);
            this.setFightModeZero(false);
        }, 82);
    };
    private final Consumer<EntityLivingBase> dash_away = (target) -> {
      //We only really want this one to be activated if the target has the boss a lot realistically
      this.setFightModeZero(true);
      this.setImmovable(true);
      this.setFullBodyUsage(true);
      this.setDashAway(true);
      addEvent(()-> {
          this.lockLook =true;
          Vec3d dirToo = this.getPositionVector().subtract(target.getPositionVector()).normalize();
          Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(20));
          this.setImmovable(false);
          ModUtils.leapTowards(this, jumpTooPos, 1.4F, 0.25F);
          this.playSound(ModSoundHandler.LORD_KNIGHT_FLY, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.8f));
      }, 10);

      addEvent(()-> {
          this.lockLook = false;
        this.setImmovable(true);
      }, 25);

      addEvent(()-> {
            this.setImmovable(false);
            this.setDashAway(false);
            this.setFullBodyUsage(false);
            this.setFightModeZero(false);
        }, 30);
    };
    private final Consumer<EntityLivingBase> swing_basic = (target) -> {
      this.setFightModeZero(true);
      this.setSwingBasic(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);

      addEvent(()-> {
        Vec3d targetedPos = target.getPositionVector();
        this.lockLook = true;
        addEvent(()-> {
            this.setImmovable(false);
            double distance = this.getPositionVector().distanceTo(targetedPos);
            ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.3),0.1F);
        }, 5);
      }, 5);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75,1.1,0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.1f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 15);

      addEvent(()-> {
        this.lockLook = false;
        this.setImmovable(true);
      }, 25);

      addEvent(()-> {
        this.setImmovable(false);
        this.setSwingBasic(false);
        this.setFullBodyUsage(false);
        this.setFightModeZero(false);
      }, 35);
    };

    private final Consumer<EntityLivingBase> quick_swing = (target) -> {
      this.setFightModeZero(true);
      if(world.rand.nextInt(2) == 0) {
          this.setQuickAttack(true);
      } else {
          this.setQuickAttackAlt(true);
      }

      addEvent(()-> {
          Vec3d targetedPos = target.getPositionVector();
          this.lockLook = true;
          addEvent(()-> {
              double distance = this.getPositionVector().distanceTo(targetedPos);
              ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.25),0.1F);
          }, 5);
      }, 5);

      addEvent(()-> {
          Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75,1.1,0)));
          DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
          float damage = this.getAttack();
          ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.1f, 0, false);
          this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
      }, 16);

      addEvent(()-> this.lockLook =false, 20);

      addEvent(()-> {
        this.setFullBodyUsage(true);
        if(this.isQuickAttackAlt()) {
            this.setImmovable(true);
        }
      }, 25);

      if(this.isQuickAttack()) {
          addEvent(()-> {
              Vec3d targetedPos = target.getPositionVector();
              addEvent(()-> {
                  double distance = this.getPositionVector().distanceTo(targetedPos);
                  ModUtils.leapTowards(this, targetedPos, (float) (distance * 0.2),0.1F);
              }, 5);
          }, 20);

          addEvent(()-> {
              Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75,1.1,0)));
              DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
              float damage = this.getAttack();
              ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.1f, 0, false);
              this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.8f));
          }, 30);

          addEvent(()-> this.setImmovable(true), 35);

          addEvent(()-> {
              this.lockLook = true;
              Vec3d dirToo = this.getPositionVector().subtract(target.getPositionVector()).normalize();
              Vec3d jumpTooPos = this.getPositionVector().add(dirToo.scale(20));
              this.setImmovable(false);
              ModUtils.leapTowards(this, jumpTooPos, 1.4F, 0.25F);
              this.playSound(ModSoundHandler.LORD_KNIGHT_FLY, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.8f));
          }, 46);

          addEvent(()-> {
              this.lockLook = false;
              this.setImmovable(true);
          }, 60);

          addEvent(()-> {
            this.setImmovable(false);
            this.setFullBodyUsage(false);
            this.setQuickAttack(false);
            this.setFightModeZero(false);
          }, 65);
      } else {
          addEvent(()-> {
              this.setImmovable(false);
              this.setFullBodyUsage(false);
              this.setQuickAttackAlt(false);
              this.setFightModeZero(false);
          }, 30);
      }

    };
    private final Consumer<EntityLivingBase> combo_dash = (target) -> {
      this.setFightModeZero(true);
      if(world.rand.nextInt(2) == 0) {
          this.setComboDashAlt(true);
      } else {
          this.setComboDash(true);
      }
      this.setImmovable(true);
      this.setFullBodyUsage(true);

      addEvent(()-> {
        this.lockLook = true;
          Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
          Vec3d adjusted = target.getPositionVector().add(posSet.scale(-2));
          Vec3d targetedPos = adjusted.add(ModUtils.yVec(1.0D));
        addEvent(()-> {
            this.setImmovable(false);
            new ActionForwardTeleport(targetedPos).performAction(this, target);
        }, 8);

      }, 40);

        //first Dash Damage
      addEvent(()-> {
          for(int i = 0; i <= 8; i += 2) {
              addEvent(()-> {
                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75,1.1,0)));
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                  float damage = this.getAttack();
                  ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.3f, 0, false);
              }, i);
          }
      }, 48);
      addEvent(()-> this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 53);

      addEvent(()-> {
        this.lockLook = false;
        this.setImmovable(true);
      }, 58);

      addEvent(()-> {
          Vec3d targetedPosTwo = target.getPositionVector();
          addEvent(()-> {
              this.setImmovable(false);
              double distance = this.getPositionVector().distanceTo(targetedPosTwo);
              ModUtils.leapTowards(this, targetedPosTwo, (float) (distance * 0.33),0.1F);
              addEvent(()-> {
                  Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75,1.1,0)));
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                  float damage = this.getAttack();
                  ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.1f, 0, false);
                  this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
              }, 5);
          }, 5);
      }, 60);

        addEvent(()-> this.setImmovable(true), 75);

      if(this.isComboDashAlt()) {
        //Follow Up with a second one
          addEvent(()-> {
              this.lockLook = true;
              Vec3d posSet = target.getPositionVector().subtract(this.getPositionVector()).normalize();
              Vec3d adjusted = target.getPositionVector().add(posSet.scale(-2));
              Vec3d targetedPos = adjusted.add(ModUtils.yVec(1.0D));
              addEvent(()-> {
                  this.setImmovable(false);
                  new ActionForwardTeleport(targetedPos).performAction(this, target);
              }, 7);
          }, 75);

          addEvent(()-> {
              for(int i = 0; i <= 8; i += 2) {
                  addEvent(()-> {
                      Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.75,1.1,0)));
                      DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                      float damage = this.getAttack();
                      ModUtils.handleAreaImpact(1.5f, (e)-> damage, this, offset, source, 0.3f, 0, false);
                  }, i);
              }
          }, 83);

          addEvent(()-> this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f)), 87);
          addEvent(()-> {
              this.lockLook = false;
              this.setImmovable(true);
              }, 95);

          addEvent(()-> {
            this.setImmovable(false);
            this.setComboDashAlt(false);
            this.setFullBodyUsage(false);
            this.setFightModeZero(false);
          }, 109);
      } else {
          //Basic Dash

          addEvent(()-> {
            this.setImmovable(false);
            this.setComboDash(false);
            this.setFullBodyUsage(false);
            this.setFightModeZero(false);
          }, 90);
      }

    };

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        animationData.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttack));
        animationData.addAnimationController(new AnimationController(this, "ring_idle", 0, this::predicateRing));
        animationData.addAnimationController(new AnimationController(this, "other_idles", 0, this::predicateOtherIdles));
    }

    private <E extends IAnimatable> PlayState predicateOtherIdles(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWORD_IDLE, true));
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState predicateRing(AnimationEvent<E> event) {
        if(this.isRingOff()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_NO_RING, true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RING_IDLE, true));
        }
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isFightModeZero()) {
            if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
                return PlayState.CONTINUE;
            }  else if (event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!this.isFullBodyUsage()) {
            if (!(event.getLimbSwingAmount() >= -0.10F && event.getLimbSwingAmount() <= 0.10F)) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
                return PlayState.CONTINUE;
            } else if (event.getLimbSwingAmount() >= -0.09F && event.getLimbSwingAmount() <= 0.09F) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isFightModeZero()) {
            if(this.isComboDash()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_DASH, false));
                return PlayState.CONTINUE;
            }
            if(this.isComboDashAlt()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_COMBO_DASH_ALT, false));
                return PlayState.CONTINUE;
            }
            if(this.isQuickAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_QUICK, false));
                return PlayState.CONTINUE;
            }
            if(this.isQuickAttackAlt()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_ALT, false));
                return PlayState.CONTINUE;
            }
            if(this.isSwingBasic()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWING_BASIC, false));
                return PlayState.CONTINUE;
            }
            if(this.isDashAway()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH_AWAY, false));
                return PlayState.CONTINUE;
            }
            if(this.isAxeAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_AXE_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isAxeAOE()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_AXE_AOE, false));
                return PlayState.CONTINUE;
            }
            if(this.isBeginBSAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BS_STRIKE, false));
                return PlayState.CONTINUE;
            }
            if(this.isFinishBSAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BS_FINISH, false));
                return PlayState.CONTINUE;
            }
            if(this.isContinueBSAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BS_CONTINUE, false));
                return PlayState.CONTINUE;
            }
            if(this.isUseHealthPotion()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_USE_POTION, false));
                return PlayState.CONTINUE;
            }
            if(this.isDoRangedAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DO_RANGED_ATTACK, false));
                return PlayState.CONTINUE;
            }
            if(this.isSummonStart()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON_START, false));
                return PlayState.CONTINUE;
            }
            if(this.isMiddleStart()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_MIDDLE_START, false));
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
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isContinueBSAttack() || source.getTrueSource() instanceof EntityControllerLift || this.isSummonStart() || this.isMiddleStart()) {
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public int tickTimer() {
        return this.ticksExisted;
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        if(owner == null) {
            this.bossInfo.addPlayer(player);
        }
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        if(owner == null) {
            this.bossInfo.removePlayer(player);
        }
    }


    @Override
    public boolean isNonBoss()
    {
        return false;
    }

    public enum SHADOW_HAND {
        HAND("HolfJL");

        public String getBoneName() {
            return this.boneName;
        }

        private String boneName;

        SHADOW_HAND(String bone) {
            this.boneName = bone;
        }

        @Nullable
        public static SHADOW_HAND getFromBoneName(String boneName) {
            if ("HolfJL".equals(boneName)) {
                return HAND;
            }
            return null;
        }

        public int getIndex() {
            if (this == SHADOW_HAND.HAND) {
                return 1;
            }
            return 0;
        }
    }


    public void equipBlock(SHADOW_HAND head, ItemStack state) {
        if(world.isRemote) {
            return;
        }
        if (head == SHADOW_HAND.HAND) {
            this.dataManager.set(ITEM_HAND, state);
        }
    }


    public ItemStack getItemFromKnightHand(SHADOW_HAND head) {
        if (head == SHADOW_HAND.HAND) {
            return this.dataManager.get(ITEM_HAND);
        }
        return null;
    }

    public void onSummon(BlockPos Pos, EntityPlayer player) {
        BlockPos offset = Pos.add(new BlockPos(0,0,0));
        this.setPosition(offset.getX(), offset.getY(), offset.getZ());
       // this.setSpawnLocation(offset);
      //  this.setSetSpawnLoc(true);
        this.setCustomNameTag(player.getName() + " Shadow");
        world.spawnEntity(this);
        double healthChange = this.getHealth() / this.getMaxHealth();
        if(healthChange == 1) {
            this.setFightModeZero(true);
            this.setFullBodyUsage(true);
            this.setSummonStart(true);
            this.setImmovable(true);
            this.beginFirstDialog();
            addEvent(()-> {
                this.setFightModeZero(false);
                this.setFullBodyUsage(false);
                this.setImmovable(false);
                this.setSummonStart(false);
            }, 180);
        }
    }



    /**
     * Dialougue Begin
     */

    public void beginFirstDialog() {
        this.lockLook = true;

        addEvent(()-> this.lockLook = false, 100);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_intro_0")));
            }
            //Dialog 1
        }, 50);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_intro_1")));
            }
            //Dialog 2
        }, 80);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_intro_2")));
            }
            //Dialog 3
        }, 140);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_intro_3")));
            }
            //Dialog 4
        }, 170);
    }

    public void beginPhaseDialog(EntityLivingBase target) {
        this.setFightModeZero(true);
        this.setImmovable(true);
        this.setFullBodyUsage(true);
        this.setMiddleStart(true);
        this.hasDonePhase = true;
        this.setRingOff(false);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_middle_0")));
            }
            //Dialog 1
        }, 10);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_middle_1")));
            }
            //Dialog 2
        }, 30);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_middle_2")));
            }
            //Dialog 3
        }, 60);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_middle_3")));
            }
            //Dialog 4
        }, 100);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_middle_4")));
            }
            //Dialog 5
        }, 120);

        addEvent(()-> {
            for (EntityPlayer player : this.bossInfo.getPlayers()) {
                player.sendMessage(new TextComponentString(TextFormatting.RED + this.getName() + ":" + TextFormatting.WHITE)
                        .appendSibling(new TextComponentTranslation(ModUtils.LANG_CHAT + "shadow_middle_5")));
            }
            //Dialog 6
        }, 140);

        addEvent(()-> new ActionArenaAOE().performAction(this, target), 133);

        addEvent(()-> {
            this.setMiddleStart(false);
            this.setImmovable(false);
            this.setFightModeZero(false);
            this.setFullBodyUsage(false);
        }, 180);
    }
}
