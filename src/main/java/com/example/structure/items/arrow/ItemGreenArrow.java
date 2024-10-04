package com.example.structure.items.arrow;

import com.example.structure.entity.arrow.EntityGreenArrow;
import com.example.structure.entity.arrow.EntityUnholyArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGreenArrow extends ItemArrowBase{
    public ItemGreenArrow(String name) {
        super(name);
    }

    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
        return new EntityGreenArrow(worldIn, shooter);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }
}
