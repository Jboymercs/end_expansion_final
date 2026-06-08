package com.jboymercs.endexpansion.items.arrow;

import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.entity.arrow.EntityChomperArrow;
import com.jboymercs.endexpansion.entity.arrow.EntityGreenArrow;
import com.jboymercs.endexpansion.entity.arrow.EntityModArrow;
import com.jboymercs.endexpansion.entity.arrow.EntityUnholyArrow;
import com.jboymercs.endexpansion.init.ModCreativeTabs;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.util.IHasModel;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemArrowBase extends ItemArrow implements IHasModel {

    public static final IBehaviorDispenseItem DISPENSER_BEHAVIOR = new BehaviorProjectileDispense()
    {
        @Override
        protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
        {
            EntityModArrow unholy_arrow = new EntityUnholyArrow(worldIn);

            if(stackIn.getItem() == ModItems.UNHOLY_ARROW)
            {
                unholy_arrow = new EntityUnholyArrow(worldIn);
            }

            if(stackIn.getItem() == ModItems.CHOMPER_ARROW) {
                unholy_arrow = new EntityChomperArrow(worldIn);
            }

            if(stackIn.getItem() == ModItems.GREEN_ARROW) {
                unholy_arrow = new EntityGreenArrow(worldIn);
            }


            unholy_arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
            unholy_arrow.setPosition(position.getX(), position.getY(), position.getZ());

            return unholy_arrow;

        }
    };

    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {

        return new EntityChomperArrow(worldIn, shooter);
    }

    public ItemArrowBase(String name)
    {
        super();
        this.setTranslationKey(ModReference.MOD_ID + "." + name);
        this.setRegistryName(name);
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        ModItems.ITEMS.add(this);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DISPENSER_BEHAVIOR);
    }




    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
