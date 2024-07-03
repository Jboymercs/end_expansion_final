package com.example.structure.entity.barrend;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.util.IPitch;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class EntityAbstractBarrendGolem extends EntityModBase implements IEntityMultiPart, IPitch {

    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_6));
    private final MultiPartEntityPart[] hitboxParts;
    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0f, 0f);
    private final MultiPartEntityPart main_body = new MultiPartEntityPart(this, "main_body", 1.0f, 3.0f);
    private final MultiPartEntityPart left_arm = new MultiPartEntityPart(this, "left_arm", 0.6f, 2.4F);
    private final MultiPartEntityPart right_arm = new MultiPartEntityPart(this, "right_arm", 0.6f, 2.4F);

    private final MultiPartEntityPart part_left_worm = new MultiPartEntityPart(this, "part_left_worm", 0.6F, 0.6F);

    private final MultiPartEntityPart part_right_worm = new MultiPartEntityPart(this, "part_right_worm", 0.6F, 0.5F);

    private final MultiPartEntityPart part_back_worm = new MultiPartEntityPart(this, "part_back_worm", 0.6F, 0.6F);
    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> BARREND_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> WORM_BACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> WORM_LEFT = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> WORM_RIGHT = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> FULL_BODY_USAGE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> PARASITE_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SWING_LEFT = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SWING_RIGHT = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> PREPARE_CHARG = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> CHARGE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> COLLIDE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SLAM = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> LEAP_SLAM = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> AWAKEN = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> STILL = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Boolean> SHOOT_PROJECTILES = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SUMMON_MINIONS = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);


    public void setFightMode(boolean value) {this.dataManager.set(BARREND_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(BARREND_MODE);}
    public void setWormBack(boolean value) {this.dataManager.set(WORM_BACK, Boolean.valueOf(value));}
    public boolean isWormBack() {return this.dataManager.get(WORM_BACK);}
    public void setWormLeft(boolean value) {this.dataManager.set(WORM_LEFT, Boolean.valueOf(value));}
    public boolean isWormLeft() {return this.dataManager.get(WORM_LEFT);}
    public void setWormRight(boolean value) {this.dataManager.set(WORM_RIGHT, Boolean.valueOf(value));}
    public boolean isWormRight() {return this.dataManager.get(WORM_RIGHT);}
    public void setFullBodyUsage(boolean value) {this.dataManager.set(FULL_BODY_USAGE, Boolean.valueOf(value));}
    public boolean isFullBodyUsage() {return this.dataManager.get(FULL_BODY_USAGE);}
    public void setSwingLeft(boolean value) {this.dataManager.set(SWING_LEFT, Boolean.valueOf(value));}
    public boolean isSwingLeft() {return this.dataManager.get(SWING_LEFT);}
    public void setSwingRight(boolean value) {this.dataManager.set(SWING_RIGHT, Boolean.valueOf(value));}
    public boolean isSwingRight() {return this.dataManager.get(SWING_RIGHT);}
    public void setParasiteAttack(boolean value) {this.dataManager.set(PARASITE_ATTACK, Boolean.valueOf(value));}
    public boolean isParasiteAttack() {return this.dataManager.get(PARASITE_ATTACK);}
    public void setPrepareCharge(boolean value) {this.dataManager.set(PREPARE_CHARG, Boolean.valueOf(value));}
    public boolean isPrepareCharge() {return this.dataManager.get(PREPARE_CHARG);}
    public void setCharge(boolean value) {this.dataManager.set(CHARGE, Boolean.valueOf(value));}
    public boolean isCharge() {return this.dataManager.get(CHARGE);}
    public void setCollide(boolean value) {this.dataManager.set(COLLIDE, Boolean.valueOf(value));}
    public boolean isCollide() {return this.dataManager.get(COLLIDE);}
    public void setLeapSlam(boolean value) {this.dataManager.set(LEAP_SLAM, Boolean.valueOf(value));}
    public boolean isLeapSlam() {return this.dataManager.get(LEAP_SLAM);}
    public void setSlam(boolean value) {this.dataManager.set(SLAM, Boolean.valueOf(value));}
    public boolean isSlam() {return this.dataManager.get(SLAM);}
    public void setAwaken(boolean value) {this.dataManager.set(AWAKEN, Boolean.valueOf(value));}
    public boolean isAwaken() {return this.dataManager.get(AWAKEN);}
    public void setStill(boolean value) {this.dataManager.set(STILL, Boolean.valueOf(value));}
    public boolean isStill() {return this.dataManager.get(STILL);}
    public void setShootProjectiles(boolean value) {this.dataManager.set(SHOOT_PROJECTILES, Boolean.valueOf(value));}
    public boolean isShootProjectiles() {return this.dataManager.get(SHOOT_PROJECTILES);}
    public void setSummonMinions(boolean value) {this.dataManager.set(SUMMON_MINIONS, Boolean.valueOf(value));}
    public boolean isSummonMinions() {return this.dataManager.get(SUMMON_MINIONS);}



    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public EntityAbstractBarrendGolem(World worldIn) {
        super(worldIn);
        this.hitboxParts = new MultiPartEntityPart[]{model, main_body, left_arm, right_arm, part_left_worm, part_right_worm, part_back_worm};
        this.setSize(2.3f, 3.2f);
        this.experienceValue = 320;
    }


    @Override
    public void entityInit() {
        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(BARREND_MODE, Boolean.valueOf(false));
        this.dataManager.register(WORM_BACK, Boolean.valueOf(false));
        this.dataManager.register(WORM_LEFT, Boolean.valueOf(false));
        this.dataManager.register(WORM_RIGHT, Boolean.valueOf(false));
        this.dataManager.register(FULL_BODY_USAGE, Boolean.valueOf(false));
        this.dataManager.register(PARASITE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SWING_RIGHT, Boolean.valueOf(false));
        this.dataManager.register(SWING_LEFT, Boolean.valueOf(false));
        this.dataManager.register(PREPARE_CHARG, Boolean.valueOf(false));
        this.dataManager.register(CHARGE, Boolean.valueOf(false));
        this.dataManager.register(COLLIDE, Boolean.valueOf(false));
        this.dataManager.register(SLAM, Boolean.valueOf(false));
        this.dataManager.register(LEAP_SLAM, Boolean.valueOf(false));
        this.dataManager.register(AWAKEN, Boolean.valueOf(false));
        this.dataManager.register(STILL, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_MINIONS, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_PROJECTILES, Boolean.valueOf(false));
        super.entityInit();
    }


    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    protected boolean currentlyHasWormActive = false;
    protected int timeInBetweenWorms = ModRand.range(80, 120);

    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

        if(this.isStill() || this.isAwaken()) {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.rotationPitch = 0;
            this.rotationYawHead = 0;
            this.rotationYaw = 0;
            this.setFullBodyUsage(true);
        }

        if(this.isStill()) {
            List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(3D), e -> !e.isCreative());

            if(!nearbyPlayers.isEmpty()) {
                setTooAwaken();
            }
        }
        //Some quick functions to handle the worms popping out of areas of the boss
        if(!this.isAwaken() && !this.isStill()) {
            if (this.hurtTime > 0 && currentlyHasWormActive) {
                this.setWormBack(false);
                this.setWormLeft(false);
                this.setWormRight(false);
                currentlyHasWormActive = false;
            } else if (timeInBetweenWorms < 0) {
                randomlyDetermineWorm();
            }

            if (!currentlyHasWormActive) {
                timeInBetweenWorms--;
            }
        }
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public void setTooAwaken() {
        this.setStill(false);
        this.setAwaken(true);
        this.playSound(ModSoundHandler.BARREND_AWAKEN, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.2f);
        addEvent(()-> {
        this.setAwaken(false);
        this.setNoAI(false);
        this.setImmovable(false);
        this.setFullBodyUsage(false);
        }, 50);
    }

    public void randomlyDetermineWorm() {
        int randomNumber = ModRand.range(1, 4);
        if(randomNumber == 1) {
            this.setWormBack(true);
            currentlyHasWormActive = true;
        } else if(randomNumber == 2) {
            this.setWormLeft(true);
            currentlyHasWormActive = true;
        } else if(randomNumber == 3) {
            this.setWormRight(true);
            currentlyHasWormActive = true;
        }

        timeInBetweenWorms = ModRand.range(80, 120);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }

        this.setHitBoxPos(main_body, new Vec3d(-0.1, 0.1, 0));
        this.setHitBoxPos(left_arm, new Vec3d(0, 0.4, 0.8));
        this.setHitBoxPos(right_arm, new Vec3d(0, 0.4, -0.8));


        if(this.isWormBack()) {
            this.setHitBoxPos(part_back_worm, new Vec3d(0.7, 1.7, 0));
        } else {
            this.setHitBoxPos(part_back_worm, new Vec3d(0, 2.5, 0).scale(0));
        }


        if(this.isWormLeft()) {
            this.setHitBoxPos(part_left_worm, new Vec3d(0, 2.8, 0.9));
        } else {
            this.setHitBoxPos(part_left_worm, new Vec3d(0, 2.5, 0).scale(0));
        }

        if(this.isWormRight()) {
            this.setHitBoxPos(part_right_worm, new Vec3d(0, 2.3, -1.5));
        } else {
            this.setHitBoxPos(part_right_worm, new Vec3d(0, 2.5, 0).scale(0));
        }



        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.barrend_golem_health * ModConfig.biome_multiplier);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.24D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.barrend_golem_attack_damage);
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

    @Override
    protected boolean canDropLoot() {
        return true;
    }


    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part , @Nonnull DamageSource damageSource, float damage) {
        if(part == main_body || part == right_arm || part == left_arm) {
            return false;
        }
            return this.attackEntityFrom(damageSource, damage);
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }

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
}
