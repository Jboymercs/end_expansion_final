package com.example.structure.entity.arrow;

import com.example.structure.entity.endking.EntityAbstractEndKing;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.init.ModItems;
import com.example.structure.init.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
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
