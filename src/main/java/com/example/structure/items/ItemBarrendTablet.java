package com.example.structure.items;

import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemBarrendTablet extends ItemBase{

    private String info_loc;

    public ItemBarrendTablet(String name, CreativeTabs tab, String info_loc) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setMaxStackSize(1);
        this.setMaxDamage(200);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + ModUtils.translateDesc(info_loc));
    }
}