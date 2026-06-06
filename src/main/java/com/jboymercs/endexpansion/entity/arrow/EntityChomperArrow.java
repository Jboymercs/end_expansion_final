package com.jboymercs.endexpansion.entity.arrow;

import com.jboymercs.endexpansion.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityChomperArrow extends EntityModArrow{
    public EntityChomperArrow(World worldIn) {
        super(worldIn);
    }

    public EntityChomperArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public void entityInit()
    {
        super.entityInit();
        this.setDamage(18);
    }


    @Override
    protected void arrowHit(EntityLivingBase living)
    {
        super.arrowHit(living);

        this.isDead = false;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.UNHOLY_ARROW, 1, 2);
    }
}
