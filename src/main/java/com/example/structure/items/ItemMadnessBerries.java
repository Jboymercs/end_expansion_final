package com.example.structure.items;

import com.example.structure.init.ModPotions;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemMadnessBerries extends ItemFoodBase{

    private String info_loc;

    public ItemMadnessBerries(String name, int amount, float saturation, boolean isWolfFood, String info_loc) {
        super(name, amount, saturation, isWolfFood);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GREEN + ModUtils.translateDesc(info_loc));
    }


    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase living) {
        if (living instanceof EntityPlayer && !world.isRemote) {
            living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1));
            living.addPotionEffect(new PotionEffect(ModPotions.MADNESS, 600, 0));
        }
        // then normal effects
        return super.onItemUseFinish(itemStack, world, living);
    }


}
