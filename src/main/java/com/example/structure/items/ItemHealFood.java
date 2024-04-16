package com.example.structure.items;

import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemHealFood extends ItemFoodBase {
    /**
     * A simple Item that applies Regeneration upon eating
     * @param name
     * @param amount
     * @param saturation
     * @param isWolfFood
     */
    private String info_loc;
    public int hasEatenTooMany = 0;
    public ItemHealFood(String name, int amount, float saturation, boolean isWolfFood, String item_desc) {
        super(name, amount, saturation, isWolfFood);
        this.info_loc = item_desc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase living) {
        // if the player is at zero food, achievements
        if (living instanceof EntityPlayer && !world.isRemote) {
            if(hasEatenTooMany >= 4) {
                living.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1000, 1));
                living.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300, 0));
                hasEatenTooMany = 0;
            }
            living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
            hasEatenTooMany++;
        }
        // then normal effects
        return super.onItemUseFinish(itemStack, world, living);
    }

    public int deCompress = 400;
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if(deCompress ==0) {
            if(hasEatenTooMany > 0) {
                hasEatenTooMany--;
            }
            deCompress = 400;
        }
        deCompress--;

    }
}
