package com.example.structure.entity.endking;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
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

public class EntityGroundSword extends EntityModBase implements IAnimatable, IAnimationTickable {
    /**
     * A Entity Class used for the Swords that hit the ground from the End King
     */

    private final String ANIM_SWORD_BASE = "attack_sword";
    private final String ANIM_SWORD_ROT_1 = "attackrot1";
    private final String ANIM_SWORD_ROT_2 = "attackrot2";

    private final String ANIM_SWORD_FAST_BASE = "attackfast";
    private final String ANIM_SWORD_FAST_ROT_1 = "attackfastrot1";
    private final String ANIM_SWORD_FAST_ROT_2 = "attackfastrot2";


    private String ANIM_SELECTION_STRING;

    protected int selection = ModRand.range(1, 4);
    protected boolean hasSelected;

    protected static final DataParameter<Boolean> FAST = EntityDataManager.createKey(EntityGroundSword.class, DataSerializers.BOOLEAN);


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Fast", this.dataManager.get(FAST));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        this.dataManager.set(FAST, nbt.getBoolean("Fast"));
    }
    public void setFast(boolean value) {this.dataManager.set(FAST, Boolean.valueOf(value));}
    public boolean isFast() {return this.dataManager.get(FAST);}
    protected boolean Fast = false;
    public EntityGroundSword(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FAST, Boolean.valueOf(false));
    }

    public EntityGroundSword(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1.1f, 7.0f);
        if(!this.isFast()) {
            selectAnimationtoPlay();
        }
        this.setNoGravity(true);
        this.noClip = true;
    }

    public EntityGroundSword(World worldIn, boolean isFast) {
        super(worldIn);

        this.Fast = isFast;
        if(this.Fast) {
            //Set the DataManager to true
            this.setFast(true);
            selectAnimationtoPlayFast();
        } else {
            selectAnimationtoPlay();
        }
        this.setImmovable(true);
        this.setNoAI(true);
        this.setSize(1.1f, 2.0f);
        this.setNoGravity(true);
        this.noClip = true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();


        if(this.ticksExisted == 3) {
            this.playSound(ModSoundHandler.TARGET_SUMMON, 0.8F, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
        }
        this.motionX = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;
        List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox(), e -> !e.getIsInvulnerable() && (!(e instanceof EntityEndKing || e instanceof EntityRedCrystal || e instanceof EntityKnightBase || e instanceof EntityGroundSword)));
        //There is two different modes for the Sword, the regular and the faster one. Used for Phase 3
        if(this.Fast) {
            if(ticksExisted == 26) {
                this.playSound(ModSoundHandler.TARGET_IMPACT, 1.0F, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
            }
            if(ticksExisted > 25 && ticksExisted < 30) {
                if(!targets.isEmpty()) {
                    Vec3d pos = this.getPositionVector().add(ModUtils.yVec(0.5));
                    DamageSource source = ModDamageSource.builder()
                            .type(ModDamageSource.MOB)
                            .directEntity(this)
                            .build();
                    float damage = this.getAttack();
                    ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, pos, source, 0.7F, 0, false );
                }
            }

            if(this.ticksExisted == 65) {
                this.setDead();
            }
        } else {

            if(ticksExisted == 40) {
                this.playSound(ModSoundHandler.TARGET_IMPACT, 1.0F, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
            }
            if(ticksExisted > 40 && ticksExisted < 45) {
                if(!targets.isEmpty()) {
                    Vec3d pos = this.getPositionVector().add(ModUtils.yVec(0.5));
                    DamageSource source = ModDamageSource.builder()
                            .type(ModDamageSource.MOB)
                            .directEntity(this)
                            .build();
                    float damage = this.getAttack();
                    ModUtils.handleAreaImpact(0.5f, (e) -> damage, this, pos, source, 0.7F, 0, false );
                }
            }

            if(this.ticksExisted == 80) {
                this.setDead();
            }
        }
    }


    public void selectAnimationtoPlay() {
        if(selection == 1) {
            ANIM_SELECTION_STRING = ANIM_SWORD_BASE;
        } else if(selection == 2) {
            ANIM_SELECTION_STRING = ANIM_SWORD_ROT_1;
        } else if(selection == 3) {
            ANIM_SELECTION_STRING = ANIM_SWORD_ROT_2;
        }
        hasSelected = true;
    }

    public void selectAnimationtoPlayFast() {
        if(selection == 1) {
            ANIM_SELECTION_STRING = ANIM_SWORD_FAST_BASE;
        } else if(selection == 2) {
            ANIM_SELECTION_STRING = ANIM_SWORD_FAST_ROT_1;
        } else if(selection == 3) {
            ANIM_SELECTION_STRING = ANIM_SWORD_FAST_ROT_2;
        }
        hasSelected = true;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.ground_sword_damage * ModConfig.biome_multiplier);
    }


    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void initEntityAI() {

        ModUtils.removeTaskOfType(this.tasks, EntityAILookIdle.class);
        ModUtils.removeTaskOfType(this.tasks, EntityAISwimming.class);
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller_sword_attack", 0, this::predicateAttack));
    }


    private<E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {

        if(this.isFast()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SELECTION_STRING, false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SELECTION_STRING, false));
        }

        return PlayState.CONTINUE;
    }

    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    private AnimationFactory factory = new AnimationFactory(this);

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
