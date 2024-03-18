package com.example.structure.entity.endking;

import com.example.structure.entity.EntityModBase;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityFireBall extends EntityModBase implements IAnimatable {

    private final String ANIM_SUMMON = "summon";
    private final String ANIM_IDLE = "roll";

    protected static final DataParameter<Boolean>  SUMMON = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    public void setSummonMode(boolean value) {this.dataManager.set(SUMMON, Boolean.valueOf(value));}
    public boolean isSummonMode() {return this.dataManager.get(SUMMON);}
    private AnimationFactory factory = new AnimationFactory(this);

    public EntityFireBall(World world) {
        super(world);
        this.setSize(1.0f, 1.0f);
        this.setSummonMode(true);
        addEvent(()-> this.setSummonMode(false), 20);
        this.setNoGravity(true);
    }
    public EntityFireBall(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
        this.setSize(1.0f, 1.0f);
        this.setNoGravity(true);
    }



    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / f;
        y = y / f;
        z = z / f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
        x = x * velocity;
        y = y * velocity;
        z = z * velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(y, f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        int ticksInGround = 0;
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(SUMMON, Boolean.valueOf(false));
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8);
    }

    protected boolean hasSpawnedExplosion = false;

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;
        boolean hasGround = false;
        if (!world.isAirBlock(getPosition().add(new BlockPos(0, -1, 0)))) {
            hasGround = true;
        }
        if(hasGround && !hasSpawnedExplosion) {
            this.onGroundTouch();
        }

        if(ticksExisted > 100) {
            this.setNoGravity(false);
        }

        if(ticksExisted > 200) {
            this.setDead();
        }

    }

    public void onGroundTouch() {
        this.hasSpawnedExplosion = true;
        if(!world.isRemote) {
            this.world.newExplosion(this, this.posX, this.posY, this.posZ, 1, true, false);
            addEvent(this::setDead, 5);
        }
    }
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(this.isSummonMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        }
        return PlayState.CONTINUE;
    }


    @Override
    protected boolean canDropLoot() {
        return false;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
