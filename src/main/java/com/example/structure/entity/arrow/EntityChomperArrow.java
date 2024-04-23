package com.example.structure.entity.arrow;

import com.example.structure.entity.endking.EntityAbstractEndKing;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.init.ModItems;
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
