package com.jboymercs.endexpansion.entity.arrow;

import com.jboymercs.endexpansion.entity.endking.EntityAbstractEndKing;
import com.jboymercs.endexpansion.entity.knighthouse.EntityKnightBase;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.init.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityUnholyArrow extends EntityModArrow{


    public EntityUnholyArrow(World worldIn) {
        super(worldIn);
    }

    public void entityInit()
    {
        super.entityInit();
        this.setDamage(9);
    }

    public EntityUnholyArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }


    @Override
    protected void arrowHit(EntityLivingBase living)
    {
        if(living instanceof EntityKnightBase || living instanceof EntityAbstractEndKing) {
            return;
        }
        super.arrowHit(living);

        if (!world.isRemote)
        {
            living.addPotionEffect(new PotionEffect(ModPotions.CORRUPTED, 150, 0, false, false));
        }

        this.isDead = false;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.UNHOLY_ARROW, 1, 1);
    }
}
