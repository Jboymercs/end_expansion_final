package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.util.IPitch;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * The New Base Mob of the End, can be found rarely in the wild and as well in the dungeons
 */

public abstract class EntityAbstractBuffker extends EntityModBase implements IEntityMultiPart, IPitch {


    protected static final DataParameter<Boolean> FIGHT_MODES = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> BLINK_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SHOOT_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SHOCKWAVE_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.FLOAT);

    private final MultiPartEntityPart[] hitboxParts;
    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0f, 0f);
    private final MultiPartEntityPart head = new MultiPartEntityPart(this, "head", 1.0f, 1.0f);

    private final MultiPartEntityPart torso = new MultiPartEntityPart(this, "torso", 1.3f, 1.2f);



    public EntityAbstractBuffker(World worldIn) {
        super(worldIn);

        this.hitboxParts = new MultiPartEntityPart[]{model, head, torso};
        this.setSize(2.0f, 2.8f);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }


    @Override
    public void entityInit() {
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(FIGHT_MODES, Boolean.valueOf(false));
        this.dataManager.register(BLINK_MODE, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHOCKWAVE_ATTACK, Boolean.valueOf(false));
        super.entityInit();


    }

    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }

        this.setHitBoxPos(torso, new Vec3d(0, 0.1, 0));
        this.setHitBoxPos(head, new Vec3d(0, 1.3, 0 ));

        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }


    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODES);}
    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODES, Boolean.valueOf(value));}

    public void setBlinkMode(boolean value) {this.dataManager.set(BLINK_MODE, Boolean.valueOf(value));}
    public boolean isBlinkMode() {return this.dataManager.get(BLINK_MODE);}
    public void setShootAttack(boolean value) {this.dataManager.set(SHOOT_ATTACK, Boolean.valueOf(value));}
    public boolean isShootAttack() {return this.dataManager.get(SHOOT_ATTACK);}
    public void setShockwaveAttack(boolean value) {this.dataManager.set(SHOCKWAVE_ATTACK, Boolean.valueOf(value));}
    public boolean isShockWaveAttack() {return this.dataManager.get(SHOCKWAVE_ATTACK);}




    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.constructor_health * ModConfig.lamented_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityEnderKnight>(this, EntityEnderKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityEnderMage>(this, EntityEnderMage.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EntityEnderShield>(this, EntityEnderShield.class, 1, true, false, null));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, false));
    }
    public static int constructor_cooldown_one = ModConfig.constructor_speed_one * 20;
    public static int constructor_cooldown_two = ModConfig.constructor_speed_two * 20;




    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "constructor");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }




    @Override
    public World getWorld() {
        return world;
    }

    public boolean damageConstructor;

    @Override
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart multiPartEntityPart,@Nonnull DamageSource damageSource, float damage) {
        if(multiPartEntityPart == this.head  && this.isFightMode()) {
            this.damageConstructor = true;
            return this.attackEntityFrom(damageSource, damage);

        }
        if (damage > 0.0F && !damageSource.isUnblockable()) {
        if (!damageSource.isProjectile()) {
            Entity entity = damageSource.getImmediateSource();

            if (entity instanceof EntityLivingBase) {
                this.blockUsingShield((EntityLivingBase) entity);
            }

             }
            this.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1.0f, 0.6f + ModRand.getFloat(0.2f));
            return false;
        }
        return false;
    }



    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        if(!this.damageConstructor && !source.isUnblockable()) {
            return false;

        }
        this.damageConstructor = false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }


    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHULKER_AMBIENT;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SHULKER_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHULKER_DEATH;
    }
}
