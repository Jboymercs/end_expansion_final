package com.example.structure.items;

import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemPurpleCrystal extends ItemBase{
    private String info_loc;


    public ItemPurpleCrystal(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }
}
