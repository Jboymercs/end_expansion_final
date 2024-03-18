package com.example.structure.entity;

import com.example.structure.entity.endking.EntityNuclearExplosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class EntityMiniNuke extends EntityModBase implements IAnimatable {
    /**
     * This class is for the Flashing
     * @param worldIn
     * @param x
     * @param y
     * @param z
     */

    private int fuse;

    private final String ANIM_LIGHT = "light";

    private AnimationFactory factory = new AnimationFactory(this);


    @Nullable
    private EntityLivingBase tntPlacedBy;


    public EntityMiniNuke(World worldIn, float x, float y, float z, EntityLivingBase igniter) {
        super(worldIn, x, y, z);
        this.setPosition(x, y, z);
        float f = (float)(Math.random() * (Math.PI * 2D));
        this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
        this.setFuse(100);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.tntPlacedBy = igniter;

    }

    public EntityMiniNuke(World worldIn) {
        super(worldIn);
        this.fuse = 100;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        --this.fuse;

        if (this.fuse <= 0)
        {
            this.setDead();

            if (!this.world.isRemote)
            {
                this.explodeNuke();
            }
        }
        else
        {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explodeNuke()
    {
        if(!world.isRemote) {
            Vec3d spawnPos = this.getPositionVector();
            EntityNuclearExplosion nuke = new EntityNuclearExplosion(world, true);
            nuke.setPosition(spawnPos.x, spawnPos.y, spawnPos.z);
            nuke.sizeScaling = 2.0f;
            world.spawnEntity(nuke);
        }
    }


    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Nullable
    public EntityLivingBase getTntPlacedBy()
    {
        return this.tntPlacedBy;
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    public void setFuse(int fuseIn)
    {

        this.fuse = fuseIn;
    }


    public int getFuse()
    {
        return this.fuse;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "moveIdle", 0, this::predicateIdle));
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_LIGHT, false));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
