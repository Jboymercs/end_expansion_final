package com.jboymercs.endexpansion.blocks.fluid;

import com.jboymercs.endexpansion.util.ModReference;
import com.jboymercs.endexpansion.Main;
import com.jboymercs.endexpansion.init.ModBlocks;
import com.jboymercs.endexpansion.init.ModCreativeTabs;
import com.jboymercs.endexpansion.init.ModItems;
import com.jboymercs.endexpansion.init.ModPotions;
import com.jboymercs.endexpansion.util.IHasModel;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockBareAcid extends BlockFluidClassic implements IHasModel {


    public BlockBareAcid(Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);
    }

    public BlockBareAcid(String name, Fluid fluid, Material material) {
        super(fluid, material);
        this.setRegistryName(name);
        this.setTranslationKey(ModReference.MOD_ID + "." + name);
        this.setCreativeTab(ModCreativeTabs.ITEMS);
        // Add both an item as a block and the block itself
        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollision(worldIn, pos, state, entityIn);
//        if (worldIn.isRemote) return;
        if (entityIn.isEntityAlive())
        {
            if(entityIn instanceof EntityPlayer) {
                if(!entityIn.world.isRemote) {
                    if(((EntityPlayer) entityIn).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != ModItems.LIDOPED_HELMET) {
                        ((EntityPlayer) entityIn).addPotionEffect(new PotionEffect(ModPotions.MADNESS, 600, 0));
                    }
                }
            }
        }
    }


    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }


}
