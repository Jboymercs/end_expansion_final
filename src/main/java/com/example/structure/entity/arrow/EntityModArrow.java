package com.example.structure.entity.arrow;

import com.example.structure.entity.magic.IMagicEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public abstract class EntityModArrow extends EntityArrow implements IProjectile, IMagicEntity {

    private int ticksInAir;

    private boolean hasNoGravity;

    public EntityModArrow(World worldIn)
    {
        super(worldIn);
        this.pickupStatus = PickupStatus.DISALLOWED;
    }

    public EntityModArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
        this.pickupStatus = PickupStatus.DISALLOWED;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.ticksInAir == 500)
        {
            this.setDead();
        }

        if (!this.onGround)
        {
            ++this.ticksInAir;
        }
    }

    @Override
    public boolean hasNoGravity()
    {
        return this.hasNoGravity;
    }

    @Override
    public void setNoGravity(boolean flight)
    {
        this.hasNoGravity = flight;
    }

    @Override
    public boolean getDoesEntityMove() {
        return true;
    }

    @Override
    public boolean isDodgeable() {
        return true;
    }

    @Override
    public Entity getOwnerFromMagic() {
        return this.shootingEntity;
    }
}
